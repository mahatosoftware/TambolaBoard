import {
    Trophy,
    ArrowUp,
    AlignJustify,
    ArrowDown,
    LayoutGrid,
    Diamond,
    Triangle,
    Star,
    Hash,
    Divide,
    Sunrise,
    Sun,
    Sunset,
    Thermometer,
    ArrowDownCircle,
    ArrowUpCircle
} from 'lucide-react';

export const rulesData = [
    {
        id: 1,
        name: "Full House",
        description: "First Player to Complete all numbers on the ticket.",
        icon: LayoutGrid,
        winningPattern: Array.from({ length: 27 }, (_, i) => i),
        weight: 50
    },
    {
        id: 2,
        name: "Second House",
        description: "Second player to complete the ticket.",
        icon: LayoutGrid,
        winningPattern: Array.from({ length: 27 }, (_, i) => i),
        weight: 30
    },
    {
        id: 3,
        name: "Third House",
        description: "Third player to complete the ticket.",
        icon: LayoutGrid,
        winningPattern: Array.from({ length: 27 }, (_, i) => i),
        weight: 20
    },
    {
        id: 4,
        name: "Early Five",
        description: "First player to mark any five numbers.",
        icon: Trophy,
        winningPattern: [0, 4, 12, 18, 26],
        weight: 10
    },
    {
        id: 5,
        name: "Top Line",
        description: "Complete all numbers in the top row.",
        icon: ArrowUp,
        winningPattern: [0, 1, 2, 3, 4, 5, 6, 7, 8],
        weight: 10
    },
    {
        id: 6,
        name: "Middle Line",
        description: "Complete all numbers in the middle row.",
        icon: AlignJustify,
        winningPattern: [9, 10, 11, 12, 13, 14, 15, 16, 17],
        weight: 10
    },
    {
        id: 7,
        name: "Bottom Line",
        description: "Complete all numbers in the bottom row.",
        icon: ArrowDown,
        winningPattern: [18, 19, 20, 21, 22, 23, 24, 25, 26],
        weight: 10
    },
    {
        id: 8,
        name: "Corner",
        description: "Top Row: 1st, 5th Number\nBottom Row: 1st, 5th Number.",
        icon: LayoutGrid,
        winningPattern: [0, 7, 18, 26],
        weight: 10
    },
    {
        id: 9,
        name: "Diamond",
        description: "Top Row: 3rd Number\nMiddle Row: 1st, 5th Number\nBottom Row: 3rd Number",
        icon: Diamond,
        winningPattern: [0, 7, 14, 18, 26], // Adjusted to match Android indices (0,7,14,18,26)
        weight: 10
    },
    {
        id: 10,
        name: "Pyramid",
        description: "Top Row: 3rd\nMiddle Row: 2nd, 4th\nBottom Row: 1st, 3rd, 5th",
        icon: Triangle,
        winningPattern: [4, 12, 15, 18, 21, 26],
        weight: 10
    },
    {
        id: 11,
        name: "Inverted Pyramid",
        description: "Top: 1st, 3rd, 5th\nMiddle: 2nd, 4th\nBottom: 3rd",
        icon: Triangle,
        iconRotate: 180,
        winningPattern: [0, 4, 7, 12, 15, 21],
        weight: 10
    },
    {
        id: 12,
        name: "Star",
        description: "Various star patterns.",
        icon: Star,
        winningPattern: [0, 4, 7, 10, 12, 14, 15, 17, 18, 21, 26],
        weight: 10
    },
    {
        id: 13,
        name: "Odds",
        description: "Top: 1,3,5\nMiddle: 1,3,5\nBottom: 1,3,5",
        icon: Hash,
        winningPattern: [0, 4, 7, 10, 14, 17, 18, 21, 26],
        weight: 10
    },
    {
        id: 14,
        name: "Even",
        description: "Top: 2,4\nMiddle: 2,4\nBottom: 2,4",
        icon: Hash,
        winningPattern: [2, 5, 12, 15, 20, 25],
        weight: 10
    },
    {
        id: 15,
        name: "First Half",
        description: "First three numbers from every row.",
        icon: Divide,
        winningPattern: [0, 2, 4, 10, 12, 14, 18, 20, 21],
        weight: 10
    },
    {
        id: 16,
        name: "Second Half",
        description: "Last three numbers from every row.",
        icon: Divide,
        winningPattern: [4, 5, 7, 14, 15, 17, 21, 25, 26],
        weight: 10
    },
    {
        id: 17,
        name: "Breakfast",
        description: "Column 1,2,3 (1-29).",
        icon: Sunrise,
        winningPattern: [0, 2, 10, 18, 20],
        weight: 10
    },
    {
        id: 18,
        name: "Lunch",
        description: "Column 4,5,6 (30-59).",
        icon: Sun,
        winningPattern: [4, 5, 12, 14, 21],
        weight: 10
    },
    {
        id: 19,
        name: "Dinner",
        description: "Column 7,8,9 (60-90).",
        icon: Sunset,
        winningPattern: [7, 15, 17, 25, 26],
        weight: 10
    },
    {
        id: 20,
        name: "Temperature",
        description: "Smallest and Largest number on ticket.",
        icon: Thermometer,
        winningPattern: [0, 26],
        weight: 10
    },
    {
        id: 21,
        name: "Below Fifty",
        description: "Numbers 1-49.",
        icon: ArrowDownCircle,
        winningPattern: [0, 2, 4, 10, 12, 18, 20, 21],
        weight: 10
    },
    {
        id: 22,
        name: "Above Fifty",
        description: "Numbers 50-90.",
        icon: ArrowUpCircle,
        winningPattern: [5, 7, 14, 15, 17, 25, 26],
        weight: 10
    }
];
