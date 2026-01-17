package com.example.menuku.repositori

import com.example.menuku.modeldata.Menu
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RepositoryMenu(
    private val firestore: FirebaseFirestore
) {

    private val menuCollection = firestore.collection("menus")

    /**
     * =========================
     * READ ALL (REALTIME)
     * =========================
     */
    fun getAllMenu(): Flow<List<Menu>> = callbackFlow {
        val listener = menuCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val menuList = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Menu::class.java)?.copy(
                    id = doc.id
                )
            } ?: emptyList()

            trySend(menuList).isSuccess
        }

        awaitClose { listener.remove() }
    }

    /**
     * =========================
     * CREATE
     * =========================
     */
    suspend fun insertMenu(menu: Menu) {
        val docRef = menuCollection.add(menu).await()

        // Simpan ID Firestore ke field id
        menuCollection.document(docRef.id)
            .update("id", docRef.id)
            .await()
    }

    /**
     * =========================
     * UPDATE
     * =========================
     */
    suspend fun updateMenu(menu: Menu) {
        require(menu.id.isNotEmpty()) {
            "Menu ID tidak boleh kosong saat update"
        }

        menuCollection.document(menu.id)
            .set(menu)
            .await()
    }

    /**
     * =========================
     * DELETE
     * =========================
     */
    suspend fun deleteMenu(menuId: String) {
        menuCollection.document(menuId)
            .delete()
            .await()
    }

    /**
     * =========================
     * READ BY ID (REALTIME)
     * =========================
     */
    fun getMenuById(menuId: String): Flow<Menu?> = callbackFlow {
        val listener = menuCollection.document(menuId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val menu = snapshot?.toObject(Menu::class.java)?.copy(
                    id = snapshot.id
                )

                trySend(menu).isSuccess
            }

        awaitClose { listener.remove() }
    }
}
