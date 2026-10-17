import 'package:flutter/foundation.dart';
import 'package:flutter_tts/flutter_tts.dart';

class TtsUtil {
  /// Known female voice names across platforms (iOS, Android, Windows, Web, etc.)
  static const Set<String> knownFemaleNames = {
    // iOS / macOS female voices
    'samantha',
    'karen',
    'moira',
    'tessa',
    'fiona',
    'victoria',
    'allison',
    'ava',
    'susan',
    'zoe',
    'lekha',
    'vani',
    'tumpa',
    'chitra',
    'monica',
    'mónica',
    'paulina',
    'amelie',
    'amélie',
    'marie',
    'aurelie',
    'aurélie',
    'anna',
    'petra',
    'marlene',
    'alice',
    'federica',
    'paola',
    'kyoko',
    'yuna',
    'ting-ting',
    'mei-jia',
    'sin-ji',
    'claire',
    'milena',
    'katya',
    'joana',
    'luciana',
    'sara',
    'laura',
    'helena',
    'carmit',
    'damayanti',
    'ellen',
    'alva',
    'klara',
    'mariska',
    'zuzana',
    'ioana',

    // Azure / Microsoft female voices
    'jenny',
    'aria',
    'swara',
    'zira',
    'eva',
  };

  /// Known male voice names to avoid
  static const Set<String> knownMaleNames = {
    'daniel',
    'oliver',
    'george',
    'rishi',
    'thomas',
    'otoya',
    'xander',
    'yuri',
    'jorge',
    'diego',
    'fred',
    'david',
    'alex',
    'mark',
    'arthur',
  };

  /// Returns the appropriate BCP-47 TTS locale for [langCode].
  static String getTtsLocale(String langCode) {
    switch (langCode) {
      case "hi":
        return "hi-IN";
      case "bn":
        return "bn-IN";
      case "or":
        return "or-IN";
      case "kn":
        return "kn-IN";
      case "ta":
        return "ta-IN";
      case "mr":
        return "mr-IN";
      case "te":
        return "te-IN";
      case "gu":
        return "gu-IN";
      case "ml":
        return "ml-IN";
      case "pa":
        return "pa-IN";
      case "as":
        return "as-IN";
      case "es":
        return "es-ES";
      case "pt":
        return "pt-PT";
      case "fr":
        return "fr-FR";
      case "de":
        return "de-DE";
      case "ar":
        return "ar-SA";
      case "id":
        return "id-ID";
      case "tr":
        return "tr-TR";
      case "it":
        return "it-IT";
      case "ja":
        return "ja-JP";
      case "ko":
        return "ko-KR";
      case "zh":
        return "zh-CN";
      case "nl":
        return "nl-NL";
      case "ru":
        return "ru-RU";
      case "vi":
        return "vi-VN";
      default:
        return "en-US";
    }
  }

  /// Calculates a female likelihood score for a given voice map.
  /// Positive score indicates female likelihood, negative indicates male likelihood.
  static int scoreVoiceForFemale(Map<dynamic, dynamic> voice) {
    final name = (voice['name'] ?? '').toString().toLowerCase();
    final gender = (voice['gender'] ?? '').toString().toLowerCase();
    final features = (voice['features'] ?? '').toString().toLowerCase();
    final quality = (voice['quality'] ?? '').toString().toLowerCase();

    int score = 0;

    // Explicit gender attribute (iOS, macOS, some Android engines)
    if (gender == 'female') {
      score += 100;
    } else if (gender == 'male') {
      score -= 100;
    }

    // Name-based checks (Android Google TTS: e.g. "hi-in-x-hie#female", "en-us-x-sfg#female")
    if (name.contains('#female')) {
      score += 90;
    } else if (name.contains('female')) {
      score += 80;
    }

    // Samsung TTS: e.g. "en_US_f00", "hi_IN_f00"
    if (RegExp(r'[_-]f\d+').hasMatch(name)) {
      score += 75;
    }

    // Feature-based checks (Android Voice.features)
    if (features.contains('female') ||
        features.contains('gender=female') ||
        features.contains('gender:female')) {
      score += 70;
    }

    // Known female names check
    for (final femaleName in knownFemaleNames) {
      if (name.contains(femaleName)) {
        score += 60;
        break;
      }
    }

    // Google Cloud / Wavenet / Standard naming (A, C, E, G are female, B, D are male)
    if (RegExp(r'-(wavenet|standard|neural2|journey)-[aceg]\b', caseSensitive: false).hasMatch(name)) {
      score += 40;
    } else if (RegExp(r'-(wavenet|standard|neural2|journey)-[bd]\b', caseSensitive: false).hasMatch(name)) {
      score -= 40;
    }

    // Male negative checks
    if (name.contains('#male')) {
      score -= 90;
    } else if (name.contains('male')) {
      score -= 80;
    }

    if (RegExp(r'[_-]m\d+').hasMatch(name)) {
      score -= 75;
    }

    if (features.contains('male') ||
        features.contains('gender=male') ||
        features.contains('gender:male')) {
      score -= 70;
    }

    for (final maleName in knownMaleNames) {
      if (name.contains(maleName)) {
        score -= 60;
        break;
      }
    }

    // Quality bonus
    if (quality == 'very high' || quality == 'high') {
      score += 2;
    }

    return score;
  }

  /// Selects the best female voice map for [targetLocale] from [availableVoices].
  /// Returns null if no suitable voice could be determined.
  static Map<String, String>? findFemaleVoice(
    List<dynamic> availableVoices,
    String targetLocale,
  ) {
    if (availableVoices.isEmpty) return null;

    final targetLang = targetLocale.split(RegExp('[-_]')).first.toLowerCase();
    final targetFull = targetLocale.replaceAll('_', '-').toLowerCase();

    // 1. Filter voices for the target language/locale
    List<Map<dynamic, dynamic>> matchingVoices = availableVoices
        .whereType<Map<dynamic, dynamic>>()
        .where((v) {
          final loc = (v['locale'] ?? '').toString().replaceAll('_', '-').toLowerCase();
          return loc == targetFull;
        })
        .toList();

    // Fall back to matching by language prefix (e.g. "hi", "en", "es")
    if (matchingVoices.isEmpty) {
      matchingVoices = availableVoices
          .whereType<Map<dynamic, dynamic>>()
          .where((v) {
            final loc = (v['locale'] ?? '').toString().replaceAll('_', '-').toLowerCase();
            return loc.startsWith('$targetLang-') || loc == targetLang;
          })
          .toList();
    }

    if (matchingVoices.isEmpty) return null;

    // 2. Score each voice
    Map<dynamic, dynamic>? bestVoice;
    int highestScore = -999;

    for (final voice in matchingVoices) {
      final score = scoreVoiceForFemale(voice);
      if (score > highestScore) {
        highestScore = score;
        bestVoice = voice;
      }
    }

    if (bestVoice != null && highestScore > 0) {
      return {
        'name': bestVoice['name']?.toString() ?? '',
        'locale': bestVoice['locale']?.toString() ?? targetLocale,
      };
    }

    // If none scored positively (no explicit female indicator), filter out negative scores (male)
    final nonMaleVoices = matchingVoices.where((v) => scoreVoiceForFemale(v) >= 0).toList();
    final fallback = nonMaleVoices.isNotEmpty ? nonMaleVoices.first : matchingVoices.first;
    return {
      'name': fallback['name']?.toString() ?? '',
      'locale': fallback['locale']?.toString() ?? targetLocale,
    };
  }

  /// Configures [tts] to speak with a female voice for [targetLocale].
  /// Also sets appropriate pitch (1.0 for female voice, 1.15 if no female voice available as fallback).
  static Future<void> configureFemaleVoice(
    FlutterTts tts,
    String targetLocale,
  ) async {
    try {
      await tts.setLanguage(targetLocale);

      final dynamic rawVoices = await tts.getVoices;
      if (rawVoices is List && rawVoices.isNotEmpty) {
        final femaleVoice = findFemaleVoice(rawVoices, targetLocale);
        if (femaleVoice != null && femaleVoice['name']!.isNotEmpty) {
          debugPrint("TtsUtil: Selected female voice: ${femaleVoice['name']} (${femaleVoice['locale']})");
          await tts.setVoice(femaleVoice);
          await tts.setPitch(1.0);
          return;
        }
      }

      // Fallback: If no voice list is returned by the engine, set a slightly higher pitch (1.15)
      // to give the default system voice a feminine timbre.
      await tts.setPitch(1.15);
    } catch (e) {
      debugPrint("TtsUtil error configuring female voice: $e");
    }
  }
}
