export interface TambolaRule {
  id: number;
  name: string;
  description: string;
  type: string;
  winningPattern: number[];
  quantity: number;
  percentage?: number;
  weight: number;
  isFullHouse: boolean;
}

export const TAMBOLA_RULES: TambolaRule[] = [
  {
    id: 1,
    name: "Full House",
    description: "First Player to Complete all numbers on the ticket.",
    type: "FULL_HOUSE",
    winningPattern: Array.from({ length: 27 }, (_, i) => i),
    quantity: 1,
    weight: 50,
    isFullHouse: true
  },
  {
    id: 2,
    name: "Second House",
    description: "Second player to complete the ticket.",
    type: "SECOND_HOUSE",
    winningPattern: Array.from({ length: 27 }, (_, i) => i),
    quantity: 1,
    weight: 30,
    isFullHouse: true
  },
  {
    id: 3,
    name: "Third House",
    description: "Third player to complete the ticket.",
    type: "THIRD_HOUSE",
    winningPattern: Array.from({ length: 27 }, (_, i) => i),
    quantity: 1,
    weight: 20,
    isFullHouse: true
  },
  {
    id: 4,
    name: "Early Five",
    description: "First player to mark any five numbers.",
    type: "EARLY_FIVE",
    winningPattern: [0, 4, 12, 18, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 5,
    name: "Top Line",
    description: "Complete all numbers in the top row.",
    type: "TOP_LINE",
    winningPattern: [0, 1, 2, 3, 4, 5, 6, 7, 8],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 6,
    name: "Middle Line",
    description: "Complete all numbers in the middle row.",
    type: "MIDDLE_LINE",
    winningPattern: [9, 10, 11, 12, 13, 14, 15, 16, 17],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 7,
    name: "Bottom Line",
    description: "Complete all numbers in the bottom row.",
    type: "BOTTOM_LINE",
    winningPattern: [18, 19, 20, 21, 22, 23, 24, 25, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 8,
    name: "Corner",
    description: "Top Row: 1st, 5th Number\nBottom Row: 1st, 5th Number.",
    type: "CORNER",
    winningPattern: [0, 7, 18, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 9,
    name: "Diamond",
    description: "Top Row: 1st, 5th Number\nMiddle Row: 3rd Number\nBottom Row: 1st, 5th Number.",
    type: "DIAMOND",
    winningPattern: [0, 7, 14, 18, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 10,
    name: "Pyramid",
    description: "Top Row: 3rd Number\nMiddle Row: 2nd, 4th Number\nBottom Row: 1st, 3rd, 5th Number",
    type: "PYRAMID",
    winningPattern: [4, 12, 15, 18, 21, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 11,
    name: "Inverted Pyramid",
    description: "Top Row: 1st, 3rd and 5th number\nMiddle Row: 2nd and 4th number\nBottom Row: 3rd number.",
    type: "INVERTED_PYRAMID",
    winningPattern: [0, 4, 7, 12, 15, 21],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 12,
    name: "Star",
    description: "Top Row: 1st, 3rd, 5th Number\nMiddle Row: All Numbers\nBottom Row: 1st, 3rd, 5th Number.",
    type: "STAR",
    winningPattern: [0, 4, 7, 10, 12, 14, 15, 17, 18, 21, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 13,
    name: "Odds",
    description: "Top Row: 1st, 3rd, 5th Number\nMiddle Row: 1st, 3rd, 5th Number\nBottom Row: 1st, 3rd, 5th Number.",
    type: "ODDS",
    winningPattern: [0, 4, 7, 10, 14, 17, 18, 21, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 14,
    name: "Even",
    description: "Top Row: 2nd, 4th Number\nMiddle Row: 2nd, 4th Number\nBottom Row: 2nd, 4th Number.",
    type: "EVEN",
    winningPattern: [2, 5, 12, 15, 20, 25],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 15,
    name: "First Half",
    description: "First three numbers from every row.",
    type: "FIRST_HALF",
    winningPattern: [0, 2, 4, 10, 12, 14, 18, 20, 21],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 16,
    name: "Second Half",
    description: "Last three numbers from every row.",
    type: "SECOND_HALF",
    winningPattern: [4, 5, 7, 14, 15, 17, 21, 25, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 17,
    name: "Breakfast",
    description: "Column 1,2,3. Mark numbers from 1 to 29.",
    type: "BREAKFAST",
    winningPattern: [0, 2, 10, 18, 20],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 18,
    name: "Lunch",
    description: "Column 4,5,6. Mark numbers from 30 to 59.",
    type: "LUNCH",
    winningPattern: [4, 5, 12, 14, 21],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 19,
    name: "Dinner",
    description: "Column 7,8,9. Mark numbers from 60 to 90.",
    type: "DINNER",
    winningPattern: [7, 15, 17, 25, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 20,
    name: "Temperature",
    description: "Mark 1st and Last numbers of a ticket.",
    type: "TEMPERATURE",
    winningPattern: [0, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 21,
    name: "Below Fifty",
    description: "All numbers present on a ticket which are less than 50. Numbers from 1 to 49",
    type: "BELOW_FIFTY",
    winningPattern: [0, 2, 4, 10, 12, 18, 20, 21],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  },
  {
    id: 22,
    name: "Above Fifty",
    description: "All numbers present on a card which are greater than equal to 50. Numbers from 50 to 90",
    type: "ABOVE_FIFTY",
    winningPattern: [5, 7, 14, 15, 17, 25, 26],
    quantity: 1,
    weight: 10,
    isFullHouse: false
  }
];
