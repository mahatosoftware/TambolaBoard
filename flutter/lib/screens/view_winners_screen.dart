import 'package:flutter/material.dart';
import '../localization/app_localizations.dart';
import '../models/winning_prize_entity.dart';
import '../utils/general_util.dart';
import '../utils/pdf_share_util.dart';
import '../utils/screen_size_util.dart';

class ViewWinnersScreen extends StatelessWidget {
  final List<WinningPrizeEntity> winners;
  final String gameId;

  const ViewWinnersScreen({
    super.key,
    required this.winners,
    this.gameId = "",
  });

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final isTv = ScreenSizeUtil.isTv(context);

    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            children: [
              Text(
                loc.translate('title_winner_board'),
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              Expanded(
                child: winners.isEmpty
                    ? Center(
                        child: Text(
                          loc.translate('msg_no_winners'),
                          style: theme.textTheme.bodyLarge?.copyWith(
                            color: theme.colorScheme.onSurfaceVariant,
                          ),
                        ),
                      )
                    : GridView.builder(
                        gridDelegate:
                            const SliverGridDelegateWithMaxCrossAxisExtent(
                          maxCrossAxisExtent: 400,
                          mainAxisExtent: 140,
                          crossAxisSpacing: 16,
                          mainAxisSpacing: 16,
                        ),
                        itemCount: winners.length,
                        itemBuilder: (context, index) {
                          final item = winners[index];
                          return _WinnerCard(item: item);
                        },
                      ),
              ),
              const SizedBox(height: 16),
              if (!isTv && winners.isNotEmpty)
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: theme.colorScheme.tertiaryContainer,
                      foregroundColor: theme.colorScheme.onTertiaryContainer,
                    ),
                    onPressed: () => PdfShareUtil.generateAndShareWinnerPdf(
                      context,
                      winners,
                      gameId,
                    ),
                    child: Text(
                      loc.translate('btn_share_pdf'),
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),
              const SizedBox(height: 8),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: theme.colorScheme.primaryContainer,
                    foregroundColor: theme.colorScheme.tertiary,
                  ),
                  onPressed: () => Navigator.pop(context),
                  child: Text(loc.translate('btn_back')),
                ),
              ),
              const SizedBox(height: 12),
              Text(
                GeneralUtil.getCopyrightMessage(),
                style: theme.textTheme.bodyMedium,
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _WinnerCard extends StatelessWidget {
  final WinningPrizeEntity item;

  const _WinnerCard({required this.item});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final loc = AppLocalizations.of(context)!;

    return Card(
      elevation: 6,
      color: theme.colorScheme.secondary,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              item.savedRule.ruleName,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: Colors.white,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              loc.translate('label_winners'),
              style: const TextStyle(
                fontSize: 14,
                fontWeight: FontWeight.w500,
                color: Colors.white70,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              item.winnerName == null || item.winnerName!.isEmpty
                  ? "Pending Claim"
                  : item.winnerName!,
              style: const TextStyle(
                color: Colors.yellow,
                fontSize: 15,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
