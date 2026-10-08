class LanguageOption {
  final String code;
  final String nameEnglish;
  final String nameNative;

  const LanguageOption({
    required this.code,
    required this.nameEnglish,
    required this.nameNative,
  });
}

const List<LanguageOption> supportedLanguages = [
  LanguageOption(code: "en", nameEnglish: "English", nameNative: "English"),
  LanguageOption(code: "hi", nameEnglish: "Hindi", nameNative: "हिंदी"),
  LanguageOption(code: "bn", nameEnglish: "Bengali", nameNative: "বাংলা"),
  LanguageOption(code: "or", nameEnglish: "Odia", nameNative: "ଓଡ଼ିଆ"),
  LanguageOption(code: "kn", nameEnglish: "Kannada", nameNative: "ಕನ್ನಡ"),
  LanguageOption(code: "ta", nameEnglish: "Tamil", nameNative: "தமிழ்"),
  LanguageOption(code: "mr", nameEnglish: "Marathi", nameNative: "मराठी"),
  LanguageOption(code: "te", nameEnglish: "Telugu", nameNative: "తెలుగు"),
  LanguageOption(code: "gu", nameEnglish: "Gujarati", nameNative: "ગુજરાતી"),
  LanguageOption(code: "ml", nameEnglish: "Malayalam", nameNative: "മലയാളം"),
  LanguageOption(code: "pa", nameEnglish: "Punjabi", nameNative: "ਪੰਜਾਬੀ"),
  LanguageOption(code: "as", nameEnglish: "Assamese", nameNative: "অসমীয়া"),
  LanguageOption(code: "es", nameEnglish: "Spanish", nameNative: "Español"),
  LanguageOption(code: "pt", nameEnglish: "Portuguese", nameNative: "Português"),
  LanguageOption(code: "fr", nameEnglish: "French", nameNative: "Français"),
  LanguageOption(code: "de", nameEnglish: "German", nameNative: "Deutsch"),
  LanguageOption(code: "ar", nameEnglish: "Arabic", nameNative: "العربية"),
  LanguageOption(code: "id", nameEnglish: "Indonesian", nameNative: "Bahasa Indonesia"),
  LanguageOption(code: "tr", nameEnglish: "Turkish", nameNative: "Türkçe"),
  LanguageOption(code: "it", nameEnglish: "Italian", nameNative: "Italiano"),
  LanguageOption(code: "ja", nameEnglish: "Japanese", nameNative: "日本語"),
  LanguageOption(code: "ko", nameEnglish: "Korean", nameNative: "한국어"),
  LanguageOption(code: "zh", nameEnglish: "Chinese", nameNative: "中文"),
  LanguageOption(code: "nl", nameEnglish: "Dutch", nameNative: "Nederlands"),
  LanguageOption(code: "ru", nameEnglish: "Russian", nameNative: "Русский"),
  LanguageOption(code: "vi", nameEnglish: "Vietnamese", nameNative: "Tiếng Việt"),
];
