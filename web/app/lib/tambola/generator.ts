export interface TambolaGrid {
  row0: number[];
  row1: number[];
  row2: number[];
}

/**
 * Generates a valid standard 3x9 Tambola (Housie) ticket grid.
 * 
 * Rules:
 * - 3 rows x 9 columns grid.
 * - Exactly 5 non-zero numbers per row (15 numbers per ticket).
 * - Column range:
 *   Col 0: 1 - 9
 *   Col 1: 10 - 19
 *   Col 2: 20 - 29
 *   Col 3: 30 - 39
 *   Col 4: 40 - 49
 *   Col 5: 50 - 59
 *   Col 6: 60 - 69
 *   Col 7: 70 - 79
 *   Col 8: 80 - 90
 * - Each column has at least 1 number across the 3 rows.
 * - Numbers in each column are sorted ascending top-to-bottom.
 * - Empty cells are represented by 0.
 */
export function generateTambolaGrid(): TambolaGrid {
  while (true) {
    const grid: number[][] = Array.from({ length: 3 }, () => Array(9).fill(0));
    
    // Step 1: Assign columns for each row ensuring 5 numbers per row
    // Each row must have 5 columns chosen.
    // Across the 3 rows, every column (0..8) must be picked at least once.
    const colCounts = Array(9).fill(0);
    const rowCols: number[][] = [];

    // Simple backtrack / trial to pick 5 cols for row0, row1, row2
    let validLayout = false;
    for (let attempt = 0; attempt < 50; attempt++) {
      colCounts.fill(0);
      rowCols.length = 0;

      for (let r = 0; r < 3; r++) {
        // Pick 5 random columns from 0..8
        const availableCols = Array.from({ length: 9 }, (_, c) => c)
          .filter((c) => colCounts[c] < 3); // Max 3 numbers per column
        
        // Shuffle availableCols
        for (let i = availableCols.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [availableCols[i], availableCols[j]] = [availableCols[j], availableCols[i]];
        }

        const selected = availableCols.slice(0, 5).sort((a, b) => a - b);
        selected.forEach((c) => colCounts[c]++);
        rowCols.push(selected);
      }

      // Check if all columns 0..8 have at least 1 count
      if (colCounts.every((count) => count >= 1)) {
        validLayout = true;
        break;
      }
    }

    if (!validLayout) continue; // Try again

    // Step 2: Generate unique numbers for each column based on count needed
    for (let c = 0; c < 9; c++) {
      const count = colCounts[c];
      let min = c === 0 ? 1 : c * 10;
      let max = c === 8 ? 90 : c * 10 + 9;
      
      const availableNumbers: number[] = [];
      for (let n = min; n <= max; n++) {
        availableNumbers.push(n);
      }

      // Shuffle available numbers
      for (let i = availableNumbers.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [availableNumbers[i], availableNumbers[j]] = [availableNumbers[j], availableNumbers[i]];
      }

      const picked = availableNumbers.slice(0, count).sort((a, b) => a - b);

      // Place picked numbers into rows that selected column c
      let pickedIndex = 0;
      for (let r = 0; r < 3; r++) {
        if (rowCols[r].includes(c)) {
          grid[r][c] = picked[pickedIndex++];
        }
      }
    }

    return {
      row0: grid[0],
      row1: grid[1],
      row2: grid[2]
    };
  }
}
