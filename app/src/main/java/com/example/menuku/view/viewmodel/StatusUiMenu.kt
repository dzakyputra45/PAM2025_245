package com.example.menuku.view.viewmodel

import com.example.menuku.modeldata.Menu

sealed interface StatusUiMenu {
    object Loading : StatusUiMenu
    data class Success(val menu: List<Menu>) : StatusUiMenu
    object Error : StatusUiMenu
}
