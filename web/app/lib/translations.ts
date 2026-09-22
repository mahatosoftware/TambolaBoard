export interface Translations {
  // App Header & Branding
  proHostEdition: string;
  appTitle: string;
  appTagline: string;
  voiceCallerPill: string;
  autoDrawPill: string;
  winnerPdfPill: string;

  // Home Screen Buttons
  startNewGame: string;
  startNewGameSub: string;
  continueLastGame: string;
  continueLastGameSub: string;
  noSavedSession: string;
  resumeBadge: string;
  viewWinners: string;
  viewWinnersSub: string;
  howToPlay: string;
  settings: string;
  ticketSample: string;
  brandingFooter: string;

  // Game Mode Selection
  selectGameModeTitle: string;
  moderatedMode: string;
  moderatedModeDesc: string;
  unmoderatedMode: string;
  unmoderatedModeDesc: string;
  back: string;

  // Game ID Input Screen
  enterGameIdTitle: string;
  enterGameIdSub: string;
  gameIdPlaceholder: string;
  continueBtn: string;

  // Rule Selection Screen
  selectRulesTitle: string;
  selectRulesSub: string;
  selectAll: string;
  clearAll: string;
  continueToPoints: string;

  // Points / Prize Distribution Screen
  setPrizeQuantitiesTitle: string;
  setPrizeQuantitiesSub: string;
  quantityLabel: string;
  startGameNow: string;

  // Game Board Screen
  gameBoardTitle: string;
  lastCalled: string;
  callNextNumber: string;
  autoCall: string;
  pauseAutoCall: string;
  claimPrizeBoardBtn: string;
  numbersCalledCount: string;
  remainingCount: string;
  resetGame: string;
  mainMenu: string;
  scanQrToJoin: string;

  // Winner Board Modal & View Winners
  winnerBoardTitle: string;
  winnerBoardSub: string;
  enterWinnerName: string;
  claimedStatus: string;
  unclaimedStatus: string;
  downloadPdfSummary: string;
  close: string;

  // How to Play Dialog
  howToPlayTitle: string;
  gotIt: string;

  // Settings Dialog (Bilingual UI)
  audioSettingsTitle: string;
  ladyVoiceActive: string;
  ladyVoiceDesc: string;
  announcementLang: string;
  done: string;

  // Rules Localized Names
  ruleFullHouse: string;
  ruleSecondHouse: string;
  ruleThirdHouse: string;
  ruleEarlyFive: string;
  ruleTopLine: string;
  ruleMiddleLine: string;
  ruleBottomLine: string;
  ruleCorner: string;
  ruleDiamond: string;
  rulePyramid: string;
  ruleInvertedPyramid: string;
  ruleStar: string;
  ruleOdds: string;
  ruleEven: string;
  ruleFirstHalf: string;
  ruleSecondHalf: string;
  ruleBreakfast: string;
  ruleLunch: string;
  ruleDinner: string;
  ruleTemperature: string;
  ruleBelowFifty: string;
  ruleAboveFifty: string;
}

const en: Translations = {
  proHostEdition: "PRO HOST EDITION",
  appTitle: "TAMBOLA BOARD",
  appTagline: "Interactive Housie Host & Multilingual Caller",
  voiceCallerPill: "Voice Caller",
  autoDrawPill: "Auto Draw",
  winnerPdfPill: "Winner PDF",
  startNewGame: "Start New Game",
  startNewGameSub: "Host a match with custom rules & prizes",
  continueLastGame: "Continue Last Game",
  continueLastGameSub: "Resume active host session",
  noSavedSession: "No saved game session",
  resumeBadge: "RESUME",
  viewWinners: "View Winners",
  viewWinnersSub: "Claimed prizes & leaderboard",
  howToPlay: "How to Play",
  settings: "Settings",
  ticketSample: "Ticket Sample",
  brandingFooter: "Mahato Software • Crafted with ❤️ for Tambola lovers",

  selectGameModeTitle: "Select Game Mode",
  moderatedMode: "Moderated / Host Board",
  moderatedModeDesc: "Host controls number calling & prize verification",
  unmoderatedMode: "Express / Quick Game",
  unmoderatedModeDesc: "Direct random draw without moderator code",
  back: "Back",

  enterGameIdTitle: "Enter Game ID / Room Code",
  enterGameIdSub: "Enter a unique code for players to join your room",
  gameIdPlaceholder: "e.g. TMB123",
  continueBtn: "Continue",

  selectRulesTitle: "Select Winning Rules",
  selectRulesSub: "Choose which rules and patterns are active for this game",
  selectAll: "Select All",
  clearAll: "Clear All",
  continueToPoints: "Continue to Prize Setup",

  setPrizeQuantitiesTitle: "Set Prize Quantities",
  setPrizeQuantitiesSub: "Specify how many winners each rule will have",
  quantityLabel: "Quantity",
  startGameNow: "🚀 Start Game Now",

  gameBoardTitle: "Tambola Host Board",
  lastCalled: "LAST CALLED",
  callNextNumber: "Call Next Number",
  autoCall: "Auto Call",
  pauseAutoCall: "Pause Auto Call",
  claimPrizeBoardBtn: "🏆 Claim Prize / Winner Board",
  numbersCalledCount: "Called",
  remainingCount: "Remaining",
  resetGame: "Reset Game",
  mainMenu: "Main Menu",
  scanQrToJoin: "Scan QR to Join",

  winnerBoardTitle: "Winner Board & Prize Claims",
  winnerBoardSub: "Record winner names and download PDF certificates",
  enterWinnerName: "Enter Winner Name",
  claimedStatus: "Claimed",
  unclaimedStatus: "Unclaimed",
  downloadPdfSummary: "📄 Download PDF Summary",
  close: "Close",

  howToPlayTitle: "📖 How to Play Tambola",
  gotIt: "Got It!",

  audioSettingsTitle: "⚙️ Settings & Audio",
  ladyVoiceActive: "Lady Voice Active",
  ladyVoiceDesc: "High-clarity female voice announcements",
  announcementLang: "🌐 Speech & UI Language",
  done: "Done / OK",

  ruleFullHouse: "Full House",
  ruleSecondHouse: "Second House",
  ruleThirdHouse: "Third House",
  ruleEarlyFive: "Early Five",
  ruleTopLine: "Top Line",
  ruleMiddleLine: "Middle Line",
  ruleBottomLine: "Bottom Line",
  ruleCorner: "Corner",
  ruleDiamond: "Diamond",
  rulePyramid: "Pyramid",
  ruleInvertedPyramid: "Inverted Pyramid",
  ruleStar: "Star",
  ruleOdds: "Odds",
  ruleEven: "Even",
  ruleFirstHalf: "First Half",
  ruleSecondHalf: "Second Half",
  ruleBreakfast: "Breakfast",
  ruleLunch: "Lunch",
  ruleDinner: "Dinner",
  ruleTemperature: "Temperature",
  ruleBelowFifty: "Below Fifty",
  ruleAboveFifty: "Above Fifty"
};

const hi: Translations = {
  ...en,
  proHostEdition: "प्रो होस्ट संस्करण",
  appTitle: "तामबोला बोर्ड",
  appTagline: "डिजिटल हाउजी होस्ट एवं वॉइस कॉलर",
  voiceCallerPill: "वॉइस कॉलर",
  autoDrawPill: "ऑटो ड्रा",
  winnerPdfPill: "विजेता पीडीएफ",
  startNewGame: "नया गेम शुरू करें",
  startNewGameSub: "कस्टम नियमों और पुरस्कारों के साथ होस्ट करें",
  continueLastGame: "पिछला गेम जारी रखें",
  continueLastGameSub: "सक्रिय होस्ट सेशन फिर से शुरू करें",
  noSavedSession: "कोई सहेजा गया सेशन नहीं",
  resumeBadge: "जारी रखें",
  viewWinners: "विजेता देखें",
  viewWinnersSub: "दावा किए गए पुरस्कार और लीडरबोर्ड",
  howToPlay: "कैसे खेलें",
  settings: "Settings / सेटिंग्स",
  ticketSample: "टिकट सैंपल",

  selectGameModeTitle: "गेम मोड चुनें",
  moderatedMode: "होस्ट बोर्ड (मॉडरेटेड)",
  moderatedModeDesc: "होस्ट नंबर पुकारने और पुरस्कार सत्यापन को नियंत्रित करता है",
  unmoderatedMode: "त्वरित गेम",
  unmoderatedModeDesc: "बिना मॉडरेटर कोड के सीधा ड्रा",
  back: "वापस",

  enterGameIdTitle: "गेम आईडी / रूम कोड दर्ज करें",
  enterGameIdSub: "खिलाड़ियों के रूम में शामिल होने के लिए कोड दर्ज करें",
  gameIdPlaceholder: "उदा. TMB123",
  continueBtn: "आगे बढ़ें",

  selectRulesTitle: "जीतने के नियम चुनें",
  selectRulesSub: "इस गेम के लिए सक्रिय नियम और पैटर्न चुनें",
  selectAll: "सभी चुनें",
  clearAll: "सभी हटाएं",
  continueToPoints: "पुरस्कार सेटअप पर जाएं",

  setPrizeQuantitiesTitle: "पुरस्कारों की संख्या निर्धारित करें",
  setPrizeQuantitiesSub: "प्रत्येक नियम के लिए विजेताओं की संख्या चुनें",
  quantityLabel: "मात्रा",
  startGameNow: "🚀 गेम शुरू करें",

  gameBoardTitle: "तामबोला होस्ट बोर्ड",
  lastCalled: "अंतिम नंबर",
  callNextNumber: "अगला नंबर बोलें",
  autoCall: "ऑटो कॉल",
  pauseAutoCall: "ऑटो कॉल रोकें",
  claimPrizeBoardBtn: "🏆 दावा करें / विजेता बोर्ड",
  numbersCalledCount: "पुकारे गए",
  remainingCount: "शेष",
  resetGame: "रीसेट गेम",
  mainMenu: "मुख्य मेनू",
  scanQrToJoin: "क्यूआर स्कैन करें",

  winnerBoardTitle: "विजेता बोर्ड और दावा",
  winnerBoardSub: "विजेताओं के नाम दर्ज करें और पीडीएफ डाउनलोड करें",
  enterWinnerName: "विजेता का नाम दर्ज करें",
  claimedStatus: "दावा किया गया",
  unclaimedStatus: "अदावाकृत",
  downloadPdfSummary: "📄 पीडीएफ डाउनलोड करें",
  close: "बंद करें",

  howToPlayTitle: "📖 तामबोला कैसे खेलें",
  gotIt: "समझ गया!",

  audioSettingsTitle: "⚙️ Settings & Audio / सेटिंग्स एवं ऑडियो",
  ladyVoiceActive: "Lady Voice Active / महिला आवाज सक्रिय",
  ladyVoiceDesc: "High-clarity female voice / उच्च-स्पष्टता महिला आवाज",
  announcementLang: "🌐 Speech & UI Language / बोली एवं इंटरफ़ेस भाषा",
  done: "Done / सम्पन्न"
};

const es: Translations = {
  ...en,
  settings: "Settings / Ajustes",
  audioSettingsTitle: "⚙️ Settings & Audio / Ajustes y Audio",
  ladyVoiceActive: "Lady Voice Active / Voz Femenina Activa",
  ladyVoiceDesc: "High-clarity female voice / Voz femenina clara",
  announcementLang: "🌐 Speech & UI Language / Idioma de Voz e Interfaz",
  done: "Done / Listo"
};

const fr: Translations = {
  ...en,
  settings: "Settings / Paramètres",
  audioSettingsTitle: "⚙️ Settings & Audio / Paramètres et Audio",
  ladyVoiceActive: "Lady Voice Active / Voix Féminine Active",
  ladyVoiceDesc: "High-clarity female voice / Annonces vocales féminines",
  announcementLang: "🌐 Speech & UI Language / Langue de la Voix et de l'Interface",
  done: "Done / Terminé"
};

const de: Translations = {
  ...en,
  settings: "Settings / Einstellungen",
  audioSettingsTitle: "⚙️ Settings & Audio / Einstellungen & Audio",
  ladyVoiceActive: "Lady Voice Active / Frauenstimme Aktiv",
  ladyVoiceDesc: "High-clarity female voice / Klarer weiblicher Sprachansager",
  announcementLang: "🌐 Speech & UI Language / Sprach- & UI-Sprache",
  done: "Done / Fertig"
};

const pt: Translations = {
  ...en,
  settings: "Settings / Configurações",
  audioSettingsTitle: "⚙️ Settings & Audio / Configurações e Áudio",
  ladyVoiceActive: "Lady Voice Active / Voz Feminina Ativa",
  ladyVoiceDesc: "High-clarity female voice / Voz feminina de alta clareza",
  announcementLang: "🌐 Speech & UI Language / Idioma da Voz e Interface",
  done: "Done / Concluído"
};

const it: Translations = {
  ...en,
  settings: "Settings / Impostazioni",
  audioSettingsTitle: "⚙️ Settings & Audio / Impostazioni e Audio",
  ladyVoiceActive: "Lady Voice Active / Voce Femminile Attiva",
  ladyVoiceDesc: "High-clarity female voice / Annunci con voce femminile",
  announcementLang: "🌐 Speech & UI Language / Lingua Voce e Interfaccia",
  done: "Done / Fatto"
};

const ar: Translations = {
  ...en,
  settings: "Settings / الإعدادات",
  audioSettingsTitle: "⚙️ Settings & Audio / الإعدادات والصوت",
  ladyVoiceActive: "Lady Voice Active / صوت أنثوي مفعل",
  ladyVoiceDesc: "High-clarity female voice / نطق أنثوي واضح",
  announcementLang: "🌐 Speech & UI Language / لغة الصوت والواجهة",
  done: "Done / تم"
};

const ja: Translations = {
  ...en,
  settings: "Settings / 設定",
  audioSettingsTitle: "⚙️ Settings & Audio / 設定と音声",
  ladyVoiceActive: "Lady Voice Active / 女性音声アクティブ",
  ladyVoiceDesc: "High-clarity female voice / 明瞭な女性アナウンス音声を使用",
  announcementLang: "🌐 Speech & UI Language / 音声と言語",
  done: "Done / 完了"
};

const ko: Translations = {
  ...en,
  settings: "Settings / 설정",
  audioSettingsTitle: "⚙️ Settings & Audio / 설정 및 오디오",
  ladyVoiceActive: "Lady Voice Active / 여성 음성 활성화",
  ladyVoiceDesc: "High-clarity female voice / 선명한 여성 음성 안내 사용",
  announcementLang: "🌐 Speech & UI Language / 음성 및 UI 언어",
  done: "Done / 완료"
};

const zh: Translations = {
  ...en,
  settings: "Settings / 设置",
  audioSettingsTitle: "⚙️ Settings & Audio / 设置与音频",
  ladyVoiceActive: "Lady Voice Active / 女声已启用",
  ladyVoiceDesc: "High-clarity female voice / 使用清晰女声播报",
  announcementLang: "🌐 Speech & UI Language / 语音与界面语言",
  done: "Done / 完成"
};

const ru: Translations = {
  ...en,
  settings: "Settings / Настройки",
  audioSettingsTitle: "⚙️ Settings & Audio / Настройки и Аудио",
  ladyVoiceActive: "Lady Voice Active / Женский Голос Активен",
  ladyVoiceDesc: "High-clarity female voice / Четкие женские голосовые объявления",
  announcementLang: "🌐 Speech & UI Language / Язык Озвучки и Интерфейса",
  done: "Done / Готово"
};

const vi: Translations = {
  ...en,
  settings: "Settings / Cài đặt",
  audioSettingsTitle: "⚙️ Settings & Audio / Cài đặt & Âm thanh",
  ladyVoiceActive: "Lady Voice Active / Giọng Nữ Đang Bật",
  ladyVoiceDesc: "High-clarity female voice / Phát âm giọng nữ rõ ràng",
  announcementLang: "🌐 Speech & UI Language / Ngôn ngữ Giọng nói & Giao diện",
  done: "Done / Xong"
};

const bn: Translations = {
  ...en,
  settings: "Settings / সেটিংস",
  audioSettingsTitle: "⚙️ Settings & Audio / সেটিংস ও অডিও",
  ladyVoiceActive: "Lady Voice Active / লেডি ভয়েস সক্রিয়",
  ladyVoiceDesc: "High-clarity female voice / স্পষ্ট নারী কণ্ঠস্বর",
  announcementLang: "🌐 Speech & UI Language / ভয়েস ও ভাষা",
  done: "Done / সম্পন্ন"
};

const ta: Translations = {
  ...en,
  settings: "Settings / அமைப்புகள்",
  audioSettingsTitle: "⚙️ Settings & Audio / அமைப்புகள் & ஒலி",
  ladyVoiceActive: "Lady Voice Active / பெண் குரல் சક્રિય",
  ladyVoiceDesc: "High-clarity female voice / தெளிவான பெண் குரல்",
  announcementLang: "🌐 Speech & UI Language / பேச்சு மற்றும் மொழி",
  done: "Done / முடிந்தது"
};

const te: Translations = {
  ...en,
  settings: "Settings / సెట్టింగ్‌లు",
  audioSettingsTitle: "⚙️ Settings & Audio / సెట్టింగ్‌లు & ఆడియో",
  ladyVoiceActive: "Lady Voice Active / లేడీ వాయిస్ సక్రియం",
  ladyVoiceDesc: "High-clarity female voice / మహిళా స్వరం",
  announcementLang: "🌐 Speech & UI Language / భాషా ఎంపిక",
  done: "Done / పూర్తయింది"
};

const mr: Translations = {
  ...en,
  settings: "Settings / सेटिंग्ज",
  audioSettingsTitle: "⚙️ Settings & Audio / सेटिंग्ज आणि ऑडिओ",
  ladyVoiceActive: "Lady Voice Active / महिला आवाज सक्रिय",
  ladyVoiceDesc: "High-clarity female voice / स्पष्ट महिला आवाज",
  announcementLang: "🌐 Speech & UI Language / भाषा निवड",
  done: "Done / पूर्ण"
};

const gu: Translations = {
  ...en,
  settings: "Settings / સેટિંગ્સ",
  audioSettingsTitle: "⚙️ Settings & Audio / સેટિંગ્સ અને ઓડિયો",
  ladyVoiceActive: "Lady Voice Active / મહિલા અવાજ સક્રિય",
  ladyVoiceDesc: "High-clarity female voice / મહિલા અવાજ",
  announcementLang: "🌐 Speech & UI Language / ભાષા પસંદગી",
  done: "Done / પૂર્ણ"
};

const kn: Translations = {
  ...en,
  settings: "Settings / ಸೆಟ್ಟಿಂಗ್‌ಗಳು",
  audioSettingsTitle: "⚙️ Settings & Audio / ಸೆಟ್ಟಿಂಗ್‌ಗಳು & ಆಡಿಯೋ",
  ladyVoiceActive: "Lady Voice Active / ಮಹಿಳಾ ಧ್ವನಿ ಸಕ್ರಿಯ",
  ladyVoiceDesc: "High-clarity female voice / ಮಹಿಳಾ ಧ್ವನಿ",
  announcementLang: "🌐 Speech & UI Language / ಭಾಷೆ ಆಯ್ಕೆ",
  done: "Done / ಮುಗಿಯಿತು"
};

const ml: Translations = {
  ...en,
  settings: "Settings / സെറ്റിംഗ്സ്",
  audioSettingsTitle: "⚙️ Settings & Audio / സെറ്റിംഗ്സ് & ഓഡിയോ",
  ladyVoiceActive: "Lady Voice Active / ലേഡി വോയ്‌സ് ആക്റ്റീവ്",
  ladyVoiceDesc: "High-clarity female voice / വനിതാ ശബ്ദം",
  announcementLang: "🌐 Speech & UI Language / ഭാഷ തെരഞ്ഞെടുപ്പ്",
  done: "Done / ശരി"
};

const pa: Translations = {
  ...en,
  settings: "Settings / ਸੈਟਿੰਗਾਂ",
  audioSettingsTitle: "⚙️ Settings & Audio / ਸੈਟਿੰਗਾਂ ਅਤੇ ਆਡੀਓ",
  ladyVoiceActive: "Lady Voice Active / ਮਹਿਲਾ ਆਵਾਜ਼ ਸਰਗਰਮ",
  ladyVoiceDesc: "High-clarity female voice / ਔਰਤ ਦੀ ਆਵਾਜ਼",
  announcementLang: "🌐 Speech & UI Language / ਭਾਸ਼ਾ ਦੀ ਚੋਣ",
  done: "Done / ਪੂਰਾ"
};

const or: Translations = {
  ...en,
  settings: "Settings / ସେଟିଂସ",
  audioSettingsTitle: "⚙️ Settings & Audio / ସେଟିଂସ ଏବଂ ଅଡିଓ",
  ladyVoiceActive: "Lady Voice Active / ମହିଳା ସ୍ୱର ସକ୍ରିୟ",
  ladyVoiceDesc: "High-clarity female voice / ମହିଳା ସ୍ୱର",
  announcementLang: "🌐 Speech & UI Language / ଭାଷା ଚୟନ",
  done: "Done / ସମ୍ପନ୍ନ"
};

const as: Translations = {
  ...en,
  settings: "Settings / ছেটিংছ",
  audioSettingsTitle: "⚙️ Settings & Audio / ছেটিংছ আৰু অডিঅ'",
  ladyVoiceActive: "Lady Voice Active / মহিলাৰ মাত সক্ৰিয়",
  ladyVoiceDesc: "High-clarity female voice / স্পষ্ট মহিলাৰ মাত",
  announcementLang: "🌐 Speech & UI Language / ভাষা নির্বাচন",
  done: "Done / সম্পন্ন"
};

const dictionaries: Record<string, Translations> = {
  en,
  hi,
  es,
  fr,
  de,
  pt,
  it,
  ar,
  ja,
  ko,
  zh,
  ru,
  vi,
  bn,
  ta,
  te,
  mr,
  gu,
  kn,
  ml,
  pa,
  or,
  as
};

export function getTranslations(lang: string = "en"): Translations {
  const code = lang.toLowerCase();
  return dictionaries[code] || dictionaries.en;
}
