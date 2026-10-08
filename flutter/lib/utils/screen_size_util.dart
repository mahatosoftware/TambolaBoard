import 'package:flutter/widgets.dart';

class ScreenSizeUtil {
  static bool isTv(BuildContext context) {
    // In Flutter, TV or Leanback devices often present as large screen / television mode.
    // We check shortestSide or Platform / MediaQuery.
    final mediaQuery = MediaQuery.of(context);
    final isTVMode = mediaQuery.navigationMode == NavigationMode.directional;
    return isTVMode;
  }

  static bool isWideScreen(BuildContext context) {
    final mediaQuery = MediaQuery.of(context);
    return isTv(context) ||
        mediaQuery.size.width >= 600 ||
        mediaQuery.orientation == Orientation.landscape;
  }
}
