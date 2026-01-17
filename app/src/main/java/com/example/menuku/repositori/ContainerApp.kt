package com.example.menuku.repositori

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore

class ContainerApp {

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val repositoryMenu: RepositoryMenu by lazy {
        RepositoryMenu(firestore)
    }
}


class MenukuApplication : Application() {

    lateinit var container: ContainerApp
        private set

    override fun onCreate() {
        super.onCreate()
        container = ContainerApp()
    }
}
