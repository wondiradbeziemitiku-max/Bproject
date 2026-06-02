export function generateCard(): { card: (number | string)[][], cardHash: string } {
  const crypto = require('crypto');
  const columns = [
    getRandomFromRange(1, 15, 5),
    getRandomFromRange(16, 30, 5),
    getRandomFromRange(31, 45, 5),
    getRandomFromRange(46, 60, 5),
    getRandomFromRange(61, 75, 5)
  ];
  const card: (number | string)[][] = [];
  for (let row = 0; row < 5; row++) {
    card[row] = [];
    for (let col = 0; col < 5; col++) {
      card[row][col] = (row === 2 && col === 2) ? 'FREE' : columns[col][row];
    }
  }
  const hash = crypto.createHash('sha256').update(JSON.stringify(card)).digest('hex');
  return { card, cardHash: hash };
}

function getRandomFromRange(min: number, max: number, count: number): number[] {
  const arr: number[] = [];
  for (let i = min; i <= max; i++) arr.push(i);
  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]];
  }
  return arr.slice(0, count);
}

export function checkCardForBingo(card: (number | string)[][], drawnNumbers: number[]): { isWinner: boolean, line: string } {
  const drawnSet = new Set(drawnNumbers);
  // rows
  for (let r = 0; r < 5; r++) {
    if (card[r].every((cell, c) => cell === 'FREE' || drawnSet.has(cell as number))) {
      return { isWinner: true, line: `Row ${r + 1}` };
    }
  }
  // columns
  for (let c = 0; c < 5; c++) {
    let complete = true;
    for (let r = 0; r < 5; r++) {
      const cell = card[r][c];
      if (cell !== 'FREE' && !drawnSet.has(cell as number)) { complete = false; break; }
    }
    if (complete) return { isWinner: true, line: `Column ${c + 1}` };
  }
  // diagonals
  let diag1 = true, diag2 = true;
  for (let i = 0; i < 5; i++) {
    if (card[i][i] !== 'FREE' && !drawnSet.has(card[i][i] as number)) diag1 = false;
    if (card[i][4 - i] !== 'FREE' && !drawnSet.has(card[i][4 - i] as number)) diag2 = false;
  }
  if (diag1) return { isWinner: true, line: 'Diagonal (TL-BR)' };
  if (diag2) return { isWinner: true, line: 'Diagonal (TR-BL)' };
  return { isWinner: false, line: '' };
}

export async function checkAllCardsForBingo(gameRef: FirebaseFirestore.DocumentReference, lastDrawn: number): Promise<{ uid: string, line: string } | null> {
  const playersSnap = await gameRef.collection('players').get();
  const drawnSnap = await gameRef.collection('drawnNumbers').get();
  const drawnNumbers: number[] = drawnSnap.docs.map(d => d.data().number);
  for (const playerDoc of playersSnap.docs) {
    const card = playerDoc.data().cardNumbers;
    const result = checkCardForBingo(card, drawnNumbers);
    if (result.isWinner) {
      return { uid: playerDoc.id, line: result.line };
    }
  }
  return null;
}
