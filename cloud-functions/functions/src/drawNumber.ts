import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { checkAllCardsForBingo } from './utils/bingoLogic';

const db = admin.firestore();

export const drawNumber = async () => {
  const activeGames = await db.collection('games').where('status', '==', 'active').get();
  for (const gameDoc of activeGames.docs) {
    const gameRef = gameDoc.ref;
    const privatePoolSnap = await gameRef.collection('private').doc('pool').get();
    if (!privatePoolSnap.exists) continue;
    const pool: number[] = privatePoolSnap.data()?.pool || [];
    const drawnSnap = await gameRef.collection('drawnNumbers').orderBy('drawnOrder', 'desc').limit(1).get();
    const nextIndex = drawnSnap.empty ? 0 : (drawnSnap.docs[0].data().drawnOrder as number);
    if (nextIndex >= pool.length) {
      // all numbers drawn, end game with no winner
      await gameRef.update({ status: 'finished', finishedAt: admin.firestore.FieldValue.serverTimestamp() });
      continue;
    }
    const number = pool[nextIndex];
    await gameRef.collection('drawnNumbers').add({
      number,
      drawnOrder: nextIndex + 1,
      drawnAt: admin.firestore.FieldValue.serverTimestamp()
    });
    const winner = await checkAllCardsForBingo(gameRef, number);
    if (winner) {
      await gameRef.update({ status: 'finished', winnerUid: winner.uid, winningLine: winner.line, finishedAt: admin.firestore.FieldValue.serverTimestamp() });
      // process prize
      // ...
    }
  }
};
