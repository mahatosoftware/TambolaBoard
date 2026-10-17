import 'package:flutter/material.dart';
import '../../db/app_database.dart';
import '../../localization/app_localizations.dart';
import '../../models/player_entity.dart';
import '../../models/winning_prize_entity.dart';
import '../../utils/pdf_share_util.dart';
import '../../utils/screen_size_util.dart';
import 'multi_select_player_dialog.dart';

class WinnerBoardDialog extends StatefulWidget {
  final String gameId;
  final VoidCallback onReloadPlayers;

  const WinnerBoardDialog({
    super.key,
    required this.gameId,
    required this.onReloadPlayers,
  });

  @override
  State<WinnerBoardDialog> createState() => _WinnerBoardDialogState();
}

class _WinnerBoardDialogState extends State<WinnerBoardDialog> {
  List<WinningPrizeEntity> _prizes = [];
  List<PlayerEntity> _players = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadData();
    if (widget.gameId.length == 6) {
      widget.onReloadPlayers();
    }
  }

  Future<void> _loadData() async {
    final prizes = await AppDatabase.instance.getAllPrizes();
    final players = await AppDatabase.instance.getPlayers(widget.gameId);
    if (mounted) {
      setState(() {
        _prizes = prizes;
        _players = players;
        _isLoading = false;
      });
    }
  }

  Future<void> _claimToggle(WinningPrizeEntity prize, bool claimed) async {
    if (!claimed && prize.isClaimed) {
      final loc = AppLocalizations.of(context)!;
      final confirm = await showDialog<bool>(
        context: context,
        builder: (ctx) => AlertDialog(
          title: Text(
            loc.translate('dialog_unclaim_title'),
            style: const TextStyle(color: Colors.amber, fontWeight: FontWeight.bold),
          ),
          content: Text(
            loc.translate('dialog_unclaim_message'),
            style: const TextStyle(color: Colors.white),
          ),
          actions: [
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blueGrey.shade700,
                foregroundColor: Colors.white,
              ),
              onPressed: () => Navigator.pop(ctx, false),
              child: Text(
                loc.translate('dialog_no'),
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red.shade700,
                foregroundColor: Colors.white,
              ),
              onPressed: () => Navigator.pop(ctx, true),
              child: Text(
                loc.translate('dialog_yes'),
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
      );
      if (confirm != true) return;
    }

    if (claimed) {
      await AppDatabase.instance.claimPrize(prize.prizeId!);
    } else {
      await AppDatabase.instance.unclaimPrize(prize.prizeId!);
    }
    _loadData();
  }

  Future<void> _updateWinnerName(int prizeId, String name) async {
    await AppDatabase.instance.updateWinnerName(prizeId, name);
    _loadData();
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final isTv = ScreenSizeUtil.isTv(context);
    final isModerated = widget.gameId.length == 6;

    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      backgroundColor: theme.colorScheme.primary,
      child: Container(
        constraints: const BoxConstraints(maxWidth: 500, maxHeight: 600),
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Stack(
              children: [
                Center(
                  child: Text(
                    loc.translate('dialog_winner_board_title'),
                    style: const TextStyle(
                      fontWeight: FontWeight.w900,
                      fontSize: 24,
                      color: Colors.white,
                    ),
                  ),
                ),
                if (isModerated)
                  Positioned(
                    right: 0,
                    top: 0,
                    child: IconButton(
                      icon: const Icon(Icons.refresh),
                      onPressed: () {
                        widget.onReloadPlayers();
                        _loadData();
                      },
                    ),
                  ),
              ],
            ),
            const SizedBox(height: 4),
            Text(
              loc.translate('winner_board_instruction'),
              style: const TextStyle(fontSize: 12, color: Colors.white70),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 12),
            _WinnerHeader(loc: loc),
            const SizedBox(height: 8),
            Expanded(
              child: _isLoading
                  ? const Center(child: CircularProgressIndicator())
                  : ListView.builder(
                      itemCount: _prizes.length,
                      itemBuilder: (context, index) {
                        final prize = _prizes[index];
                        return _WinnerItemRow(
                          prize: prize,
                          players: _players,
                          isModerated: isModerated,
                          onClaimToggle: (val) => _claimToggle(prize, val),
                          onUpdateWinnerName: (name) =>
                              _updateWinnerName(prize.prizeId!, name),
                          onReloadPlayers: () {
                            widget.onReloadPlayers();
                            _loadData();
                          },
                        );
                      },
                    ),
            ),
            const SizedBox(height: 12),
            if (!isTv && _prizes.isNotEmpty)
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: theme.colorScheme.tertiaryContainer,
                    foregroundColor: theme.colorScheme.onTertiaryContainer,
                  ),
                  onPressed: () => PdfShareUtil.generateAndShareWinnerPdf(
                    context,
                    _prizes,
                    widget.gameId,
                  ),
                  child: Text(
                    loc.translate('btn_share_pdf'),
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 15,
                    ),
                  ),
                ),
              ),
            const SizedBox(height: 8),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () => Navigator.pop(context),
                child: Text(
                  loc.translate('btn_close_board'),
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _WinnerHeader extends StatelessWidget {
  final AppLocalizations loc;

  const _WinnerHeader({required this.loc});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      decoration: BoxDecoration(
        color: const Color(0xFF38006B),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          Expanded(
            child: Text(
              loc.translate('header_prize'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                color: Colors.white,
                fontSize: 13,
              ),
            ),
          ),
          Expanded(
            child: Text(
              loc.translate('header_winner'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                color: Colors.white,
                fontSize: 13,
              ),
            ),
          ),
          Text(
            loc.translate('header_claim'),
            style: const TextStyle(
              fontWeight: FontWeight.w900,
              color: Colors.white,
              fontSize: 13,
            ),
          ),
        ],
      ),
    );
  }
}

class _WinnerItemRow extends StatefulWidget {
  final WinningPrizeEntity prize;
  final List<PlayerEntity> players;
  final bool isModerated;
  final Function(bool) onClaimToggle;
  final Function(String) onUpdateWinnerName;
  final VoidCallback onReloadPlayers;

  const _WinnerItemRow({
    required this.prize,
    required this.players,
    required this.isModerated,
    required this.onClaimToggle,
    required this.onUpdateWinnerName,
    required this.onReloadPlayers,
  });

  @override
  State<_WinnerItemRow> createState() => _WinnerItemRowState();
}

class _WinnerItemRowState extends State<_WinnerItemRow> {
  late TextEditingController _controller;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.prize.winnerName ?? "");
  }

  @override
  void didUpdateWidget(covariant _WinnerItemRow oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.prize.winnerName != widget.prize.winnerName) {
      _controller.text = widget.prize.winnerName ?? "";
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _openMultiSelectDialog() {
    final currentSelections = (_controller.text.isEmpty)
        ? <String>{}
        : _controller.text.split(',').map((e) => e.trim()).toSet();

    showDialog(
      context: context,
      builder: (ctx) => MultiSelectPlayerDialog(
        allPlayers: widget.players,
        initialSelections: currentSelections,
        onConfirm: (selected) {
          final newName = (selected..sort()).join(", ");
          _controller.text = newName;
          widget.onUpdateWinnerName(newName);
        },
        onReloadPlayers: widget.onReloadPlayers,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final isClaimed = widget.prize.isClaimed;

    return Container(
      padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 8),
      margin: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        children: [
          Expanded(
            child: Text(
              widget.prize.savedRule.ruleName.toUpperCase(),
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 15,
                color: isClaimed ? Colors.red : Colors.white,
              ),
            ),
          ),
          Expanded(
            child: widget.isModerated
                ? InkWell(
                    onTap: isClaimed ? null : _openMultiSelectDialog,
                    child: InputDecorator(
                      decoration: InputDecoration(
                        hintText: loc.translate('placeholder_select_winners'),
                        hintStyle: const TextStyle(color: Colors.grey, fontSize: 13),
                        contentPadding:
                            const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                        border: const OutlineInputBorder(),
                        suffixIcon: isClaimed
                            ? null
                            : const Icon(Icons.arrow_drop_down, color: Colors.white),
                      ),
                      child: Text(
                        _controller.text,
                        style: const TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                          fontSize: 14,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  )
                : TextField(
                    controller: _controller,
                    enabled: !isClaimed,
                    onChanged: (val) => widget.onUpdateWinnerName(val),
                    style: const TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 14,
                    ),
                    decoration: InputDecoration(
                      hintText: loc.translate('placeholder_add_winner'),
                      hintStyle: const TextStyle(color: Colors.grey, fontSize: 13),
                      contentPadding:
                          const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                      border: const OutlineInputBorder(),
                    ),
                  ),
          ),
          Checkbox(
            value: isClaimed,
            activeColor: const Color(0xFF1E88E5),
            onChanged: (val) {
              if (val != null) {
                widget.onClaimToggle(val);
              }
            },
          ),
        ],
      ),
    );
  }
}
