import 'package:flutter_test/flutter_test.dart';
import 'package:tambola_board/localization/language_option.dart';
import 'package:tambola_board/utils/tts_util.dart';

void main() {
  group('TtsUtil Tests', () {
    test('All 26 supported languages have valid BCP-47 TTS locales', () {
      for (final lang in supportedLanguages) {
        final locale = TtsUtil.getTtsLocale(lang.code);
        expect(locale, isNotEmpty, reason: 'Language ${lang.code} should have a valid locale');
        expect(locale.contains('-'), isTrue, reason: 'Locale $locale should be in language-REGION format');
      }
    });

    test('findFemaleVoice correctly selects Android Google TTS female voices (#female)', () {
      final sampleVoices = [
        {'name': 'hi-in-x-hia#male', 'locale': 'hi-IN'},
        {'name': 'hi-in-x-hie#female', 'locale': 'hi-IN'},
        {'name': 'en-us-x-sfg#female', 'locale': 'en-US'},
        {'name': 'en-us-x-iom#male', 'locale': 'en-US'},
        {'name': 'ta-in-x-tag#female', 'locale': 'ta-IN'},
        {'name': 'ta-in-x-tah#male', 'locale': 'ta-IN'},
        {'name': 'bn-in-x-bnf#female', 'locale': 'bn-IN'},
        {'name': 'bn-in-x-bna#male', 'locale': 'bn-IN'},
        {'name': 'es-es-x-eed#female', 'locale': 'es-ES'},
        {'name': 'es-es-x-eea#male', 'locale': 'es-ES'},
      ];

      final englishVoice = TtsUtil.findFemaleVoice(sampleVoices, 'en-US');
      expect(englishVoice, isNotNull);
      expect(englishVoice!['name'], 'en-us-x-sfg#female');

      final hindiVoice = TtsUtil.findFemaleVoice(sampleVoices, 'hi-IN');
      expect(hindiVoice, isNotNull);
      expect(hindiVoice!['name'], 'hi-in-x-hie#female');

      final tamilVoice = TtsUtil.findFemaleVoice(sampleVoices, 'ta-IN');
      expect(tamilVoice, isNotNull);
      expect(tamilVoice!['name'], 'ta-in-x-tag#female');

      final bengaliVoice = TtsUtil.findFemaleVoice(sampleVoices, 'bn-IN');
      expect(bengaliVoice, isNotNull);
      expect(bengaliVoice!['name'], 'bn-in-x-bnf#female');

      final spanishVoice = TtsUtil.findFemaleVoice(sampleVoices, 'es-ES');
      expect(spanishVoice, isNotNull);
      expect(spanishVoice!['name'], 'es-es-x-eed#female');
    });

    test('findFemaleVoice correctly selects iOS voices by gender attribute', () {
      final sampleVoices = [
        {'name': 'Samantha', 'locale': 'en-US', 'gender': 'female'},
        {'name': 'Daniel', 'locale': 'en-US', 'gender': 'male'},
        {'name': 'Lekha', 'locale': 'hi-IN', 'gender': 'female'},
        {'name': 'Rishi', 'locale': 'hi-IN', 'gender': 'male'},
        {'name': 'Vani', 'locale': 'ta-IN', 'gender': 'female'},
        {'name': 'Amelie', 'locale': 'fr-FR', 'gender': 'female'},
        {'name': 'Thomas', 'locale': 'fr-FR', 'gender': 'male'},
        {'name': 'Kyoko', 'locale': 'ja-JP', 'gender': 'female'},
        {'name': 'Otoya', 'locale': 'ja-JP', 'gender': 'male'},
      ];

      final englishVoice = TtsUtil.findFemaleVoice(sampleVoices, 'en-US');
      expect(englishVoice, isNotNull);
      expect(englishVoice!['name'], 'Samantha');

      final hindiVoice = TtsUtil.findFemaleVoice(sampleVoices, 'hi-IN');
      expect(hindiVoice, isNotNull);
      expect(hindiVoice!['name'], 'Lekha');

      final frenchVoice = TtsUtil.findFemaleVoice(sampleVoices, 'fr-FR');
      expect(frenchVoice, isNotNull);
      expect(frenchVoice!['name'], 'Amelie');

      final japaneseVoice = TtsUtil.findFemaleVoice(sampleVoices, 'ja-JP');
      expect(japaneseVoice, isNotNull);
      expect(japaneseVoice!['name'], 'Kyoko');
    });

    test('findFemaleVoice identifies Samsung TTS female voice patterns', () {
      final sampleVoices = [
        {'name': 'en_US_m00', 'locale': 'en_US'},
        {'name': 'en_US_f00', 'locale': 'en_US'},
        {'name': 'hi_IN_m00', 'locale': 'hi_IN'},
        {'name': 'hi_IN_f00', 'locale': 'hi_IN'},
      ];

      final englishVoice = TtsUtil.findFemaleVoice(sampleVoices, 'en-US');
      expect(englishVoice, isNotNull);
      expect(englishVoice!['name'], 'en_US_f00');

      final hindiVoice = TtsUtil.findFemaleVoice(sampleVoices, 'hi-IN');
      expect(hindiVoice, isNotNull);
      expect(hindiVoice!['name'], 'hi_IN_f00');
    });

    test('findFemaleVoice matches language prefix when region differs', () {
      final sampleVoices = [
        {'name': 'en-gb-x-rjs#female', 'locale': 'en-GB'},
        {'name': 'en-gb-x-gbb#male', 'locale': 'en-GB'},
      ];

      // Request en-US when only en-GB is available
      final voice = TtsUtil.findFemaleVoice(sampleVoices, 'en-US');
      expect(voice, isNotNull);
      expect(voice!['name'], 'en-gb-x-rjs#female');
    });

    test('findFemaleVoice avoids male voices when female is not explicitly marked', () {
      final sampleVoices = [
        {'name': 'custom-voice-male', 'locale': 'de-DE'},
        {'name': 'custom-voice-neutral', 'locale': 'de-DE'},
      ];

      final voice = TtsUtil.findFemaleVoice(sampleVoices, 'de-DE');
      expect(voice, isNotNull);
      expect(voice!['name'], 'custom-voice-neutral');
    });

    test('scoreVoiceForFemale correctly ranks female over male', () {
      final femaleScore = TtsUtil.scoreVoiceForFemale({
        'name': 'hi-in-x-cfc#female',
        'locale': 'hi-IN',
        'gender': 'female',
      });

      final maleScore = TtsUtil.scoreVoiceForFemale({
        'name': 'hi-in-x-cfc#male',
        'locale': 'hi-IN',
        'gender': 'male',
      });

      expect(femaleScore, greaterThan(0));
      expect(maleScore, lessThan(0));
      expect(femaleScore, greaterThan(maleScore));
    });
  });
}
