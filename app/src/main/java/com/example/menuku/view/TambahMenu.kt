package com.example.menuku.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.menuku.repositori.RepositoryMenu
import com.example.menuku.view.viewmodel.TambahMenuViewModel
import com.example.menuku.view.viewmodel.TambahMenuViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMenuScreen(
    onBack: () -> Unit,
    repository: RepositoryMenu
) {
    val viewModel: TambahMenuViewModel = viewModel(
        factory = TambahMenuViewModelFactory(repository)
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setImage(it) }
    }
    val context = LocalContext.current
    val imageUri by viewModel.imageUri.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Menu") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ===== FOTO ===== */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { launcher.launch("image/*") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddCircle, contentDescription = null)
                            Text("Tambah Foto")
                        }
                    } else {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            /* ===== NAMA ===== */
            OutlinedTextField(
                value = viewModel.nama,
                onValueChange = { viewModel.nama = it },
                label = { Text("Nama Menu") },
                modifier = Modifier.fillMaxWidth()
            )

            /* ===== HARGA ===== */
            OutlinedTextField(
                value = viewModel.harga,
                onValueChange = { viewModel.harga = it },
                label = { Text("Harga") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            /* ===== KATEGORI ===== */
            ExposedDropdownMenuBox(
                expanded = viewModel.expanded,
                onExpandedChange = {
                    viewModel.expanded = !viewModel.expanded
                }
            ) {
                OutlinedTextField(
                    value = viewModel.kategori,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategori") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = viewModel.expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = viewModel.expanded,
                    onDismissRequest = { viewModel.expanded = false }
                ) {
                    listOf("Makanan", "Minuman").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                viewModel.kategori = it
                                viewModel.expanded = false
                            }
                        )
                    }
                }
            }

            /* ===== TERSEDIA ===== */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tersedia")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.tersedia,
                    onCheckedChange = { viewModel.tersedia = it }
                )
            }

            /* ===== SIMPAN ===== */
            Button(
                onClick = {
                    viewModel.simpanMenu(
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Menu berhasil ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBack()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simpan Menu")
            }
        }
    }
}
