import 'package:flutter/material.dart';
import '../db/app_database.dart';
import '../localization/app_localizations.dart';
import '../models/saved_rule_entity.dart';
import '../models/tambola_rule.dart';
import '../utils/general_util.dart';
import 'game_screen.dart';

const Color purpleBgStart = Color(0xFF4A148C);
const Color purpleBgEnd = Color(0xFF7B1FA2);

class PointsDistributionScreen extends StatefulWidget {
  final List<TambolaRule> selectedRules;
  final String? gameId;
  final bool isModerated;

  const PointsDistributionScreen({
    super.key,
    required this.selectedRules,
    this.gameId,
    this.isModerated = false,
  });

  @override
  State<PointsDistributionScreen> createState() =>
      _PointsDistributionScreenState();
}

class _PointsDistributionScreenState
    extends State<PointsDistributionScreen> {
  late List<TambolaRule> _rules;

  @override
  void initState() {
    super.initState();
    _rules = List.from(widget.selectedRules);
    _autoDistribute();
  }

  void _updateQuantity(int index, int delta) {
    setState(() {
      final current = _rules[index];
      final newQty = (current.quantity + delta).clamp(1, 99);
      _rules[index] = current.copyWith(quantity: newQty);
      _autoDistribute();
    });
  }

  void _autoDistribute() {
    if (_rules.isEmpty) return;

    final fullHouses = _rules.where((r) => r.isFullHouse).toList();
    final others = _rules.where((r) => !r.isFullHouse).toList();

    int fullHousePercent = 0;
    if (fullHouses.length == 1) {
      fullHousePercent = 40;
    } else if (fullHouses.length > 1) {
      fullHousePercent = 60;
    }

    final remainingPercent = 100 - fullHousePercent;
    final fullHouseWeight =
        fullHouses.fold<int>(0, (sum, item) => sum + item.weight);
    final otherTotalWeight =
        others.fold<int>(0, (sum, item) => sum + (item.weight * item.quantity));

    final updated = _rules.map((rule) {
      if (rule.isFullHouse && fullHouses.isNotEmpty) {
        final percent = (fullHousePercent * rule.weight) ~/ fullHouseWeight;
        return rule.copyWith(percentage: percent);
      } else if (!rule.isFullHouse && otherTotalWeight > 0) {
        final percent =
            (((rule.weight * rule.quantity) / otherTotalWeight) * remainingPercent)
                .toInt();
        return rule.copyWith(percentage: percent);
      }
      return rule.copyWith(percentage: 0);
    }).toList();

    _rules = _normalize(updated);
  }

  List<TambolaRule> _normalize(List<TambolaRule> rules) {
    final total = rules.fold<int>(0, (sum, item) => sum + item.percentage);
    final diff = 100 - total;
    if (diff == 0 || rules.isEmpty) return rules;

    TambolaRule maxRule = rules.first;
    for (var r in rules) {
      if (r.percentage > maxRule.percentage) {
        maxRule = r;
      }
    }

    return rules.map((r) {
      if (r.id == maxRule.id) {
        return r.copyWith(percentage: r.percentage + diff);
      }
      return r;
    }).toList();
  }

  Future<void> _saveRulesAndStartGame() async {
    const totalPoints = 1000;
    final validRules = _rules.where((r) => r.quantity > 0).toList();

    final savedRuleEntities = validRules.map((rule) {
      final amountPerItem = ((rule.percentage / 100.0) * totalPoints).toInt();
      final totalRuleAmount = amountPerItem * rule.quantity;

      return SavedRuleEntity(
        ruleId: rule.id,
        ruleName: rule.name,
        percentage: rule.percentage,
        quantity: rule.quantity,
        amountPerItem: amountPerItem,
        totalRuleAmount: totalRuleAmount,
      );
    }).toList();

    await AppDatabase.instance.replaceRules(savedRuleEntities);

    final finalGameId = widget.gameId ?? GeneralUtil.generateGameId();

    if (!mounted) return;
    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(
        builder: (context) => GameScreen(
          isNewGame: true,
          intentGameId: finalGameId,
        ),
      ),
      (route) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);

    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [purpleBgStart, purpleBgEnd],
          ),
        ),
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                Text(
                  loc.translate('title_select_quantity'),
                  style: const TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 12),
                Expanded(
                  child: Card(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16),
                    ),
                    color: Colors.black.withValues(alpha: 0.25),
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        children: [
                          _RulesHeader(loc: loc),
                          const Divider(color: Colors.white24),
                          Expanded(
                            child: ListView.builder(
                              itemCount: _rules.length,
                              itemBuilder: (context, index) {
                                final rule = _rules[index];
                                return _RuleRow(
                                  rule: rule,
                                  onMinus: () => _updateQuantity(index, -1),
                                  onPlus: () => _updateQuantity(index, 1),
                                );
                              },
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 12),
                SizedBox(
                  width: double.infinity,
                  height: 60,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: theme.colorScheme.primaryContainer,
                      foregroundColor: theme.colorScheme.tertiary,
                    ),
                    onPressed: _saveRulesAndStartGame,
                    child: Text(
                      loc.translate('btn_save_rules'),
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w900,
                      ),
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
      ),
    );
  }
}

class _RulesHeader extends StatelessWidget {
  final AppLocalizations loc;

  const _RulesHeader({required this.loc});

  @override
  Widget build(BuildContext context) {
    const style = TextStyle(
      fontWeight: FontWeight.bold,
      fontSize: 11,
      color: Colors.lightGreenAccent,
    );

    return Row(
      children: [
        Expanded(
          flex: 3,
          child: Text(loc.translate('header_rule'), style: style),
        ),
        Expanded(
          flex: 2,
          child: Text(
            loc.translate('header_quantity'),
            style: style,
            textAlign: TextAlign.center,
          ),
        ),
      ],
    );
  }
}

class _RuleRow extends StatelessWidget {
  final TambolaRule rule;
  final VoidCallback onMinus;
  final VoidCallback onPlus;

  const _RuleRow({
    required this.rule,
    required this.onMinus,
    required this.onPlus,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        children: [
          Expanded(
            flex: 3,
            child: Text(
              rule.name,
              style: const TextStyle(color: Colors.white, fontSize: 16),
            ),
          ),
          Expanded(
            flex: 2,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                IconButton(
                  icon: const Icon(Icons.remove_circle_outline, color: Colors.white),
                  onPressed: onMinus,
                ),
                SizedBox(
                  width: 32,
                  child: Text(
                    "${rule.quantity}",
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.add_circle_outline, color: Colors.white),
                  onPressed: onPlus,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
