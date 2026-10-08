import 'package:flutter/foundation.dart';
import 'package:google_mobile_ads/google_mobile_ads.dart';
import 'ad_config.dart';

class AdInterstitialHelper {
  static InterstitialAd? _interstitialAd;
  static bool _isAdLoading = false;

  static void loadInterstitialAd() {
    if (kIsWeb) return;
    if (_interstitialAd != null || _isAdLoading) return;

    _isAdLoading = true;
    InterstitialAd.load(
      adUnitId: AdConfig.interstitialAdUnitId,
      request: const AdRequest(),
      adLoadCallback: InterstitialAdLoadCallback(
        onAdLoaded: (ad) {
          _interstitialAd = ad;
          _isAdLoading = false;
        },
        onAdFailedToLoad: (error) {
          _interstitialAd = null;
          _isAdLoading = false;
        },
      ),
    );
  }

  static void showInterstitialAd({required Function() onAdDismissed}) {
    if (kIsWeb) {
      onAdDismissed();
      return;
    }

    if (_interstitialAd != null) {
      _interstitialAd!.fullScreenContentCallback = FullScreenContentCallback(
        onAdDismissedFullScreenContent: (ad) {
          ad.dispose();
          _interstitialAd = null;
          loadInterstitialAd();
          onAdDismissed();
        },
        onAdFailedToShowFullScreenContent: (ad, error) {
          ad.dispose();
          _interstitialAd = null;
          onAdDismissed();
        },
      );
      _interstitialAd!.show();
    } else {
      onAdDismissed();
      loadInterstitialAd();
    }
  }
}
