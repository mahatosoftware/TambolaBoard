class PlayerEntity {
  final int? id;
  final String gameId;
  final String name;

  PlayerEntity({
    this.id,
    required this.gameId,
    required this.name,
  });

  Map<String, dynamic> toMap() {
    final map = <String, dynamic>{
      'gameId': gameId,
      'name': name,
    };
    if (id != null) {
      map['id'] = id;
    }
    return map;
  }

  factory PlayerEntity.fromMap(Map<String, dynamic> map) {
    return PlayerEntity(
      id: map['id'] as int?,
      gameId: map['gameId'] as String,
      name: map['name'] as String,
    );
  }
}
