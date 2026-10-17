import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../db/app_database.dart';
import '../localization/app_localizations.dart';
import '../utils/ad_interstitial_helper.dart';
import '../utils/general_util.dart';
import '../utils/screen_size_util.dart';
import 'dialogs/how_to_play_dialog.dart';
import 'dialogs/language_settings_dialog.dart';
import 'game_mode_selection_screen.dart';
import 'game_screen.dart';
import 'view_winners_screen.dart';

class MainScreen extends StatefulWidget {
  final Function(String)? onLanguageChanged;

  const MainScreen({super.key, this.onLanguageChanged});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  @override
  void initState() {
    super.initState();
    AdInterstitialHelper.loadInterstitialAd();
  }

  void _onNewGame() {
    AdInterstitialHelper.showInterstitialAd(
      onAdDismissed: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const GameModeSelectionScreen(),
          ),
        );
      },
    );
  }

  void _onContinue() async {
    final savedGameId = await AppDatabase.instance.getSavedGameId();
    if (!mounted) return;
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => GameScreen(
          isNewGame: false,
          intentGameId: savedGameId ?? "",
        ),
      ),
    );
  }

  void _onViewWinners() async {
    final winners = await AppDatabase.instance.getAllPrizes();
    final gameId = await AppDatabase.instance.getSavedGameId() ?? "";
    if (!mounted) return;
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ViewWinnersScreen(
          winners: winners,
          gameId: gameId,
        ),
      ),
    );
  }

  String _getBilingualText(BuildContext context, String key, String englishDefault) {
    final loc = AppLocalizations.of(context)!;
    String localized = loc.translate(key);
    final String currentLang = loc.locale.languageCode;

    if (localized.isEmpty || localized == englishDefault) {
      if (key == 'btn_settings') {
        localized = loc.translate('title_settings');
      }
    }

    if (currentLang == 'en') {
      if (key == 'btn_settings') return 'Settings / सेटिंग्स';
      if (key == 'btn_exit') return 'Exit / बाहर निकलें';
      return englishDefault;
    }

    if (localized.isEmpty || localized == englishDefault) {
      return englishDefault;
    }

    return '$englishDefault / $localized';
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final isWide = ScreenSizeUtil.isWideScreen(context);
    final theme = Theme.of(context);
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: theme.colorScheme.surface,
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const SizedBox(height: 16),

                // Header Title with App Logo
                Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(
                      width: isWide ? 180 : 140,
                      height: isWide ? 180 : 140,
                      child: Image.asset(
                        'assets/images/app_logo.png',
                        fit: BoxFit.contain,
                        errorBuilder: (context, error, stackTrace) =>
                            const Icon(Icons.casino, size: 100, color: Colors.amber),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      loc.translate('main_welcome_message'),
                      style: TextStyle(
                        fontSize: isWide ? 26 : 20,
                        fontWeight: FontWeight.w900,
                        color: theme.colorScheme.onSurface,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),

                const SizedBox(height: 16),

                Expanded(
                  child: Center(
                    child: isWide
                        ? Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              // Left Column: Hero Cards (45% Width)
                              Expanded(
                                flex: 45,
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Expanded(
                                      child: _DashboardTile(
                                        text: loc.translate('btn_start_new_game'),
                                        onClick: _onNewGame,
                                        isHero: true,
                                      ),
                                    ),
                                    const SizedBox(height: 16),
                                    Expanded(
                                      child: _DashboardTile(
                                        text: loc.translate('btn_continue_last_game'),
                                        onClick: _onContinue,
                                        isHero: true,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                              const SizedBox(width: 20),

                              // Right Column: 2x2 Grid (55% Width)
                              Expanded(
                                flex: 55,
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Expanded(
                                      child: Row(
                                        children: [
                                          Expanded(
                                            child: _DashboardTile(
                                              text: loc.translate('btn_view_winners'),
                                              onClick: _onViewWinners,
                                            ),
                                          ),
                                          const SizedBox(width: 16),
                                          Expanded(
                                            child: _DashboardTile(
                                              text: loc.translate('btn_how_to_play'),
                                              onClick: () {
                                                showDialog(
                                                  context: context,
                                                  builder: (ctx) =>
                                                      const HowToPlayDialog(),
                                                );
                                              },
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    const SizedBox(height: 16),
                                    Expanded(
                                      child: Row(
                                        children: [
                                          Expanded(
                                            child: _DashboardTile(
                                              text: _getBilingualText(context, 'btn_settings', 'Settings'),
                                              onClick: () {
                                                showDialog(
                                                  context: context,
                                                  builder: (ctx) =>
                                                      LanguageSettingsDialog(
                                                    onLanguageChanged:
                                                        widget.onLanguageChanged,
                                                  ),
                                                );
                                              },
                                            ),
                                          ),
                                          const SizedBox(width: 16),
                                          Expanded(
                                            child: _DashboardTile(
                                              text: _getBilingualText(context, 'btn_exit', 'Exit'),
                                              onClick: () => SystemNavigator.pop(),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          )
                        : SingleChildScrollView(
                            child: Center(
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                crossAxisAlignment: CrossAxisAlignment.center,
                                children: [
                                  _MobileButton(
                                    text: loc.translate('btn_start_new_game'),
                                    onClick: _onNewGame,
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                  const SizedBox(height: 14),
                                  _MobileButton(
                                    text: loc.translate('btn_continue_last_game'),
                                    onClick: _onContinue,
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                  const SizedBox(height: 14),
                                  _MobileButton(
                                    text: loc.translate('btn_view_winners'),
                                    onClick: _onViewWinners,
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                  const SizedBox(height: 14),
                                  _MobileButton(
                                    text: loc.translate('btn_how_to_play'),
                                    onClick: () {
                                      showDialog(
                                        context: context,
                                        builder: (ctx) => const HowToPlayDialog(),
                                      );
                                    },
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                  const SizedBox(height: 14),
                                  _MobileButton(
                                    text: _getBilingualText(context, 'btn_settings', 'Settings'),
                                    onClick: () {
                                      showDialog(
                                        context: context,
                                        builder: (ctx) => LanguageSettingsDialog(
                                          onLanguageChanged:
                                              widget.onLanguageChanged,
                                        ),
                                      );
                                    },
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                  const SizedBox(height: 14),
                                  _MobileButton(
                                    text: _getBilingualText(context, 'btn_exit', 'Exit'),
                                    onClick: () => SystemNavigator.pop(),
                                    width: size.width * 0.8 > 380 ? 380 : size.width * 0.8,
                                  ),
                                ],
                              ),
                            ),
                          ),
                  ),
                ),

                const SizedBox(height: 12),

                // Copyright
                Text(
                  GeneralUtil.getCopyrightMessage(),
                  style: theme.textTheme.bodyMedium?.copyWith(
                    color: theme.colorScheme.onSurface.withValues(alpha: 0.7),
                  ),
                  textAlign: TextAlign.center,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _DashboardTile extends StatefulWidget {
  final String text;
  final VoidCallback onClick;
  final bool isHero;

  const _DashboardTile({
    required this.text,
    required this.onClick,
    this.isHero = false,
  });

  @override
  State<_DashboardTile> createState() => _DashboardTileState();
}

class _DashboardTileState extends State<_DashboardTile> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isFocused = _isFocused;
    final isHero = widget.isHero;

    return AnimatedScale(
      scale: isFocused ? 1.05 : 1.0,
      duration: const Duration(milliseconds: 150),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20),
          boxShadow: isFocused
              ? [
                  BoxShadow(
                    color: const Color(0xFFFFD700).withValues(alpha: 0.6),
                    blurRadius: 12,
                    spreadRadius: 2,
                  ),
                ]
              : null,
        ),
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: BorderSide(
              color: isFocused ? const Color(0xFFFFD700) : Colors.transparent,
              width: 3.5,
            ),
          ),
          color: isFocused
              ? const Color(0xFF3C0084)
              : (isHero
                  ? theme.colorScheme.primaryContainer
                  : theme.colorScheme.primaryContainer.withValues(alpha: 0.75)),
          elevation: isFocused ? 12 : 4,
          child: InkWell(
            borderRadius: BorderRadius.circular(20),
            onFocusChange: (focused) {
              setState(() {
                _isFocused = focused;
              });
            },
            onTap: widget.onClick,
            child: Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.all(16),
              child: Text(
                widget.text,
                style: TextStyle(
                  fontWeight: (isFocused || isHero)
                      ? FontWeight.w900
                      : FontWeight.bold,
                  fontSize: isHero ? 20 : 16,
                  color: isFocused
                      ? const Color(0xFFFFD700)
                      : theme.colorScheme.tertiary,
                  height: 1.3,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _MobileButton extends StatefulWidget {
  final String text;
  final VoidCallback onClick;
  final double width;

  const _MobileButton({
    required this.text,
    required this.onClick,
    required this.width,
  });

  @override
  State<_MobileButton> createState() => _MobileButtonState();
}

class _MobileButtonState extends State<_MobileButton> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isFocused = _isFocused;

    return AnimatedScale(
      scale: isFocused ? 1.05 : 1.0,
      duration: const Duration(milliseconds: 150),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        width: widget.width,
        height: 50,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(25),
          boxShadow: isFocused
              ? [
                  BoxShadow(
                    color: const Color(0xFFFFD700).withValues(alpha: 0.6),
                    blurRadius: 10,
                    spreadRadius: 2,
                  ),
                ]
              : null,
        ),
        child: ElevatedButton(
          onFocusChange: (focused) {
            setState(() {
              _isFocused = focused;
            });
          },
          style: ElevatedButton.styleFrom(
            backgroundColor: isFocused
                ? const Color(0xFF3C0084)
                : theme.colorScheme.primaryContainer,
            foregroundColor: isFocused
                ? const Color(0xFFFFD700)
                : theme.colorScheme.tertiary,
            side: BorderSide(
              color: isFocused ? const Color(0xFFFFD700) : Colors.transparent,
              width: 3.0,
            ),
            padding: const EdgeInsets.symmetric(horizontal: 8),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(25),
            ),
            elevation: isFocused ? 8 : 2,
          ),
          onPressed: widget.onClick,
          child: Text(
            widget.text,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 15,
              color: isFocused ? const Color(0xFFFFD700) : theme.colorScheme.tertiary,
            ),
            textAlign: TextAlign.center,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ),
    );
  }
}
