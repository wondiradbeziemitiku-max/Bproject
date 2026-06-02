import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { generateCard } from './utils/bingoLogic';

const db = admin.firestore();

export const joinGame = functions.https.onCall(async (data, context) => {
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError('unauthenticated', 'Must be logged in.');

  const gameId = data.gameId;
  const gameRef = db.collection('games').doc(gameId);
  const gameSnap = await gameRef.get();
  if (!gameSnap.exists) throw new functions.https.HttpsError('not-found', 'Game not found.');
  const game = gameSnap.data();
  if (game?.status !== 'countdown' && game?.status !== 'waiting') {
    throw new functions.https.HttpsError('failed-precondition', 'Game not open.');
  }

  const userRef = db.collection('users').doc(uid);
  const userSnap = await userRef.get();
  const user = userSnap.data();
  if (!user) throw new functions.https.HttpsError('not-found', 'User not found.');
  if (user.accountStatus !== 'active') throw new functions.https.HttpsError('permission-denied', 'Account suspended.');

  const isFree = !user.hasClaimedWelcomeBonus;

  if (!isFree) {
    if (user.coins < game.cardPriceCoins) {
      throw new functions.https.HttpsError('resource-exhausted', 'Insufficient coins.');
    }
    await userRef.update({ coins: admin.firestore.FieldValue.increment(-game.cardPriceCoins) });
    // record transaction
    await db.collection('transactions').add({
      uid, type: 'purchase', amount: -game.cardPriceCoins, createdAt: admin.firestore.FieldValue.serverTimestamp(), gameId
    });
  } else {
    await userRef.update({ hasClaimedWelcomeBonus: true });
    await db.collection('transactions').add({
      uid, type: 'free_bonus', amount: 0, createdAt: admin.firestore.FieldValue.serverTimestamp(), gameId
    });
  }

  const { card, cardHash } = generateCard();
  await gameRef.collection('players').doc(uid).set({
    username: user.username,
    cardNumbers: card,
    cardHash,
    isFreeCard: isFree,
    daubedNumbers: [],
    isWinner: false,
    joinedAt: admin.firestore.FieldValue.serverTimestamp()
  });

  await gameRef.update({ playerCount: admin.firestore.FieldValue.increment(1) });

  return { cardNumbers: card, cardHash, isFreeCard: isFree };
});
