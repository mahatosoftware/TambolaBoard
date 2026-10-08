class SavedRuleEntity {
  final int ruleId;
  final String ruleName;
  final int percentage;
  final int quantity;
  final int amountPerItem;
  final int totalRuleAmount;

  SavedRuleEntity({
    required this.ruleId,
    required this.ruleName,
    required this.percentage,
    required this.quantity,
    required this.amountPerItem,
    required this.totalRuleAmount,
  });

  Map<String, dynamic> toMap() {
    return {
      'ruleId': ruleId,
      'ruleName': ruleName,
      'percentage': percentage,
      'quantity': quantity,
      'amountPerItem': amountPerItem,
      'totalRuleAmount': totalRuleAmount,
    };
  }

  factory SavedRuleEntity.fromMap(Map<String, dynamic> map) {
    return SavedRuleEntity(
      ruleId: map['ruleId'] as int,
      ruleName: map['ruleName'] as String,
      percentage: map['percentage'] as int,
      quantity: map['quantity'] as int,
      amountPerItem: map['amountPerItem'] as int,
      totalRuleAmount: map['totalRuleAmount'] as int,
    );
  }
}
