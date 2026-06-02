import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { checkCardForBingo } from './utils/bingoLogic';

export const callBingo = functions.https.onCall(async (data, context) => {
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError('unauthenticated', 'Must be logged in.');

  const gameId = data.gameId;
  const gameRef = admin.firestore().collection('games').doc(gameId);
  const playerSnap = await gameRef.collection('players').doc(uid).get();
  if (!playerSnap.exists) throw new functions.https.HttpsError('not-found', 'Not in game.');
  const card = playerSnap.data()?.cardNumbers;
  const drawnNumbersSnap = await gameRef.collection('drawnNumbers').get();
  const drawnNumbers: number[] = drawnNumbersSnap.docs.map(d => d.data().number);
  const result = checkCardForBingo(card, drawnNumbers);
  if (result.isWinner) {
    return { isWinner: true, line: result.line };
  } else {
    // cooldown logic handled on client
    return { isWinner: false };
  }
});
