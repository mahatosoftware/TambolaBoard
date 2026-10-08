import 'dart:math';
import 'package:url_launcher/url_launcher.dart';

class GeneralUtil {
  static String getCopyrightMessage({
    String appName = "Tambola Board",
    String? ownerName = "Debasish Mahato",
  }) {
    final year = DateTime.now().year;
    final nameToShow = ownerName ?? appName;
    return "© $year $nameToShow.";
  }

  static String generateGameId() {
    const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    final rnd = Random();
    return List.generate(5, (index) => chars[rnd.nextInt(chars.length)]).join();
  }

  static Future<void> openPlayStore({
    String packageName = "in.mahato.tambolaticket",
  }) async {
    final marketUri = Uri.parse("market://details?id=$packageName");
    final webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName");

    if (await canLaunchUrl(marketUri)) {
      await launchUrl(marketUri, mode: LaunchMode.externalApplication);
    } else if (await canLaunchUrl(webUri)) {
      await launchUrl(webUri, mode: LaunchMode.externalApplication);
    }
  }
}
