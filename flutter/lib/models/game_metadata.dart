class GameMetadata {
  final String gameId;

  GameMetadata({required this.gameId});

  Map<String, dynamic> toMap() {
    return {
      'gameId': gameId,
    };
  }

  factory GameMetadata.fromMap(Map<String, dynamic> map) {
    return GameMetadata(
      gameId: map['gameId'] as String,
    );
  }
}
