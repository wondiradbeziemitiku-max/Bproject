import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

export const purchaseCoins = functions.https.onCall(async (data, context) => {
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError('unauthenticated', 'Must be logged in.');
  const { productId, purchaseToken } = data;
  // Verify purchase with Google Play Developer API (omitted for brevity)
  const coinAmounts: {[key: string]: number} = {
    'coin_pack_100': 100,
    'coin_pack_500': 500,
    'coin_pack_1000': 1000,
    'coin_pack_5000': 5000
  };
  const coins = coinAmounts[productId] || 0;
  if (coins === 0) throw new functions.https.HttpsError('invalid-argument', 'Invalid product.');
  await admin.firestore().collection('users').doc(uid).update({
    coins: admin.firestore.FieldValue.increment(coins)
  });
  await admin.firestore().collection('transactions').add({
    uid, type: 'coin_pack', amount: coins, createdAt: admin.firestore.FieldValue.serverTimestamp()
  });
  const userSnap = await admin.firestore().collection('users').doc(uid).get();
  return { newBalance: userSnap.data()?.coins };
});
