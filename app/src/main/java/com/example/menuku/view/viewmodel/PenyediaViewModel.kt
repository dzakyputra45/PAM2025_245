package com.example.menuku.view.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.menuku.repositori.MenukuApplication

fun CreationExtras.menukuApplication(): MenukuApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
            as MenukuApplication

object PenyediaViewModel {

    val Factory = viewModelFactory {


        initializer {
            HomeViewModel(
                menukuApplication().container.repositoryMenu
            )
        }


        initializer {
            EditMenuViewModel(
                menukuApplication().container.repositoryMenu,
                this.createSavedStateHandle()
            )
        }
        initializer {
            TambahMenuViewModel(
                menukuApplication().container.repositoryMenu
            )
        }
    }
}
