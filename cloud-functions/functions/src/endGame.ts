import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

export const endGame = functions.https.onCall(async (data, context) => {
  const gameId = data.gameId;
  const gameRef = admin.firestore().collection('games').doc(gameId);
  const gameSnap = await gameRef.get();
  if (!gameSnap.exists) throw new functions.https.HttpsError('not-found', 'Game not found.');
  const game = gameSnap.data();
  if (!game) return;
  const winnerUid = game.winnerUid;
  const prize = game.prizePool;
  if (winnerUid && prize > 0) {
    await admin.firestore().collection('users').doc(winnerUid).update({
      coins: admin.firestore.FieldValue.increment(prize)
    });
    await admin.firestore().collection('transactions').add({
      uid: winnerUid, type: 'prize', amount: prize, gameId, createdAt: admin.firestore.FieldValue.serverTimestamp()
    });
  }
  await gameRef.update({ status: 'finished' });
});
