import 'saved_rule_entity.dart';

class WinningPrizeEntity {
  final int? prizeId;
  final SavedRuleEntity savedRule;
  final String? winnerName;
  final bool isClaimed;

  WinningPrizeEntity({
    this.prizeId,
    required this.savedRule,
    this.winnerName,
    this.isClaimed = false,
  });

  WinningPrizeEntity copyWith({
    int? prizeId,
    SavedRuleEntity? savedRule,
    String? winnerName,
    bool? isClaimed,
  }) {
    return WinningPrizeEntity(
      prizeId: prizeId ?? this.prizeId,
      savedRule: savedRule ?? this.savedRule,
      winnerName: winnerName ?? this.winnerName,
      isClaimed: isClaimed ?? this.isClaimed,
    );
  }

  Map<String, dynamic> toMap() {
    final map = <String, dynamic>{
      'ruleId': savedRule.ruleId,
      'ruleName': savedRule.ruleName,
      'percentage': savedRule.percentage,
      'quantity': savedRule.quantity,
      'amountPerItem': savedRule.amountPerItem,
      'totalRuleAmount': savedRule.totalRuleAmount,
      'winnerName': winnerName,
      'isClaimed': isClaimed ? 1 : 0,
    };
    if (prizeId != null) {
      map['prizeId'] = prizeId;
    }
    return map;
  }

  factory WinningPrizeEntity.fromMap(Map<String, dynamic> map) {
    final savedRule = SavedRuleEntity(
      ruleId: map['ruleId'] as int,
      ruleName: map['ruleName'] as String,
      percentage: map['percentage'] as int,
      quantity: map['quantity'] as int,
      amountPerItem: map['amountPerItem'] as int,
      totalRuleAmount: map['totalRuleAmount'] as int,
    );

    return WinningPrizeEntity(
      prizeId: map['prizeId'] as int?,
      savedRule: savedRule,
      winnerName: map['winnerName'] as String?,
      isClaimed: (map['isClaimed'] as int) == 1,
    );
  }
}
