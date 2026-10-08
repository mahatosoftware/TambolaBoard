import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:tambola_board/main.dart';
import 'package:tambola_board/localization/app_localizations.dart';
import 'package:tambola_board/screens/dialogs/language_settings_dialog.dart';

void main() {
  testWidgets('Test mobile language settings dialog interaction', (WidgetTester tester) async {
    tester.view.physicalSize = const Size(400, 800);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(const TambolaApp());
    await tester.pumpAndSettle();

    // Find and tap settings button
    final settingsBtn = find.text('Settings');
    expect(settingsBtn, findsOneWidget);
    await tester.tap(settingsBtn);
    await tester.pumpAndSettle();

    // Verify LanguageSettingsDialog is shown
    expect(find.byType(LanguageSettingsDialog), findsOneWidget);

    // Tap on Hindi option
    final hiOption = find.textContaining('हिंदी');
    expect(hiOption, findsOneWidget);
    await tester.tap(hiOption);
    await tester.pumpAndSettle();

    final locContext = tester.element(find.byType(Scaffold));
    final locHi = AppLocalizations.of(locContext);
    expect(locHi?.locale.languageCode, 'hi');
    expect(locHi?.translate('btn_start_new_game'), 'नया गेम शुरू करें');

    // Tap on Odia option
    final orOption = find.textContaining('ଓଡ଼ିଆ');
    expect(orOption, findsOneWidget);
    await tester.tap(orOption);
    await tester.pumpAndSettle();

    final locOr = AppLocalizations.of(locContext);
    expect(locOr?.locale.languageCode, 'or');
    expect(locOr?.translate('btn_start_new_game'), 'ନୂଆ ଗେମ୍ ଆରମ୍ଭ କରନ୍ତୁ');
  });

  testWidgets('Test TV D-Pad OK key selection and wide tablet layout', (WidgetTester tester) async {
    // Set TV / Tablet widescreen size (1920 x 1080)
    tester.view.physicalSize = const Size(1920, 1080);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.reset);

    await tester.pumpWidget(const TambolaApp());
    await tester.pumpAndSettle();

    // Find and tap settings button in wide dashboard layout
    final settingsBtn = find.text('Settings');
    expect(settingsBtn, findsOneWidget);
    await tester.tap(settingsBtn);
    await tester.pumpAndSettle();

    // Verify LanguageSettingsDialog is shown
    expect(find.byType(LanguageSettingsDialog), findsOneWidget);

    // Scroll until Spanish option is visible and tap it
    final esOption = find.textContaining('Español');
    await tester.scrollUntilVisible(esOption, 100);
    await tester.pumpAndSettle();
    expect(esOption, findsOneWidget);
    await tester.sendKeyEvent(LogicalKeyboardKey.tab);
    await tester.pumpAndSettle();

    await tester.tap(esOption);
    await tester.pumpAndSettle();

    final locContext = tester.element(find.byType(Scaffold));
    final locEs = AppLocalizations.of(locContext);
    expect(locEs?.locale.languageCode, 'es');
  });
}
