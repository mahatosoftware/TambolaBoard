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

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final isWide = ScreenSizeUtil.isWideScreen(context);
    final theme = Theme.of(context);
    final size = MediaQuery.of(context).size;

    return Scaffold(
      backgroundColor: theme.colorScheme.surface,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
          child: Column(
            children: [
              SizedBox(height: isWide ? 24 : 48),

              // Header Title with App Logo
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
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
                child: isWide
                    ? Row(
                        children: [
                          // Left Column: Hero Cards (45% Width)
                          Expanded(
                            flex: 45,
                            child: Column(
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
                                          text: loc.translate('btn_settings'),
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
                                          text: loc.translate('btn_exit'),
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
                        child: Column(
                          children: [
                            _MobileButton(
                              text: loc.translate('btn_start_new_game'),
                              onClick: _onNewGame,
                              width: size.width * 0.8,
                            ),
                            const SizedBox(height: 14),
                            _MobileButton(
                              text: loc.translate('btn_continue_last_game'),
                              onClick: _onContinue,
                              width: size.width * 0.8,
                            ),
                            const SizedBox(height: 14),
                            _MobileButton(
                              text: loc.translate('btn_view_winners'),
                              onClick: _onViewWinners,
                              width: size.width * 0.8,
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
                              width: size.width * 0.8,
                            ),
                            const SizedBox(height: 14),
                            _MobileButton(
                              text: loc.translate('btn_settings'),
                              onClick: () {
                                showDialog(
                                  context: context,
                                  builder: (ctx) => LanguageSettingsDialog(
                                    onLanguageChanged:
                                        widget.onLanguageChanged,
                                  ),
                                );
                              },
                              width: size.width * 0.8,
                            ),
                            const SizedBox(height: 14),
                            _MobileButton(
                              text: loc.translate('btn_exit'),
                              onClick: () => SystemNavigator.pop(),
                              width: size.width * 0.8,
                            ),
                          ],
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
    );
  }
}

class _DashboardTile extends StatelessWidget {
  final String text;
  final VoidCallback onClick;
  final bool isHero;

  const _DashboardTile({
    required this.text,
    required this.onClick,
    this.isHero = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      color: isHero
          ? theme.colorScheme.primaryContainer
          : theme.colorScheme.primaryContainer.withValues(alpha: 0.75),
      elevation: 4,
      child: InkWell(
        borderRadius: BorderRadius.circular(20),
        onTap: onClick,
        child: Container(
          alignment: Alignment.center,
          padding: const EdgeInsets.all(16),
          child: Text(
            text,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: isHero ? 20 : 16,
              color: theme.colorScheme.tertiary,
              height: 1.3,
            ),
            textAlign: TextAlign.center,
          ),
        ),
      ),
    );
  }
}

class _MobileButton extends StatelessWidget {
  final String text;
  final VoidCallback onClick;
  final double width;

  const _MobileButton({
    required this.text,
    required this.onClick,
    required this.width,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return SizedBox(
      width: width,
      height: 50,
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          backgroundColor: theme.colorScheme.primaryContainer,
          foregroundColor: theme.colorScheme.tertiary,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(25),
          ),
        ),
        onPressed: onClick,
        child: Text(
          text,
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
        ),
      ),
    );
  }
}
