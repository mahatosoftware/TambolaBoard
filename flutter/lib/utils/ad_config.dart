import 'package:flutter/foundation.dart';

class AdConfig {
  // Official Google AdMob Test Ad Unit IDs
  static const String _testBannerAdUnitId =
      "ca-app-pub-3940256099942544/6300978111";
  static const String _testInterstitialAdUnitId =
      "ca-app-pub-3940256099942544/1033173712";

  // Production Ad Unit IDs
  static const String _prodBannerAdUnitId =
      "ca-app-pub-8382655413286804/2266125437";
  static const String _prodInterstitialAdUnitId =
      "ca-app-pub-8382655413286804/7364823698";

  static String get bannerAdUnitId =>
      kDebugMode ? _testBannerAdUnitId : _prodBannerAdUnitId;

  static String get interstitialAdUnitId =>
      kDebugMode ? _testInterstitialAdUnitId : _prodInterstitialAdUnitId;
}
