import 'package:flutter_test/flutter_test.dart';
import 'package:tambola_board/localization/language_option.dart';
import 'package:tambola_board/utils/funny_phrase_util.dart';

void main() {
  group('FunnyPhraseUtil tests', () {
    test('Number to words works correctly for English', () {
      expect(FunnyPhraseUtil.numberToWords(1), 'One');
      expect(FunnyPhraseUtil.numberToWords(22), 'Twenty Two');
      expect(FunnyPhraseUtil.numberToWords(50), 'Fifty');
      expect(FunnyPhraseUtil.numberToWords(90), 'Ninety');
    });

    test('All supported languages return valid non-empty phrases for 1..90', () {
      expect(supportedLanguages.length, 26);

      for (final lang in supportedLanguages) {
        for (int num = 1; num <= 90; num++) {
          final phrase = FunnyPhraseUtil.getFunnyPhrase(num, lang.code);
          expect(phrase, isNotEmpty, reason: 'Failed for ${lang.code} at number $num');
          if (lang.code == 'en') {
            expect(phrase, contains(FunnyPhraseUtil.numberToWords(num)),
                reason: 'Number word for $num not found in phrase for en');
          } else {
            expect(phrase, contains('$num'),
                reason: 'Number $num not found in phrase for ${lang.code}');
          }
        }
      }
    });

    test('Specific phrases for Indian languages match expectations', () {
      // Marathi
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'mr'), contains('श्रीगणेशाच्या'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'mr'), contains('अर्धशतक'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'mr'), contains('शेवटचा नंबर'));

      // Telugu
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'te'), contains('మొదటి అడుగు'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'te'), contains('హాఫ్ సెంచరీ'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'te'), contains('చివరి నంబర్'));

      // Gujarati
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'gu'), contains('ગણપતિ'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'gu'), contains('અડધી સદી'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'gu'), contains('છેલ્લો નંબર'));

      // Malayalam
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'ml'), contains('ശുഭാരംഭം'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'ml'), contains('ഹാഫ് സെഞ്ചുറി'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'ml'), contains('അവസാന നമ്പർ'));

      // Punjabi
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'pa'), contains('ਵਾਹਿਗੁਰੂ'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'pa'), contains('ਅੱਧਾ ਸੈਂਕੜਾ'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'pa'), contains('ਆਖਰੀ ਨੰਬਰ'));

      // Assamese
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'as'), contains('জয় আই অসম'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'as'), contains('আধা শতক'));
      expect(FunnyPhraseUtil.getFunnyPhrase(90, 'as'), contains('শেষ নম্বৰ'));
    });

    test('Specific phrases for European and Asian languages match expectations', () {
      // Spanish
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'es'), contains('fiesta'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'es'), contains('Cincuenta'));

      // French
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'fr'), contains("c'est parti"));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'fr'), contains('Cinquante'));

      // German
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'de'), contains('Anfang'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'de'), contains('Fünfzig'));

      // Portuguese
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'pt'), contains('começou'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'pt'), contains('Cinquenta'));

      // Italian
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'it'), contains('si parte'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'it'), contains('Cinquanta'));

      // Dutch
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'nl'), contains('daar gaan we'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'nl'), contains('Vijftig'));

      // Russian
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'ru'), contains('начинаем'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'ru'), contains('Пятьдесят'));

      // Arabic
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'ar'), contains('بسم الله'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'ar'), contains('خمسون'));

      // Indonesian
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'id'), contains('awal'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'id'), contains('Lima puluh'));

      // Turkish
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'tr'), contains('başlangıç'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'tr'), contains('Elli'));

      // Japanese
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'ja'), contains('始まり'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'ja'), contains('五十'));

      // Korean
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'ko'), contains('시작'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'ko'), contains('오십'));

      // Chinese
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'zh'), contains('一鸣惊人'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'zh'), contains('五十'));

      // Vietnamese
      expect(FunnyPhraseUtil.getFunnyPhrase(1, 'vi'), contains('may mắn'));
      expect(FunnyPhraseUtil.getFunnyPhrase(50, 'vi'), contains('Năm mươi'));
    });
  });
}
