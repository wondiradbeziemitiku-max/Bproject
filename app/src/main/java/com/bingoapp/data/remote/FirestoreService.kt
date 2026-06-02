package com.bingoapp.data.remote

import com.bingoapp.data.model.DrawnNumber
import com.bingoapp.data.model.Game
import com.bingoapp.data.model.GameConfig
import com.bingoapp.data.model.PlayerInGame
import com.bingoapp.data.model.Transaction
import com.bingoapp.data.model.User
import com.bingoapp.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistrationimport com.google.firebase.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun usersCollection() = firestore.collection(Constants.COLLECTION_USERS)
    private fun gamesCollection() = firestore.collection(Constants.COLLECTION_GAMES)
    private fun transactionsCollection() = firestore.collection(Constants.COLLECTION_TRANSACTIONS)
    private fun configDoc() = firestore.collection(Constants.COLLECTION_CONFIG)
        .document(Constants.DOC_GLOBAL_CONFIG)

    // ─── USER OPERATIONS ───

    suspend fun createUser(uid: String, phoneNumber: String) {
        val user = User(phoneNumber = phoneNumber)
        usersCollection().document(uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val doc = usersCollection().document(uid).get().await()
        return doc.toObject<User>()
    }

    fun observeUser(uid: String): Flow<User?> = callbackFlow {
        val listener = usersCollection().document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject<User>())
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateUsername(uid: String, username: String) {
        usersCollection().document(uid).update("username", username).await()
    }

    suspend fun checkUsernameExists(username: String): Boolean {
        val result = usersCollection()
            .whereEqualTo("username", username)
            .limit(1)
            .get()
            .await()
        return !result.isEmpty
    }

    suspend fun updateFcmToken(uid: String, token: String) {
        usersCollection().document(uid).update("fcmToken", token).await()
    }

    suspend fun updateUserField(uid: String, field: String, value: Any) {
        usersCollection().document(uid).update(field, value).await()
    }

    // ─── GAME OPERATIONS ───

    fun observeCurrentGame(): Flow<Game?> = callbackFlow {
        val listener = gamesCollection()
            .whereIn("status", listOf("waiting", "countdown", "active"))
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val game = snapshot?.documents?.firstOrNull()?.toObject<Game>()
                trySend(game)
            }
        awaitClose { listener.remove() }
    }

    fun observeGame(gameId: String): Flow<Game?> = callbackFlow {
        val listener = gamesCollection().document(gameId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject<Game>())
            }
        awaitClose { listener.remove() }
    }

    fun observePlayerCount(gameId: String): Flow<Long> = callbackFlow {
        val listener = gamesCollection().document(gameId)
            .collection(Constants.SUBCOLLECTION_PLAYERS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size()?.toLong() ?: 0L)
            }
        awaitClose { listener.remove() }
    }

    fun observeDrawnNumbers(gameId: String): Flow<List<DrawnNumber>> = callbackFlow {
        val listener = gamesCollection().document(gameId)
            .collection(Constants.SUBCOLLECTION_DRAWN_NUMBERS)
            .orderBy("drawnOrder")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val numbers = snapshot?.documents?.mapNotNull { it.toObject<DrawnNumber>() } ?: emptyList()
                trySend(numbers)
            }
        awaitClose { listener.remove() }
    }

    fun observeMyCard(gameId: String, uid: String): Flow<PlayerInGame?> = callbackFlow {
        val listener = gamesCollection().document(gameId)
            .collection(Constants.SUBCOLLECTION_PLAYERS)
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject<PlayerInGame>())
            }
        awaitClose { listener.remove() }
    }

    // ─── CONFIG ───

    fun observeGameConfig(): Flow<GameConfig?> = callbackFlow {
        val listener = configDoc().addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject<GameConfig>())
        }
        awaitClose { listener.remove() }
    }

    // ─── TRANSACTIONS ───

    fun observeTransactions(uid: String, limit: Long): Flow<List<Transaction>> = callbackFlow {
        val listener = transactionsCollection()
            .whereEqualTo("uid", uid)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val txns = snapshot?.documents?.mapNotNull { it.toObject<Transaction>() } ?: emptyList()
                trySend(txns)
            }
        awaitClose { listener.remove() }
    }

    fun isAdmin(uid: String): Boolean {
        val adminUids = listOf("ADMIN_UID_PLACEHOLDER_1", "ADMIN_UID_PLACEHOLDER_2")
        return uid in adminUids
    }
}
