class CalledNumber {
  final int number;
  final bool isLast;

  CalledNumber({
    required this.number,
    this.isLast = false,
  });

  Map<String, dynamic> toMap() {
    return {
      'number': number,
      'isLast': isLast ? 1 : 0,
    };
  }

  factory CalledNumber.fromMap(Map<String, dynamic> map) {
    return CalledNumber(
      number: map['number'] as int,
      isLast: (map['isLast'] as int) == 1,
    );
  }
}
