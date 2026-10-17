import 'package:flutter/material.dart';
import '../localization/app_localizations.dart';
import '../utils/ad_banner_widget.dart';
import '../utils/general_util.dart';
import 'game_id_input_screen.dart';
import 'rule_selection_screen.dart';

class GameModeSelectionScreen extends StatelessWidget {
  const GameModeSelectionScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final size = MediaQuery.of(context).size;
    final buttonWidth = size.width * 0.8;

    return Scaffold(
      body: SafeArea(
        child: Stack(
          children: [
            Center(
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      loc.translate('title_select_game_mode'),
                      style: theme.textTheme.headlineMedium?.copyWith(
                        color: theme.colorScheme.onPrimaryContainer,
                        fontWeight: FontWeight.bold,
                      ),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 48),

                    // Moderated Button
                    _ModeButton(
                      width: buttonWidth > 350 ? 350 : buttonWidth,
                      text: loc.translate('btn_moderated'),
                      onPressed: () {
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                            builder: (context) =>
                                const GameIdInputScreen(),
                          ),
                        );
                      },
                    ),

                    const SizedBox(height: 24),

                    // Unmoderated Button
                    _ModeButton(
                      width: buttonWidth > 350 ? 350 : buttonWidth,
                      text: loc.translate('btn_unmoderated'),
                      onPressed: () {
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                            builder: (context) =>
                                const RuleSelectionScreen(),
                          ),
                        );
                      },
                    ),

                    const SizedBox(height: 48),

                    Text(
                      GeneralUtil.getCopyrightMessage(),
                      style: theme.textTheme.bodyMedium,
                    ),
                  ],
                ),
              ),
            ),
            const Positioned(
              bottom: 16,
              left: 0,
              right: 0,
              child: AdBannerWidget(),
            ),
          ],
        ),
      ),
    );
  }
}

class _ModeButton extends StatefulWidget {
  final String text;
  final VoidCallback onPressed;
  final double width;

  const _ModeButton({
    required this.text,
    required this.onPressed,
    required this.width,
  });

  @override
  State<_ModeButton> createState() => _ModeButtonState();
}

class _ModeButtonState extends State<_ModeButton> {
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
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(25),
            ),
            elevation: isFocused ? 8 : 2,
          ),
          onPressed: widget.onPressed,
          child: Text(
            widget.text,
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: isFocused ? const Color(0xFFFFD700) : theme.colorScheme.tertiary,
            ),
          ),
        ),
      ),
    );
  }
}
