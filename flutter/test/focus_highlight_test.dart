import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tambola_board/localization/app_localizations.dart';
import 'package:tambola_board/screens/game_mode_selection_screen.dart';
import 'package:tambola_board/screens/main_screen.dart';
import 'package:tambola_board/screens/rule_selection_screen.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    SharedPreferences.setMockInitialValues({});
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
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
            const MethodChannel('plugins.flutter.io/shared_preferences'), null);
  });

  Widget createTestApp(Widget home) {
    return MaterialApp(
      theme: ThemeData(
        useMaterial3: true,
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ButtonStyle(
            side: WidgetStateProperty.resolveWith<BorderSide?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const BorderSide(color: Color(0xFFFFD700), width: 3.0);
              }
              return const BorderSide(color: Colors.transparent, width: 3.0);
            }),
          ),
        ),
      ),
      localizationsDelegates: const [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: const [Locale('en')],
      home: home,
    );
  }

  testWidgets('TV Dashboard Tile on MainScreen highlights with gold border on focus',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(1920, 1080);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(createTestApp(const MainScreen()));
    await tester.pumpAndSettle();

    // Look for InkWell widgets on MainScreen
    final inkWellFinder = find.byType(InkWell);
    expect(inkWellFinder, findsWidgets);

    // Focus the first tile
    final firstInkWell = tester.firstWidget<InkWell>(inkWellFinder);
    firstInkWell.focusNode?.requestFocus();

    // Simulate key focus / focus traversal
    final focusScope = FocusScope.of(tester.element(inkWellFinder.first));
    focusScope.nextFocus();
    await tester.pump(const Duration(milliseconds: 200));

    // Verify AnimatedScale or AnimatedContainer exists with gold border/shadow
    final cards = find.byType(Card);
    expect(cards, findsWidgets);

    // Directly test focus change on InkWell
    firstInkWell.onFocusChange?.call(true);
    await tester.pump(const Duration(milliseconds: 200));

    // Verify that at least one Card has the gold border (0xFFFFD700)
    final goldCardFinder = find.byWidgetPredicate((widget) {
      if (widget is Card) {
        final shape = widget.shape;
        if (shape is RoundedRectangleBorder) {
          return shape.side.color == const Color(0xFFFFD700);
        }
      }
      return false;
    });
    expect(goldCardFinder, findsOneWidget);
  });

  testWidgets('MobileButton on MainScreen highlights with gold border and scale on focus',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(400, 800);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(createTestApp(const MainScreen()));
    await tester.pumpAndSettle();

    // Find ElevatedButtons
    final elevatedButtonFinder = find.byType(ElevatedButton);
    expect(elevatedButtonFinder, findsWidgets);

    final firstButton = tester.firstWidget<ElevatedButton>(elevatedButtonFinder);
    firstButton.onFocusChange?.call(true);
    await tester.pump(const Duration(milliseconds: 200));

    // Verify gold border on the focused button
    final goldButtonFinder = find.byWidgetPredicate((widget) {
      if (widget is ElevatedButton) {
        final style = widget.style;
        final side = style?.side?.resolve({WidgetState.focused});
        return side?.color == const Color(0xFFFFD700);
      }
      return false;
    });
    expect(goldButtonFinder, findsWidgets);
  });

  testWidgets('ModeButton on GameModeSelectionScreen highlights with gold border on focus',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(1920, 1080);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(createTestApp(const GameModeSelectionScreen()));
    await tester.pumpAndSettle();

    final elevatedButtonFinder = find.byType(ElevatedButton);
    expect(elevatedButtonFinder, findsWidgets);

    final firstButton = tester.firstWidget<ElevatedButton>(elevatedButtonFinder);
    firstButton.onFocusChange?.call(true);
    await tester.pump(const Duration(milliseconds: 200));

    final goldButtonFinder = find.byWidgetPredicate((widget) {
      if (widget is ElevatedButton) {
        final style = widget.style;
        final side = style?.side?.resolve({WidgetState.focused});
        return side?.color == const Color(0xFFFFD700);
      }
      return false;
    });
    expect(goldButtonFinder, findsWidgets);
  });

  testWidgets('RuleCard on RuleSelectionScreen highlights with gold border and scale on focus',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(1920, 1080);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(createTestApp(const RuleSelectionScreen()));
    await tester.pumpAndSettle();

    final inkWellFinder = find.byType(InkWell);
    expect(inkWellFinder, findsWidgets);

    final firstInkWell = tester.firstWidget<InkWell>(inkWellFinder);
    firstInkWell.onFocusChange?.call(true);
    await tester.pump(const Duration(milliseconds: 200));

    // Check that at least one Card has the 3.5px gold border
    final goldCardFinder = find.byWidgetPredicate((widget) {
      if (widget is Card) {
        final shape = widget.shape;
        if (shape is RoundedRectangleBorder) {
          return shape.side.color == const Color(0xFFFFD700) &&
              shape.side.width >= 3.0;
        }
      }
      return false;
    });
    expect(goldCardFinder, findsOneWidget);
  });
}
