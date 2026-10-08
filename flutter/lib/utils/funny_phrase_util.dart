class FunnyPhraseUtil {
  static String getFunnyPhrase(int number, [String languageCode = "en"]) {
    switch (languageCode) {
      case "bn":
        return _getBengaliNumberPhrase(number);
      case "or":
        return _getOdiaNumberPhrase(number);
      case "hi":
        return _getHindiNumberPhrase(number);
      case "kn":
        return _getKannadaNumberPhrase(number);
      case "ta":
        return _getTamilNumberPhrase(number);
      case "mr":
        return _getMarathiNumberPhrase(number);
      case "te":
        return _getTeluguNumberPhrase(number);
      case "gu":
        return _getGujaratiNumberPhrase(number);
      case "ml":
        return _getMalayalamNumberPhrase(number);
      case "pa":
        return _getPunjabiNumberPhrase(number);
      case "as":
        return _getAssameseNumberPhrase(number);
      case "es":
        return _getSpanishNumberPhrase(number);
      case "pt":
        return _getPortugueseNumberPhrase(number);
      case "fr":
        return _getFrenchNumberPhrase(number);
      case "de":
        return _getGermanNumberPhrase(number);
      case "ar":
        return _getArabicNumberPhrase(number);
      case "id":
        return _getIndonesianNumberPhrase(number);
      case "tr":
        return _getTurkishNumberPhrase(number);
      case "it":
        return _getItalianNumberPhrase(number);
      case "ja":
        return _getJapaneseNumberPhrase(number);
      case "ko":
        return _getKoreanNumberPhrase(number);
      case "zh":
        return _getChineseNumberPhrase(number);
      case "nl":
        return _getDutchNumberPhrase(number);
      case "ru":
        return _getRussianNumberPhrase(number);
      case "vi":
        return _getVietnameseNumberPhrase(number);
      default:
        return _getEnglishNumberPhrase(number);
    }
  }

  static String _getEnglishNumberPhrase(int number) {
    final text = _englishFunnyPhrases[number] ?? "Number ${numberToWords(number)}";
    return "$text Number ${numberToWords(number)}";
  }

  static String _getHindiNumberPhrase(int number) {
    final rhyme = _hindiFunnyPhrases[number];
    return rhyme != null ? "$rhyme, नंबर $number" : "नंबर $number";
  }

  static String _getBengaliNumberPhrase(int number) {
    final rhyme = _bengaliFunnyPhrases[number];
    return rhyme != null ? "$rhyme, নম্বর $number" : "নম্বর $number";
  }

  static String _getOdiaNumberPhrase(int number) {
    final rhyme = _odiaFunnyPhrases[number];
    return rhyme != null ? "$rhyme, ନମ୍ବର $number" : "ନମ୍ବର $number";
  }

  static String _getKannadaNumberPhrase(int number) {
    final rhyme = _kannadaFunnyPhrases[number];
    return rhyme != null ? "$rhyme, ಸಂಖ್ಯೆ $number" : "ಸಂಖ್ಯೆ $number";
  }

  static String _getTamilNumberPhrase(int number) {
    final rhyme = _tamilFunnyPhrases[number];
    return rhyme != null ? "$rhyme, எண் $number" : "எண் $number";
  }

  static String _getMarathiNumberPhrase(int number) => "नंबर $number";
  static String _getTeluguNumberPhrase(int number) => "సంఖ్య $number";
  static String _getGujaratiNumberPhrase(int number) => "નંબર $number";
  static String _getMalayalamNumberPhrase(int number) => "നമ്പർ $number";
  static String _getPunjabiNumberPhrase(int number) => "ਨੰਬਰ $number";
  static String _getAssameseNumberPhrase(int number) => "নম্বৰ $number";
  static String _getSpanishNumberPhrase(int number) => "Número $number";
  static String _getPortugueseNumberPhrase(int number) => "Número $number";
  static String _getFrenchNumberPhrase(int number) => "Numéro $number";
  static String _getGermanNumberPhrase(int number) => "Nummer $number";
  static String _getArabicNumberPhrase(int number) => "رقم $number";
  static String _getIndonesianNumberPhrase(int number) => "Nomor $number";
  static String _getTurkishNumberPhrase(int number) => "Numara $number";
  static String _getItalianNumberPhrase(int number) => "Numero $number";
  static String _getJapaneseNumberPhrase(int number) => "番号 $number";
  static String _getKoreanNumberPhrase(int number) => "번호 $number";
  static String _getChineseNumberPhrase(int number) => "号码 $number";
  static String _getDutchNumberPhrase(int number) => "Nummer $number";
  static String _getRussianNumberPhrase(int number) => "Номер $number";
  static String _getVietnameseNumberPhrase(int number) => "Số $number";

  static String numberToWords(int number) {
    const units = [
      "", "One", "Two", "Three", "Four", "Five", "Six", "Seven",
      "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
      "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    ];
    const tens = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"];

    if (number < 1 || number > 90) return "Out of range";
    if (number < 20) return units[number];
    if (number % 10 == 0) return tens[number ~/ 10];
    return "${tens[number ~/ 10]} ${units[number % 10]}";
  }

  static const Map<int, String> _englishFunnyPhrases = {
    1: "At the Beginning.",
    2: "Me and you.",
    3: "Happy family.",
    4: "Two Plus Two.",
    5: "High five everyone.",
    6: "Bottom heavy.",
    7: "Lucky number.",
    8: "Big fat lady.",
    9: "Doctor's time.",
    10: "A big fat hen.",
    11: "One and one.",
    12: "One dozen.",
    13: "Unlucky for some.",
    14: "Valentine's Day.",
    15: "The age when attitude starts.",
    16: "Sweet sixteen.",
    17: "Not so sweet.",
    18: "Voting age.",
    19: "Last of the teens.",
    20: "One score.",
    21: "Women's age never crosses.",
    22: "Two little ducks.",
    23: "You and me.",
    24: "Two dozen.",
    25: "Silver Jubilee Number.",
    26: "Mix and fix.",
    27: "Gateway to heaven.",
    28: "Not so late at.",
    29: "Rise and Shine at.",
    30: "Women get flirty at.",
    31: "Time for fun.",
    32: "Buckle my shoe.",
    33: "All the 3s.",
    34: "Ask for more.",
    35: "Three and Five.",
    36: "Popular size.",
    37: "Mixed luck.",
    38: "Oversize.",
    39: "Watch your waistline.",
    40: "Men get Naughty at.",
    41: "Four and one.",
    42: "Answer to everything.",
    43: "Pain in the knee.",
    44: "All the Fours.",
    45: "Halfway there.",
    46: "Four and six.",
    47: "Heaven’s seven.",
    48: "Four dozen.",
    49: "Four and Nine.",
    50: "Half a century.",
    51: "Five and one.",
    52: "Weeks in a year.",
    53: "Five and three.",
    54: "Time for Mooor.",
    55: "All the fives.",
    56: "Pick up sticks.",
    57: "Mutiny Year.",
    58: "Time to retire.",
    59: "Five and Nine.",
    60: "Five dozen.",
    61: "Bakers bun.",
    62: "Turn the screw.",
    63: "Tickle me.",
    64: "Six and Four.",
    65: "Old age pension.",
    66: "Six and Six.",
    67: "Made in heaven.",
    68: "Check your weight.",
    69: "Favourite of mine.",
    70: "Lucky blind.",
    71: "Bang on the drum.",
    72: "Lucky two.",
    73: "Under the tree.",
    74: "Still want more.",
    75: "Diamond Jubilee.",
    76: "Lucky six.",
    77: "Two hockey sticks.",
    78: "Heaven's gate.",
    79: "One more time.",
    80: "Eight and Zero.",
    81: "Corner shot.",
    82: "Fat lady with a duck.",
    83: "Old but gold.",
    84: "Seven Dozen.",
    85: "Staying alive.",
    86: "Between the sticks.",
    87: "Grandpa age.",
    88: "Two fat ladies.",
    89: "All but one.",
    90: "Top of the house.",
  };

  static const Map<int, String> _hindiFunnyPhrases = {
    1: "एक से भले दो, लेकिन खेल शुरू एक से",
    2: "दो दिल, एक टिकट",
    3: "तीन तिगाड़ा, काम बिगाड़ा",
    4: "चार यार, मस्ती अपार",
    5: "पाँच पांडव मैदान में",
    6: "छक्का मारो, बाउंड्री पार",
    7: "सात सुरों का संगम",
    8: "आठ का ठाठ ही अलग है",
    9: "नौ दिन चले अढ़ाई कोस",
    10: "दस का दम, खेल में गरम",
    11: "एक और एक ग्यारह",
    12: "बारह बजे, अब खेल सजे",
    13: "तेरह में किस्मत का फेर",
    14: "चौदहवीं का चाँद",
    15: "पंद्रह अगस्त, आज़ादी का जश्न",
    16: "सोलह श्रृंगार, खेल शानदार",
    17: "सत्रह में किस्मत जागी",
    18: "अठारह में जोश जवान",
    19: "उन्नीस, बीस आने वाला है",
    20: "बीस का नोट, किस्मत की चोट",
    21: "इक्कीस तोपों की सलामी",
    22: "दो हंसों का जोड़ा",
    25: "पच्चीस, बसंती का डायलॉग याद आया",
    50: "पचास! आधा शतक, तालियाँ बजाओ",
    90: "नब्बे! आखिरी नंबर, टिकट देख लो",
  };

  static const Map<int, String> _bengaliFunnyPhrases = {
    1: "একলা চলো রে",
    2: "দুই বাংলার এক মন",
    3: "তিন গোয়েন্দা হাজির",
    4: "চারমূর্তি বেরিয়েছে অভিযানে",
    5: "পাঁচফোড়নে জমে গেল রান্না",
    6: "ছয় ঋতুর বাংলা",
    7: "সাত পাকে বাঁধা",
    8: "অষ্টমীর অঞ্জলি",
    9: "নবমীর রাত, ঠাকুর দেখার মাত",
    10: "দশমীর মন খারাপ",
    50: "পঞ্চাশে পঞ্চব্যঞ্জন চাই",
    90: "নব্বইয়ে শেষ, এবার মিষ্টিমুখ বেশ",
  };

  static const Map<int, String> _odiaFunnyPhrases = {
    1: "ଶ୍ରୀଜଗନ୍ନାଥଙ୍କ ନାମରେ ଶୁଭ ଆରମ୍ଭ",
    2: "ତୁମେ ଆଉ ମୁଁ, ଦୁଇଜଣ",
    3: "ତିନି ରଥର ମହିମା",
    4: "ଚାରିଧାମ ଯାତ୍ରା",
    5: "ପାଞ୍ଚ ପାଣ୍ଡବଙ୍କ ବୀରତ୍ୱ",
    6: "ଛଅ ଋତୁର ଓଡ଼ିଶା",
    7: "ସାତ ଦରିଆ ପାରି",
    8: "ଅଷ୍ଟମୀର ଅଞ୍ଜଳି",
    9: "ନବମୀର ଭୋଗ, ଆହା କି ମଜା",
    10: "ଦଶମୀରେ ବିଜୟା ଦଶମୀ",
    50: "ପଚାଶ! ଅଧା ଶତକ, ତାଳି ହେଉ",
    90: "ନବେ! ଶେଷ ଡାକ, ଟିକେଟ୍ ଦେଖନ୍ତୁ",
  };

  static const Map<int, String> _kannadaFunnyPhrases = {
    1: "ಶುಭಾರಂಭ, ಮೊದಲ ಹೆಜ್ಜೆ",
    2: "ನೀನು ನಾನು, ಇಬ್ಬರು",
    3: "ಮೂರು ಮೂರ್ತಿ, ಭರ್ಜರಿ ಎಂಟ್ರಿ",
    4: "ನಾಲ್ಕು ದಿಕ್ಕಲ್ಲೂ ಸಂಭ್ರಮ",
    5: "ಪಂಚ ಪಾಂಡವರ ಪವರ್",
    6: "ಸಿಕ್ಸರ್, ಬೌಂಡರಿ ದಾಟಿತು",
    7: "ಏಳು ಬೆಟ್ಟದ ತಿರುಪತಿ ನೆನಪು",
    8: "ಎಂಟರ ಆಟ, ಭರ್ಜರಿ ಜೋಶ್",
    9: "ಒಂಬತ್ತು ಹೆಜ್ಜೆ, ಗೆಲುವಿನ ಕಡೆ",
    10: "ಹತ್ತು ಹತ್ತು, ಆಟ ಸೂಪರ್",
    50: "ಐವತ್ತು! ಅರ್ಧ ಶತಕದ ಸಂಭ್ರಮ",
    90: "ತೊಂಬತ್ತು! ಕೊನೆಯ ನಂಬರ್, ಟಿಕೆಟ್ ನೋಡಿ!",
  };

  static const Map<int, String> _tamilFunnyPhrases = {
    1: "முதல் படி, வெற்றியின் தொடக்கம்",
    2: "நீயும் நானும், ரெண்டு பேரு",
    3: "மூணு பேரு சேர்ந்தா மாஸ்",
    4: "நாலு திசையும் நம்ம பக்கம்",
    5: "அஞ்சு பாண்டவர் பலம்",
    6: "சிக்ஸர் அடிச்சாச்சு, பவுண்டரி தாண்டியாச்சு",
    7: "ஏழு கடல் தாண்டி வந்த ஏழு",
    8: "எட்டு திக்கும் புகழ்",
    9: "ஒன்பது, அதிர்ஷ்டம் வருது",
    10: "பத்து பத்து, ஆட்டம் கெத்து",
    50: "ஐம்பது! அரை சதம், கைதட்டுங்க!",
    90: "தொண்ணூறு! கடைசி நம்பர், டிக்கெட் பாருங்க!",
  };
}
