import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { generateShuffledPool } from './utils/fairness';

const db = admin.firestore();

export const startGame = functions.https.onCall(async (data, context) => {
  const { pool, hash } = generateShuffledPool();
  const gameRef = db.collection('games').doc();
  await gameRef.set({
    status: 'countdown',
    playerCount: 0,
    countdownSeconds: 30,
    drawIntervalSeconds: 6,
    cardPriceCoins: 10,
    prizePoolPercentage: 80,
    numberPoolHash: hash,
    startedAt: admin.firestore.FieldValue.serverTimestamp(),
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });
  // store shuffled pool in a private doc
  await gameRef.collection('private').doc('pool').set({ pool });
  return { gameId: gameRef.id };
});
