import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { startGame } from './startGame';
import { joinGame } from './joinGame';
import { drawNumber } from './drawNumber';
import { callBingo } from './callBingo';
import { endGame } from './endGame';
import { purchaseCoins } from './purchaseCoins';
import { adminStartGame, adminCancelGame } from './admin';

admin.initializeApp();

export const startGameFunction = functions.https.onCall(startGame);
export const joinGameFunction = functions.https.onCall(joinGame);
export const drawNumberFunction = functions.pubsub.schedule('every 6 seconds').onRun(drawNumber);
export const callBingoFunction = functions.https.onCall(callBingo);
export const endGameFunction = functions.https.onCall(endGame);
export const purchaseCoinsFunction = functions.https.onCall(purchaseCoins);
export const adminStartGameFunction = functions.https.onCall(adminStartGame);
export const adminCancelGameFunction = functions.https.onCall(adminCancelGame);
