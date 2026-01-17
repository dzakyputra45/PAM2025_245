package com.example.menuku.view.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.menuku.modeldata.Menu
import com.example.menuku.repositori.RepositoryMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TambahMenuViewModel(
    private val repository: RepositoryMenu
) : ViewModel() {

    // ===== FORM STATE =====
    var nama by mutableStateOf("")
    var harga by mutableStateOf("")          // STRING (WAJIB)
    var kategori by mutableStateOf("Makanan")
    var tersedia by mutableStateOf(true)
    var expanded by mutableStateOf(false)

    // ===== IMAGE =====
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    fun setImage(uri: Uri) {
        _imageUri.value = uri
    }

    // ===== SAVE =====
    fun simpanMenu(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nama.isBlank() || harga.isBlank() || _imageUri.value == null) {
            onError("Semua field wajib diisi")
            return
        }

        val menu = Menu(
            name = nama,
            price = harga.toInt(),
            category = kategori,
            available = tersedia,
            photo = _imageUri.value.toString()
        )


        viewModelScope.launch {
            try {
                repository.insertMenu(menu)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal menyimpan menu")
            }
        }
    }
}

class TambahMenuViewModelFactory(
    private val repository: RepositoryMenu
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TambahMenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TambahMenuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
