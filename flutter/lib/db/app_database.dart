import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import '../models/called_number.dart';
import '../models/game_metadata.dart';
import '../models/player_entity.dart';
import '../models/saved_rule_entity.dart';
import '../models/winning_prize_entity.dart';

class AppDatabase {
  static final AppDatabase instance = AppDatabase._init();
  static Database? _database;

  AppDatabase._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('tambola_db.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, filePath);

    return await openDatabase(
      path,
      version: 1,
      onCreate: _createDB,
    );
  }

  Future<void> _createDB(Database db, int version) async {
    await db.execute('''
      CREATE TABLE called_numbers (
        number INTEGER PRIMARY KEY,
        isLast INTEGER NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE game_metadata (
        gameId TEXT PRIMARY KEY
      )
    ''');

    await db.execute('''
      CREATE TABLE saved_rules (
        ruleId INTEGER PRIMARY KEY,
        ruleName TEXT NOT NULL,
        percentage INTEGER NOT NULL,
        quantity INTEGER NOT NULL,
        amountPerItem INTEGER NOT NULL,
        totalRuleAmount INTEGER NOT NULL
      )
    ''');

    await db.execute('''
      CREATE TABLE winning_prizes (
        prizeId INTEGER PRIMARY KEY AUTOINCREMENT,
        ruleId INTEGER NOT NULL,
        ruleName TEXT NOT NULL,
        percentage INTEGER NOT NULL,
        quantity INTEGER NOT NULL,
        amountPerItem INTEGER NOT NULL,
        totalRuleAmount INTEGER NOT NULL,
        winnerName TEXT,
        isClaimed INTEGER NOT NULL DEFAULT 0
      )
    ''');

    await db.execute('''
      CREATE TABLE players (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        gameId TEXT NOT NULL,
        name TEXT NOT NULL
      )
    ''');
  }

  // --- CALLED NUMBERS DAO ---
  Future<List<CalledNumber>> getAllCalledNumbers() async {
    final db = await instance.database;
    final maps = await db.query('called_numbers');
    return maps.map((e) => CalledNumber.fromMap(e)).toList();
  }

  Future<void> insertCalledNumber(CalledNumber number) async {
    final db = await instance.database;
    await db.insert('called_numbers', number.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<void> resetLastCalledNumber() async {
    final db = await instance.database;
    await db.update('called_numbers', {'isLast': 0});
  }

  Future<void> deleteCalledNumber(int number) async {
    final db = await instance.database;
    await db.delete('called_numbers', where: 'number = ?', whereArgs: [number]);
  }

  Future<void> setLastCalledNumber(int number) async {
    final db = await instance.database;
    await resetLastCalledNumber();
    await db.update('called_numbers', {'isLast': 1},
        where: 'number = ?', whereArgs: [number]);
  }

  Future<void> resetBoard() async {
    final db = await instance.database;
    await db.delete('called_numbers');
    await db.delete('game_metadata');
  }

  // --- GAME METADATA DAO ---
  Future<String?> getSavedGameId() async {
    final db = await instance.database;
    final result = await db.query('game_metadata', limit: 1);
    if (result.isNotEmpty) {
      return result.first['gameId'] as String;
    }
    return null;
  }

  Future<void> saveGameMetadata(GameMetadata metadata) async {
    final db = await instance.database;
    await db.delete('game_metadata');
    await db.insert('game_metadata', metadata.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // --- SAVED RULES DAO ---
  Future<List<SavedRuleEntity>> getAllSavedRules() async {
    final db = await instance.database;
    final maps = await db.query('saved_rules');
    return maps.map((e) => SavedRuleEntity.fromMap(e)).toList();
  }

  Future<void> replaceRules(List<SavedRuleEntity> rules) async {
    final db = await instance.database;
    await db.transaction((txn) async {
      await txn.delete('saved_rules');
      for (final rule in rules) {
        await txn.insert('saved_rules', rule.toMap());
      }
    });
  }

  // --- WINNING PRIZES DAO ---
  Future<List<WinningPrizeEntity>> getAllPrizes() async {
    final db = await instance.database;
    final maps = await db.query('winning_prizes', orderBy: 'prizeId ASC');
    return maps.map((e) => WinningPrizeEntity.fromMap(e)).toList();
  }

  Future<void> insertPrize(WinningPrizeEntity prize) async {
    final db = await instance.database;
    await db.insert('winning_prizes', prize.toMap());
  }

  Future<void> clearAllPrizes() async {
    final db = await instance.database;
    await db.delete('winning_prizes');
  }

  Future<void> updateWinnerName(int prizeId, String? winnerName) async {
    final db = await instance.database;
    await db.update(
      'winning_prizes',
      {'winnerName': winnerName},
      where: 'prizeId = ?',
      whereArgs: [prizeId],
    );
  }

  Future<void> claimPrize(int prizeId) async {
    final db = await instance.database;
    await db.update(
      'winning_prizes',
      {'isClaimed': 1},
      where: 'prizeId = ?',
      whereArgs: [prizeId],
    );
  }

  Future<void> unclaimPrize(int prizeId) async {
    final db = await instance.database;
    await db.update(
      'winning_prizes',
      {'isClaimed': 0},
      where: 'prizeId = ?',
      whereArgs: [prizeId],
    );
  }

  // --- PLAYERS DAO ---
  Future<List<PlayerEntity>> getPlayers(String gameId) async {
    final db = await instance.database;
    final maps = await db.query(
      'players',
      where: 'gameId = ?',
      whereArgs: [gameId],
    );
    return maps.map((e) => PlayerEntity.fromMap(e)).toList();
  }

  Future<void> deletePlayersByGameId(String gameId) async {
    final db = await instance.database;
    await db.delete('players', where: 'gameId = ?', whereArgs: [gameId]);
  }

  Future<void> insertPlayers(List<PlayerEntity> players) async {
    final db = await instance.database;
    await db.transaction((txn) async {
      for (final p in players) {
        await txn.insert('players', p.toMap());
      }
    });
  }
}
