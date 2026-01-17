package com.example.menuku.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menuku.modeldata.Menu
import com.example.menuku.repositori.RepositoryMenu
import com.example.menuku.view.route.DestinasiEdit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/* ================= HOME VIEWMODEL ================= */

class HomeViewModel(
    private val repository: RepositoryMenu
) : ViewModel() {
    private var allMenu: List<Menu> = emptyList()

    var statusUiMenu: StatusUiMenu by mutableStateOf(StatusUiMenu.Loading)
        private set

    init {
        loadMenu()
    }

    private fun loadMenu() {
        viewModelScope.launch {
            try {
                repository.getAllMenu().collect { menus ->
                    allMenu = menus
                    statusUiMenu = StatusUiMenu.Success(menus)
                }
            } catch (e: Exception) {
                statusUiMenu = StatusUiMenu.Error
            }
        }
    }

    // 🔍 SEARCH MENU (REALTIME, TANPA FIRESTORE QUERY)
    fun searchMenu(keyword: String) {
        val result = if (keyword.isBlank()) {
            allMenu
        } else {
            allMenu.filter { menu ->
                menu.name.contains(keyword, ignoreCase = true) ||
                        menu.category.contains(keyword, ignoreCase = true)
            }
        }

        statusUiMenu = StatusUiMenu.Success(result)
    }

    fun deleteMenu(menuId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMenu(menuId)
            } catch (e: Exception) {
                // optional: log / snackbar
            }
        }
    }
}

/* ================= EDIT VIEWMODEL ================= */

class EditMenuViewModel(
    private val repository: RepositoryMenu,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val menuId: String? =
        savedStateHandle[DestinasiEdit.MENU_ID]

    val menu: StateFlow<Menu?> =
        if (menuId.isNullOrBlank()) {
            MutableStateFlow(null)
        } else {
            repository.getMenuById(menuId)
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    null
                )
        }

    fun update(menu: Menu, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateMenu(menu)
            onSuccess()
        }
    }
}
