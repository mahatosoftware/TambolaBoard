export const BCP47_MAP: Record<string, string> = {
  en: "en-US",
  hi: "hi-IN",
  kn: "kn-IN",
  bn: "bn-IN",
  or: "or-IN",
  ta: "ta-IN",
  mr: "mr-IN",
  te: "te-IN",
  gu: "gu-IN",
  ml: "ml-IN",
  pa: "pa-IN",
  as: "as-IN",
  pt: "pt-PT",
  fr: "fr-FR",
  de: "de-DE",
  ar: "ar-SA",
  id: "id-ID",
  tr: "tr-TR",
  it: "it-IT",
  ja: "ja-JP",
  ko: "ko-KR",
  zh: "zh-CN",
  nl: "nl-NL",
  ru: "ru-RU",
  vi: "vi-VN"
};

const englishPhrases: Record<number, string> = {
  1: "At the Beginning", 2: "Me and you", 3: "Happy family", 4: "Two Plus Two",
  5: "High five everyone", 6: "Bottom heavy", 7: "Lucky number", 8: "Big fat lady",
  9: "Doctor's time", 10: "A big fat hen", 11: "One and one", 12: "One dozen",
  13: "Unlucky for some", 14: "Valentine's Day", 15: "The age when attitude starts",
  16: "Sweet sixteen", 17: "Not so sweet", 18: "Voting age", 19: "Last of the teens",
  20: "One score", 21: "Women's age never crosses", 22: "Two little ducks", 23: "You and me",
  24: "Two dozen", 25: "Silver Jubilee Number", 26: "Mix and fix", 27: "Gateway to heaven",
  28: "Not so late at", 29: "Rise and Shine at", 30: "Women get flirty at", 31: "Time for fun",
  32: "Buckle my shoe", 33: "All the 3s", 34: "Ask for more", 35: "Three and Five",
  36: "Popular size", 37: "Mixed luck", 38: "Oversize", 39: "Watch your waistline",
  40: "Men get Naughty at", 41: "Four and one", 42: "Answer to everything", 43: "Pain in the knee",
  44: "All the Fours", 45: "Halfway there", 46: "Four and six", 47: "Heaven's seven",
  48: "Four dozen", 49: "Four and Nine", 50: "Half a century", 51: "Five and one",
  52: "Weeks in a year", 53: "Five and three", 54: "Time for Mooor", 55: "All the fives",
  56: "Pick up sticks", 57: "Mutiny Year", 58: "Time to retire", 59: "Five and Nine",
  60: "Five dozen", 61: "Bakers bun", 62: "Turn the screw", 63: "Tickle me",
  64: "Six and Four", 65: "Old age pension", 66: "Six and Six", 67: "Made in heaven",
  68: "Check your weight", 69: "Favourite of mine", 70: "Lucky blind", 71: "Bang on the drum",
  72: "Lucky two", 73: "Under the tree", 74: "Still want more", 75: "Diamond Jubilee",
  76: "Lucky six", 77: "Two hockey sticks", 78: "Heaven's gate", 79: "One more time",
  80: "Eight and Zero", 81: "Corner shot", 82: "Fat lady with a duck", 83: "Old but gold",
  84: "Seven Dozen", 85: "Staying alive", 86: "Between the sticks", 87: "Grandpa age",
  88: "Two fat ladies", 89: "All but one", 90: "Top of the house"
};

const hindiPhrases: Record<number, string> = {
  1: "एक से भले दो", 2: "दो दिल, एक टिकट", 3: "तीन तिगाड़ा, काम बिगाड़ा", 4: "चार यार, मस्ती अपार",
  5: "पाँच पांडव मैदान में", 6: "छक्का मारो, बाउंड्री पार", 7: "सात सुरों का संगम", 8: "आठ का ठाठ",
  9: "नौ दिन चले अढ़ाई कोस", 10: "दस का दम", 11: "एक और एक ग्यारह", 12: "बारह बजे",
  13: "तेरह में किस्मत", 14: "चौदहवीं का चाँद", 15: "पंद्रह अगस्त", 16: "सोलह श्रृंगार",
  20: "बीस का नोट", 21: "इक्कीस तोपों की सलामी", 22: "दो हंसों का जोड़ा", 25: "पच्चीस साल",
  26: "छब्बीस जनवरी", 30: "तीस का आंकड़ा", 32: "बत्तीस दाँत", 33: "डबल तीन",
  40: "चालीस का चक्कर", 44: "डबल चार", 50: "पचास! आधा शतक", 55: "डबल पाँच",
  60: "साठ की बात", 66: "डबल छक्का", 70: "सत्तर का दशक", 77: "दो बल्ले, सतहत्तर",
  80: "अस्सी का दशक", 88: "दो गोल-गोल, अट्ठासी", 90: "नब्बे! आखिरी नंबर"
};

export function numberToWords(n: number): string {
  const units = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"];
  const tens = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"];
  if (n < 20) return units[n];
  if (n % 10 === 0) return tens[Math.floor(n / 10)];
  return `${tens[Math.floor(n / 10)]} ${units[n % 10]}`;
}

export function getFunnyPhrase(number: number, languageCode: string = "en"): string {
  const lang = languageCode.toLowerCase();

  if (lang === "hi") {
    const phrase = hindiPhrases[number] || "";
    if (phrase) return `${phrase}... नंबर ${number}!`;
    return `नंबर ${number}!`;
  }
  if (lang === "es") return `¡Número ${number}!`;
  if (lang === "fr") return `Numéro ${number}!`;
  if (lang === "de") return `Nummer ${number}!`;
  if (lang === "pt") return `Número ${number}!`;
  if (lang === "it") return `Numero ${number}!`;
  if (lang === "nl") return `Nummer ${number}!`;
  if (lang === "ru") return `Номер ${number}!`;
  if (lang === "ar") return `رقم ${number}!`;
  if (lang === "ja") return `番号 ${number}!`;
  if (lang === "ko") return `${number}번!`;
  if (lang === "zh") return `${number}号!`;
  if (lang === "vi") return `Số ${number}!`;
  if (lang === "id") return `Nomor ${number}!`;
  if (lang === "tr") return `Numara ${number}!`;
  if (lang === "bn") return `নম্বর ${number}!`;
  if (lang === "or") return `ନମ୍ବର ${number}!`;
  if (lang === "kn") return `ಸಂಖ್ಯೆ ${number}!`;
  if (lang === "ta") return `எண் ${number}!`;
  if (lang === "mr") return `नंबर ${number}!`;
  if (lang === "te") return `సంఖ్య ${number}!`;
  if (lang === "gu") return `નંબર ${number}!`;
  if (lang === "ml") return `നമ്പർ ${number}!`;
  if (lang === "pa") return `ਨੰਬਰ ${number}!`;
  if (lang === "as") return `নম্বৰ ${number}!`;

  // Default English
  const phrase = englishPhrases[number] || "";
  const numberWord = numberToWords(number);
  if (phrase) {
    return `${phrase}... Number ${number}! ${numberWord}!`;
  }
  return `Number ${number}... ${numberWord}!`;
}
