package com.example.menuku.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menuku.view.viewmodel.EditMenuViewModel
import com.example.menuku.view.viewmodel.PenyediaViewModel

@Composable
fun EditMenuScreen(
    onBack: () -> Unit,
    viewModel: EditMenuViewModel = viewModel(
        factory = PenyediaViewModel.Factory
    )
) {

    val menu by viewModel.menu.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var available by rememberSaveable { mutableStateOf(true) }

    // 🔥 FLAG: isi data HANYA SEKALI
    var initialized by rememberSaveable { mutableStateOf(false) }

    if (menu != null && !initialized) {
        val m = menu!!
        name = m.name
        price = m.price.toString()
        category = m.category
        available = m.available
        initialized = true
    }

    if (menu == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text("Edit Menu", fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Menu") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Harga") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = available,
                onCheckedChange = { available = it }
            )
            Text("Tersedia")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val hargaInt = price.toIntOrNull() ?: return@Button

                viewModel.update(
                    menu!!.copy(
                        name = name,
                        price = hargaInt,
                        category = category,
                        available = available
                    )
                ) {
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Perubahan")
        }
    }
}
