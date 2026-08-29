package `in`.mahato.tambola.game.util

object FunnyPhraseUtil {

    fun getFunnyPhrase(number: Int, languageCode: String = "en"): String {
        return when (languageCode) {
            "bn" -> getBengaliNumberPhrase(number)
            "or" -> getOdiaNumberPhrase(number)
            "hi" -> getHindiNumberPhrase(number)
            "kn" -> getKannadaNumberPhrase(number)
            "ta" -> getTamilNumberPhrase(number)
            "mr" -> getMarathiNumberPhrase(number)
            "te" -> getTeluguNumberPhrase(number)
            "gu" -> getGujaratiNumberPhrase(number)
            "ml" -> getMalayalamNumberPhrase(number)
            "pa" -> getPunjabiNumberPhrase(number)
            "as" -> getAssameseNumberPhrase(number)
            "es" -> getSpanishNumberPhrase(number)
            "pt" -> getPortugueseNumberPhrase(number)
            "fr" -> getFrenchNumberPhrase(number)
            "de" -> getGermanNumberPhrase(number)
            "ar" -> getArabicNumberPhrase(number)
            "id" -> getIndonesianNumberPhrase(number)
            "tr" -> getTurkishNumberPhrase(number)
            "it" -> getItalianNumberPhrase(number)
            "ja" -> getJapaneseNumberPhrase(number)
            "ko" -> getKoreanNumberPhrase(number)
            "zh" -> getChineseNumberPhrase(number)
            "nl" -> getDutchNumberPhrase(number)
            "ru" -> getRussianNumberPhrase(number)
            "vi" -> getVietnameseNumberPhrase(number)
            else -> getEnglishNumberPhrase(number)
        }
    }

    private fun getEnglishNumberPhrase(number: Int): String {
        val text = englishFunnyPhrases[number] ?: "Number ${numberToWords(number)}"
        return "$text Number ${numberToWords(number)}"
    }

    private fun getBengaliNumberPhrase(number: Int): String {
        val word = bengaliNumberWords[number] ?: number.toString()
        val rhyme = bengaliFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, নম্বর $word" else "নম্বর $word, $number"
    }

    private fun getOdiaNumberPhrase(number: Int): String {
        val word = odiaNumberWords[number] ?: number.toString()
        val rhyme = odiaFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, ନମ୍ବର $word" else "ନମ୍ବର $word, $number"
    }

    private fun getHindiNumberPhrase(number: Int): String {
        val word = hindiNumberWords[number] ?: number.toString()
        val rhyme = hindiFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, नंबर $word" else "नंबर $word, $number"
    }

    private fun getKannadaNumberPhrase(number: Int): String {
        val word = kannadaNumberWords[number] ?: number.toString()
        val rhyme = kannadaFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, ಸಂಖ್ಯೆ $word" else "ಸಂಖ್ಯೆ $word, $number"
    }

    private fun getTamilNumberPhrase(number: Int): String {
        val word = tamilNumberWords[number] ?: number.toString()
        val rhyme = tamilFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, எண் $word" else "எண் $word, $number"
    }

    private fun getMarathiNumberPhrase(number: Int): String {
        val word = marathiNumberWords[number] ?: number.toString()
        val rhyme = marathiFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, नंबर $word" else "नंबर $word, $number"
    }

    private fun getTeluguNumberPhrase(number: Int): String {
        val word = teluguNumberWords[number] ?: number.toString()
        val rhyme = teluguFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, సంఖ్య $word" else "సంఖ్య $word, $number"
    }

    private fun getGujaratiNumberPhrase(number: Int): String {
        val word = gujaratiNumberWords[number] ?: number.toString()
        val rhyme = gujaratiFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, નંબર $word" else "નંબર $word, $number"
    }

    private fun getMalayalamNumberPhrase(number: Int): String {
        val word = malayalamNumberWords[number] ?: number.toString()
        val rhyme = malayalamFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, നമ്പർ $word" else "നമ്പർ $word, $number"
    }

    private fun getPunjabiNumberPhrase(number: Int): String {
        val word = punjabiNumberWords[number] ?: number.toString()
        val rhyme = punjabiFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, ਨੰਬਰ $word" else "ਨੰਬਰ $word, $number"
    }

    private fun getAssameseNumberPhrase(number: Int): String {
        val word = assameseNumberWords[number] ?: number.toString()
        val rhyme = assameseFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, নম্বৰ $word" else "নম্বৰ $word, $number"
    }

    private fun getSpanishNumberPhrase(number: Int): String {
        val word = spanishNumberWords[number] ?: number.toString()
        val rhyme = spanishFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, número $word" else "Número $word, $number"
    }

    private fun getPortugueseNumberPhrase(number: Int): String {
        val word = portugueseNumberWords[number] ?: number.toString()
        val rhyme = portugueseFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, número $word" else "Número $word, $number"
    }

    private fun getFrenchNumberPhrase(number: Int): String {
        val word = frenchNumberWords[number] ?: number.toString()
        val rhyme = frenchFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, numéro $word" else "Numéro $word, $number"
    }

    private fun getGermanNumberPhrase(number: Int): String {
        val word = germanNumberWords[number] ?: number.toString()
        val rhyme = germanFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, Nummer $word" else "Nummer $word, $number"
    }

    private fun getArabicNumberPhrase(number: Int): String {
        val word = arabicNumberWords[number] ?: number.toString()
        val rhyme = arabicFunnyPhrases[number]
        return if (rhyme != null) "$rhyme، رقم $word" else "رقم $word، $number"
    }

    private fun getIndonesianNumberPhrase(number: Int): String {
        val word = indonesianNumberWords[number] ?: number.toString()
        val rhyme = indonesianFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, nomor $word" else "Nomor $word, $number"
    }

    private fun getTurkishNumberPhrase(number: Int): String {
        val word = turkishNumberWords[number] ?: number.toString()
        val rhyme = turkishFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, numara $word" else "Numara $word, $number"
    }

    private fun getItalianNumberPhrase(number: Int): String {
        val word = italianNumberWords[number] ?: number.toString()
        val rhyme = italianFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, numero $word" else "Numero $word, $number"
    }

    private fun getJapaneseNumberPhrase(number: Int): String {
        val word = japaneseNumberWords[number] ?: number.toString()
        val rhyme = japaneseFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, 番号 $word" else "番号 $word, $number"
    }

    private fun getKoreanNumberPhrase(number: Int): String {
        val word = koreanNumberWords[number] ?: number.toString()
        val rhyme = koreanFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, 번호 $word" else "번호 $word, $number"
    }

    private fun getChineseNumberPhrase(number: Int): String {
        val word = chineseNumberWords[number] ?: number.toString()
        val rhyme = chineseFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, 号码 $word" else "号码 $word, $number"
    }

    private fun getDutchNumberPhrase(number: Int): String {
        val word = dutchNumberWords[number] ?: number.toString()
        val rhyme = dutchFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, nummer $word" else "Nummer $word, $number"
    }

    private fun getRussianNumberPhrase(number: Int): String {
        val word = russianNumberWords[number] ?: number.toString()
        val rhyme = russianFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, номер $word" else "Номер $word, $number"
    }

    private fun getVietnameseNumberPhrase(number: Int): String {
        val word = vietnameseNumberWords[number] ?: number.toString()
        val rhyme = vietnameseFunnyPhrases[number]
        return if (rhyme != null) "$rhyme, số $word" else "Số $word, $number"
    }

    fun numberToWords(number: Int): String {
        val units = arrayOf(
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
            "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
        )
        val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

        return when {
            number !in 1..90 -> "Out of range"
            number < 20 -> units[number]
            number % 10 == 0 -> tens[number / 10]
            else -> tens[number / 10] + " " + units[number % 10]
        }
    }

    private val englishFunnyPhrases = mapOf(
        1 to "At the Beginning.",
        2 to "Me and you.",
        3 to "Happy family.",
        4 to "Two Plus Two.",
        5 to "High five everyone.",
        6 to "Bottom heavy.",
        7 to "Lucky number.",
        8 to "Big fat lady.",
        9 to "Doctor's time.",
        10 to "A big fat hen.",
        11 to "One and one.",
        12 to "One dozen.",
        13 to "Unlucky for some.",
        14 to "Valentine's Day.",
        15 to "The age when attitude starts.",
        16 to "Sweet sixteen.",
        17 to "Not so sweet.",
        18 to "Voting age.",
        19 to "Last of the teens.",
        20 to "One score.",
        21 to "Women's age never crosses.",
        22 to "Two little ducks.",
        23 to "You and me.",
        24 to "Two dozen.",
        25 to "Silver Jubilee Number.",
        26 to "Mix and fix.",
        27 to "Gateway to heaven.",
        28 to "Not so late at.",
        29 to "Rise and Shine at.",
        30 to "Women get flirty at.",
        31 to "Time for fun.",
        32 to "Buckle my shoe.",
        33 to "All the 3s.",
        34 to "Ask for more.",
        35 to "Three and Five.",
        36 to "Popular size.",
        37 to "Mixed luck.",
        38 to "Oversize.",
        39 to "Watch your waistline.",
        40 to "Men get Naughty at.",
        41 to "Four and one.",
        42 to "Answer to everything.",
        43 to "Pain in the knee.",
        44 to "All the Fours.",
        45 to "Halfway there.",
        46 to "Four and six.",
        47 to "Heaven’s seven.",
        48 to "Four dozen.",
        49 to "Four and Nine.",
        50 to "Half a century.",
        51 to "Five and one.",
        52 to "Weeks in a year.",
        53 to "Five and three.",
        54 to "Time for Mooor.",
        55 to "All the fives.",
        56 to "Pick up sticks.",
        57 to "Mutiny Year.",
        58 to "Time to retire.",
        59 to "Five and Nine.",
        60 to "Five dozen.",
        61 to "Bakers bun.",
        62 to "Turn the screw.",
        63 to "Tickle me.",
        64 to "Six and Four.",
        65 to "Old age pension.",
        66 to "Six and Six.",
        67 to "Made in heaven.",
        68 to "Check your weight.",
        69 to "Favourite of mine.",
        70 to "Lucky blind.",
        71 to "Bang on the drum.",
        72 to "Lucky two.",
        73 to "Under the tree.",
        74 to "Still want more.",
        75 to "Diamond Jubilee.",
        76 to "Lucky six.",
        77 to "Two hockey sticks.",
        78 to "Heaven's gate.",
        79 to "One more time.",
        80 to "Eight and Zero.",
        81 to "Corner shot.",
        82 to "Fat lady with a duck.",
        83 to "Old but gold.",
        84 to "Seven Dozen.",
        85 to "Staying alive.",
        86 to "Between the sticks.",
        87 to "Grandpa age.",
        88 to "Two fat ladies.",
        89 to "All but one.",
        90 to "Top of the house."
    )

    private val bengaliFunnyPhrases = mapOf(
        1 to "একলা চলো রে",
        2 to "দুই বাংলার এক মন",
        3 to "তিন গোয়েন্দা হাজির",
        4 to "চারমূর্তি বেরিয়েছে অভিযানে",
        5 to "পাঁচফোড়নে জমে গেল রান্না",
        6 to "ছয় ঋতুর বাংলা",
        7 to "সাত পাকে বাঁধা",
        8 to "অষ্টমীর অঞ্জলি",
        9 to "নবমীর রাত, ঠাকুর দেখার মাত",
        10 to "দশমীর মন খারাপ",

        12 to "বারো মাসে তেরো পার্বণ",
        13 to "তেরো পার্বণে বাঙালি হাজির",
        14 to "চোদ্দ শাক, ভূত চতুর্দশী",
        15 to "পনেরোতেই পয়লা বৈশাখের আমেজ",
        16 to "ষোলো আনা বাঙালি",

        21 to "একুশে ফেব্রুয়ারি, ভাষার গর্ব",
        22 to "বাইশে শ্রাবণ, কবিগুরুকে স্মরণ",
        23 to "তেইশে জানুয়ারি, নেতাজিকে সেলাম",

        25 to "পঁচিশে বৈশাখ, কবিগুরুর জন্মদিন",

        32 to "বত্রিশ পাটি দাঁত বার করে হাসুন",
        36 to "ছত্রিশ রকম রান্না, বাঙালির খানা",

        40 to "চল্লিশে চালসে নয়, খেলা এখনও বাকি",

        50 to "পঞ্চাশে পঞ্চব্যঞ্জন চাই",

        52 to "বাহান্নর ভাষা আন্দোলন",

        60 to "ষাটে এসে মিষ্টিমুখ হোক",

        64 to "চৌষট্টি কলায় পারদর্শী",

        70 to "সত্তরের দশক, কলকাতার অন্য রূপ",

        75 to "পঁচাত্তরে পাকা বাঙালি",

        80 to "আশিতে আসিও না, মাছ-ভাত খেয়ে যেও",

        90 to "নব্বইয়ে শেষ, এবার মিষ্টিমুখ বেশ"
    )

    private val odiaFunnyPhrases = mapOf(
        1 to "ଶ୍ରୀଜଗନ୍ନାଥଙ୍କ ନାମରେ ଶୁଭ ଆରମ୍ଭ",
        2 to "ତୁମେ ଆଉ ମୁଁ, ଦୁଇଜଣ",
        3 to "ତିନି ରଥର ମହିମା",
        4 to "ଚାରିଧାମ ଯାତ୍ରା",
        5 to "ପାଞ୍ଚ ପାଣ୍ଡବଙ୍କ ବୀରତ୍ୱ",
        6 to "ଛଅ ଋତୁର ଓଡ଼ିଶା",
        7 to "ସାତ ଦରିଆ ପାରି",
        8 to "ଅଷ୍ଟମୀର ଅଞ୍ଜଳି",
        9 to "ନବମୀର ଭୋଗ, ଆହା କି ମଜା",
        10 to "ଦଶମୀରେ ବିଜୟା ଦଶମୀ",

        11 to "ଏକ ଆଉ ଏକ, ଏଗାର",
        12 to "ବାରଟି ରସଗୋଲା, କିଏ ଖାଇବ?",
        13 to "ତେରରେ ଭାଗ୍ୟ ଖୋଲିଲା",
        14 to "ଚଉଦ ଶାଗର ସ୍ୱାଦ",
        15 to "ପନ୍ଦର ଅଗଷ୍ଟ, ସ୍ୱାଧୀନତାର ଦିନ",
        16 to "ଷୋହଳ କଳାରେ ସମ୍ପୂର୍ଣ୍ଣ",
        17 to "ସତରରେ ସାତ ସୁରର ମଜା",
        18 to "ଅଠରରେ ଆଜି ଭାଗ୍ୟ ପରୀକ୍ଷା",
        19 to "ଊଣେଇଶରେ ଟିକେଟ୍ ଦେଖନ୍ତୁ",
        20 to "କୋଡ଼ିଏରେ ଖେଳ ଜମିଲା",

        21 to "ଏକୋଇଶରେ ଏକ ନୂଆ ଆଶା",
        22 to "ଦୁଇଟି ହଂସ, ବାଇଶ",
        23 to "ତେଇଶରେ ତାଳିଟା ହେଉ",
        24 to "ଚବିଶ ଘଣ୍ଟା ଗପ ହେଲେ ବି କମ୍",
        25 to "ପଚିଶେ ପଖାଳ ଭାତ ମନେ ପଡ଼ିଲା",
        26 to "ଛବିଶରେ ଛକା ନୁହେଁ, ଟିକେଟ୍ ଦେଖ",
        27 to "ସତେଇଶରେ ସବୁଙ୍କ ନଜର",
        28 to "ଅଠେଇଶରେ ଆଡ଼ା ଜମିଲା",
        29 to "ଅଣତିରିଶରେ ଉତ୍ସାହ ବଢ଼ିଲା",
        30 to "ତିରିଶରେ ଟିକେଟ୍ ଚେକ୍ କରନ୍ତୁ",

        31 to "ଏକତିରିଶ, ଏବେ ଟିକେ ଧ୍ୟାନ ଦିଅନ୍ତୁ",
        32 to "ବତିଶ ପାଟି ଦେଖାଇ ହସନ୍ତୁ",
        33 to "ଡବଲ୍ ତିନି, ଡବଲ୍ ମଜା",
        34 to "ଚଉତିରିଶରେ ଚମକ",
        35 to "ପଇଁତିରିଶରେ ପକୋଡ଼ି ଦରକାର",
        36 to "ଛତିଶ ପ୍ରକାର ଖାଇବା, ଓଡ଼ିଆଙ୍କ ମନ ଖୁସି",
        37 to "ସଇଁତିରିଶରେ ସବୁଠି ଶାନ୍ତି",
        38 to "ଅଠତିରିଶରେ ଆଡ଼ା ଆଉ ଜୋର",
        39 to "ଅଣଚାଳିଶରେ ଆଉ ଟିକେ ଅପେକ୍ଷା",
        40 to "ଚାଳିଶରେ ଚା' ଆଉ ଗପ ଜମିବ",

        41 to "ଏକଚାଳିଶରେ ଏକ ନୂଆ ମୋଡ଼",
        42 to "ବୟାଳିଶରେ ବାଜି ଜମିଲା",
        43 to "ତେୟାଳିଶରେ ଟିକେଟ୍ ଦେଖନ୍ତୁ",
        44 to "ଡବଲ୍ ଚାରି, ଡବଲ୍ ଧମାଲ୍",
        45 to "ପଇଁଚାଳିଶରେ ପେଟ ପୂଜା ଦରକାର",
        46 to "ଛୟାଳିଶରେ ଛକା ମାର",
        47 to "ସତଚାଳିଶରେ ସବୁଙ୍କ ଆଖି ଏଠି",
        48 to "ଅଠଚାଳିଶରେ ଅପେକ୍ଷା ଶେଷ",
        49 to "ଅଣପଚାଶ, ପଚାଶ ଆସୁଛି",
        50 to "ପଚାଶ! ଅଧା ଶତକ, ତାଳି ହେଉ",

        51 to "ଏକାବନରେ ଏବେ ମଜା ଆରମ୍ଭ",
        52 to "ବାଉନରେ ବାଜି ଆପଣଙ୍କର",
        53 to "ତେପନରେ ଟିକେଟ୍ ଦେଖନ୍ତୁ",
        54 to "ଚଉବନରେ ଚା' ହେବ କି?",
        55 to "ଡବଲ୍ ପାଞ୍ଚ, ଭାଗ୍ୟ ଆଜି ଭଲ",
        56 to "ଛପନରେ ଛକା ଲାଗିଗଲା",
        57 to "ସତାବନରେ ସବୁଙ୍କ ନଜର",
        58 to "ଅଠାବନରେ ଆଉ ଟିକେ ଟେନସନ",
        59 to "ଅଣଷାଠିରେ ଷାଠି ପାଖେଇଲା",
        60 to "ଷାଠି! ଛେନାପୋଡ଼ ଖାଇବାକୁ ହେବ",

        61 to "ଏକଷଠିରେ ଏବେ ନିକଟରେ",
        62 to "ବାଷଠିରେ ବାଜି ଗରମ",
        63 to "ତେଷଠିରେ ଟେନସନ ବଢ଼ୁଛି",
        64 to "ଚଉଷଠି କଳାରେ କିଏ ମାହିର?",
        65 to "ପଇଁଷଠିରେ ପଖାଳ ଭାତ ଚାହିଁ",
        66 to "ଡବଲ୍ ଛଅ, ଏବେ ଛକା ହେବ",
        67 to "ସତଷଠିରେ ସବୁଠି ଶାନ୍ତ",
        68 to "ଅଠଷଠିରେ ଆଡ଼ା ଜମିଲା",
        69 to "ଅଣସ୍ତରିରେ ଉଫ୍! କି ଟେନସନ",
        70 to "ସତୁରିରେ ସବୁଙ୍କ ନଜର",

        71 to "ଏକସ୍ତରିରେ ଏକଦମ ନିକଟ",
        72 to "ବାହତ୍ତରରେ ବାଜିମାତ",
        73 to "ତେସ୍ତରିରେ ଟିକେଟ୍ ଚେକ୍ କରନ୍ତୁ",
        74 to "ଚଉସ୍ତରିରେ ଚମତ୍କାର",
        75 to "ପଞ୍ଚସ୍ତରିରେ ପାକା ଖେଳାଳି",
        76 to "ଛିଅସ୍ତରିରେ ଛକା",
        77 to "ଦୁଇଟି ସୁନ୍ଦର ଠେଙ୍ଗା, ସତସ୍ତରି",
        78 to "ଅଠସ୍ତରିରେ ଆଉ ଟିକେ ଧୈର୍ଯ୍ୟ",
        79 to "ଅଣାଅଶୀରେ ଉତ୍ସାହ ଚରମରେ",
        80 to "ଅଶୀରେ ଆଶା ଆହୁରି ବଢ଼ିଲା",

        81 to "ଏକାଅଶୀରେ ଏକଦମ ନିକଟ",
        82 to "ବୟାଅଶୀରେ ବାଜି ଜମିଲା",
        83 to "ତେୟାଅଶୀରେ ଟିକେଟ୍ ଦେଖନ୍ତୁ",
        84 to "ଚଉରାଅଶୀରେ ଚମକ ଦେଖାଦେଲା",
        85 to "ପଞ୍ଚାଅଶୀରେ ପାଖେଇଲା ଜିତ",
        86 to "ଛଅଅଶୀରେ ଛକା ନିଶ୍ଚିତ",
        87 to "ସତାଅଶୀରେ ସବୁଙ୍କ ଆଖି ବୋର୍ଡରେ",
        88 to "ଦୁଇଟି ଗୋଲା ଗୋଲା, ଅଠାଅଶୀ",
        89 to "ଅଣନବେରେ ଶେଷ ପୂର୍ବର ଡାକ",
        90 to "ନବେ! ଶେଷ ଡାକ, ଟିକେଟ୍ ଦେଖନ୍ତୁ"
    )

    private val hindiFunnyPhrases = mapOf(
        1 to "एक से भले दो, लेकिन खेल शुरू एक से",
        2 to "दो दिल, एक टिकट",
        3 to "तीन तिगाड़ा, काम बिगाड़ा",
        4 to "चार यार, मस्ती अपार",
        5 to "पाँच पांडव मैदान में",
        6 to "छक्का मारो, बाउंड्री पार",
        7 to "सात सुरों का संगम",
        8 to "आठ का ठाठ ही अलग है",
        9 to "नौ दिन चले अढ़ाई कोस",
        10 to "दस का दम, खेल में गरम",

        11 to "एक और एक ग्यारह",
        12 to "बारह बजे, अब खेल सजे",
        13 to "तेरह में किस्मत का फेर",
        14 to "चौदहवीं का चाँद",
        15 to "पंद्रह अगस्त, आज़ादी का जश्न",
        16 to "सोलह श्रृंगार, खेल शानदार",
        17 to "सत्रह में किस्मत जागी",
        18 to "अठारह में जोश जवान",
        19 to "उन्नीस, बीस आने वाला है",
        20 to "बीस का नोट, किस्मत की चोट",

        21 to "इक्कीस तोपों की सलामी",
        22 to "दो हंसों का जोड़ा",
        23 to "तेईस, अब क्या है शेष?",
        24 to "चौबीस घंटे भी कम पड़ जाएँ",
        25 to "पच्चीस, बसंती का डायलॉग याद आया",
        26 to "छब्बीस जनवरी, गणतंत्र का त्योहार",
        27 to "सत्ताईस में सस्पेंस बरकरार",
        28 to "अट्ठाईस, अब तो किस्मत खोलो",
        29 to "उनतीस, जीत से एक कदम दूर",
        30 to "तीस का आंकड़ा, खेल तगड़ा",

        31 to "इकतीस, अब ध्यान से देखो",
        32 to "बत्तीस दाँत, मुस्कान अनंत",
        33 to "डबल तीन, डबल मज़ा",
        34 to "चौंतीस में चौका लगाओ",
        35 to "पैंतीस, चाय का टाइम हुआ क्या?",
        36 to "छत्तीस का आंकड़ा, दुश्मनी पुरानी",
        37 to "सैंतीस में सस्पेंस जारी",
        38 to "अड़तीस, किस्मत के दरवाज़े खोलो",
        39 to "उनतालीस, चालीस बस आने वाला है",
        40 to "चालीस का चक्कर, खेल का मज़ा",

        41 to "इकतालीस, किसकी किस्मत चमकी?",
        42 to "बयालीस, जवाब तो बनता है",
        43 to "तैंतालीस, टिकट संभाल के रखना",
        44 to "डबल चार, डबल धमाल",
        45 to "पैंतालीस, चाय और पकौड़े चाहिए",
        46 to "छियालीस, छक्का लगाने का टाइम",
        47 to "सैंतालीस, सबकी नजर टिकट पर",
        48 to "अड़तालीस, जीत अब पास है",
        49 to "उनचास, पचास की तैयारी",
        50 to "पचास! आधा शतक, तालियाँ बजाओ",

        51 to "इक्यावन, जीत की तरफ कदम",
        52 to "बावन, अब खेल हुआ जवान",
        53 to "तिरेपन, किस्मत का क्या कहना",
        54 to "चौवन, चाय का एक कप हो जाए",
        55 to "डबल पाँच, डबल खुशियाँ",
        56 to "छप्पन, छप्पन भोग की याद आई",
        57 to "सत्तावन, टिकट फिर से देखो",
        58 to "अट्ठावन, टेंशन बढ़ रही है",
        59 to "उनसठ, साठ बस आने वाला है",
        60 to "साठ! अब तो जीत की बात",

        61 to "इकसठ, अब मामला दिलचस्प है",
        62 to "बासठ, बाज़ी गर्म है",
        63 to "तिरसठ, किस्मत पलट सकती है",
        64 to "चौंसठ कला में कौन माहिर?",
        65 to "पैंसठ, अब तो नज़र बोर्ड पर",
        66 to "डबल छक्का, क्रिकेट का मज़ा",
        67 to "सड़सठ, सबकी धड़कन तेज़",
        68 to "अड़सठ, जीत की खुशबू आ रही है",
        69 to "उनहत्तर, अरे वाह क्या नंबर है",
        70 to "सत्तर, अब खेल गंभीर",

        71 to "इकहत्तर, एक कदम और",
        72 to "बहत्तर, बाज़ी किसकी?",
        73 to "तिहत्तर, टिकट चेक करो भाई",
        74 to "चौहत्तर, चौका लगने वाला है",
        75 to "पचहत्तर, पौन शतक पूरा",
        76 to "छिहत्तर, छक्का फिर से",
        77 to "दो बल्ले खड़े, सतहत्तर",
        78 to "अठहत्तर, किस्मत का खेल",
        79 to "उन्नासी, नब्बे करीब है",
        80 to "अस्सी, अब तो दिल थाम लो",

        81 to "इक्यासी, जीत बहुत पास",
        82 to "बयासी, बाज़ी पलट सकती है",
        83 to "तिरासी, टिकट संभालो",
        84 to "चौरासी, कौन बनेगा विजेता?",
        85 to "पचासी, अब तो दिल धड़क रहा है",
        86 to "छियासी, छक्का पक्का",
        87 to "सत्तासी, सबकी नजर बोर्ड पर",
        88 to "दो गोल-गोल, अट्ठासी",
        89 to "नवासी, आखिरी पड़ाव",
        90 to "नब्बे! आखिरी नंबर, टिकट देख लो"
    )

    private val kannadaFunnyPhrases = mapOf(
        1 to "ಶುಭಾರಂಭ, ಮೊದಲ ಹೆಜ್ಜೆ",
        2 to "ನೀನು ನಾನು, ಇಬ್ಬರು",
        3 to "ಮೂರು ಮೂರ್ತಿ, ಭರ್ಜರಿ ಎಂಟ್ರಿ",
        4 to "ನಾಲ್ಕು ದಿಕ್ಕಲ್ಲೂ ಸಂಭ್ರಮ",
        5 to "ಪಂಚ ಪಾಂಡವರ ಪವರ್",
        6 to "ಸಿಕ್ಸರ್, ಬೌಂಡರಿ ದಾಟಿತು",
        7 to "ಏಳು ಬೆಟ್ಟದ ತಿರುಪತಿ ನೆನಪು",
        8 to "ಎಂಟರ ಆಟ, ಭರ್ಜರಿ ಜೋಶ್",
        9 to "ಒಂಬತ್ತು ಹೆಜ್ಜೆ, ಗೆಲುವಿನ ಕಡೆ",
        10 to "ಹತ್ತು ಹತ್ತು, ಆಟ ಸೂಪರ್",

        11 to "ಒಂದು ಪಕ್ಕ ಒಂದು, ಹನ್ನೊಂದು",
        12 to "ಒಂದು ಡಜನ್ ಬಾಳೆಹಣ್ಣು",
        13 to "ಹದಿಮೂರು, ಅದೃಷ್ಟದ ಆಟ",
        14 to "ಹದಿನಾಲ್ಕು, ಏನು ಮಜಾ ಗುರು!",
        15 to "ಹದಿನೈದು ಆಗಸ್ಟ್, ಸ್ವಾತಂತ್ರ್ಯದ ಸಂಭ್ರಮ",
        16 to "ಹದಿನಾರು ಕಲೆಗಳಲ್ಲಿ ಸಂಪೂರ್ಣ",
        17 to "ಹದಿನೇಳು, ಅದೃಷ್ಟ ಎಚ್ಚರವಾಗಿದೆ",
        18 to "ಹದಿನೆಂಟು, ಜೋಶ್ ಜೋರಾಗಿದೆ",
        19 to "ಹತ್ತೊಂಬತ್ತು, ಇಪ್ಪತ್ತು ಹತ್ತಿರ",
        20 to "ಇಪ್ಪತ್ತು, ಆಟಕ್ಕೆ ಕಾವು",

        21 to "ಇಪ್ಪತ್ತೊಂದು, ಗೆಲುವಿನ ಮೊದಲ ಹೆಜ್ಜೆ",
        22 to "ಎರಡು ಬಾತುಕೋಳಿಗಳು, ಇಪ್ಪತ್ತೆರಡು",
        23 to "ಇಪ್ಪತ್ತಮೂರು, ಟಿಕೆಟ್ ನೋಡಿ ಗುರು",
        24 to "ಇಪ್ಪತ್ನಾಲ್ಕು, ದಿನದ ಇಪ್ಪತ್ನಾಲ್ಕು ಗಂಟೆ",
        25 to "ಇಪ್ಪತ್ತೈದು, ಇವತ್ತು ಅದೃಷ್ಟ ನಿಮ್ಮದೇ?",
        26 to "ಇಪ್ಪತ್ತಾರು ಜನವರಿ, ಗಣರಾಜ್ಯೋತ್ಸವ",
        27 to "ಇಪ್ಪತ್ತೇಳು, ಆಟ ಇನ್ನೂ ಬಾಕಿ",
        28 to "ಇಪ್ಪತ್ತೆಂಟು, ಗೆಲುವು ಹತ್ತಿರ",
        29 to "ಇಪ್ಪತ್ತೊಂಬತ್ತು, ಇನ್ನೊಂದು ಹೆಜ್ಜೆ",
        30 to "ಮೂವತ್ತು, ಆಟ ಗಂಭೀರವಾಗಿದೆ",

        31 to "ಮೂವತ್ತೊಂದು, ಟಿಕೆಟ್ ಚೆಕ್ ಮಾಡಿ",
        32 to "ಮೂವತ್ತೆರಡು ಹಲ್ಲು, ನಗು ಮಾತ್ರ ಕಡಿಮೆ",
        33 to "ಡಬಲ್ ಮೂರು, ಡಬಲ್ ಮಜಾ",
        34 to "ಮೂವತ್ನಾಲ್ಕು, ಚೌಕಾ ಹೊಡೆಯೋಣ",
        35 to "ಮೂವತ್ತೈದು, ಚಹಾ ಬೇಕಾ ಗುರು?",
        36 to "ಮೂವತ್ತಾರು, ಭರ್ಜರಿ ಬಾಂಧವ್ಯ",
        37 to "ಮೂವತ್ತೇಳು, ಎಲ್ಲರ ಕಣ್ಣು ಬೋರ್ಡ್ ಮೇಲೆ",
        38 to "ಮೂವತ್ತೆಂಟು, ಆಟ ರಂಗೇರಿದೆ",
        39 to "ಮೂವತ್ತೊಂಬತ್ತು, ನಲವತ್ತು ಹತ್ತಿರ",
        40 to "ನಲವತ್ತು, ಟೀ ಟೈಮ್ ಆಯ್ತಾ?",

        41 to "ನಲವತ್ತೊಂದು, ಅದೃಷ್ಟ ಯಾರ ಕಡೆ?",
        42 to "ನಲವತ್ತೆರಡು, ಆಟ ಕಾವೇರಿದೆ",
        43 to "ನಲವತ್ತಮೂರು, ಟಿಕೆಟ್ ನೋಡ್ಕೊಳ್ಳಿ",
        44 to "ಡಬಲ್ ನಾಲ್ಕು, ಡಬಲ್ ಧಮಾಕಾ",
        45 to "ನಲವತ್ತೈದು, ಬಿಸಿ ಬಿಸಿ ಬಜ್ಜಿ ಬೇಕು",
        46 to "ನಲವತ್ತಾರು, ಸಿಕ್ಸರ್ ಹೊಡೆಯೋಣ",
        47 to "ನಲವತ್ತೇಳು, ಎಲ್ಲರ ಕಣ್ಣು ಇಲ್ಲಿ",
        48 to "ನಲವತ್ತೆಂಟು, ಗೆಲುವು ಹತ್ತಿರ",
        49 to "ನಲವತ್ತೊಂಬತ್ತು, ಐವತ್ತು ಬರ್ತಿದೆ",
        50 to "ಐವತ್ತು! ಅರ್ಧ ಶತಕದ ಸಂಭ್ರಮ",

        51 to "ಐವತ್ತೊಂದು, ಆಟ ಜೋರಾಗಿದೆ",
        52 to "ಐವತ್ತೆರಡು, ಅದೃಷ್ಟದ ಆಟ",
        53 to "ಐವತ್ತಮೂರು, ಟಿಕೆಟ್ ಚೆಕ್ ಮಾಡಿ",
        54 to "ಐವತ್ತನಾಲ್ಕು, ಒಂದು ಕಪ್ ಚಹಾ ಆಗಲಿ",
        55 to "ಡಬಲ್ ಐದು, ಡಬಲ್ ಖುಷಿ",
        56 to "ಐವತ್ತಾರು, ಈಗ ಆಟ ಬಿಸಿ",
        57 to "ಐವತ್ತೇಳು, ಗೆಲುವು ಯಾರದ್ದು?",
        58 to "ಐವತ್ತೆಂಟು, ಟೆನ್ಷನ್ ಶುರು",
        59 to "ಐವತ್ತೊಂಬತ್ತು, ಅರವತ್ತು ಹತ್ತಿರ",
        60 to "ಅರವತ್ತು, ಈಗ ನಿಜವಾದ ಆಟ",

        61 to "ಅರವತ್ತೊಂದು, ಗೆಲುವು ಹತ್ತಿರ",
        62 to "ಅರವತ್ತೆರಡು, ಅದೃಷ್ಟ ಪರೀಕ್ಷೆ",
        63 to "ಅರವತ್ತಮೂರು, ಟೆನ್ಷನ್ ಜಾಸ್ತಿ",
        64 to "ಅರವತ್ತನಾಲ್ಕು, ಅರವತ್ತನಾಲ್ಕು ಕಲೆಗಳು",
        65 to "ಅರವತ್ತೈದು, ಟಿಕೆಟ್ ನೋಡಿದ್ರಾ?",
        66 to "ಡಬಲ್ ಆರು, ಡಬಲ್ ಸಿಕ್ಸರ್",
        67 to "ಅರವತ್ತೇಳು, ಹೃದಯ ಬಡಿತ ಜೋರು",
        68 to "ಅರವತ್ತೆಂಟು, ಗೆಲುವಿನ ವಾಸನೆ",
        69 to "ಅರವತ್ತೊಂಬತ್ತು, ಅಯ್ಯೋ ಏನ್ ನಂಬರ್!",
        70 to "ಎಪ್ಪತ್ತು, ಆಟ ಕ್ಲೈಮ್ಯಾಕ್ಸ್ ಕಡೆ",

        71 to "ಎಪ್ಪತ್ತೊಂದು, ಇನ್ನೊಂದು ಹೆಜ್ಜೆ",
        72 to "ಎಪ್ಪತ್ತೆರಡು, ಬಾಜಿ ಯಾರದ್ದು?",
        73 to "ಎಪ್ಪತ್ತಮೂರು, ಟಿಕೆಟ್ ನೋಡ್ಕೊಳ್ಳಿ",
        74 to "ಎಪ್ಪತ್ತನಾಲ್ಕು, ಚೌಕಾ ಹೊಡೆಯೋಣ",
        75 to "ಎಪ್ಪತ್ತೈದು, ಪೌಣ ಶತಕದ ಸಂಭ್ರಮ",
        76 to "ಎಪ್ಪತ್ತಾರು, ಸಿಕ್ಸರ್ ಮತ್ತೆ",
        77 to "ಎರಡು ಹಾಕಿ ಸ್ಟಿಕ್, ಎಪ್ಪತ್ತೇಳು",
        78 to "ಎಪ್ಪತ್ತೆಂಟು, ಗೆಲುವು ಹತ್ತಿರ",
        79 to "ಎಪ್ಪತ್ತೊಂಬತ್ತು, ಎಂಭತ್ತು ಬರ್ತಿದೆ",
        80 to "ಎಂಭತ್ತು, ಈಗ ಹೃದಯ ಹಿಡ್ಕೊಳ್ಳಿ",

        81 to "ಎಂಭತ್ತೊಂದು, ಗೆಲುವು ತುಂಬಾ ಹತ್ತಿರ",
        82 to "ಎಂಭತ್ತೆರಡು, ಬಾಜಿ ಬದಲಾಗಬಹುದು",
        83 to "ಎಂಭತ್ತಮೂರು, ಟಿಕೆಟ್ ಚೆಕ್ ಮಾಡಿ",
        84 to "ಎಂಭತ್ತನಾಲ್ಕು, ಅದೃಷ್ಟದ ಆಟ",
        85 to "ಎಂಭತ್ತೈದು, ಟೆನ್ಷನ್ ಜೋರಾಗಿದೆ",
        86 to "ಎಂಭತ್ತಾರು, ಸಿಕ್ಸರ್ ಪಕ್ಕಾ",
        87 to "ಎಂಭತ್ತೇಳು, ಎಲ್ಲರ ಕಣ್ಣು ಬೋರ್ಡ್ ಮೇಲೆ",
        88 to "ಎರಡು ಗೋಲು ಗೋಲು, ಎಂಭತ್ತೆಂಟು",
        89 to "ಎಂಭತ್ತೊಂಬತ್ತು, ಕೊನೆಯ ಹಂತ",
        90 to "ತೊಂಬತ್ತು! ಕೊನೆಯ ನಂಬರ್, ಟಿಕೆಟ್ ನೋಡಿ!"
    )

    private val tamilFunnyPhrases = mapOf(
        1 to "முதல் படி, வெற்றியின் தொடக்கம்",
        2 to "நீயும் நானும், ரெண்டு பேரு",
        3 to "மூணு பேரு சேர்ந்தா மாஸ்",
        4 to "நாலு திசையும் நம்ம பக்கம்",
        5 to "அஞ்சு பாண்டவர் பலம்",
        6 to "சிக்ஸர் அடிச்சாச்சு, பவுண்டரி தாண்டியாச்சு",
        7 to "ஏழு கடல் தாண்டி வந்த ஏழு",
        8 to "எட்டு திக்கும் புகழ்",
        9 to "ஒன்பது, அதிர்ஷ்டம் வருது",
        10 to "பத்து பத்து, ஆட்டம் கெத்து",

        11 to "ஒன்றும் ஒன்றும் பதினொன்று",
        12 to "ஒரு டஜன் வாழைப்பழம்",
        13 to "பதிமூன்று, அதிர்ஷ்டம் உண்டா பார்ப்போம்",
        14 to "பதினான்கு, டிக்கெட்டை பாருங்க",
        15 to "ஆகஸ்ட் பதினைந்து, சுதந்திர தினம்",
        16 to "பதினாறு வயதினிலே",
        17 to "பதினேழு, ஆட்டம் தொடருது",
        18 to "பதினெட்டு, ஜாலியா போகுது",
        19 to "பத்தொன்பது, இருபது பக்கத்துல",
        20 to "இருபது, ஆட்டம் சூடு பிடிக்குது",

        21 to "இருபத்தொன்று, வெற்றி வாசலில்",
        22 to "இரண்டு வாத்துக்கள், இருபத்திரண்டு",
        23 to "இருபத்துமூன்று, டிக்கெட் பாருங்க",
        24 to "இருபத்துநான்கு மணி நேரமும் ஆட்டம்",
        25 to "இருபத்தைந்து, அதிர்ஷ்டம் கதவைத் தட்டுது",
        26 to "ஜனவரி இருபத்தாறு, குடியரசு தினம்",
        27 to "இருபத்தேழு, இன்னும் ஆட்டம் இருக்கு",
        28 to "இருபத்தெட்டு, ஜெயிக்கப் போறது யாரு?",
        29 to "இருபத்தொன்பது, முப்பது வருது",
        30 to "முப்பது, ஆட்டம் வேற லெவல்",

        31 to "முப்பத்தொன்று, டிக்கெட்டை செக் பண்ணுங்க",
        32 to "முப்பத்திரண்டு பல்லும் தெரிய சிரிங்க",
        33 to "டபுள் மூணு, டபுள் ஜாலி",
        34 to "முப்பத்திநான்கு, ஒரு பவுண்டரி போடலாம்",
        35 to "முப்பத்தைந்து, ஒரு டீ போடலாமா?",
        36 to "முப்பத்தாறு, பகை எங்கேயோ இருக்கு",
        37 to "முப்பத்தேழு, டென்ஷன் தொடருது",
        38 to "முப்பத்தெட்டு, ஆட்டம் சூடாகுது",
        39 to "முப்பத்தொன்பது, நாற்பது கிட்ட வந்தாச்சு",
        40 to "நாற்பது, டீ டைம் ஆச்சா?",

        41 to "நாற்பத்தொன்று, யாருக்கு அதிர்ஷ்டம்?",
        42 to "நாற்பத்திரண்டு, ஆட்டம் கலக்குது",
        43 to "நாற்பத்துமூன்று, டிக்கெட்டை பாருங்க",
        44 to "டபுள் நாலு, டபுள் தூள்",
        45 to "நாற்பத்தைந்து, பஜ்ஜி சாப்பிடலாமா?",
        46 to "நாற்பத்தாறு, சிக்ஸர் அடிக்கலாம்",
        47 to "நாற்பத்தேழு, எல்லாரும் டிக்கெட்டை பாருங்க",
        48 to "நாற்பத்தெட்டு, வெற்றி கிட்ட வருது",
        49 to "நாற்பத்தொன்பது, ஐம்பது வருது",
        50 to "ஐம்பது! அரை சதம், கைதட்டுங்க!",

        51 to "ஐம்பத்தொன்று, ஆட்டம் இன்னும் இருக்கு",
        52 to "ஐம்பத்திரண்டு, யார் ஜெயிக்கப் போறாங்க?",
        53 to "ஐம்பத்துமூன்று, டிக்கெட் செக் பண்ணுங்க",
        54 to "ஐம்பத்துநான்கு, ஒரு காபி வேணுமா?",
        55 to "டபுள் அஞ்சு, டபுள் சந்தோஷம்",
        56 to "ஐம்பத்தாறு, ஆட்டம் சூடு",
        57 to "ஐம்பத்தேழு, அதிர்ஷ்டம் யார் பக்கம்?",
        58 to "ஐம்பத்தெட்டு, டென்ஷன் ஏறுது",
        59 to "ஐம்பத்தொன்பது, அறுபது பக்கத்துல",
        60 to "அறுபது, இப்போதான் உண்மையான ஆட்டம்",

        61 to "அறுபத்தொன்று, வெற்றி கிட்டத்தட்ட",
        62 to "அறுபத்திரண்டு, ஆட்டம் மாறலாம்",
        63 to "அறுபத்துமூன்று, டென்ஷன் அதிகம்",
        64 to "அறுபத்துநான்கு கலைகளில் நீங்க எதில் கில்லாடி?",
        65 to "அறுபத்தைந்து, டிக்கெட்டை மறுபடியும் பாருங்க",
        66 to "டபுள் ஆறு, டபுள் சிக்ஸர்",
        67 to "அறுபத்தேழு, எல்லாருடைய பார்வையும் இங்கே",
        68 to "அறுபத்தெட்டு, வெற்றி வாசனை வருது",
        69 to "அறுபத்தொன்பது, என்ன ஒரு நம்பர்!",
        70 to "எழுபது, கிளைமாக்ஸ் கிட்ட வருது",

        71 to "எழுபத்தொன்று, இன்னும் ஒரு அடி",
        72 to "எழுபத்திரண்டு, ஆட்டம் யார் கையில்?",
        73 to "எழுபத்துமூன்று, டிக்கெட்டை பாருங்க",
        74 to "எழுபத்துநான்கு, ஒரு சிக்ஸர் போடலாம்",
        75 to "எழுபத்தைந்து, முக்கால் சதம்",
        76 to "எழுபத்தாறு, மறுபடியும் சிக்ஸர்",
        77 to "இரண்டு பேட் நிமிர்ந்து நிற்குது, எழுபத்தேழு",
        78 to "எழுபத்தெட்டு, வெற்றி ரொம்ப கிட்ட",
        79 to "எழுபத்தொன்பது, எண்பது வருது",
        80 to "எண்பது, இதயத்தை பிடிச்சுக்கோங்க",

        81 to "எண்பத்தொன்று, வெற்றி ரொம்ப ரொம்ப கிட்ட",
        82 to "எண்பத்திரண்டு, ஆட்டம் மாறலாம்",
        83 to "எண்பத்துமூன்று, டிக்கெட்டை செக் பண்ணுங்க",
        84 to "எண்பத்துநான்கு, அதிர்ஷ்டம் கதவைத் திறக்குது",
        85 to "எண்பத்தைந்து, டென்ஷன் உச்சத்தில்",
        86 to "எண்பத்தாறு, சிக்ஸர் பக்கா",
        87 to "எண்பத்தேழு, எல்லாரும் போர்டை பாருங்க",
        88 to "இரண்டு உருண்டை, எண்பத்தெட்டு",
        89 to "எண்பத்தொன்பது, கடைசி கட்டம்",
        90 to "தொண்ணூறு! கடைசி நம்பர், டிக்கெட்டை பாருங்க!"
    )

    private val marathiFunnyPhrases = mapOf(
        1 to "श्रीगणेशाच्या नावाने शुभारंभ",
        2 to "तू आणि मी, दोघेही भारी",
        3 to "तीन देवांचे दर्शन",
        4 to "चारही बाजूंनी आनंद",
        5 to "पाच पांडवांची ताकद",
        6 to "सिक्सर मारला, सीमापार",
        7 to "सात जन्मांची साथ",
        8 to "आठ दिशांना जयजयकार",
        9 to "नऊवारी साडीचा थाट",
        10 to "दहाचा आकडा, खेळ झकास",

        11 to "एक आणि एक, अकरा",
        12 to "बारा महिने, तेरा सण",
        13 to "तेराला नशीब खुललं",
        14 to "चौदा विद्या, चौसष्ट कला",
        15 to "पंधरा ऑगस्ट, स्वातंत्र्याचा दिवस",
        16 to "सोळा शृंगार, खेळ शानदार",
        17 to "सतरा, नशीबाची लॉटरी",
        18 to "अठरा, आता रंगत आली",
        19 to "एकोणीस, वीस जवळ आलं",
        20 to "वीस, आता खेळ तापला",

        21 to "एकवीस, गणपती बाप्पा मोरया",
        22 to "दोन बदकांचा जोडा, बावीस",
        23 to "तेवीस, तिकीट तपासा",
        24 to "चोवीस तास खेळलो तरी कमीच",
        25 to "पंचवीस, नशीब दारात आलं",
        26 to "छब्बीस जानेवारी, प्रजासत्ताक दिन",
        27 to "सत्तावीस, अजून खेळ बाकी",
        28 to "अठ्ठावीस, जिंकणार कोण?",
        29 to "एकोणतीस, तीस अगदी जवळ",
        30 to "तीस, आता खेळ रंगला",

        31 to "एकतीस, तिकीट नीट बघा",
        32 to "बत्तीस दात दाखवून हसा",
        33 to "डबल तीन, डबल मजा",
        34 to "चौतीस, चौकार मारूया",
        35 to "पस्तीस, चहाची वेळ झाली का?",
        36 to "छत्तीसचा आकडा, जुनी दुश्मनी",
        37 to "सदतीस, नशीबाची परीक्षा",
        38 to "अडतीस, रंगत वाढली",
        39 to "एकोणचाळीस, चाळीस येतोय",
        40 to "चाळीस, चहा आणि गप्पा",

        41 to "एक्केचाळीस, नशीब कोणाचं?",
        42 to "बेचाळीस, आता मजा येतेय",
        43 to "त्रेचाळीस, तिकीट तपासा",
        44 to "डबल चार, डबल धमाल",
        45 to "पंचेचाळीस, वडा-पाव खाऊया",
        46 to "सेहेचाळीस, सिक्सरची तयारी",
        47 to "सत्तेचाळीस, सगळ्यांची नजर बोर्डवर",
        48 to "अठ्ठेचाळीस, विजय जवळ आला",
        49 to "एकोणपन्नास, पन्नास येतोय",
        50 to "पन्नास! धडाकेबाज अर्धशतक",

        51 to "एकावन्न, खेळ अजून बाकी",
        52 to "बावन्न, नशीबाची बाजी",
        53 to "त्रेपन्न, तिकीट बघा भाऊ",
        54 to "चोपन्न, एक कटिंग चहा होऊ दे",
        55 to "डबल पाच, डबल आनंद",
        56 to "छप्पन्न, आता खेळ तापला",
        57 to "सत्तावन्न, नशीब कुणाच्या बाजूने?",
        58 to "अठ्ठावन्न, टेन्शन वाढलं",
        59 to "एकोणसाठ, साठ अगदी जवळ",
        60 to "साठ, आता खरी मजा",

        61 to "एकसष्ट, विजय जवळ आला",
        62 to "बासष्ट, बाजी पलटू शकते",
        63 to "त्रेसष्ट, टेन्शन वाढतंय",
        64 to "चौसष्ट कला, तुम्ही किती जाणता?",
        65 to "पासष्ट, तिकीट पुन्हा तपासा",
        66 to "डबल सहा, डबल सिक्सर",
        67 to "सदुसष्ट, सगळ्यांचे डोळे बोर्डवर",
        68 to "अडुसष्ट, विजयाचा वास येतोय",
        69 to "एकोणसत्तर, काय नंबर आहे!",
        70 to "सत्तर, आता क्लायमॅक्स",

        71 to "एकाहत्तर, अजून एक पाऊल",
        72 to "बहात्तर, बाजी कोणाची?",
        73 to "त्र्याहत्तर, तिकीट तपासा",
        74 to "चौऱ्याहत्तर, चौकार मारूया",
        75 to "पंचाहत्तर, पाऊण शतक",
        76 to "शहात्तर, पुन्हा सिक्सर",
        77 to "दोन हॉकी स्टिक, सत्याहत्तर",
        78 to "अठ्ठ्याहत्तर, विजय जवळ",
        79 to "एकोणऐंशी, ऐंशी येतोय",
        80 to "ऐंशी, आता काळीज हातात",

        81 to "एक्याऐंशी, विजय अगदी जवळ",
        82 to "ब्याऐंशी, बाजी कधीही पलटेल",
        83 to "त्र्याऐंशी, तिकीट तपासा",
        84 to "चौर्‍याऐंशी, नशीब खुलतंय",
        85 to "पंच्याऐंशी, टेन्शन शिगेला",
        86 to "शहाऐंशी, सिक्सर पक्का",
        87 to "सत्त्याऐंशी, सगळ्यांची नजर बोर्डवर",
        88 to "दोन गोल गोल, अठ्ठ्याऐंशी",
        89 to "एकोणनव्वद, शेवट जवळ आला",
        90 to "नव्वद! शेवटचा नंबर, तिकीट बघा!"
    )

    private val teluguFunnyPhrases = mapOf(
        1 to "శ్రీకారం చుట్టిన మొదటి అడుగు",
        2 to "నువ్వూ నేనూ, ఇద్దరం అదుర్స్",
        3 to "ముగ్గురు మూర్తులు, ముచ్చటైన నంబరు",
        4 to "నాలుగు దిక్కులా సందడి",
        5 to "పంచ పాండవుల బలం",
        6 to "సిక్సర్ కొట్టి బౌండరీ దాటింది",
        7 to "ఏడు కొండల వాడి ఆశీర్వాదం",
        8 to "ఎనిమిదిలో ఎనలేని సందడి",
        9 to "తొమ్మిది, అదృష్టం తలుపు తట్టింది",
        10 to "పది, ఆట అదిరింది",

        11 to "ఒకటి ఒకటి పదకొండు",
        12 to "పన్నెండు, పులిహోర గుర్తొచ్చింది",
        13 to "పదమూడు, అదృష్టం ఎలా ఉందో చూద్దాం",
        14 to "పద్నాలుగు, టికెట్ ఒకసారి చూడండి",
        15 to "ఆగస్టు పదిహేను, స్వాతంత్ర్య సంబరం",
        16 to "పదహారు, పదహారేళ్ల వయసు గుర్తొచ్చిందా?",
        17 to "పదిహేడు, ఆట ఇంకా ఉంది",
        18 to "పద్దెనిమిది, జోష్ మొదలైంది",
        19 to "పందొమ్మిది, ఇరవై వచ్చేస్తోంది",
        20 to "ఇరవై, ఆట వేడెక్కుతోంది",

        21 to "ఇరవై ఒకటి, గెలుపు వైపు మొదటి అడుగు",
        22 to "రెండు బాతులు, ఇరవై రెండు",
        23 to "ఇరవై మూడు, టికెట్ చూడండి బాబూ",
        24 to "ఇరవై నాలుగు గంటలు ఆడినా సరదా తీరదు",
        25 to "ఇరవై ఐదు, అదృష్టం తలుపు దగ్గరే ఉంది",
        26 to "జనవరి ఇరవై ఆరు, గణతంత్ర దినోత్సవం",
        27 to "ఇరవై ఏడు, ఇంకా ఆట మిగిలే ఉంది",
        28 to "ఇరవై ఎనిమిది, గెలుపు ఎవరిది?",
        29 to "ఇరవై తొమ్మిది, ముప్పైకి ముందు టెన్షన్",
        30 to "ముప్పై, ఆట మస్తుగా ఉంది",

        31 to "ముప్పై ఒకటి, టికెట్ చెక్ చేసుకోండి",
        32 to "ముప్పై రెండు పళ్ళు చూపించి నవ్వండి",
        33 to "డబుల్ మూడు, డబుల్ మజా",
        34 to "ముప్పై నాలుగు, ఒక ఫోర్ కొడదాం",
        35 to "ముప్పై ఐదు, టీ టైమ్ అయ్యిందా?",
        36 to "ముప్పై ఆరు, ఇప్పుడు అసలు ఆట",
        37 to "ముప్పై ఏడు, టెన్షన్ పెరుగుతోంది",
        38 to "ముప్పై ఎనిమిది, ఆట రసవత్తరంగా ఉంది",
        39 to "ముప్పై తొమ్మిది, నలభై వచ్చేస్తోంది",
        40 to "నలభై, టీ తాగుతూ టికెట్ చూడండి",

        41 to "నలభై ఒకటి, అదృష్టం ఎవరి వైపు?",
        42 to "నలభై రెండు, ఆట వేడెక్కింది",
        43 to "నలభై మూడు, టికెట్ జాగ్రత్త",
        44 to "డబుల్ నాలుగు, డబుల్ ధమాకా",
        45 to "నలభై ఐదు, బజ్జీ తింటారా?",
        46 to "నలభై ఆరు, మరో సిక్సర్ కొడదాం",
        47 to "నలభై ఏడు, అందరి చూపు బోర్డు మీదే",
        48 to "నలభై ఎనిమిది, గెలుపు దగ్గర్లోనే",
        49 to "నలభై తొమ్మిది, యాభైకి రెడీ",
        50 to "యాభై! హాఫ్ సెంచరీ సంబరం",

        51 to "యాభై ఒకటి, ఆట ఇంకా జోరుగా ఉంది",
        52 to "యాభై రెండు, బాజి ఎవరిది?",
        53 to "యాభై మూడు, టికెట్ ఒకసారి చూడండి",
        54 to "యాభై నాలుగు, ఒక కప్పు టీ కావాలా?",
        55 to "డబుల్ ఐదు, డబుల్ ఆనందం",
        56 to "యాభై ఆరు, ఆట మరింత వేడెక్కింది",
        57 to "యాభై ఏడు, అదృష్టం ఎవరిది?",
        58 to "యాభై ఎనిమిది, టెన్షన్ మొదలైంది",
        59 to "యాభై తొమ్మిది, అరవై వచ్చేస్తోంది",
        60 to "అరవై, ఇప్పుడు అసలు మజా",

        61 to "అరవై ఒకటి, గెలుపు దగ్గరలోనే",
        62 to "అరవై రెండు, ఆట ఎప్పుడైనా మారొచ్చు",
        63 to "అరవై మూడు, టెన్షన్ పెరుగుతోంది",
        64 to "అరవై నాలుగు కళల్లో మీకు ఏది వచ్చు?",
        65 to "అరవై ఐదు, టికెట్ మళ్లీ చూడండి",
        66 to "డబుల్ ఆరు, డబుల్ సిక్సర్",
        67 to "అరవై ఏడు, అందరి గుండెల్లో టెన్షన్",
        68 to "అరవై ఎనిమిది, గెలుపు వాసన వస్తోంది",
        69 to "అరవై తొమ్మిది, ఏం నంబర్ గురూ!",
        70 to "డెబ్బై, క్లైమాక్స్ మొదలైంది",

        71 to "డెబ్బై ఒకటి, మరో అడుగు ముందుకు",
        72 to "డెబ్బై రెండు, బాజి ఎవరిది?",
        73 to "డెబ్బై మూడు, టికెట్ చెక్ చేసుకోండి",
        74 to "డెబ్బై నాలుగు, ఫోర్ కొట్టేద్దాం",
        75 to "డెబ్బై ఐదు, ముప్పావు సెంచరీ సంబరం",
        76 to "డెబ్బై ఆరు, మళ్లీ సిక్సర్",
        77 to "రెండు హాకీ స్టిక్స్, డెబ్బై ఏడు",
        78 to "డెబ్బై ఎనిమిది, గెలుపు చాలా దగ్గర",
        79 to "డెబ్బై తొమ్మిది, ఎనభై వచ్చేస్తోంది",
        80 to "ఎనభై, గుండె పట్టుకోండి",

        81 to "ఎనభై ఒకటి, గెలుపు చాలా చాలా దగ్గర",
        82 to "ఎనభై రెండు, ఆట ఎప్పుడైనా మారొచ్చు",
        83 to "ఎనభై మూడు, టికెట్ చూడండి",
        84 to "ఎనభై నాలుగు, అదృష్టం తలుపు తెరిచింది",
        85 to "ఎనభై ఐదు, టెన్షన్ పీక్స్‌లో ఉంది",
        86 to "ఎనభై ఆరు, సిక్సర్ పక్కా",
        87 to "ఎనభై ఏడు, అందరి చూపు బోర్డు మీదే",
        88 to "రెండు గుండ్రాలు, ఎనభై ఎనిమిది",
        89 to "ఎనభై తొమ్మిది, చివరి మజిలీ",
        90 to "తొంభై! చివరి నంబర్, టికెట్ చూడండి!"
    )

    private val gujaratiFunnyPhrases = mapOf(
        1 to "શુભ શરૂઆત, ગણપતિ બાપ્પા",
        2 to "બે જણા, મજા બમણી",
        3 to "ત્રણ તિગાડા, કામ બગાડા",
        4 to "ચાર ધામની યાત્રા",
        5 to "પાંચ પાંડવની તાકાત",
        6 to "છગ્ગો માર્યો, બાઉન્ડ્રી પાર",
        7 to "સાત જન્મનો સાથ",
        8 to "આઠમના ગરબા યાદ આવ્યા",
        9 to "નવ દિવસ, નવ રાત ગરબા",
        10 to "દસનો આંકડો, ખેલ જબરદસ્ત",

        11 to "એક ને એક અગિયાર",
        12 to "બાર મહિના, તેર તહેવાર",
        13 to "તેર, નસીબનો ફેર",
        14 to "ચૌદ, ફાફડા-જલેબી યાદ આવ્યા",
        15 to "પંદરમી ઓગસ્ટ, આઝાદીનો દિવસ",
        16 to "સોળ કળાએ સંપન્ન",
        18 to "અઢાર, હવે રંગ જામ્યો",
        21 to "એકવીસ, ગણપતિ બાપ્પા મોરિયા",
        22 to "બે બતક, બાવીસ",
        25 to "પચ્ચીસ, હવે નાસ્તો જોઈએ",
        26 to "છવ્વીસ જાન્યુઆરી, પ્રજાસત્તાક દિવસ",
        32 to "બત્રીસ દાંત બતાવીને હસો",
        33 to "ડબલ ત્રણ, ડબલ મજા",
        36 to "છત્રીસનો આંકડો, જૂની દુશ્મની",
        40 to "ચાલીસ, ચા-નાસ્તાનો સમય",
        44 to "ડબલ ચાર, ડબલ ધમાલ",
        45 to "પિસ્તાલીસ, ફાફડા ખાવા છે?",
        50 to "પચાસ! અડધી સદી, તાળીઓ પાડો",
        55 to "ડબલ પાંચ, ડબલ ખુશી",
        60 to "સાઠ, હવે ખેલ ગરમ",
        64 to "ચોસઠ કળામાં કોણ નિષ્ણાત?",
        66 to "ડબલ છ, ડબલ છગ્ગો",
        69 to "અણસિત્તેર, વાહ શું નંબર!",
        75 to "પંચોતેર, પોણી સદી",
        77 to "બે હોકી સ્ટિક, સિત્યોતેર",
        88 to "બે ગોળ ગોળ, અઠ્ઠ્યાસી",
        90 to "નેવું! છેલ્લો નંબર, ટિકિટ જોઈ લો"
    )

    private val malayalamFunnyPhrases = mapOf(
        1 to "ശുഭാരംഭം, ഗണപതി ബാപ്പാ",
        2 to "നീയും ഞാനും, രണ്ടു പേർ",
        3 to "മൂന്ന് മൂർത്തികളുടെ അനുഗ്രഹം",
        4 to "നാല് ദിക്കിലും സന്തോഷം",
        5 to "പഞ്ച പാണ്ഡവർ",
        6 to "സിക്സർ, അതിർത്തി കടന്നു",
        7 to "ഏഴ് മലകളുടെ നാഥൻ",
        8 to "എട്ട്, ഓണം ഓർമ്മയായി",
        9 to "ഒമ്പത്, ഭാഗ്യം വരുന്നു",
        10 to "പത്ത്, കളി പൊളിച്ചു",

        11 to "ഒന്നും ഒന്നും പതിനൊന്ന്",
        12 to "പന്ത്രണ്ട്, സദ്യ ഓർമ്മയായി",
        13 to "പതിമൂന്ന്, ഭാഗ്യം എങ്ങനെയെന്ന് നോക്കാം",
        14 to "പതിനാല്, ടിക്കറ്റ് നോക്കണേ",
        15 to "ഓഗസ്റ്റ് പതിനഞ്ച്, സ്വാതന്ത്ര്യ ദിനം",
        16 to "പതിനാറ്, കളി കിടിലം",
        18 to "പതിനെട്ട്, ഇനി കളി ചൂടാകും",
        21 to "ഇരുപത്തൊന്ന്, വിജയം അടുത്ത്",
        22 to "രണ്ട് താറാവുകൾ, ഇരുപത്തിരണ്ട്",
        25 to "ഇരുപത്തിയഞ്ച്, സദ്യ വേണോ?",
        26 to "ജനുവരി ഇരുപത്തിയാറ്, റിപ്പബ്ലിക് ദിനം",
        32 to "മുപ്പത്തിരണ്ട് പല്ലും കാണിച്ച് ചിരിക്കൂ",
        33 to "ഡബിൾ മൂന്ന്, ഡബിൾ മസാ",
        36 to "മുപ്പത്താറ്, ഇനി കളി വേറെ ലെവൽ",
        40 to "നാൽപ്പത്, ഒരു ചായ ആയിക്കോട്ടെ",
        44 to "ഡബിൾ നാല്, ഡബിൾ ധമാക്ക",
        45 to "നാൽപ്പത്തിയഞ്ച്, പഴംപൊരി വേണോ?",
        50 to "അമ്പത്! ഹാഫ് സെഞ്ചുറി, കയ്യടി വേണം",
        55 to "ഡബിൾ അഞ്ച്, ഡബിൾ സന്തോഷം",
        60 to "അറുപത്, ഇനി ശരിക്കും കളി",
        64 to "അറുപത്തിനാല് കലകളിൽ നിങ്ങൾ എത്ര അറിയാം?",
        66 to "ഡബിൾ ആറ്, ഡബിൾ സിക്സർ",
        69 to "അറുപത്തൊമ്പത്, എന്തൊരു നമ്പർ!",
        75 to "എഴുപത്തിയഞ്ച്, മുക്കാൽ സെഞ്ചുറി",
        77 to "രണ്ട് ഹോക്കി സ്റ്റിക്കുകൾ, എഴുപത്തേഴു",
        88 to "രണ്ട് ഉരുണ്ട വട്ടങ്ങൾ, എൺപത്തിയെട്ട്",
        90 to "തൊണ്ണൂറ്! അവസാന നമ്പർ, ടിക്കറ്റ് നോക്കൂ"
    )

    private val punjabiFunnyPhrases = mapOf(
        1 to "ਵਾਹਿਗੁਰੂ ਜੀ, ਸ਼ੁਭ ਸ਼ੁਰੂਆਤ",
        2 to "ਤੂੰ ਤੇ ਮੈਂ, ਦੋਵੇਂ ਕਮਾਲ",
        3 to "ਤਿੰਨ ਤਿਗਾੜਾ, ਕੰਮ ਵਿਗਾੜਾ",
        4 to "ਚਾਰੇ ਪਾਸੇ ਭੰਗੜਾ ਪਾਓ",
        5 to "ਪੰਜ ਪਾਂਡਵਾਂ ਦੀ ਤਾਕਤ",
        6 to "ਛੱਕਾ ਮਾਰਿਆ, ਬਾਊਂਡਰੀ ਪਾਰ",
        7 to "ਸੱਤ ਜਨਮਾਂ ਦਾ ਸਾਥ",
        8 to "ਅੱਠ, ਹੁਣ ਤਾਂ ਮੌਜ ਆ ਗਈ",
        9 to "ਨੌਂ, ਕਿਸਮਤ ਖੁੱਲ੍ਹਣ ਵਾਲੀ",
        10 to "ਦੱਸ, ਖੇਡ ਪੂਰੀ ਝਕਾਸ",

        11 to "ਇੱਕ ਤੇ ਇੱਕ ਗਿਆਰਾਂ",
        12 to "ਬਾਰਾਂ ਮਹੀਨੇ, ਤੇਰਾਂ ਤਿਉਹਾਰ",
        13 to "ਤੇਰਾਂ, ਕਿਸਮਤ ਦਾ ਫੇਰ",
        14 to "ਚੌਦਾਂ, ਪਰਾਂਠੇ ਯਾਦ ਆ ਗਏ",
        15 to "ਪੰਦਰਾਂ ਅਗਸਤ, ਆਜ਼ਾਦੀ ਦਾ ਦਿਨ",
        16 to "ਸੋਲਾਂ ਕਲਾਂ ਵਿੱਚ ਮਾਹਿਰ",
        18 to "ਅਠਾਰਾਂ, ਹੁਣ ਖੇਡ ਜੰਮੀ",
        21 to "ਇੱਕੀ, ਜਿੱਤ ਵੱਲ ਪਹਿਲਾ ਕਦਮ",
        22 to "ਦੋ ਬੱਤਖਾਂ, ਬਾਈ",
        25 to "ਪੱਚੀ, ਹੁਣ ਲੱਸੀ ਪਿਲਾਓ",
        26 to "ਛੱਬੀ ਜਨਵਰੀ, ਗਣਤੰਤਰ ਦਿਵਸ",
        32 to "ਬੱਤੀ ਦੰਦ ਕੱਢ ਕੇ ਹੱਸੋ",
        33 to "ਡਬਲ ਤਿੰਨ, ਡਬਲ ਮੌਜ",
        36 to "ਛੱਤੀ ਦਾ ਅੰਕੜਾ, ਪੁਰਾਣੀ ਦੁਸ਼ਮਣੀ",
        40 to "ਚਾਲੀ, ਚਾਹ ਤੇ ਗੱਲਾਂ ਦਾ ਟਾਈਮ",
        44 to "ਡਬਲ ਚਾਰ, ਡਬਲ ਧਮਾਲ",
        45 to "ਪੈਂਤਾਲੀ, ਪਰੌਂਠੇ ਖਾਣੇ ਆ?",
        50 to "ਪੰਜਾਹ! ਅੱਧਾ ਸੈਂਕੜਾ, ਤਾੜੀਆਂ ਹੋ ਜਾਣ",
        55 to "ਡਬਲ ਪੰਜ, ਡਬਲ ਖੁਸ਼ੀ",
        60 to "ਸੱਠ, ਹੁਣ ਖੇਡ ਗਰਮ ਹੈ",
        64 to "ਚੌਂਹਠ ਕਲਾਵਾਂ ਵਿੱਚ ਕੌਣ ਮਾਹਿਰ?",
        66 to "ਡਬਲ ਛੇ, ਡਬਲ ਛੱਕਾ",
        69 to "ਉਣਹੱਤਰ, ਵਾਹ ਕੀ ਨੰਬਰ ਆ",
        75 to "ਪਚੱਤਰ, ਪੌਣਾ ਸੈਂਕੜਾ",
        77 to "ਦੋ ਹਾਕੀ ਸਟਿੱਕਾਂ, ਸਤੱਤਰ",
        88 to "ਦੋ ਗੋਲ ਗੋਲ, ਅਠਾਸੀ",
        90 to "ਨੱਬੇ! ਆਖਰੀ ਨੰਬਰ, ਟਿਕਟ ਦੇਖ ਲਓ"
    )

    private val assameseFunnyPhrases = mapOf(
        1 to "জয় আই অসম, শুভ আৰম্ভণি",
        2 to "তুমি আৰু মই, দুজন",
        3 to "তিনি দেৱতাৰ আশীৰ্বাদ",
        4 to "চাৰিওফালে আনন্দ",
        5 to "পাঁচ পাণ্ডৱৰ শক্তি",
        6 to "ছয়ৰ ছিক্সাৰ, বাউণ্ডেৰী পাৰ",
        7 to "সাত সাগৰ পাৰ হৈ আহিল",
        8 to "আঠ, এতিয়া খেল জমিল",
        9 to "ন, ভাগ্যৰ দুৱাৰ খুলিছে",
        10 to "দহ, খেল একেবাৰে জমজমাট",

        11 to "এক আৰু এক এঘাৰ",
        12 to "বাৰ মাহত তেৰটা উৎসৱ",
        13 to "তেৰ, ভাগ্যৰ কি খবৰ?",
        14 to "চৈধ্য, পিঠা খোৱাৰ মন গ'ল",
        15 to "পোন্ধৰ আগষ্ট, স্বাধীনতা দিৱস",
        16 to "ষোল্ল, খেল এতিয়া গৰম",
        18 to "ওঠৰ, এতিয়া মজা আৰম্ভ",
        21 to "একৈশ, জয়ৰ প্ৰথম খোজ",
        22 to "দুটা হাঁহ, বাইশ",
        25 to "পঁচিশ, এতিয়া চাহ লাগে",
        26 to "ছাব্বিশ জানুৱাৰী, গণৰাজ্য দিৱস",
        32 to "বত্ৰিশটা দাঁত দেখুৱাই হাঁহক",
        33 to "ডাবল তিনি, ডাবল মজা",
        36 to "ছয়ত্ৰিশ, খেল এতিয়া জমিছে",
        40 to "চল্লিশ, চাহ আৰু আড্ডাৰ সময়",
        44 to "ডাবল চাৰি, ডাবল ধামাকা",
        45 to "পঞ্চল্লিশ, পিঠা খাব নেকি?",
        50 to "পঞ্চাশ! আধা শতক, হাত চাপৰি দিয়ক",
        55 to "ডাবল পাঁচ, ডাবল আনন্দ",
        60 to "ষাঠি, এতিয়া আচল খেল",
        64 to "চৌষট্টি কলাৰ কথা মনত পৰিল",
        66 to "ডাবল ছয়, ডাবল ছিক্সাৰ",
        69 to "ঊনসত্তৰ, কি সুন্দৰ নম্বৰ!",
        75 to "পঁচাত্তৰ, পোৱা শতকৰ আনন্দ",
        77 to "দুটা লাঠি, সাতসত্তৰ",
        88 to "দুটা গোল গোল, আঠাশী",
        90 to "নব্বৈ! শেষ নম্বৰ, টিকট চাওক"
    )

    private val spanishFunnyPhrases = mapOf(
        1 to "Uno, empieza la fiesta",
        2 to "Dos, tú y yo",
        3 to "Tres, por si las moscas",
        4 to "Cuatro, que empiece el teatro",
        5 to "Cinco, cinco de mayo",
        6 to "Seis, ¡que venga el seis!",
        7 to "Siete, número de la suerte",
        8 to "Ocho, ¡qué derroche!",
        9 to "Nueve, la suerte se mueve",
        10 to "Diez, ¡esto se pone bien!",

        11 to "Uno y uno, once",
        12 to "Una docena, ¡qué buena!",
        13 to "Trece, ¿será de suerte?",
        14 to "Catorce, ¡qué noche!",
        15 to "Quince, ya empieza el baile",
        16 to "Dieciséis, ¡vamos otra vez!",
        17 to "Diecisiete, la suerte promete",
        18 to "Dieciocho, ¡qué gozo!",
        19 to "Diecinueve, la suerte se mueve",
        20 to "Veinte, ¡qué ambiente!",

        21 to "Veintiuno, empieza lo bueno",
        22 to "Dos patitos, veintidós",
        23 to "Veintitrés, ¡a ver qué ves!",
        24 to "Veinticuatro, seguimos el teatro",
        25 to "Veinticinco, ¡qué brinco!",
        26 to "Veintiséis, ¡otra vez!",
        27 to "Veintisiete, la suerte promete",
        28 to "Veintiocho, ¡qué gozo!",
        29 to "Veintinueve, el treinta se mueve",
        30 to "Treinta, ¡la cosa se calienta!",

        31 to "Treinta y uno, seguimos con uno",
        32 to "Treinta y dos, ¡mira el cartón!",
        33 to "Doble tres, doble placer",
        34 to "Treinta y cuatro, ¡qué buen rato!",
        35 to "Treinta y cinco, ¡pego un brinco!",
        36 to "Treinta y seis, ¡qué bien!",
        37 to "Treinta y siete, ¡nadie se duerme!",
        38 to "Treinta y ocho, ¡qué derroche!",
        39 to "Treinta y nueve, el cuarenta se mueve",
        40 to "Cuarenta, ¡la partida se calienta!",

        41 to "Cuarenta y uno, sigue lo bueno",
        42 to "Cuarenta y dos, ¡mira el cartón!",
        43 to "Cuarenta y tres, ¡a ver quién es!",
        44 to "Doble cuatro, doble teatro",
        45 to "Cuarenta y cinco, ¡qué brinco!",
        46 to "Cuarenta y seis, ¡otra vez!",
        47 to "Cuarenta y siete, ¡que nadie se duerme!",
        48 to "Cuarenta y ocho, ¡qué gozo!",
        49 to "Cuarenta y nueve, el cincuenta se mueve",
        50 to "¡Cincuenta! ¡Media centena, palmas!",

        51 to "Cincuenta y uno, sigue lo bueno",
        52 to "Cincuenta y dos, ¡vamos los dos!",
        53 to "Cincuenta y tres, ¡a ver qué ves!",
        54 to "Cincuenta y cuatro, ¡qué buen rato!",
        55 to "Doble cinco, doble brinco",
        56 to "Cincuenta y seis, ¡qué bien!",
        57 to "Cincuenta y siete, la suerte promete",
        58 to "Cincuenta y ocho, ¡qué derroche!",
        59 to "Cincuenta y nueve, el sesenta se mueve",
        60 to "Sesenta, ¡la partida revienta!",

        61 to "Sesenta y uno, ya viene lo bueno",
        62 to "Sesenta y dos, ¡mira el cartón!",
        63 to "Sesenta y tres, ¡a ver qué ves!",
        64 to "Sesenta y cuatro, ¡qué buen rato!",
        65 to "Sesenta y cinco, ¡pego un brinco!",
        66 to "Doble seis, doble suerte",
        67 to "Sesenta y siete, ¡que nadie se duerme!",
        68 to "Sesenta y ocho, ¡qué gozo!",
        69 to "Sesenta y nueve, ¡la suerte se mueve!",
        70 to "Setenta, ¡la cosa está que revienta!",

        71 to "Setenta y uno, sigue lo bueno",
        72 to "Setenta y dos, ¡vamos los dos!",
        73 to "Setenta y tres, ¡a ver qué ves!",
        74 to "Setenta y cuatro, ¡qué buen rato!",
        75 to "¡Setenta y cinco! ¡Tres cuartos de siglo!",
        76 to "Setenta y seis, ¡otra vez!",
        77 to "Dos palos, setenta y siete",
        78 to "Setenta y ocho, ¡qué derroche!",
        79 to "Setenta y nueve, el ochenta se mueve",
        80 to "Ochenta, ¡aguanta el corazón!",

        81 to "Ochenta y uno, ya viene lo bueno",
        82 to "Ochenta y dos, ¡mira el cartón!",
        83 to "Ochenta y tres, ¡a ver qué ves!",
        84 to "Ochenta y cuatro, ¡qué buen rato!",
        85 to "Ochenta y cinco, ¡pego un brinco!",
        86 to "Ochenta y seis, ¡otra vez!",
        87 to "Ochenta y siete, ¡que nadie se duerme!",
        88 to "Dos bolitas, ochenta y ocho",
        89 to "Ochenta y nueve, ¡el final se mueve!",
        90 to "¡Noventa! ¡El último, revisa tu cartón!"
    )

    private val portugueseFunnyPhrases = mapOf(
        1 to "Um, começou a festa!",
        2 to "Dois, você e eu",
        3 to "Três, três é a conta que Deus fez",
        4 to "Quatro, quatro cantos do mundo",
        5 to "Cinco, os cinco dedos da mão",
        6 to "Seis, vem o golaço!",
        7 to "Sete, número da sorte",
        8 to "Oito, a sorte está chegando",
        9 to "Nove, quase dez!",
        10 to "Dez, agora o jogo esquenta!",

        11 to "Um e um, onze!",
        12 to "Doze, uma dúzia!",
        13 to "Treze, dá sorte ou azar?",
        14 to "Quatorze, olha a cartela!",
        15 to "Quinze, a festa continua",
        16 to "Dezesseis, vamos de novo!",
        17 to "Dezessete, a sorte promete",
        18 to "Dezoito, agora ficou quente",
        19 to "Dezenove, vinte está chegando",
        20 to "Vinte, agora começou de verdade",

        21 to "Vinte e um, olho na cartela!",
        22 to "Dois patinhos, vinte e dois",
        23 to "Vinte e três, quem tem?",
        24 to "Vinte e quatro, o dia inteiro!",
        25 to "Vinte e cinco, um quarto de século",
        26 to "Vinte e seis, confere a cartela!",
        27 to "Vinte e sete, quem marcou?",
        28 to "Vinte e oito, a sorte bateu!",
        29 to "Vinte e nove, trinta vem aí",
        30 to "Trinta, agora o jogo ferveu!",

        31 to "Trinta e um, atenção na cartela",
        32 to "Trinta e dois, sorriso de orelha a orelha",
        33 to "Três três, sorte em dobro",
        34 to "Trinta e quatro, vamos em frente",
        35 to "Trinta e cinco, hora do cafezinho",
        36 to "Trinta e seis, agora ficou sério",
        37 to "Trinta e sete, a sorte continua",
        38 to "Trinta e oito, está esquentando",
        39 to "Trinta e nove, quarenta está chegando",
        40 to "Quarenta, cafézinho e cartela",

        41 to "Quarenta e um, quem está com sorte?",
        42 to "Quarenta e dois, a resposta para tudo",
        43 to "Quarenta e três, confere aí!",
        44 to "Quatro quatro, sorte em dobro",
        45 to "Quarenta e cinco, pão de queijo na mesa?",
        46 to "Quarenta e seis, o jogo está pegando fogo",
        47 to "Quarenta e sete, todo mundo de olho",
        48 to "Quarenta e oito, a vitória está perto",
        49 to "Quarenta e nove, cinquenta vem aí",
        50 to "Cinquenta! Meio século, palmas!",

        51 to "Cinquenta e um, continua a festa",
        52 to "Cinquenta e dois, quem leva essa?",
        53 to "Cinquenta e três, confere a cartela",
        54 to "Cinquenta e quatro, mais um cafezinho?",
        55 to "Cinco cinco, sorte em dobro",
        56 to "Cinquenta e seis, o jogo esquenta",
        57 to "Cinquenta e sete, quem está na frente?",
        58 to "Cinquenta e oito, a tensão aumentou",
        59 to "Cinquenta e nove, sessenta chegando",
        60 to "Sessenta, agora é pra valer!",

        61 to "Sessenta e um, a vitória está perto",
        62 to "Sessenta e dois, não perde essa!",
        63 to "Sessenta e três, olho na cartela",
        64 to "Sessenta e quatro, quatro vezes dezesseis",
        65 to "Sessenta e cinco, não dorme!",
        66 to "Seis seis, sorte em dobro",
        67 to "Sessenta e sete, coração acelerado",
        68 to "Sessenta e oito, está chegando!",
        69 to "Sessenta e nove, que número!",
        70 to "Setenta, reta final começou",

        71 to "Setenta e um, mais um passinho",
        72 to "Setenta e dois, quem vai ganhar?",
        73 to "Setenta e três, confere aí!",
        74 to "Setenta e quatro, quase lá",
        75 to "Setenta e cinco, três quartos de século",
        76 to "Setenta e seis, mais um número",
        77 to "Dois tacos, setenta e sete",
        78 to "Setenta e oito, a vitória está perto",
        79 to "Setenta e nove, oitenta chegando",
        80 to "Oitenta, segura o coração!",

        81 to "Oitenta e um, vitória bem perto",
        82 to "Oitenta e dois, não perde!",
        83 to "Oitenta e três, confere a cartela",
        84 to "Oitenta e quatro, a sorte chegou",
        85 to "Oitenta e cinco, quase no fim",
        86 to "Oitenta e seis, vai dar sorte!",
        87 to "Oitenta e sete, todo mundo olhando",
        88 to "Duas bolinhas, oitenta e oito",
        89 to "Oitenta e nove, última curva",
        90 to "Noventa! Último número, confere a cartela!"
    )

    private val frenchFunnyPhrases = mapOf(
        1 to "Un, c'est parti !",
        2 to "Deux, toi et moi",
        3 to "Trois, jamais deux sans trois",
        4 to "Quatre, aux quatre coins du monde",
        5 to "Cinq, les cinq doigts de la main",
        6 to "Six, ça sent le six !",
        7 to "Sept, porte-bonheur",
        8 to "Huit, ça va vite !",
        9 to "Neuf, du neuf dans le jeu",
        10 to "Dix, le jeu devient sérieux",

        11 to "Un et un font onze",
        12 to "Douze, une douzaine bien française",
        13 to "Treize, porte-bonheur ou malheur ?",
        14 to "Quatorze, on garde le rythme",
        15 to "Quinze, le jeu est lancé",
        16 to "Seize, ça se précise",
        17 to "Dix-sept, la chance arrive",
        18 to "Dix-huit, ça devient chaud",
        19 to "Dix-neuf, encore un effort",
        20 to "Vingt, ça commence à chauffer",

        21 to "Vingt et un, comme au blackjack",
        22 to "Deux petits canards, vingt-deux",
        23 to "Vingt-trois, à vos cartons !",
        24 to "Vingt-quatre, toujours en course",
        25 to "Vingt-cinq, le quart du chemin",
        26 to "Vingt-six, la chance se lève",
        27 to "Vingt-sept, ça continue !",
        28 to "Vingt-huit, attention au carton",
        29 to "Vingt-neuf, le trente arrive",
        30 to "Trente, ça devient sérieux",

        31 to "Trente et un, on ne lâche rien",
        32 to "Trente-deux, souriez !",
        33 to "Double trois, double plaisir",
        34 to "Trente-quatre, quel numéro !",
        35 to "Trente-cinq, un petit café ?",
        36 to "Trente-six, ça porte bonheur",
        37 to "Trente-sept, la chance continue",
        38 to "Trente-huit, ça chauffe !",
        39 to "Trente-neuf, presque quarante",
        40 to "Quarante, un petit verre et ça repart",

        41 to "Quarante et un, qui sera l'heureux gagnant ?",
        42 to "Quarante-deux, la réponse à tout",
        43 to "Quarante-trois, gardez l'œil ouvert",
        44 to "Double quatre, double bonheur",
        45 to "Quarante-cinq, une petite baguette ?",
        46 to "Quarante-six, ça sent le jackpot",
        47 to "Quarante-sept, tous aux cartons",
        48 to "Quarante-huit, la victoire approche",
        49 to "Quarante-neuf, bientôt cinquante",
        50 to "Cinquante ! Demi-siècle, applaudissez !",

        51 to "Cinquante et un, on continue",
        52 to "Cinquante-deux, à qui la chance ?",
        53 to "Cinquante-trois, gardez l'œil sur le carton",
        54 to "Cinquante-quatre, un café peut-être ?",
        55 to "Double cinq, double chance",
        56 to "Cinquante-six, ça chauffe encore",
        57 to "Cinquante-sept, la chance tourne",
        58 to "Cinquante-huit, attention !",
        59 to "Cinquante-neuf, soixante arrive",
        60 to "Soixante, maintenant ça devient sérieux",

        61 to "Soixante et un, la victoire approche",
        62 to "Soixante-deux, ne ratez rien",
        63 to "Soixante-trois, à vos cartons !",
        64 to "Soixante-quatre, quatre fois seize",
        65 to "Soixante-cinq, encore un effort",
        66 to "Double six, double chance",
        67 to "Soixante-sept, la tension monte",
        68 to "Soixante-huit, quel numéro !",
        69 to "Soixante-neuf, tout le monde sourit",
        70 to "Soixante-dix, ça sent la finale",

        71 to "Soixante et onze, on approche",
        72 to "Soixante-douze, à qui le tour ?",
        73 to "Soixante-treize, gardez vos yeux ouverts",
        74 to "Soixante-quatorze, encore un pas",
        75 to "Soixante-quinze, trois quarts de siècle",
        76 to "Soixante-seize, encore un numéro",
        77 to "Deux bâtons, soixante-dix-sept",
        78 to "Soixante-dix-huit, la tension monte",
        79 to "Soixante-dix-neuf, bientôt quatre-vingts",
        80 to "Quatre-vingts, tenez bon !",

        81 to "Quatre-vingt-un, la victoire est proche",
        82 to "Quatre-vingt-deux, ça devient chaud",
        83 to "Quatre-vingt-trois, regardez vos cartons",
        84 to "Quatre-vingt-quatre, encore un effort",
        85 to "Quatre-vingt-cinq, presque au bout",
        86 to "Quatre-vingt-six, la chance est là",
        87 to "Quatre-vingt-sept, tout le monde regarde",
        88 to "Deux petites boules, quatre-vingt-huit",
        89 to "Quatre-vingt-neuf, dernier virage",
        90 to "Quatre-vingt-dix ! Le dernier, vérifiez vos cartons !"
    )

    private val germanFunnyPhrases = mapOf(
        1 to "Eins, aller Anfang ist schwer",
        2 to "Zwei, du und ich",
        3 to "Drei, aller guten Dinge sind drei",
        4 to "Vier, alle viere von sich strecken",
        5 to "Fünf, fünf Finger an der Hand",
        6 to "Sechs, jetzt wird's heiß",
        7 to "Sieben, die Glückszahl",
        8 to "Acht, aufgepasst!",
        9 to "Neun, die Spannung steigt",
        10 to "Zehn, jetzt wird's ernst",

        11 to "Eins und eins macht elf",
        12 to "Zwölf, ein Dutzend",
        13 to "Dreizehn, Glück oder Pech?",
        14 to "Vierzehn, weiter geht's",
        15 to "Fünfzehn, das Spiel läuft",
        16 to "Sechzehn, jetzt wird's spannend",
        17 to "Siebzehn, die Glückssträhne geht weiter",
        18 to "Achtzehn, jetzt wird's heiß",
        19 to "Neunzehn, die Zwanzig kommt",
        20 to "Zwanzig, jetzt geht's richtig los",

        21 to "Einundzwanzig, Blackjack lässt grüßen",
        22 to "Zwei kleine Enten, zweiundzwanzig",
        23 to "Dreiundzwanzig, Augen auf!",
        24 to "Vierundzwanzig, rund um die Uhr",
        25 to "Fünfundzwanzig, ein Viertelhundert",
        26 to "Sechsundzwanzig, Deutschland lässt grüßen",
        27 to "Siebenundzwanzig, die Spannung steigt",
        28 to "Achtundzwanzig, wer hat ihn?",
        29 to "Neunundzwanzig, die Dreißig kommt",
        30 to "Dreißig, jetzt wird's ernst",

        31 to "Einunddreißig, Augen auf die Karte",
        32 to "Zweiunddreißig, Zähne zeigen und lächeln",
        33 to "Doppel drei, doppeltes Glück",
        34 to "Vierunddreißig, weiter geht's",
        35 to "Fünfunddreißig, Zeit für Kaffee",
        36 to "Sechsunddreißig, jetzt wird's spannend",
        37 to "Siebenunddreißig, die Jagd geht weiter",
        38 to "Achtunddreißig, alle aufgepasst",
        39 to "Neununddreißig, gleich vierzig",
        40 to "Vierzig, Zeit für eine Brezel",

        41 to "Einundvierzig, wer hat Glück?",
        42 to "Zweiundvierzig, die Antwort auf alles",
        43 to "Dreiundvierzig, Karte kontrollieren",
        44 to "Doppel vier, doppelter Spaß",
        45 to "Fünfundvierzig, Kaffee und Kuchen?",
        46 to "Sechsundvierzig, weiter geht's",
        47 to "Siebenundvierzig, alle Augen auf die Karte",
        48 to "Achtundvierzig, der Gewinn kommt näher",
        49 to "Neunundvierzig, gleich fünfzig",
        50 to "Fünfzig! Ein halbes Jahrhundert, Applaus!",

        51 to "Einundfünfzig, weiter geht die wilde Fahrt",
        52 to "Zweiundfünfzig, wer ist auf der Glücksspur?",
        53 to "Dreiundfünfzig, Karte prüfen!",
        54 to "Vierundfünfzig, noch einen Kaffee?",
        55 to "Doppel fünf, doppeltes Glück",
        56 to "Sechsundfünfzig, die Spannung steigt",
        57 to "Siebenundfünfzig, wer kommt zuerst?",
        58 to "Achtundfünfzig, jetzt wird's spannend",
        59 to "Neunundfünfzig, sechzig ist nah",
        60 to "Sechzig, jetzt wird's richtig ernst",

        61 to "Einundsechzig, der Gewinn ist nah",
        62 to "Zweiundsechzig, Augen auf!",
        63 to "Dreiundsechzig, die Spannung steigt",
        64 to "Vierundsechzig, vier mal sechzehn",
        65 to "Fünfundsechzig, nicht einschlafen!",
        66 to "Doppel sechs, doppelter Volltreffer",
        67 to "Siebenundsechzig, alle schauen auf die Karte",
        68 to "Achtundsechzig, jetzt wird's spannend",
        69 to "Neunundsechzig, was für eine Zahl!",
        70 to "Siebzig, Endspurt beginnt",

        71 to "Einundsiebzig, nur noch ein paar Schritte",
        72 to "Zweiundsiebzig, wer gewinnt?",
        73 to "Dreiundsiebzig, Karte kontrollieren",
        74 to "Vierundsiebzig, fast geschafft",
        75 to "Fünfundsiebzig, drei Viertel Jahrhundert",
        76 to "Sechsundsiebzig, weiter geht's",
        77 to "Zwei Hockeyschläger, siebenundsiebzig",
        78 to "Achtundsiebzig, der Endspurt läuft",
        79 to "Neunundsiebzig, gleich achtzig",
        80 to "Achtzig, jetzt Herz festhalten",

        81 to "Einundachtzig, der Gewinn ist ganz nah",
        82 to "Zweiundachtzig, jetzt bloß nichts verpassen",
        83 to "Dreiundachtzig, Karte checken",
        84 to "Vierundachtzig, die Spannung steigt",
        85 to "Fünfundachtzig, fast am Ziel",
        86 to "Sechsundachtzig, Glück voraus!",
        87 to "Siebenundachtzig, alle Augen auf die Karte",
        88 to "Zwei kleine Kreise, achtundachtzig",
        89 to "Neunundachtzig, die letzte Kurve",
        90 to "Neunzig! Die letzte Zahl, Karten prüfen!"
    )

    private val arabicFunnyPhrases = mapOf(
        1 to "بسم الله، نبدأ اللعبة",
        2 to "أنا وأنت، اثنان",
        3 to "ثلاثة، البركة في البداية",
        4 to "أربعة، من كل الجهات",
        5 to "خمسة، أصابع اليد",
        6 to "ستة، هات الستة!",
        7 to "سبعة، رقم الحظ",
        8 to "ثمانية، الحظ جاي",
        9 to "تسعة، قربنا من العشرة",
        10 to "عشرة، اللعب حمي",

        11 to "واحد وواحد، أحد عشر",
        12 to "اثنا عشر، دزينة كاملة",
        13 to "ثلاثة عشر، يا ترى حظ ولا نحس؟",
        14 to "أربعة عشر، العين على البطاقة",
        15 to "خمسة عشر، واللعبة مستمرة",
        16 to "ستة عشر، الحظ يتحرك",
        17 to "سبعة عشر، شدوا الهمة",
        18 to "ثمانية عشر، الحماس زاد",
        19 to "تسعة عشر، العشرين جاية",
        20 to "عشرين، الآن الجد",

        21 to "واحد وعشرون، الفوز قريب",
        22 to "بطتان، اثنان وعشرون",
        23 to "ثلاثة وعشرون، شوف البطاقة",
        24 to "أربعة وعشرون، يوم وليلة",
        25 to "خمسة وعشرون، ربع قرن",
        26 to "ستة وعشرون، الحظ معنا",
        27 to "سبعة وعشرون، مين عنده؟",
        28 to "ثمانية وعشرون، اللعب حلو",
        29 to "تسعة وعشرون، الثلاثين قربت",
        30 to "ثلاثون، اللعب صار جد",

        33 to "ثلاثة وثلاثون، الحظ مضاعف",
        35 to "خمسة وثلاثون، وقت القهوة",
        36 to "ستة وثلاثون، اللعب سخن",
        40 to "أربعون، قهوة وحلاوة",
        44 to "أربعة وأربعون، فرحة مضاعفة",
        45 to "خمسة وأربعون، مين يجيب الشاي؟",
        50 to "خمسون! نصف قرن، صفقوا!",
        55 to "خمسة وخمسة، فرحتان",
        60 to "ستون، الآن اللعب الحقيقي",
        64 to "أربعة وستون، أربع مرات ستة عشر",
        66 to "ستة وستون، حظ مضاعف",
        69 to "تسعة وستون، يا له من رقم!",
        70 to "سبعون، بدأ السباق الأخير",
        75 to "خمسة وسبعون، ثلاثة أرباع قرن",
        77 to "عصوان، سبعة وسبعون",
        80 to "ثمانون، امسكوا قلوبكم!",
        88 to "دائرتان صغيرتان، ثمانية وثمانون",
        89 to "تسعة وثمانون، الرقم قبل الأخير",
        90 to "تسعون! آخر رقم، راجعوا البطاقات!"
    )

    private val indonesianFunnyPhrases = mapOf(
        1 to "Satu, awal yang baik",
        2 to "Dua, kamu dan aku",
        3 to "Tiga, hoki mulai datang",
        4 to "Empat, ke segala arah",
        5 to "Lima, lima jari satu tangan",
        6 to "Enam, siap untuk menang!",
        7 to "Tujuh, angka keberuntungan",
        8 to "Delapan, rezeki berdatangan",
        9 to "Sembilan, sebentar lagi sepuluh",
        10 to "Sepuluh, permainan makin seru",

        11 to "Satu dan satu, sebelas",
        12 to "Dua belas, satu lusin",
        13 to "Tiga belas, hoki atau sial?",
        14 to "Empat belas, cek kartunya",
        15 to "Lima belas, permainan lanjut",
        16 to "Enam belas, hoki mendekat",
        17 to "Tujuh belas, makin seru",
        18 to "Delapan belas, semangat!",
        19 to "Sembilan belas, dua puluh segera",
        20 to "Dua puluh, sekarang serius",

        21 to "Dua puluh satu, menang makin dekat",
        22 to "Dua bebek, dua puluh dua",
        23 to "Dua puluh tiga, cek kartumu",
        24 to "Dua puluh empat, siang malam bermain",
        25 to "Dua puluh lima, seperempat abad",
        26 to "Dua puluh enam, hoki datang",
        27 to "Dua puluh tujuh, siapa punya?",
        28 to "Dua puluh delapan, rezeki datang",
        29 to "Dua puluh sembilan, tiga puluh segera",
        30 to "Tiga puluh, permainan makin panas",

        33 to "Tiga tiga, hoki berlipat",
        35 to "Tiga puluh lima, waktunya ngopi",
        36 to "Tiga puluh enam, permainan panas",
        40 to "Empat puluh, kopi dulu?",
        44 to "Empat empat, hoki berlipat",
        45 to "Empat puluh lima, gorengan dulu?",
        50 to "Lima puluh! Setengah abad, tepuk tangan!",
        55 to "Lima lima, bahagia berlipat",
        60 to "Enam puluh, sekarang makin serius",
        64 to "Enam puluh empat, empat kali enam belas",
        66 to "Enam enam, hoki berlipat",
        69 to "Enam puluh sembilan, angka spesial",
        70 to "Tujuh puluh, masuk babak akhir",
        75 to "Tujuh puluh lima, tiga perempat abad",
        77 to "Dua stik, tujuh puluh tujuh",
        80 to "Delapan puluh, tahan jantung!",
        88 to "Dua bulatan, delapan puluh delapan",
        89 to "Delapan puluh sembilan, hampir selesai",
        90 to "Sembilan puluh! Nomor terakhir, cek kartumu!"
    )

    private val turkishFunnyPhrases = mapOf(
        1 to "Bir, hayırlı başlangıç",
        2 to "İki, sen ve ben",
        3 to "Üç, üç kere maşallah",
        4 to "Dört, dört bir yanımız neşe",
        5 to "Beş, beş parmak bir elde",
        6 to "Altı, altıdan gol geldi!",
        7 to "Yedi, uğurlu sayı",
        8 to "Sekiz, şans kapıda",
        9 to "Dokuz, on geliyor",
        10 to "On, oyun kızışıyor",

        11 to "Bir ve bir, on bir",
        12 to "On iki, bir düzine",
        13 to "On üç, uğur mu uğursuzluk mu?",
        14 to "On dört, kartına bak",
        15 to "On beş, oyun devam",
        16 to "On altı, şans dönüyor",
        17 to "On yedi, heyecan artıyor",
        18 to "On sekiz, oyun ısınıyor",
        19 to "On dokuz, yirmi geliyor",
        20 to "Yirmi, şimdi işler ciddileşti",

        21 to "Yirmi bir, şans kapıyı çalıyor",
        22 to "İki küçük ördek, yirmi iki",
        23 to "Yirmi üç, kartını kontrol et",
        24 to "Yirmi dört, gece gündüz oyun",
        25 to "Yirmi beş, çeyrek asır",
        26 to "Yirmi altı, şans senden yana",
        27 to "Yirmi yedi, kimde bu sayı?",
        28 to "Yirmi sekiz, şans geliyor",
        29 to "Yirmi dokuz, otuz kapıda",
        30 to "Otuz, oyun iyice kızıştı",

        33 to "Çifte üç, çifte şans",
        35 to "Otuz beş, bir çay molası?",
        36 to "Otuz altı, oyun ısınıyor",
        40 to "Kırk, çay ve sohbet zamanı",
        44 to "Çifte dört, çifte neşe",
        45 to "Kırk beş, çay yanında simit?",
        50 to "Elli! Yarım asır, alkışlar!",
        55 to "Çifte beş, çifte mutluluk",
        60 to "Altmış, şimdi gerçek oyun",
        64 to "Altmış dört, dört kere on altı",
        66 to "Çifte altı, çifte şans",
        69 to "Altmış dokuz, ne sayı ama!",
        70 to "Yetmiş, final başladı",
        75 to "Yetmiş beş, dörtte üç asır",
        77 to "İki çubuk, yetmiş yedi",
        80 to "Seksen, kalbinizi tutun!",
        88 to "İki yuvarlak, seksen sekiz",
        89 to "Seksen dokuz, son viraj",
        90 to "Doksan! Son sayı, kartları kontrol edin!"
    )

    private val italianFunnyPhrases = mapOf(
        1 to "Uno, si parte!",
        2 to "Due, io e te",
        3 to "Tre, tre volte fortuna",
        4 to "Quattro, quattro angoli del mondo",
        5 to "Cinque, cinque dita della mano",
        6 to "Sei, arriva il sei!",
        7 to "Sette, numero fortunato",
        8 to "Otto, la fortuna è dietro l'angolo",
        9 to "Nove, quasi dieci!",
        10 to "Dieci, adesso si fa sul serio",

        11 to "Uno e uno fanno undici",
        12 to "Dodici, una dozzina",
        13 to "Tredici, porta fortuna o sfortuna?",
        14 to "Quattordici, occhi sulla cartella",
        15 to "Quindici, si continua!",
        16 to "Sedici, la fortuna gira",
        17 to "Diciassette, avanti tutta",
        18 to "Diciotto, che emozione!",
        19 to "Diciannove, venti si avvicina",
        20 to "Venti, ora si fa sul serio",

        21 to "Ventuno, la vittoria si avvicina",
        22 to "Due paperini, ventidue",
        23 to "Ventitré, chi ce l'ha?",
        24 to "Ventiquattro, giorno e notte",
        25 to "Venticinque, un quarto di secolo",
        26 to "Ventisei, controlla la cartella",
        27 to "Ventisette, la fortuna sorride",
        28 to "Ventotto, siamo in gioco",
        29 to "Ventinove, trenta sta arrivando",
        30 to "Trenta, il gioco si scalda",

        33 to "Doppio tre, doppia fortuna",
        35 to "Trentacinque, un caffè?",
        36 to "Trentasei, ora si fa interessante",
        40 to "Quaranta, caffè e biscotti",
        44 to "Doppio quattro, doppia festa",
        45 to "Quarantacinque, pizza time!",
        50 to "Cinquanta! Mezzo secolo, applausi!",
        55 to "Doppio cinque, doppia gioia",
        60 to "Sessanta, adesso si gioca davvero",
        64 to "Sessantaquattro, quattro volte sedici",
        66 to "Doppio sei, doppia fortuna",
        69 to "Sessantanove, che numero!",
        70 to "Settanta, parte il finale",
        75 to "Settantacinque, tre quarti di secolo",
        77 to "Due bastoncini, settantasette",
        80 to "Ottanta, tenetevi forte!",
        88 to "Due palline, ottantotto",
        89 to "Ottantanove, ultima curva",
        90 to "Novanta! Ultimo numero, controllate le cartelle!"
    )

    private val japaneseFunnyPhrases = mapOf(
        1 to "一、ゲームの始まり！",
        2 to "二、あなたと私",
        3 to "三、三拍子そろった！",
        4 to "四、四方八方から幸運",
        5 to "五、五本の指",
        6 to "六、ろくでもない？いや、ラッキー！",
        7 to "七、ラッキーセブン",
        8 to "八、末広がりで縁起がいい",
        9 to "九、あと一つで十",
        10 to "十、ここから本番！",

        11 to "一足す一、十一",
        12 to "十二、一ダース",
        13 to "十三、運を試そう",
        14 to "十四、カードをチェック",
        15 to "十五、まだまだこれから",
        16 to "十六、ゲームは続く",
        17 to "十七、運が近づいてきた",
        18 to "十八、盛り上がってきた！",
        19 to "十九、二十が近い",
        20 to "二十、ここから真剣勝負",

        21 to "二十一、勝利が近い",
        22 to "二羽のアヒル、二十二",
        23 to "二十三、カードを見て！",
        24 to "二十四、丸一日遊べるね",
        25 to "二十五、四分の一世紀",
        26 to "二十六、運が来た！",
        27 to "二十七、誰が持ってる？",
        28 to "二十八、当たるかな？",
        29 to "二十九、三十が来るぞ",
        30 to "三十、ゲームが熱くなってきた",

        33 to "ゾロ目の三十三、ダブルラッキー",
        35 to "三十五、お茶でもどうぞ",
        36 to "三十六、まだまだ勝負",
        40 to "四十、お茶の時間？",
        44 to "ゾロ目の四十四、幸運も二倍",
        45 to "四十五、そろそろ一休み？",
        50 to "五十！半世紀、拍手！",
        55 to "ゾロ目の五十五、ラッキー倍増",
        60 to "六十、ここからが本番",
        64 to "六十四、十六の四倍",
        66 to "ゾロ目の六十六、ダブルラッキー",
        69 to "六十九、なんという数字！",
        70 to "七十、ラストスパート！",
        75 to "七十五、四分の三世紀",
        77 to "二本の棒、七十七",
        80 to "八十、心臓に気をつけて！",
        88 to "二つの丸、八十八",
        89 to "八十九、最後のカーブ",
        90 to "九十！最後の数字、カードを確認！"
    )

    private val koreanFunnyPhrases = mapOf(
        1 to "하나, 게임 시작!",
        2 to "둘, 너와 나",
        3 to "셋, 행운이 시작된다",
        4 to "넷, 사방으로 행운이",
        5 to "다섯, 손가락 다섯 개",
        6 to "여섯, 식스가 나왔다!",
        7 to "일곱, 럭키 세븐",
        8 to "여덟, 행운이 온다",
        9 to "아홉, 열이 코앞",
        10 to "열, 이제 진짜 시작!",

        11 to "하나 더하기 하나, 열하나",
        12 to "열둘, 한 다스",
        13 to "열셋, 행운이냐 불운이냐?",
        14 to "열넷, 카드 확인하세요",
        15 to "열다섯, 아직 멀었어요",
        16 to "열여섯, 게임은 계속됩니다",
        17 to "열일곱, 행운이 다가온다",
        18 to "열여덟, 점점 뜨거워진다",
        19 to "열아홉, 스물이 온다",
        20 to "스물, 이제 진지하게",

        21 to "스물하나, 승리가 가까워요",
        22 to "오리 두 마리, 스물둘",
        23 to "스물셋, 카드 확인!",
        24 to "스물넷, 하루 종일 게임",
        25 to "스물다섯, 사분의 일 세기",
        26 to "스물여섯, 행운이 왔다",
        27 to "스물일곱, 누가 가지고 있나요?",
        28 to "스물여덟, 당첨될까?",
        29 to "스물아홉, 서른이 온다",
        30 to "서른, 게임이 뜨거워진다",

        33 to "더블 셋, 행운도 두 배",
        35 to "서른다섯, 커피 한 잔?",
        36 to "서른여섯, 이제 진짜 게임",
        40 to "마흔, 커피 타임?",
        44 to "더블 넷, 더블 행운",
        45 to "마흔다섯, 간식 타임?",
        50 to "오십! 반세기, 박수!",
        55 to "더블 다섯, 기쁨도 두 배",
        60 to "예순, 이제부터 진짜 승부",
        64 to "예순넷, 열여섯의 네 배",
        66 to "더블 여섯, 더블 행운",
        69 to "예순아홉, 특별한 숫자!",
        70 to "일흔, 마지막 질주 시작",
        75 to "일흔다섯, 사분의 삼 세기",
        77 to "두 개의 막대기, 일흔일곱",
        80 to "여든, 심장 꽉 잡으세요!",
        88 to "동그라미 두 개, 여든여덟",
        89 to "여든아홉, 마지막 코너",
        90 to "아흔! 마지막 숫자, 카드 확인!"
    )

    private val chineseFunnyPhrases = mapOf(
        1 to "一鸣惊人，开个好头",
        2 to "你和我，成双成对",
        3 to "三生有幸",
        4 to "四季平安",
        5 to "五福临门",
        6 to "六六大顺",
        7 to "七上八下，先来个七",
        8 to "八方来财",
        9 to "长长久久",
        10 to "十全十美",

        11 to "一心一意，十一来了",
        12 to "十二，一打满满的",
        13 to "十三，运气来了吗？",
        14 to "十四，看看你的票",
        15 to "十五，十五的月亮",
        16 to "十六，好运继续",
        17 to "十七，别眨眼",
        18 to "十八，财运发发",
        19 to "十九，离二十不远了",
        20 to "二十，好戏开始",

        21 to "二十一，好运向前",
        22 to "两只小鸭子，二十二",
        23 to "二十三，糖瓜粘",
        24 to "二十四，扫房子",
        25 to "二十五，磨豆腐",
        26 to "二十六，年味越来越浓",
        27 to "二十七，洗疚疾",
        28 to "二十八，把面发",
        29 to "二十九，蒸馒头",
        30 to "三十，除夕夜",

        31 to "三十一，票子看仔细",
        32 to "三十二，笑一个",
        33 to "三三得九，好运九九",
        34 to "三十四，继续冲",
        35 to "三十五，来杯茶吧",
        36 to "三十六，顺顺利利",
        37 to "三十七，好运继续",
        38 to "三十八，发财发发",
        39 to "三十九，长长久久",
        40 to "四十，四季发财",

        41 to "四十一，谁的运气好？",
        42 to "四十二，看看票子",
        43 to "四十三，别错过了",
        44 to "四四，事事如意",
        45 to "四十五，茶点来一份？",
        46 to "四十六，顺风顺水",
        47 to "四十七，大家看票",
        48 to "四十八，发财啦",
        49 to "四十九，五十快来了",
        50 to "五十！半个世纪，鼓掌！",

        51 to "五十一，好运继续",
        52 to "五十二，谁拿到了？",
        53 to "五十三，看看票子",
        54 to "五十四，喝杯茶再玩",
        55 to "五五，喜上加喜",
        56 to "五十六，顺顺利利",
        57 to "五十七，好运来了",
        58 to "五十八，发财发发",
        59 to "五十九，六十马上到",
        60 to "六十，顺到最后",

        61 to "六十一，胜利在望",
        62 to "六十二，别眨眼",
        63 to "六十三，顺顺当当",
        64 to "六十四，六六大顺",
        65 to "六十五，票子检查一下",
        66 to "六六，大顺特顺",
        67 to "六十七，好运不停",
        68 to "六十八，顺顺发发",
        69 to "六十九，长长久久",
        70 to "七十，进入冲刺阶段",

        71 to "七十一，再来一步",
        72 to "七十二，谁要赢？",
        73 to "七十三，看看你的票",
        74 to "七十四，坚持住",
        75 to "七十五，四分之三世纪",
        76 to "七十六，继续冲刺",
        77 to "两根棍子，七十七",
        78 to "七十八，一起发财",
        79 to "七十九，八十要来了",
        80 to "八十，发财到最后",

        81 to "八十一，好运就在眼前",
        82 to "八十二，千万别漏掉",
        83 to "八十三，票子看一看",
        84 to "八十四，四季发财",
        85 to "八十五，快到终点",
        86 to "八十六，顺顺发财",
        87 to "八十七，好运继续",
        88 to "双八，发发发发",
        89 to "八十九，长长久久",
        90 to "九十！最后一个，赶紧检查票子！"
    )

    private val dutchFunnyPhrases = mapOf(
        1 to "Eén, daar gaan we!",
        2 to "Twee, jij en ik",
        3 to "Drie, drie keer is scheepsrecht",
        4 to "Vier, vier hoeken van de wereld",
        5 to "Vijf, vijf vingers aan één hand",
        6 to "Zes, daar komt de zes!",
        7 to "Zeven, geluksgetal",
        8 to "Acht, goed opgelet!",
        9 to "Negen, de spanning stijgt",
        10 to "Tien, nu wordt het serieus",

        11 to "Eén plus één is elf",
        12 to "Twaalf, een dozijn",
        13 to "Dertien, geluk of pech?",
        14 to "Veertien, lekker doorgaan",
        15 to "Vijftien, het spel zit erin",
        16 to "Zestien, nog even!",
        17 to "Zeventien, de spanning stijgt",
        18 to "Achttien, nu wordt het spannend",
        19 to "Negentien, twintig komt eraan",
        20 to "Twintig, nu gaat het los",

        21 to "Eenentwintig, de winst komt dichterbij",
        22 to "Twee eendjes, tweeëntwintig",
        23 to "Drieëntwintig, kaartje checken!",
        24 to "Vierentwintig, dag en nacht",
        25 to "Vijfentwintig, een kwart eeuw",
        26 to "Zesentwintig, lekker bezig!",
        27 to "Zevenentwintig, wie heeft hem?",
        28 to "Achtentwintig, de spanning loopt op",
        29 to "Negenentwintig, bijna dertig",
        30 to "Dertig, nu wordt het gezellig",

        31 to "Eenendertig, ogen op het kaartje",
        32 to "Tweeëndertig, tanden laten zien en lachen",
        33 to "Dubbel drie, dubbel plezier",
        34 to "Vierendertig, lekker doorgaan",
        35 to "Vijfendertig, tijd voor koffie",
        36 to "Zesendertig, nu wordt het spannend",
        37 to "Zevenendertig, de jacht gaat door",
        38 to "Achtendertig, iedereen opletten",
        39 to "Negenendertig, veertig komt eraan",
        40 to "Veertig, tijd voor een stroopwafel",

        41 to "Eenenveertig, wie heeft geluk?",
        42 to "Tweeënveertig, het antwoord op alles",
        43 to "Drieënveertig, kaartje controleren",
        44 to "Dubbel vier, dubbel gezellig",
        45 to "Vijfenveertig, bitterballen erbij?",
        46 to "Zesenveertig, blijf scherp!",
        47 to "Zevenenveertig, alle ogen op de kaart",
        48 to "Achtenveertig, de winst komt dichterbij",
        49 to "Negenenveertig, bijna vijftig",
        50 to "Vijftig! Een halve eeuw, applaus!",

        51 to "Eenenvijftig, we gaan gewoon door",
        52 to "Tweeënvijftig, wie zit op de geluksstoel?",
        53 to "Drieënvijftig, kaartje checken!",
        54 to "Vierenvijftig, nog een koffie?",
        55 to "Dubbel vijf, dubbel geluk",
        56 to "Zesenvijftig, de spanning stijgt",
        57 to "Zevenenvijftig, wie gaat er winnen?",
        58 to "Achtenvijftig, nu wordt het spannend",
        59 to "Negenenvijftig, zestig komt eraan",
        60 to "Zestig, nu wordt het serieus",

        61 to "Eenenzestig, de winst is dichtbij",
        62 to "Tweeënzestig, ogen open!",
        63 to "Drieënzestig, de spanning stijgt",
        64 to "Vierenzestig, vier keer zestien",
        65 to "Vijfenzestig, niet in slaap vallen!",
        66 to "Dubbel zes, dubbel feest",
        67 to "Zevenenzestig, iedereen kijkt mee",
        68 to "Achtenzestig, de winst ruikt dichtbij",
        69 to "Negenenzestig, wat een nummer!",
        70 to "Zeventig, de eindsprint begint",

        71 to "Eenenzeventig, nog een paar te gaan",
        72 to "Tweeënzeventig, wie pakt hem?",
        73 to "Drieënzeventig, kaartje controleren",
        74 to "Vierenzeventig, bijna zover",
        75 to "Vijfenzeventig, drie kwart eeuw",
        76 to "Zesenzeventig, nog eentje erbij",
        77 to "Twee hockeysticks, zevenenzeventig",
        78 to "Achtenzeventig, de finale komt eraan",
        79 to "Negenenzeventig, bijna tachtig",
        80 to "Tachtig, houd je hart vast!",

        81 to "Eenentachtig, de winst is heel dichtbij",
        82 to "Tweeëntachtig, nu niets missen",
        83 to "Drieëntachtig, kaartje checken",
        84 to "Vierentachtig, de spanning stijgt",
        85 to "Vijfentachtig, bijna bij de finish",
        86 to "Zesentachtig, geluk gewenst!",
        87 to "Zevenentachtig, iedereen kijkt naar de kaart",
        88 to "Twee rondjes, achtentachtig",
        89 to "Negenentachtig, de laatste bocht",
        90 to "Negentig! De laatste, controleer je kaart!"
    )

    private val russianFunnyPhrases = mapOf(
        1 to "Раз пошла такая пьянка — начинаем",
        2 to "Ты да я, да мы с тобой",
        3 to "Три богатыря",
        4 to "На все четыре стороны",
        5 to "Пять пальцев одной руки",
        6 to "Шесть, а не шестьсот",
        7 to "Семь раз отмерь",
        8 to "Восемь, держим строй",
        9 to "Девять, удача рядом",
        10 to "Десять, пошла жара",

        11 to "Одиннадцать, барабанные палочки",
        12 to "Двенадцать, дюжина",
        13 to "Тринадцать, число с характером",
        14 to "Четырнадцать, игра продолжается",
        15 to "Пятнадцать, ещё не вечер",
        16 to "Шестнадцать, держим темп",
        17 to "Семнадцать, удача идёт",
        18 to "Восемнадцать, становится жарко",
        19 to "Девятнадцать, двадцать не за горами",
        20 to "Двадцать, вот это начало",

        21 to "Двадцать один, очко!",
        22 to "Два утёнка, двадцать два",
        23 to "Двадцать три, карту смотри",
        24 to "Двадцать четыре, круглые сутки",
        25 to "Двадцать пять, четверть века",
        26 to "Двадцать шесть, всё ещё в игре",
        27 to "Двадцать семь, кто его зачеркнул?",
        28 to "Двадцать восемь, напряжение растёт",
        29 to "Двадцать девять, тридцать на подходе",
        30 to "Тридцать, игра становится серьёзной",

        31 to "Тридцать один, глаза на карточку",
        32 to "Тридцать два, улыбаемся во все зубы",
        33 to "Двойная тройка, двойная удача",
        34 to "Тридцать четыре, идём дальше",
        35 to "Тридцать пять, пора пить чай",
        36 to "Тридцать шесть, дело пошло",
        37 to "Тридцать семь, удача не спит",
        38 to "Тридцать восемь, держим ухо востро",
        39 to "Тридцать девять, сорок уже рядом",
        40 to "Сорок, самое время для чая",

        41 to "Сорок один, кому сегодня повезёт?",
        42 to "Сорок два, ответ на всё",
        43 to "Сорок три, карточку проверь",
        44 to "Две четвёрки, двойная радость",
        45 to "Сорок пять, чай с пирожками?",
        46 to "Сорок шесть, игра кипит",
        47 to "Сорок семь, все смотрят на карточки",
        48 to "Сорок восемь, победа близко",
        49 to "Сорок девять, пятьдесят уже рядом",
        50 to "Пятьдесят! Полвека, аплодисменты!",

        51 to "Пятьдесят один, продолжаем",
        52 to "Пятьдесят два, кому достанется удача?",
        53 to "Пятьдесят три, карточку проверь",
        54 to "Пятьдесят четыре, ещё чаю?",
        55 to "Две пятёрки, двойная удача",
        56 to "Пятьдесят шесть, игра накаляется",
        57 to "Пятьдесят семь, кто впереди?",
        58 to "Пятьдесят восемь, напряжение растёт",
        59 to "Пятьдесят девять, шестьдесят близко",
        60 to "Шестьдесят, теперь всё серьёзно",

        61 to "Шестьдесят один, победа близко",
        62 to "Шестьдесят два, не моргай",
        63 to "Шестьдесят три, удача может повернуться",
        64 to "Шестьдесят четыре, четыре раза шестнадцать",
        65 to "Шестьдесят пять, не зевай!",
        66 to "Две шестёрки, двойной шестёрочный удар",
        67 to "Шестьдесят семь, сердце стучит",
        68 to "Шестьдесят восемь, победа пахнет близко",
        69 to "Шестьдесят девять, вот это число!",
        70 to "Семьдесят, начинается финишная прямая",

        71 to "Семьдесят один, ещё один шаг",
        72 to "Семьдесят два, кто заберёт победу?",
        73 to "Семьдесят три, карточку проверь",
        74 to "Семьдесят четыре, почти готово",
        75 to "Семьдесят пять, три четверти века",
        76 to "Семьдесят шесть, ещё номерок",
        77 to "Два топорика, семьдесят семь",
        78 to "Семьдесят восемь, финиш близко",
        79 to "Семьдесят девять, восемьдесят на подходе",
        80 to "Восемьдесят, держим сердце!",

        81 to "Восемьдесят один, победа совсем близко",
        82 to "Восемьдесят два, ничего не пропусти",
        83 to "Восемьдесят три, проверь карточку",
        84 to "Восемьдесят четыре, удача рядом",
        85 to "Восемьдесят пять, почти финиш",
        86 to "Восемьдесят шесть, удача с нами",
        87 to "Восемьдесят семь, все смотрят на поле",
        88 to "Две матрёшки, восемьдесят восемь",
        89 to "Восемьдесят девять, последний поворот",
        90 to "Девяносто! Последний номер, проверяйте карточки!"
    )

    private val vietnameseFunnyPhrases = mapOf(
        1 to "Một, khởi đầu may mắn",
        2 to "Hai, có đôi có cặp",
        3 to "Ba, tài lộc đầy nhà",
        4 to "Bốn phương tám hướng",
        5 to "Năm, ngũ hành hội tụ",
        6 to "Sáu, lộc về đầy nhà",
        7 to "Bảy, may mắn đang tới",
        8 to "Tám, phát tài phát lộc",
        9 to "Chín, con số quyền lực",
        10 to "Mười, cuộc chơi bắt đầu nóng",

        11 to "Mười một, một với một thành mười một",
        12 to "Mười hai, một tá đầy đặn",
        13 to "Mười ba, vận may đang đến",
        14 to "Mười bốn, nhìn vé cho kỹ",
        15 to "Mười lăm, may mắn thêm năm",
        16 to "Mười sáu, lộc đang tới",
        17 to "Mười bảy, vận may tiếp tục",
        18 to "Mười tám, phát tài phát lộc",
        19 to "Mười chín, hai mươi sắp tới",
        20 to "Hai mươi, cuộc chơi nóng rồi",

        21 to "Hai mươi mốt, may mắn đầu xuân",
        22 to "Hai con vịt, hai mươi hai",
        23 to "Hai mươi ba, xem vé nào",
        24 to "Hai mươi bốn, cả ngày lẫn đêm",
        25 to "Hai mươi lăm, một phần tư thế kỷ",
        26 to "Hai mươi sáu, may mắn đang tăng",
        27 to "Hai mươi bảy, ai có số này?",
        28 to "Hai mươi tám, phát tài phát lộc",
        29 to "Hai mươi chín, ba mươi sắp đến",
        30 to "Ba mươi, cuộc chơi thật vui",

        31 to "Ba mươi mốt, nhìn vé đi nhé",
        32 to "Ba mươi hai, cười thật tươi",
        33 to "Ba ba, tam tài may mắn",
        34 to "Ba mươi tư, tiếp tục nào",
        35 to "Ba mươi lăm, cà phê nhé?",
        36 to "Ba mươi sáu, tài lộc đủ đầy",
        37 to "Ba mươi bảy, may mắn tiếp tục",
        38 to "Ba mươi tám, phát tài phát lộc",
        39 to "Ba mươi chín, thần tài nhỏ",
        40 to "Bốn mươi, phở hay cà phê nào?",

        41 to "Bốn mươi mốt, ai may mắn đây?",
        42 to "Bốn mươi hai, nhìn vé cho kỹ",
        43 to "Bốn mươi ba, vận may tới rồi",
        44 to "Bốn bốn, may mắn nhân đôi",
        45 to "Bốn mươi lăm, bánh mì thôi nào",
        46 to "Bốn mươi sáu, cuộc chơi nóng lên",
        47 to "Bốn mươi bảy, mọi mắt nhìn vào vé",
        48 to "Bốn mươi tám, phát tài phát lộc",
        49 to "Bốn mươi chín, năm mươi sắp tới",
        50 to "Năm mươi! Nửa thế kỷ, vỗ tay nào!",

        51 to "Năm mươi mốt, tiếp tục cuộc chơi",
        52 to "Năm mươi hai, ai sẽ thắng đây?",
        53 to "Năm mươi ba, kiểm tra vé nhé",
        54 to "Năm mươi bốn, thêm ly cà phê không?",
        55 to "Năm năm, may mắn nhân đôi",
        56 to "Năm mươi sáu, lộc đang về",
        57 to "Năm mươi bảy, ai có số may?",
        58 to "Năm mươi tám, phát tài lần nữa",
        59 to "Năm mươi chín, sáu mươi sắp tới",
        60 to "Sáu mươi, giờ mới thật sự gay cấn",

        61 to "Sáu mươi mốt, chiến thắng rất gần",
        62 to "Sáu mươi hai, đừng bỏ lỡ nhé",
        63 to "Sáu mươi ba, tài lộc đang đến",
        64 to "Sáu mươi tư, vận may đầy nhà",
        65 to "Sáu mươi lăm, kiểm tra vé nào",
        66 to "Sáu sáu, lộc nhân đôi",
        67 to "Sáu mươi bảy, tim đập nhanh rồi",
        68 to "Sáu mươi tám, phát lộc phát tài",
        69 to "Sáu mươi chín, con số đặc biệt",
        70 to "Bảy mươi, nước rút bắt đầu",

        71 to "Bảy mươi mốt, thêm một bước nữa",
        72 to "Bảy mươi hai, ai sẽ về đích?",
        73 to "Bảy mươi ba, kiểm tra vé nhé",
        74 to "Bảy mươi tư, gần tới rồi",
        75 to "Bảy mươi lăm, ba phần tư thế kỷ",
        76 to "Bảy mươi sáu, thêm một số nữa",
        77 to "Hai cây gậy, bảy mươi bảy",
        78 to "Bảy mươi tám, phát tài phát lộc",
        79 to "Bảy mươi chín, tám mươi sắp tới",
        80 to "Tám mươi, giữ chặt trái tim!",

        81 to "Tám mươi mốt, chiến thắng rất gần",
        82 to "Tám mươi hai, đừng bỏ lỡ",
        83 to "Tám mươi ba, xem vé ngay",
        84 to "Tám mươi tư, may mắn đang tới",
        85 to "Tám mươi lăm, gần về đích rồi",
        86 to "Tám mươi sáu, lộc tới rồi",
        87 to "Tám mươi bảy, mọi mắt nhìn vào vé",
        88 to "Hai vòng tròn, tám mươi tám",
        89 to "Tám mươi chín, khúc cua cuối",
        90 to "Chín mươi! Số cuối cùng, kiểm tra vé!"
    )

    private val bengaliNumberWords = mapOf(
        1 to "এক", 2 to "দুই", 3 to "তিন", 4 to "চার", 5 to "পাঁচ", 6 to "ছয়", 7 to "সাত", 8 to "আট", 9 to "নয়", 10 to "দশ",
        11 to "এগারো", 12 to "বারো", 13 to "তেরো", 14 to "চৌদ্দ", 15 to "পোনেরো", 16 to "ষোলো", 17 to "সতেরো", 18 to "আঠারো", 19 to "উনিশ", 20 to "বিশ",
        30 to "ত্রিশ", 40 to "চল্লিশ", 50 to "পঞ্চাশ", 60 to "ষাট", 70 to "সত্তর", 80 to "আশি", 90 to "নব্বই"
    )

    private val odiaNumberWords = mapOf(
        1 to "ଏକ", 2 to "ଦୁଇ", 3 to "ତିନି", 4 to "ଚାରି", 5 to "ପାଞ୍ଚ", 6 to "ଛଅ", 7 to "ସାତ", 8 to "ଆଠ", 9 to "ନଅ", 10 to "ଦଶ",
        11 to "ଏଗାର", 12 to "ବାର", 13 to "ତେର", 14 to "ଚଉଦ", 15 to "ପନ୍ଦର", 16 to "ଷୋହଳ", 17 to "ସତର", 18 to "ଅଷ୍ଠାର", 19 to "ଉଣିଶ", 20 to "କୋଡ଼ିଏ",
        30 to "ତିରିଶି", 40 to "ଚାଳିଶି", 50 to "ପଚାଶ", 60 to "ଷଠି", 70 to "ସ୍ତରି", 80 to "ଅଶୀ", 90 to "ନବୁଇ"
    )

    private val hindiNumberWords = mapOf(
        1 to "एक", 2 to "दो", 3 to "तीन", 4 to "चार", 5 to "पांच", 6 to "छह", 7 to "सात", 8 to "आठ", 9 to "नौ", 10 to "दस",
        11 to "ग्यारह", 12 to "बारह", 13 to "तेरह", 14 to "चौदह", 15 to "पंद्रह", 16 to "सोलह", 17 to "सत्रह", 18 to "अठारह", 19 to "उन्नीस", 20 to "बीस",
        30 to "तीस", 40 to "चालिस", 50 to "पचास", 60 to "साठ", 70 to "सत्तर", 80 to "अस्सी", 90 to "नब्बे"
    )

    private val kannadaNumberWords = mapOf(
        1 to "ಒಂದು", 2 to "ಎರಡು", 3 to "ಮೂರು", 4 to "ನಾಲ್ಕು", 5 to "ಐದು", 6 to "ಆರು", 7 to "ಏಳು", 8 to "ಎಂಟು", 9 to "ಒಂಬತ್ತು", 10 to "ಹತ್ತು",
        11 to "ಹನ್ನೊಂದು", 12 to "ಹನ್ನೆರಡು", 13 to "ಹದಿಮೂರು", 14 to "ಹದಿನಾಲ್ಕು", 15 to "ಹದಿನೈದು", 16 to "ಹದಿನಾರು", 17 to "ಹದಿನೇಳು", 18 to "ಹದಿನೆಂಟು", 19 to "ಹತ್ತೊಂಬತ್ತು", 20 to "ಇಪ್ಪತ್ತು",
        30 to "ಮೂವತ್ತು", 40 to "ನಲವತ್ತು", 50 to "ಐವತ್ತು", 60 to "ಅರವತ್ತು", 70 to "ಎಪ್ಪತ್ತು", 80 to "ಎಂಬತ್ತು", 90 to "ತೊಂಬತ್ತು"
    )

    private val tamilNumberWords = mapOf(
        1 to "ஒன்று", 2 to "இரண்டு", 3 to "மூன்று", 4 to "நான்கு", 5 to "ஐந்து", 6 to "ஆறு", 7 to "ஏழு", 8 to "எட்டு", 9 to "ஒன்பது", 10 to "பத்து",
        11 to "பதினொன்று", 12 to "பன்னிரண்டு", 13 to "பதிமூன்று", 14 to "பதினான்கு", 15 to "பதினைந்து", 16 to "பதினாறு", 17 to "பதினேழு", 18 to "பதினெட்டு", 19 to "பத்தொன்பது", 20 to "இருபது",
        30 to "முப்பது", 40 to "நாற்பது", 50 to "ஐம்பது", 60 to "அறுபது", 70 to "எழுபது", 80 to "எண்பது", 90 to "தொண்ணூறு"
    )

    private val marathiNumberWords = mapOf(
        1 to "एक", 2 to "दोन", 3 to "तीन", 4 to "चार", 5 to "पांच", 6 to "सहा", 7 to "सात", 8 to "आठ", 9 to "नऊ", 10 to "दहा",
        11 to "अकरा", 12 to "बारा", 13 to "तेरा", 14 to "चौदा", 15 to "पंधरा", 16 to "सोळा", 17 to "सतरा", 18 to "अठरा", 19 to "एकोणीस", 20 to "वीस",
        30 to "तीस", 40 to "चाळीस", 50 to "पन्नास", 60 to "साठ", 70 to "सत्तर", 80 to "ऐंशी", 90 to "नव्वद"
    )

    private val teluguNumberWords = mapOf(
        1 to "ఒకటి", 2 to "రెండు", 3 to "మూడు", 4 to "నాలుగు", 5 to "ఐదు", 6 to "ఆరు", 7 to "ఏడు", 8 to "ఎనిమిది", 9 to "తొమ్మిది", 10 to "పది",
        11 to "పదకొండు", 12 to "పన్నెండు", 13 to "పదమూడు", 14 to "పద్నాలుగు", 15 to "పదిహేను", 16 to "పదహారు", 17 to "పదిహేడు", 18 to "పద్ధెనిమిది", 19 to "పంతొమ్మిది", 20 to "ఇరవై",
        30 to "ముప్పై", 40 to "నలభై", 50 to "యాభై", 60 to "అరవై", 70 to "దెబ్బై", 80 to "ఎనభై", 90 to "తొంబై"
    )

    private val gujaratiNumberWords = mapOf(
        1 to "એક", 2 to "બે", 3 to "ત્રણ", 4 to "ચાર", 5 to "પાંચ", 6 to "છ", 7 to "સાત", 8 to "આઠ", 9 to "નવ", 10 to "દસ",
        11 to "અગિયાર", 12 to "બાર", 13 to "તેર", 14 to "ચૌદ", 15 to "પંદર", 16 to "સોળ", 17 to "સત્તર", 18 to "અઢાર", 19 to "ઓગણીસ", 20 to "વીસ",
        30 to "ત્રીસ", 40 to "ચાળીસ", 50 to "પચાસ", 60 to "સાઠ", 70 to "સિત્તર", 80 to "એંસી", 90 to "નેવુ"
    )

    private val malayalamNumberWords = mapOf(
        1 to "ഒന്ന്", 2 to "രണ്ട്", 3 to "മൂന്ന്", 4 to "നാല്", 5 to "അഞ്ച്", 6 to "ആറ്", 7 to "ഏഴ്", 8 to "എട്ട്", 9 to "ഒൻപത്", 10 to "പത്ത്",
        11 to "പതിനൊന്ന്", 12 to "പന്ത്രണ്ട്", 13 to "പതിമൂന്ന്", 14 to "പതിനാല്", 15 to "പതിനഞ്ച്", 16 to "പതിനാറ്", 17 to "പതിനേഴ്", 18 to "പതിനെട്ട്", 19 to "പത്തൊൻപത്", 20 to "ഇരുപത്",
        30 to "മുപ്പത്", 40 to "നാൽപ്പത്", 50 to "അൻപത്", 60 to "അറുപത്", 70 to "എഴുപത്", 80 to "എൺപത്", 90 to "തൊണ്ണൂറ്"
    )

    private val punjabiNumberWords = mapOf(
        1 to "ਇੱਕ", 2 to "ਦੋ", 3 to "ਤਿੰਨ", 4 to "ਚਾਰ", 5 to "ਪੰਜ", 6 to "ਛੇ", 7 to "ਸੱਤ", 8 to "ਅੱਠ", 9 to "ਨੌਂ", 10 to "ਦੱਸ",
        11 to "ਗਿਆਰਾਂ", 12 to "ਬਾਰ੍ਹਾਂ", 13 to "ਤੇਰ੍ਹਾਂ", 14 to "ਚੌਧਾਂ", 15 to "ਪੰਦਰ੍ਹਾਂ", 16 to "ਸੋਲਾਂ", 17 to "ਸਤਾਰ੍ਹਾਂ", 18 to "ਅਠਾਰ੍ਹਾਂ", 19 to "ਉੱਨੀ", 20 to "ਵੀਹ",
        30 to "ਤੀਹ", 40 to "ਚਾਲੀ", 50 to "ਪੰਜਾਹ", 60 to "ਸੱਠ", 70 to "ਸੱਤਰ", 80 to "ਅੱਸੀ", 90 to "ਨੱਬੇ"
    )

    private val assameseNumberWords = mapOf(
        1 to "এক", 2 to "দুই", 3 to "তিনি", 4 to "চাৰি", 5 to "পাঁচ", 6 to "ছয়", 7 to "সাত", 8 to "আঠ", 9 to "ন", 10 to "দহ",
        11 to "এঘাৰ", 12 to "বাৰ", 13 to "তেৰ", 14 to "চৈধ্য", 15 to "পোন্ধৰ", 16 to "ষোল্ল", 17 to "সত্তৰ", 18 to "আঠাৰ", 19 to "ঊনবিছ", 20 to "বিছ",
        30 to "ত্ৰিছ", 40 to "চল্লিশ", 50 to "পঞ্চাশ", 60 to "ষাঠি", 70 to "সত্তৰ", 80 to "আশী", 90 to "নব্বৈ"
    )

    private val spanishNumberWords = mapOf(
        1 to "uno", 2 to "dos", 3 to "tres", 4 to "cuatro", 5 to "cinco", 6 to "seis", 7 to "siete", 8 to "ocho", 9 to "nueve", 10 to "diez",
        11 to "once", 12 to "doce", 15 to "quince", 20 to "veinte", 30 to "treinta", 40 to "cuarenta", 50 to "cincuenta", 60 to "sesenta", 70 to "setenta", 80 to "ochenta", 90 to "noventa"
    )

    private val portugueseNumberWords = mapOf(
        1 to "um", 2 to "dois", 3 to "três", 4 to "quatro", 5 to "cinco", 6 to "seis", 7 to "sete", 8 to "oito", 9 to "nove", 10 to "dez",
        11 to "onze", 12 to "doze", 15 to "quinze", 20 to "vinte", 30 to "trinta", 40 to "quarenta", 50 to "cinquenta", 60 to "sessenta", 70 to "setenta", 80 to "oitenta", 90 to "noventa"
    )

    private val frenchNumberWords = mapOf(
        1 to "un", 2 to "deux", 3 to "trois", 4 to "quatre", 5 to "cinq", 6 to "six", 7 to "sept", 8 to "huit", 9 to "neuf", 10 to "dix",
        11 to "onze", 12 to "douze", 15 to "quinze", 20 to "vingt", 30 to "trente", 40 to "quarante", 50 to "cinquante", 60 to "soixante", 70 to "soixante-dix", 80 to "quatre-vingts", 90 to "quatre-vingt-dix"
    )

    private val germanNumberWords = mapOf(
        1 to "eins", 2 to "zwei", 3 to "drei", 4 to "vier", 5 to "fünf", 6 to "sechs", 7 to "sieben", 8 to "acht", 9 to "neun", 10 to "zehn",
        11 to "elf", 12 to "zwölf", 15 to "fünfzehn", 20 to "zwanzig", 30 to "dreißig", 40 to "vierzig", 50 to "fünfzig", 60 to "sechzig", 70 to "siebzig", 80 to "achtzig", 90 to "neunzig"
    )

    private val arabicNumberWords = mapOf(
        1 to "واحد", 2 to "اثنان", 3 to "ثلاثة", 4 to "أربعة", 5 to "خمسة", 6 to "ستة", 7 to "سبعة", 8 to "ثمانية", 9 to "تسعة", 10 to "عشرة",
        11 to "أحد عشر", 12 to "اثنا عشر", 20 to "عشرون", 30 to "ثلاثون", 40 to "اربعون", 50 to "خمسون", 60 to "ستون", 70 to "سبعون", 80 to "ثمانون", 90 to "تسعون"
    )

    private val indonesianNumberWords = mapOf(
        1 to "satu", 2 to "dua", 3 to "tiga", 4 to "empat", 5 to "lima", 6 to "enam", 7 to "tujuh", 8 to "delapan", 9 to "sembilan", 10 to "sepuluh",
        11 to "sebelas", 12 to "dua belas", 20 to "dua puluh", 30 to "tiga puluh", 40 to "empat puluh", 50 to "lima puluh", 60 to "enam puluh", 70 to "tujuh puluh", 80 to "delapan puluh", 90 to "sembilan puluh"
    )

    private val turkishNumberWords = mapOf(
        1 to "bir", 2 to "iki", 3 to "üç", 4 to "dört", 5 to "beş", 6 to "altı", 7 to "yedi", 8 to "sekiz", 9 to "dokuz", 10 to "on",
        11 to "on bir", 12 to "on iki", 20 to "yirmi", 30 to "otuz", 40 to "kırk", 50 to "elli", 60 to "altmış", 70 to "yetmiş", 80 to "seksen", 90 to "doksan"
    )

    private val italianNumberWords = mapOf(
        1 to "uno", 2 to "due", 3 to "tre", 4 to "quattro", 5 to "cinque", 6 to "sei", 7 to "sette", 8 to "otto", 9 to "nove", 10 to "dieci",
        11 to "undici", 12 to "dodici", 15 to "quindici", 20 to "venti", 30 to "trenta", 40 to "quaranta", 50 to "cinquanta", 60 to "sessanta", 70 to "settanta", 80 to "ottanta", 90 to "novanta"
    )

    private val japaneseNumberWords = mapOf(
        1 to "いち", 2 to "に", 3 to "さん", 4 to "よん", 5 to "ご", 6 to "ろく", 7 to "なな", 8 to "はち", 9 to "きゅう", 10 to "じゅう",
        11 to "じゅういち", 12 to "じゅうに", 20 to "にじゅう", 30 to "さんじゅう", 40 to "よんじゅう", 50 to "ごじゅう", 60 to "ろくじゅう", 70 to "ななじゅう", 80 to "はちじゅう", 90 to "きゅうじゅう"
    )

    private val koreanNumberWords = mapOf(
        1 to "일", 2 to "이", 3 to "삼", 4 to "사", 5 to "오", 6 to "육", 7 to "칠", 8 to "팔", 9 to "구", 10 to "십",
        11 to "십일", 12 to "십이", 20 to "이십", 30 to "삼십", 40 to "사십", 50 to "오십", 60 to "육십", 70 to "칠십", 80 to "팔십", 90 to "구십"
    )

    private val chineseNumberWords = mapOf(
        1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "七", 8 to "八", 9 to "九", 10 to "十",
        11 to "十一", 12 to "十二", 20 to "二十", 30 to "三十", 40 to "四十", 50 to "五十", 60 to "六十", 70 to "七十", 80 to "八十", 90 to "九十"
    )

    private val dutchNumberWords = mapOf(
        1 to "een", 2 to "twee", 3 to "drie", 4 to "vier", 5 to "vijf", 6 to "zes", 7 to "zeven", 8 to "acht", 9 to "negen", 10 to "tien",
        11 to "elf", 12 to "twaalf", 20 to "twintig", 30 to "dertig", 40 to "veertig", 50 to "vijftig", 60 to "zestig", 70 to "zeventig", 80 to "tachtig", 90 to "negentig"
    )

    private val russianNumberWords = mapOf(
        1 to "один", 2 to "два", 3 to "три", 4 to "четыре", 5 to "пять", 6 to "шесть", 7 to "семь", 8 to "восемь", 9 to "девять", 10 to "десять",
        11 to "одиннадцать", 12 to "двенадцать", 20 to "двадцать", 30 to "тридцать", 40 to "сорок", 50 to "пятьдесят", 60 to "шестьдесят", 70 to "семьдесят", 80 to "восемьдесят", 90 to "девяносто"
    )

    private val vietnameseNumberWords = mapOf(
        1 to "một", 2 to "hai", 3 to "ba", 4 to "bốn", 5 to "năm", 6 to "sáu", 7 to "bảy", 8 to "tám", 9 to "chín", 10 to "mười",
        11 to "mười một", 12 to "mười hai", 20 to "hai mươi", 30 to "ba mươi", 40 to "bốn mươi", 50 to "năm mươi", 60 to "sáu mươi", 70 to "bảy mươi", 80 to "tám mươi", 90 to "chín mươi"
    )
}