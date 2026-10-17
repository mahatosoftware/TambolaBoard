import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:sqflite/sqflite.dart';
import 'package:tambola_board/localization/app_localizations.dart';
import 'package:tambola_board/screens/game_screen.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    SharedPreferences.setMockInitialValues({});
    databaseFactory = databaseFactorySqflitePlugin;

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
      const MethodChannel('plugins.flutter.io/shared_preferences'),
      (MethodCall methodCall) async {
        if (methodCall.method == 'getAll') {
          return <String, dynamic>{};
        }
        return null;
      },
    );

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
      const MethodChannel('flutter_tts'),
      (MethodCall methodCall) async {
        if (methodCall.method == 'getVoices') {
          return [
            {'name': 'en-us-x-sfg#female', 'locale': 'en-US'}
          ];
        }
        return 1;
      },
    );

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
      const MethodChannel('com.tekartik.sqflite'),
      (MethodCall methodCall) async {
        if (methodCall.method == 'getDatabasesPath') {
          return '/mock/path';
        }
        if (methodCall.method == 'openDatabase') {
          return 1;
        }
        if (methodCall.method == 'query') {
          return <Map<String, dynamic>>[];
        }
        if (methodCall.method == 'insert' ||
            methodCall.method == 'update' ||
            methodCall.method == 'delete' ||
            methodCall.method == 'execute') {
          return 0;
        }
        return null;
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(const MethodChannel('flutter_tts'), null);
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
            const MethodChannel('com.tekartik.sqflite'), null);
  });

  testWidgets('GameScreen displays LAST NUMBER and REMAINING counters in portrait and landscape',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(400, 800);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(
      const MaterialApp(
        localizationsDelegates: [
          AppLocalizations.delegate,
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        supportedLocales: [Locale('en')],
        home: GameScreen(isNewGame: true, intentGameId: "123456"),
      ),
    );
    await tester.pump(const Duration(milliseconds: 100));
    await tester.pump(const Duration(milliseconds: 100));

    // Verify both header labels are present
    expect(find.text('LAST NUMBER'), findsOneWidget);
    expect(find.text('REMAINING'), findsOneWidget);

    // Find the scoreboard Card
    final scoreboardCard = find.byWidgetPredicate(
      (w) => w is Card && find.descendant(of: find.byWidget(w), matching: find.text('REMAINING')).evaluate().isNotEmpty,
    );
    expect(scoreboardCard, findsOneWidget);

    // Initial state: Last number is "--", Remaining is "90"
    expect(find.descendant(of: scoreboardCard, matching: find.text('--')), findsOneWidget);
    expect(find.descendant(of: scoreboardCard, matching: find.text('90')), findsOneWidget);

    // Call next number
    final callNextBtn = find.text('Call Next');
    expect(callNextBtn, findsOneWidget);
    await tester.tap(callNextBtn);
    await tester.pump(const Duration(milliseconds: 100));

    // Remaining inside card should now be 89
    expect(find.descendant(of: scoreboardCard, matching: find.text('89')), findsOneWidget);
    // Last number should no longer be "--"
    expect(find.descendant(of: scoreboardCard, matching: find.text('--')), findsNothing);
  });

  testWidgets('GameScreen displays LAST NUMBER and REMAINING in tablet/landscape mode without overflow',
      (WidgetTester tester) async {
    // Landscape / wide layout
    tester.view.physicalSize = const Size(1024, 768);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(
      const MaterialApp(
        localizationsDelegates: [
          AppLocalizations.delegate,
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
          GlobalCupertinoLocalizations.delegate,
        ],
        supportedLocales: [Locale('en')],
        home: GameScreen(isNewGame: true, intentGameId: ""),
      ),
    );
    await tester.pump(const Duration(milliseconds: 100));
    await tester.pump(const Duration(milliseconds: 100));

    final scoreboardCard = find.byWidgetPredicate(
      (w) => w is Card && find.descendant(of: find.byWidget(w), matching: find.text('REMAINING')).evaluate().isNotEmpty,
    );
    expect(scoreboardCard, findsOneWidget);
    expect(find.descendant(of: scoreboardCard, matching: find.text('LAST NUMBER')), findsOneWidget);
    expect(find.descendant(of: scoreboardCard, matching: find.text('REMAINING')), findsOneWidget);
    expect(find.descendant(of: scoreboardCard, matching: find.text('90')), findsOneWidget);
  });
}
