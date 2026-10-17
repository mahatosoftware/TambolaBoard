import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_tts/flutter_tts.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../db/app_database.dart';
import '../localization/app_localizations.dart';
import '../models/called_number.dart';
import '../models/game_metadata.dart';
import '../models/player_entity.dart';
import '../models/winning_prize_entity.dart';
import '../utils/funny_phrase_util.dart';
import '../utils/general_util.dart';
import '../utils/screen_size_util.dart';
import '../utils/tts_util.dart';
import 'dialogs/language_settings_dialog.dart';
import 'dialogs/winner_board_dialog.dart';
import 'main_screen.dart';

class GameScreen extends StatefulWidget {
  final bool isNewGame;
  final String intentGameId;

  const GameScreen({
    super.key,
    required this.isNewGame,
    required this.intentGameId,
  });

  @override
  State<GameScreen> createState() => _GameScreenState();
}

class _GameScreenState extends State<GameScreen>
    with SingleTickerProviderStateMixin {
  final FlutterTts _tts = FlutterTts();
  bool _isTtsReady = false;
  List<int> _calledNumbers = [];
  int? _lastNumber;
  bool _isAutoCalling = false;
  Timer? _autoCallTimer;
  String _gameId = "";
  String _selectedLang = "en";

  late AnimationController _flashController;
  late Animation<Color?> _flashAnimation;

  @override
  void initState() {
    super.initState();
    _flashController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 500),
    )..repeat(reverse: true);

    _flashAnimation = ColorTween(
      begin: Colors.grey,
      end: Colors.cyan,
    ).animate(_flashController);

    _initTts();
    _initGame();
  }

  @override
  void dispose() {
    _autoCallTimer?.cancel();
    _flashController.dispose();
    _tts.stop();
    super.dispose();
  }

  String _getTtsLocale(String langCode) => TtsUtil.getTtsLocale(langCode);

  Future<void> _updateLanguage(String newLang) async {
    _selectedLang = newLang;
    final ttsLocale = _getTtsLocale(newLang);
    await TtsUtil.configureFemaleVoice(_tts, ttsLocale);
    if (mounted) {
      setState(() {});
    }
  }

  Future<void> _initTts() async {
    final prefs = await SharedPreferences.getInstance();
    _selectedLang = prefs.getString('selected_language') ?? 'en';

    final ttsLocale = _getTtsLocale(_selectedLang);
    await TtsUtil.configureFemaleVoice(_tts, ttsLocale);

    if (mounted) {
      setState(() {
        _isTtsReady = true;
      });
    }
    if (!mounted) return;
    final loc = AppLocalizations.of(context)!;
    _tts.speak(loc.translate('welcome_speech'));
  }

  Future<void> _initGame() async {
    final savedId = await AppDatabase.instance.getSavedGameId();

    if (widget.isNewGame || savedId == null) {
      await AppDatabase.instance.resetBoard();

      final newId = widget.intentGameId.isNotEmpty
          ? widget.intentGameId
          : GeneralUtil.generateGameId();
      await AppDatabase.instance.saveGameMetadata(GameMetadata(gameId: newId));

      await AppDatabase.instance.clearAllPrizes();
      final savedRules = await AppDatabase.instance.getAllSavedRules();

      if (savedRules.isNotEmpty) {
        for (final rule in savedRules) {
          for (int i = 0; i < rule.quantity; i++) {
            await AppDatabase.instance.insertPrize(
              WinningPrizeEntity(savedRule: rule),
            );
          }
        }
      }

      if (widget.intentGameId.isNotEmpty) {
        await _syncPlayers(newId);
      }

      setState(() {
        _gameId = newId;
        _calledNumbers = [];
        _lastNumber = null;
      });
    } else {
      final numbers = await AppDatabase.instance.getAllCalledNumbers();
      setState(() {
        _gameId = savedId;
        _calledNumbers = numbers.map((e) => e.number).toList();
        _lastNumber = numbers.any((e) => e.isLast)
            ? numbers.firstWhere((e) => e.isLast).number
            : null;
      });
    }
  }

  Future<void> _syncPlayers(String gId) async {
    if (gId.length != 6) return;
    try {
      final firestore = FirebaseFirestore.instance;
      final Set<String> namesSet = {};

      try {
        final ticketsSnapshot = await firestore
            .collection('games')
            .doc(gId)
            .collection('tickets')
            .get();
        for (var doc in ticketsSnapshot.docs) {
          final data = doc.data();
          final name = data['name'] ??
              data['playerName'] ??
              data['player_name'] ??
              data['userName'] ??
              data['user_name'] ??
              data['ticketHolder'];
          if (name != null && name.toString().trim().isNotEmpty) {
            namesSet.add(name.toString().trim());
          }
        }
      } catch (e) {
        debugPrint("Error checking tickets subcollection: $e");
      }

      try {
        final playersSnapshot = await firestore
            .collection('games')
            .doc(gId)
            .collection('players')
            .get();
        for (var doc in playersSnapshot.docs) {
          final data = doc.data();
          final name = data['name'] ??
              data['playerName'] ??
              data['player_name'] ??
              data['userName'] ??
              data['user_name'];
          if (name != null && name.toString().trim().isNotEmpty) {
            namesSet.add(name.toString().trim());
          }
        }
      } catch (e) {
        debugPrint("Error checking players subcollection: $e");
      }

      try {
        final gameDoc = await firestore.collection('games').doc(gId).get();
        if (gameDoc.exists) {
          final data = gameDoc.data();
          final playersList = data?['players'] as List?;
          playersList?.forEach((item) {
            if (item is String && item.trim().isNotEmpty) {
              namesSet.add(item.trim());
            } else if (item is Map) {
              final name = item['name'] ?? item['playerName'] ?? item['userName'];
              if (name != null && name.toString().trim().isNotEmpty) {
                namesSet.add(name.toString().trim());
              }
            }
          });
        }
      } catch (e) {
        debugPrint("Error checking root game doc: $e");
      }

      final playerEntities = namesSet
          .map((name) => PlayerEntity(gameId: gId, name: name))
          .toList();

      await AppDatabase.instance.deletePlayersByGameId(gId);
      await AppDatabase.instance.insertPlayers(playerEntities);
    } catch (e) {
      debugPrint("Error syncing players: $e");
    }
  }

  void _callNumber() {
    if (!_isTtsReady) return;
    final remaining =
        List.generate(90, (i) => i + 1).toSet().difference(_calledNumbers.toSet());
    if (remaining.isNotEmpty) {
      final list = remaining.toList()..shuffle();
      final newNumber = list.first;

      setState(() {
        _calledNumbers.add(newNumber);
        _lastNumber = newNumber;
      });

      AppDatabase.instance.resetLastCalledNumber();
      AppDatabase.instance.insertCalledNumber(
        CalledNumber(number: newNumber, isLast: true),
      );

      final phrase = FunnyPhraseUtil.getFunnyPhrase(newNumber, _selectedLang);
      _tts.speak(phrase);
    } else {
      _stopAutoCall();
    }
  }

  void _toggleAutoCall() {
    setState(() {
      _isAutoCalling = !_isAutoCalling;
    });

    if (_isAutoCalling) {
      _autoCallTimer = Timer.periodic(const Duration(seconds: 6), (_) {
        if (_calledNumbers.length < 90 && _isAutoCalling) {
          _callNumber();
        } else {
          _stopAutoCall();
        }
      });
    } else {
      _stopAutoCall();
    }
  }

  void _stopAutoCall() {
    _autoCallTimer?.cancel();
    if (mounted) {
      setState(() {
        _isAutoCalling = false;
      });
    }
  }

  void _undoLastNumber() {
    if (_calledNumbers.isNotEmpty) {
      final numberToRemove = _calledNumbers.last;
      setState(() {
        _calledNumbers.removeLast();
        _lastNumber = _calledNumbers.isNotEmpty ? _calledNumbers.last : null;
      });

      AppDatabase.instance.deleteCalledNumber(numberToRemove);
      if (_lastNumber != null) {
        AppDatabase.instance.setLastCalledNumber(_lastNumber!);
      }
    }
  }

  Future<void> _resetBoard() async {
    _stopAutoCall();
    final newId = GeneralUtil.generateGameId();
    setState(() {
      _calledNumbers.clear();
      _lastNumber = null;
      _gameId = newId;
    });

    await AppDatabase.instance.resetBoard();
    await AppDatabase.instance.saveGameMetadata(GameMetadata(gameId: newId));
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final orientation = MediaQuery.of(context).orientation;
    final isLandscape = orientation == Orientation.landscape;
    final isWide = ScreenSizeUtil.isWideScreen(context);
    final isTwoColumn = isLandscape || isWide;

    return Scaffold(
      backgroundColor: theme.colorScheme.primary,
      appBar: AppBar(
        title: Text(loc.translate('app_name')),
        centerTitle: true,
        backgroundColor: theme.colorScheme.primary,
        foregroundColor: theme.colorScheme.onPrimary,
        actions: [
          IconButton(
            icon: const Icon(Icons.language),
            tooltip: loc.translate('title_settings'),
            onPressed: () {
              showDialog(
                context: context,
                builder: (ctx) => LanguageSettingsDialog(
                  onLanguageChanged: (newLang) {
                    _updateLanguage(newLang);
                  },
                ),
              );
            },
          ),
        ],
      ),
      body: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: isTwoColumn
                  ? Row(
                      children: [
                        Expanded(
                          flex: 35,
                          child: SingleChildScrollView(
                            child: _buildGameControls(loc, theme, true),
                          ),
                        ),
                        Expanded(
                          flex: 65,
                          child: _buildNumberGrid(),
                        ),
                      ],
                    )
                  : Column(
                      children: [
                        _buildGameControls(loc, theme, false),
                        const SizedBox(height: 8),
                        Expanded(child: _buildNumberGrid()),
                      ],
                    ),
            ),
            Padding(
              padding: const EdgeInsets.only(bottom: 8.0),
              child: Text(
                GeneralUtil.getCopyrightMessage(),
                style: theme.textTheme.labelSmall?.copyWith(
                  color: Colors.white70,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildGameControls(
      AppLocalizations loc, ThemeData theme, bool isLandscape) {
    final history = _calledNumbers.length > 1
        ? _calledNumbers.sublist(0, _calledNumbers.length - 1).reversed.take(5).toList()
        : <int>[];

    return Container(
      width: double.infinity,
      alignment: Alignment.center,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          if (_gameId.isNotEmpty)
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Container(
                  padding: const EdgeInsets.all(4),
                  color: Colors.white,
                  child: QrImageView(
                    data: _gameId,
                    version: QrVersions.auto,
                    size: isLandscape ? 60 : 80,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  "${loc.translate('game_id_label')}$_gameId",
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                    fontSize: 14,
                  ),
                ),
              ],
            ),

          const SizedBox(height: 8),

          // Last Number & Remaining Numbers Card
          Card(
            color: theme.colorScheme.secondary,
            elevation: 4,
            child: Padding(
              padding: EdgeInsets.symmetric(
                horizontal: isLandscape ? 10 : 18,
                vertical: 6,
              ),
              child: IntrinsicHeight(
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    // Last Number
                    Padding(
                      padding: EdgeInsets.symmetric(horizontal: isLandscape ? 6 : 10),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text(
                            loc.translate('last_number_label'),
                            style: TextStyle(
                              fontSize: isLandscape ? 10 : 11,
                              fontWeight: FontWeight.bold,
                              color: Colors.white70,
                            ),
                          ),
                          Text(
                            _lastNumber != null ? "$_lastNumber" : "--",
                            style: TextStyle(
                              fontSize: isLandscape ? 28 : 38,
                              fontWeight: FontWeight.w900,
                              color: theme.colorScheme.onSecondary,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const VerticalDivider(
                      color: Colors.white24,
                      thickness: 1,
                      indent: 4,
                      endIndent: 4,
                    ),
                    // Remaining Numbers on the Board
                    Padding(
                      padding: EdgeInsets.symmetric(horizontal: isLandscape ? 6 : 10),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text(
                            loc.translate('remaining_label'),
                            style: TextStyle(
                              fontSize: isLandscape ? 10 : 11,
                              fontWeight: FontWeight.bold,
                              color: Colors.white70,
                            ),
                          ),
                          Text(
                            "${(90 - _calledNumbers.toSet().length).clamp(0, 90)}",
                            style: TextStyle(
                              fontSize: isLandscape ? 28 : 38,
                              fontWeight: FontWeight.w900,
                              color: const Color(0xFF69F0AE),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),

          // Last 5 Numbers
          if (history.isNotEmpty)
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 4.0),
              child: Card(
                color: theme.colorScheme.secondary,
                elevation: 2,
                child: Padding(
                  padding: const EdgeInsets.all(6.0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const Text(
                        "Last 5",
                        style: TextStyle(
                          fontSize: 10,
                          fontWeight: FontWeight.bold,
                          color: Colors.white70,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisSize: MainAxisSize.min,
                        children: history.map((n) {
                          return Container(
                            margin: const EdgeInsets.symmetric(horizontal: 3),
                            width: 28,
                            height: 28,
                            decoration: BoxDecoration(
                              color: theme.colorScheme.onSecondary,
                              shape: BoxShape.circle,
                            ),
                            alignment: Alignment.center,
                            child: Text(
                              "$n",
                              style: TextStyle(
                                color: theme.colorScheme.secondary,
                                fontWeight: FontWeight.bold,
                                fontSize: 12,
                              ),
                            ),
                          );
                        }).toList(),
                      ),
                    ],
                  ),
                ),
              ),
            ),

          const SizedBox(height: 8),

          // Control Buttons Wrap
          SizedBox(
            width: double.infinity,
            child: Center(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 8.0),
                child: Wrap(
                  alignment: WrapAlignment.center,
                  runAlignment: WrapAlignment.center,
                  crossAxisAlignment: WrapCrossAlignment.center,
                  spacing: 6,
                  runSpacing: 6,
                  children: [
            _GameActionButton(
              icon: Icons.play_arrow,
              label: loc.translate('btn_call_next'),
              backgroundColor: const Color(0xFF009E60),
              onPressed: _callNumber,
            ),
            _GameActionButton(
              icon: _isAutoCalling ? Icons.pause : Icons.autorenew,
              label: _isAutoCalling
                  ? loc.translate('btn_pause')
                  : loc.translate('btn_auto_call'),
              backgroundColor: _isAutoCalling ? Colors.amber.shade800 : Colors.orange.shade800,
              onPressed: _toggleAutoCall,
            ),
            _GameActionButton(
              icon: Icons.emoji_events,
              label: loc.translate('btn_claim_prize'),
              backgroundColor: Colors.teal.shade700,
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (ctx) => WinnerBoardDialog(
                    gameId: _gameId,
                    onReloadPlayers: () => _syncPlayers(_gameId),
                  ),
                );
              },
            ),
            _GameActionButton(
              icon: Icons.undo,
              label: loc.translate('btn_undo'),
              backgroundColor: Colors.deepOrange.shade600,
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (ctx) => AlertDialog(
                    title: const Text("Undo Last Number?", style: TextStyle(color: Colors.amber, fontWeight: FontWeight.bold)),
                    content: const Text("Are you sure you want to undo the last change?", style: TextStyle(color: Colors.white)),
                    actions: [
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blueGrey.shade700,
                        ),
                        onPressed: () => Navigator.pop(ctx),
                        child: Text(loc.translate('dialog_no'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.deepOrange.shade600,
                        ),
                        onPressed: () {
                          Navigator.pop(ctx);
                          _undoLastNumber();
                        },
                        child: Text(loc.translate('dialog_yes'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                    ],
                  ),
                );
              },
            ),
            _GameActionButton(
              icon: Icons.refresh,
              label: loc.translate('btn_reset'),
              backgroundColor: Colors.red.shade700,
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (ctx) => AlertDialog(
                    title: Text(loc.translate('dialog_reset_title'),
                        style: const TextStyle(color: Colors.amber, fontWeight: FontWeight.bold)),
                    content: Text(loc.translate('dialog_reset_message'), style: const TextStyle(color: Colors.white)),
                    actions: [
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blueGrey.shade700,
                        ),
                        onPressed: () => Navigator.pop(ctx),
                        child: Text(loc.translate('dialog_no'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.red.shade700,
                        ),
                        onPressed: () {
                          Navigator.pop(ctx);
                          _resetBoard();
                        },
                        child: Text(loc.translate('dialog_yes'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                    ],
                  ),
                );
              },
            ),
            _GameActionButton(
              icon: Icons.home,
              label: loc.translate('btn_return_main_menu'),
              backgroundColor: const Color(0xFF4A007E),
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (ctx) => AlertDialog(
                    title: Text(loc.translate('dialog_exit_title'),
                        style: const TextStyle(color: Colors.amber, fontWeight: FontWeight.bold)),
                    content: Text(loc.translate('dialog_exit_message'), style: const TextStyle(color: Colors.white)),
                    actions: [
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blueGrey.shade700,
                        ),
                        onPressed: () => Navigator.pop(ctx),
                        child: Text(loc.translate('dialog_no'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                      ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF009E60),
                        ),
                        onPressed: () {
                          Navigator.pop(ctx);
                          Navigator.pushAndRemoveUntil(
                            context,
                            MaterialPageRoute(
                              builder: (context) => const MainScreen(),
                            ),
                            (route) => false,
                          );
                        },
                        child: Text(loc.translate('dialog_yes'), style: const TextStyle(fontWeight: FontWeight.bold)),
                      ),
                    ],
                  ),
                );
              },
            ),
          ],
        ),
      ),
    ),
  ),
],
),
);
}

  Widget _buildNumberGrid() {
    return AnimatedBuilder(
      animation: _flashAnimation,
      builder: (context, child) {
        return LayoutBuilder(
          builder: (context, constraints) {
            const double padding = 6.0;
            const double spacing = 3.0;
            final double gridWidth = constraints.maxWidth - (padding * 2);
            final double gridHeight = constraints.maxHeight - (padding * 2);

            final double cellWidth = (gridWidth - (9 * spacing)) / 10;
            final double cellHeight = (gridHeight - (8 * spacing)) / 9;

            final double safeWidth = cellWidth > 0 ? cellWidth : 1;
            final double safeHeight = cellHeight > 0 ? cellHeight : 1;
            final double aspectRatio = safeWidth / safeHeight;

            final double fontSize = (safeHeight * 0.42).clamp(10.0, 26.0);

            return Padding(
              padding: const EdgeInsets.all(padding),
              child: GridView.builder(
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 10,
                  childAspectRatio: aspectRatio,
                  crossAxisSpacing: spacing,
                  mainAxisSpacing: spacing,
                ),
                itemCount: 90,
                itemBuilder: (context, index) {
                  final number = index + 1;
                  final isCalled = _calledNumbers.contains(number);
                  final isLast = _lastNumber == number;

                  Color bgColor = Theme.of(context).colorScheme.secondary;
                  if (isLast) {
                    bgColor = _flashAnimation.value ?? Colors.cyan;
                  } else if (isCalled) {
                    bgColor = Colors.purpleAccent.shade700;
                  }

                  return Container(
                    decoration: BoxDecoration(
                      color: bgColor,
                      borderRadius: BorderRadius.circular(4),
                      border: Border.all(color: Colors.black26),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      "$number",
                      style: TextStyle(
                        fontSize: fontSize,
                        fontWeight: FontWeight.bold,
                        color: (isCalled || isLast) ? Colors.white : Colors.white70,
                      ),
                    ),
                  );
                },
              ),
            );
          },
        );
      },
    );
  }
}

class _GameActionButton extends StatefulWidget {
  final IconData icon;
  final String label;
  final Color backgroundColor;
  final VoidCallback onPressed;

  const _GameActionButton({
    required this.icon,
    required this.label,
    required this.backgroundColor,
    required this.onPressed,
  });

  @override
  State<_GameActionButton> createState() => _GameActionButtonState();
}

class _GameActionButtonState extends State<_GameActionButton> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    return AnimatedScale(
      scale: _isFocused ? 1.08 : 1.0,
      duration: const Duration(milliseconds: 150),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20),
          boxShadow: _isFocused
              ? [
                  BoxShadow(
                    color: const Color(0xFFFFD700).withValues(alpha: 0.6),
                    blurRadius: 10,
                    spreadRadius: 2,
                  ),
                ]
              : null,
        ),
        child: ElevatedButton.icon(
          onFocusChange: (focused) {
            setState(() {
              _isFocused = focused;
            });
          },
          icon: Icon(
            widget.icon,
            size: 18,
            color: _isFocused ? const Color(0xFFFFD700) : Colors.white,
          ),
          style: ElevatedButton.styleFrom(
            backgroundColor: _isFocused
                ? const Color(0xFF3C0084)
                : widget.backgroundColor,
            foregroundColor: _isFocused ? const Color(0xFFFFD700) : Colors.white,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
            side: BorderSide(
              color: _isFocused ? const Color(0xFFFFD700) : Colors.transparent,
              width: 3,
            ),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
            ),
            elevation: _isFocused ? 8 : 2,
          ),
          onPressed: widget.onPressed,
          label: Text(
            widget.label,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: _isFocused ? const Color(0xFFFFD700) : Colors.white,
            ),
          ),
        ),
      ),
    );
  }
}
