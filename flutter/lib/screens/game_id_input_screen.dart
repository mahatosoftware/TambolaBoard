import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import '../localization/app_localizations.dart';
import '../utils/general_util.dart';
import 'rule_selection_screen.dart';

class GameIdInputScreen extends StatefulWidget {
  const GameIdInputScreen({super.key});

  @override
  State<GameIdInputScreen> createState() => _GameIdInputScreenState();
}

class _GameIdInputScreenState extends State<GameIdInputScreen> {
  final TextEditingController _controller = TextEditingController();
  bool _isLoading = false;
  String? _errorMessage;

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  Future<void> _submitGameId() async {
    final gameId = _controller.text.trim().toUpperCase();
    final loc = AppLocalizations.of(context)!;

    if (gameId.isEmpty) {
      setState(() {
        _errorMessage = loc.translate('error_empty_game_id');
      });
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final auth = FirebaseAuth.instance;
      if (auth.currentUser == null) {
        setState(() {
          _errorMessage = loc.translate('status_authenticating');
        });
        await auth.signInAnonymously();
      }

      setState(() {
        _errorMessage = loc.translate('status_verifying_game_id');
      });

      final db = FirebaseFirestore.instance;
      final docRef = db.collection('games').doc(gameId);
      final snapshot = await docRef.get().timeout(const Duration(seconds: 30));

      if (snapshot.exists) {
        if (!mounted) return;
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => RuleSelectionScreen(
              gameId: gameId,
              isModerated: true,
            ),
          ),
        );
      } else {
        setState(() {
          _isLoading = false;
          _errorMessage = loc.translate('error_game_id_not_found');
        });
      }
    } catch (e) {
      if (!mounted) return;
      setState(() {
        _isLoading = false;
        if (e.toString().contains('TimeoutException')) {
          _errorMessage = loc.translate('error_verification_timeout');
        } else {
          _errorMessage = "Error verifying Game ID: $e";
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);

    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                loc.translate('title_enter_game_id'),
                style: theme.textTheme.headlineMedium?.copyWith(
                  color: theme.colorScheme.onPrimaryContainer,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),

              const SizedBox(height: 24),

              TextField(
                controller: _controller,
                textCapitalization: TextCapitalization.characters,
                decoration: InputDecoration(
                  labelText: loc.translate('label_game_id'),
                  border: const OutlineInputBorder(),
                  errorText: _errorMessage,
                ),
                onChanged: (_) {
                  if (_errorMessage != null) {
                    setState(() => _errorMessage = null);
                  }
                },
              ),

              const SizedBox(height: 8),

              Text(
                loc.translate('hint_game_id'),
                style: theme.textTheme.bodySmall?.copyWith(
                  color: theme.colorScheme.onSurface.withValues(alpha: 0.7),
                ),
                textAlign: TextAlign.center,
              ),

              const SizedBox(height: 12),

              OutlinedButton(
                style: OutlinedButton.styleFrom(
                  side: const BorderSide(color: Colors.white, width: 1.5),
                  minimumSize: const Size.fromHeight(50),
                ),
                onPressed: () => GeneralUtil.openPlayStore(),
                child: const Text(
                  "📲 Get Tambola Tickets App on Google Play",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                    color: Colors.white,
                  ),
                ),
              ),

              const SizedBox(height: 24),

              SizedBox(
                width: double.infinity,
                height: 56,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: theme.colorScheme.primaryContainer,
                    foregroundColor: theme.colorScheme.tertiary,
                  ),
                  onPressed: _isLoading ? null : _submitGameId,
                  child: _isLoading
                      ? const CircularProgressIndicator()
                      : Text(
                          loc.translate('btn_submit'),
                          style: const TextStyle(fontSize: 18),
                        ),
                ),
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
    );
  }
}
