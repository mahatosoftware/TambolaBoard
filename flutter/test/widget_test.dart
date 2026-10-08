import 'package:flutter_test/flutter_test.dart';
import 'package:tambola_board/main.dart';

void main() {
  testWidgets('TambolaApp renders successfully test', (WidgetTester tester) async {
    await tester.pumpWidget(const TambolaApp());
    await tester.pump();

    expect(find.textContaining('Tambola'), findsAtLeastNWidgets(1));
  });
}
