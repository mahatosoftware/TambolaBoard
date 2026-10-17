import 'package:flutter/material.dart';
import '../../localization/app_localizations.dart';
import '../../models/player_entity.dart';

class MultiSelectPlayerDialog extends StatefulWidget {
  final List<PlayerEntity> allPlayers;
  final Set<String> initialSelections;
  final Function(List<String>) onConfirm;
  final VoidCallback onReloadPlayers;

  const MultiSelectPlayerDialog({
    super.key,
    required this.allPlayers,
    required this.initialSelections,
    required this.onConfirm,
    required this.onReloadPlayers,
  });

  @override
  State<MultiSelectPlayerDialog> createState() =>
      _MultiSelectPlayerDialogState();
}

class _MultiSelectPlayerDialogState extends State<MultiSelectPlayerDialog> {
  late Set<String> _selectedNames;
  String _searchQuery = "";

  @override
  void initState() {
    super.initState();
    _selectedNames = Set.from(widget.initialSelections);
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);

    final filteredPlayers = widget.allPlayers.where((p) {
      if (_searchQuery.isEmpty) return true;
      return p.name.toLowerCase().contains(_searchQuery.toLowerCase());
    }).toList();

    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Container(
        constraints: const BoxConstraints(maxHeight: 600, maxWidth: 500),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              loc.translate('dialog_multi_select_title'),
              style: theme.textTheme.titleLarge?.copyWith(
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: TextField(
                    onChanged: (val) => setState(() => _searchQuery = val),
                    decoration: InputDecoration(
                      hintText: loc.translate('placeholder_search_player'),
                      prefixIcon: const Icon(Icons.search),
                      border: const OutlineInputBorder(),
                      contentPadding:
                          const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                IconButton(
                  icon: const Icon(Icons.refresh),
                  tooltip: loc.translate('desc_reload_players'),
                  onPressed: widget.onReloadPlayers,
                ),
              ],
            ),
            const SizedBox(height: 12),
            Expanded(
              child: ListView.builder(
                itemCount: filteredPlayers.length,
                itemBuilder: (context, index) {
                  final player = filteredPlayers[index];
                  final isSelected = _selectedNames.contains(player.name);

                  return CheckboxListTile(
                    value: isSelected,
                    title: Text(player.name),
                    onChanged: (checked) {
                      setState(() {
                        if (checked == true) {
                          _selectedNames.add(player.name);
                        } else {
                          _selectedNames.remove(player.name);
                        }
                      });
                    },
                  );
                },
              ),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: Text(
                    loc.translate('btn_cancel'),
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                const SizedBox(width: 12),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                  onPressed: () {
                    widget.onConfirm(_selectedNames.toList());
                    Navigator.pop(context);
                  },
                  child: Text(
                    loc.translate('btn_confirm'),
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
