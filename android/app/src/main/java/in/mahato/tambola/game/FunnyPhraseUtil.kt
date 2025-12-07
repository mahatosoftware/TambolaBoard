package `in`.mahato.tambola.game

object FunnyPhraseUtil {

    fun getFunnyPhrase(number: Int): String {
        val text = when (number) {
            1 -> "At the Beginning."
            2 -> "Me and you."
            3 -> "Happy family."
            4 -> "Two Plus Two."
            5 -> "Punjab mail."
            6 -> "Bottom heavy."
            7 -> "Lucky number."
            8 -> "Big fat lady."
            9 -> "Doctor's time."
            10 -> "A big fat hen."
            11 -> "One and one."
            12 -> "One dozen."
            13 -> "Unlucky for some."
            14 -> "Valentine's Day."
            15 -> "The age when attitude starts."
            16 -> "Sweet sixteen."
            17 -> "Not so sweet."
            18 -> "Voting age."
            19 -> "Last of the teens."
            20 -> "One score."
            21 -> "Women's age never crosses."
            22 -> "Two little ducks."
            23 -> "You and me."
            24 -> "Two dozen."
            25 -> "Silver Jubilee Number."
            26 -> "Republic Day"
            27 -> "Gateway to heaven"
            28 -> "Not so late at."
            29 -> "Rise and Shine at."
            30 -> "Women get flirty at."
            31 -> "Time for fun."
            32 -> "Buckle my shoe."
            33 -> "All the 3s."
            34 -> "Ask for more."
            35 -> "Three and Five."
            36 -> "Popular size."
            37 -> "Mixed luck."
            38 -> "Oversize."
            39 -> "Watch your waistline."
            40 -> "Men get Naughty at."
            41 -> "Four and one."
            42 -> "Quit India Movement."
            43 -> "Pain in the knee."
            44 -> "All the Fours."
            45 -> "Halfway there."
            46 -> "Four and six."
            47 -> "Year of Independence."
            48 -> "Four dozen."
            49 -> "Four and Nine."
            50 -> "Half a century"
            51 -> "Five and one."
            52 -> "Weeks in a year."
            53 -> "Five and three."
            54 -> "Time for Mooor."
            55 -> "All the fives."
            56 -> "Pick up sticks."
            57 -> "Mutiny Year."
            58 -> "Time to retire."
            59 -> "Five and Nine."
            60 -> "Five dozen."
            61 -> "Bakers bun."
            62 -> "Turn the screw."
            63 -> "Tickle me."
            64 -> "Six and Four."
            65 -> "Old age pension."
            66 -> "Chhakke pe chhakka."
            67 -> "Made in heaven."
            68 -> "Check your weight."
            69 -> "Favourite of mine."
            70 -> "Lucky blind."
            71 -> "Bang on the drum."
            72 -> "Lucky two."
            73 -> "Under the tree."
            74 -> "Still want more."
            75 -> "Diamond Jublee."
            76 -> "Lucky six."
            77 -> "Two hockey sticks."
            78 -> "Heaven's gate."
            79 -> "One more time."
            80 -> "Eight and Blank."
            81 -> "Corner shot."
            82 -> "Fat lady with a duck."
            83 -> "India wins Cricket World Cup at."
            84 -> "Seven Dozen."
            85 -> "Staying alive."
            86 -> "Between the sticks."
            87 -> "Grandpa age."
            88 -> "Two fat ladies."
            89 -> "All but one."
            90 -> "Top of the house."
            else -> "Number out of range"
        }

        return "$text Number ${numberToWords(number)}"
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
}
