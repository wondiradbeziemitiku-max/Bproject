import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

export const adminStartGame = functions.https.onCall(async (data, context) => {
  // check admin
  const { pool, hash } = generateShuffledPool();
  const gameRef = admin.firestore().collection('games').doc();
  await gameRef.set({
    status: 'countdown',
    playerCount: 0,
    countdownSeconds: 30,
    drawIntervalSeconds: 6,
    cardPriceCoins: 10,
    prizePoolPercentage: 80,
    numberPoolHash: hash,
    startedAt: admin.firestore.FieldValue.serverTimestamp()
  });
  await gameRef.collection('private').doc('pool').set({ pool });
  return { gameId: gameRef.id };
});

function generateShuffledPool() {
  const crypto = require('crypto');
  const pool = Array.from({length:75}, (_,i)=>i+1);
  for (let i=pool.length-1; i>0; i--) {
    const j = crypto.randomBytes(4).readUInt32BE(0) % (i+1);
    [pool[i], pool[j]] = [pool[j], pool[i]];
  }
  const hash = crypto.createHash('sha256').update(pool.join(',')).digest('hex');
  return { pool, hash };
}

export const adminCancelGame = functions.https.onCall(async (data, context) => {
  const gameId = data.gameId;
  await admin.firestore().collection('games').doc(gameId).update({ status: 'cancelled' });
});
