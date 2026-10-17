import 'package:flutter/material.dart';
import '../localization/app_localizations.dart';
import '../models/tambola_rule.dart';
import '../utils/general_util.dart';
import '../utils/screen_size_util.dart';
import 'points_distribution_screen.dart';

class RuleSelectionScreen extends StatefulWidget {
  final String? gameId;
  final bool isModerated;

  const RuleSelectionScreen({
    super.key,
    this.gameId,
    this.isModerated = false,
  });

  @override
  State<RuleSelectionScreen> createState() => _RuleSelectionScreenState();
}

class _RuleSelectionScreenState extends State<RuleSelectionScreen> {
  final Set<int> _selectedRuleIds = {};

  void _toggleRule(int ruleId) {
    setState(() {
      if (_selectedRuleIds.contains(ruleId)) {
        _selectedRuleIds.remove(ruleId);
      } else {
        _selectedRuleIds.add(ruleId);
      }
    });
  }

  void _showRuleDetail(TambolaRule rule) {
    showDialog(
      context: context,
      builder: (ctx) => _RuleDetailDialog(rule: rule),
    );
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final isTv = ScreenSizeUtil.isTv(context);

    return Scaffold(
      backgroundColor: theme.colorScheme.primary,
      appBar: AppBar(
        title: Text(loc.translate('title_select_rules')),
        centerTitle: true,
        backgroundColor: theme.colorScheme.primary,
        foregroundColor: theme.colorScheme.onPrimaryContainer,
      ),
      bottomNavigationBar: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              SizedBox(
                width: double.infinity,
                height: 56,
                child: _DistributePointsButton(
                  isEnabled: _selectedRuleIds.isNotEmpty,
                  onPressed: _selectedRuleIds.isEmpty
                      ? null
                      : () {
                          final selectedRules = tambolaRules
                              .where((r) => _selectedRuleIds.contains(r.id))
                              .toList();
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => PointsDistributionScreen(
                                selectedRules: selectedRules,
                                gameId: widget.gameId,
                                isModerated: widget.isModerated,
                              ),
                            ),
                          );
                        },
                  text: loc.translate(
                    'btn_distribute_points',
                    [_selectedRuleIds.length],
                  ),
                ),
              ),
              const SizedBox(height: 8),
              Text(
                GeneralUtil.getCopyrightMessage(),
                style: theme.textTheme.bodyMedium?.copyWith(
                  color: Colors.white70,
                ),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
      body: GridView.builder(
        padding: const EdgeInsets.all(12),
        gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
          maxCrossAxisExtent: 180,
          childAspectRatio: 1.0,
          crossAxisSpacing: 12,
          mainAxisSpacing: 12,
        ),
        itemCount: tambolaRules.length,
        itemBuilder: (context, index) {
          final rule = tambolaRules[index];
          final isSelected = _selectedRuleIds.contains(rule.id);

          return _RuleCard(
            rule: rule,
            isSelected: isSelected,
            isTv: isTv,
            onToggle: () => _toggleRule(rule.id),
            onInfo: () => _showRuleDetail(rule),
          );
        },
      ),
    );
  }
}

class _DistributePointsButton extends StatefulWidget {
  final bool isEnabled;
  final VoidCallback? onPressed;
  final String text;

  const _DistributePointsButton({
    required this.isEnabled,
    required this.onPressed,
    required this.text,
  });

  @override
  State<_DistributePointsButton> createState() => _DistributePointsButtonState();
}

class _DistributePointsButtonState extends State<_DistributePointsButton> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isFocused = _isFocused && widget.isEnabled;

    return AnimatedScale(
      scale: isFocused ? 1.03 : 1.0,
      duration: const Duration(milliseconds: 150),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(28),
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
              borderRadius: BorderRadius.circular(28),
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

class _RuleCard extends StatefulWidget {
  final TambolaRule rule;
  final bool isSelected;
  final bool isTv;
  final VoidCallback onToggle;
  final VoidCallback onInfo;

  const _RuleCard({
    required this.rule,
    required this.isSelected,
    required this.isTv,
    required this.onToggle,
    required this.onInfo,
  });

  @override
  State<_RuleCard> createState() => _RuleCardState();
}

class _RuleCardState extends State<_RuleCard> {
  bool _isFocused = false;

  IconData _getIconForType(TambolaRuleType type) {
    switch (type) {
      case TambolaRuleType.fullHouse:
      case TambolaRuleType.secondHouse:
      case TambolaRuleType.thirdHouse:
        return Icons.home;
      case TambolaRuleType.earlyFive:
      case TambolaRuleType.star:
        return Icons.star;
      case TambolaRuleType.topLine:
      case TambolaRuleType.middleLine:
      case TambolaRuleType.bottomLine:
        return Icons.horizontal_rule;
      case TambolaRuleType.corner:
        return Icons.aspect_ratio;
      case TambolaRuleType.diamond:
        return Icons.diamond;
      case TambolaRuleType.pyramid:
      case TambolaRuleType.invertedPyramid:
        return Icons.change_history;
      case TambolaRuleType.odds:
      case TambolaRuleType.even:
        return Icons.tag;
      case TambolaRuleType.firstHalf:
      case TambolaRuleType.secondHalf:
        return Icons.view_column;
      case TambolaRuleType.breakfast:
      case TambolaRuleType.lunch:
      case TambolaRuleType.dinner:
        return Icons.restaurant;
      case TambolaRuleType.temperature:
        return Icons.thermostat;
      case TambolaRuleType.belowFifty:
      case TambolaRuleType.aboveFifty:
        return Icons.pin;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isSelected = widget.isSelected;
    final isFocused = _isFocused;

    return AnimatedScale(
      scale: isFocused ? 1.05 : 1.0,
      duration: const Duration(milliseconds: 150),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
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
          elevation: isFocused ? 12 : (isSelected ? 6 : 2),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
            side: BorderSide(
              color: isFocused
                  ? const Color(0xFFFFD700)
                  : (isSelected
                      ? theme.colorScheme.primaryContainer
                      : Colors.grey.withValues(alpha: 0.3)),
              width: isFocused ? 3.5 : (isSelected ? 3 : 1),
            ),
          ),
          color: isFocused
              ? const Color(0xFF3C0084)
              : (isSelected
                  ? theme.colorScheme.tertiaryContainer
                  : theme.colorScheme.secondary),
          child: InkWell(
            onFocusChange: (focused) {
              setState(() {
                _isFocused = focused;
              });
            },
            onTap: widget.onToggle,
            onLongPress: widget.onInfo,
            borderRadius: BorderRadius.circular(16),
            child: Stack(
              children: [
                if (!widget.isTv)
                  Positioned(
                    top: 4,
                    right: 4,
                    child: IconButton(
                      icon: const Icon(Icons.info_outline, size: 20),
                      onPressed: widget.onInfo,
                    ),
                  ),
                if (isSelected)
                  Positioned(
                    top: 8,
                    left: 8,
                    child: Container(
                      padding: const EdgeInsets.all(2),
                      decoration: const BoxDecoration(
                        color: Colors.green,
                        shape: BoxShape.circle,
                      ),
                      child: const Icon(Icons.check, size: 16, color: Colors.white),
                    ),
                  ),
                Center(
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          _getIconForType(widget.rule.type),
                          size: 40,
                          color: isFocused
                              ? const Color(0xFFFFD700)
                              : (isSelected
                                  ? theme.colorScheme.onTertiaryContainer
                                  : theme.colorScheme.onSecondary),
                        ),
                        const SizedBox(height: 8),
                        Text(
                          widget.rule.name,
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 14,
                            color: isFocused ? const Color(0xFFFFD700) : Colors.white,
                          ),
                          textAlign: TextAlign.center,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _RuleDetailDialog extends StatelessWidget {
  final TambolaRule rule;

  const _RuleDetailDialog({required this.rule});

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;

    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      backgroundColor: const Color(0xFF3C0084),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  rule.name,
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.close),
                  onPressed: () => Navigator.pop(context),
                ),
              ],
            ),
            const SizedBox(height: 16),

            // 3x9 Ticket Visualizer
            _TicketVisualizer(winningPattern: rule.winningPattern),

            const SizedBox(height: 16),
            Text(
              rule.description,
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.white70, fontSize: 14),
            ),
            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () => Navigator.pop(context),
                child: Text(
                  loc.translate('btn_close'),
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

class _TicketVisualizer extends StatelessWidget {
  final List<int> winningPattern;

  const _TicketVisualizer({required this.winningPattern});

  @override
  Widget build(BuildContext context) {
    final ticket = [
      [1, null, 23, null, 41, 52, null, 78, null],
      [null, 15, null, 34, null, 56, 63, null, 82],
      [9, null, 27, 38, null, null, null, 74, 90],
    ];

    return Container(
      padding: const EdgeInsets.all(6),
      decoration: BoxDecoration(
        color: const Color(0xFFE0F7FA),
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: const Color(0xFF006064), width: 2),
      ),
      child: Column(
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.symmetric(vertical: 4),
            decoration: BoxDecoration(
              color: const Color(0xFF006064),
              borderRadius: BorderRadius.circular(6),
            ),
            child: const Text(
              "TAMBOLA BOARD",
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 12,
                letterSpacing: 1,
              ),
              textAlign: TextAlign.center,
            ),
          ),
          const SizedBox(height: 6),
          Column(
            children: List.generate(3, (rowIndex) {
              return Row(
                children: List.generate(9, (colIndex) {
                  final number = ticket[rowIndex][colIndex];
                  final index = rowIndex * 9 + colIndex;
                  final active = winningPattern.contains(index) && number != null;

                  return Expanded(
                    child: AspectRatio(
                      aspectRatio: 1.0,
                      child: Container(
                        margin: const EdgeInsets.all(1),
                        decoration: BoxDecoration(
                          color: number == null
                              ? Colors.transparent
                              : active
                                  ? const Color(0xFF00BCD4)
                                  : Colors.white,
                          borderRadius: BorderRadius.circular(4),
                          border: number != null
                              ? Border.all(color: Colors.grey.shade300)
                              : null,
                        ),
                        alignment: Alignment.center,
                        child: number != null
                            ? Text(
                                "$number",
                                style: TextStyle(
                                  fontSize: 11,
                                  fontWeight:
                                      active ? FontWeight.bold : FontWeight.normal,
                                  color: active ? Colors.white : Colors.black,
                                ),
                              )
                            : null,
                      ),
                    ),
                  );
                }),
              );
            }),
          ),
        ],
      ),
    );
  }
}
