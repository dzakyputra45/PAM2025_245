package com.example.menuku.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.menuku.modeldata.Menu
import com.example.menuku.view.component.DetailMenuDialog
import com.example.menuku.view.route.DestinasiEdit
import com.example.menuku.view.route.DestinasiEntry
import com.example.menuku.view.route.DestinasiHome
import com.example.menuku.view.viewmodel.HomeViewModel
import com.example.menuku.view.viewmodel.PenyediaViewModel
import com.example.menuku.view.viewmodel.StatusUiMenu

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = PenyediaViewModel.Factory
    )
) {
    var showAddMenu by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf<Menu?>(null) }
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(searchText) {
        kotlinx.coroutines.delay(300) // 🔥 debounce
        viewModel.searchMenu(searchText)
    }

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
    val uiState = viewModel.statusUiMenu

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        /* ================= HEADER ================= */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color(0xFFB86752))
                .padding(20.dp)
        ) {
            Text(
                text = "Dashboard",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Hi Selamat Datang Manager Ku",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    viewModel.searchMenu(text) // 🔍 SEARCH AKTIF
                },
                placeholder = { Text("Cari Menu Disini") },
                trailingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

        }

        /* ================= CONTENT ================= */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Daftar Menu",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDDDDDD))
                        .padding(horizontal = 28.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (uiState) {

                is StatusUiMenu.Loading -> {
                    CircularProgressIndicator()
                }

                is StatusUiMenu.Error -> {
                    Text("Gagal memuat menu")
                }

                is StatusUiMenu.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.menu) { menu ->
                            MenuCard(
                                menu = menu,
                                onClick = { selectedMenu = menu }
                            )
                        }
                    }
                }
            }
        }

        /* ================= BOTTOM BAR ================= */
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFFFA559))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    BottomIconWithIndicator(
                        icon = Icons.Default.Menu,
                        isActive = currentRoute == DestinasiHome.route,
                        onClick = {
                            navController.navigate(DestinasiHome.route) {
                                popUpTo(DestinasiHome.route) { inclusive = true }
                            }
                        }
                    )

                    Button(
                        onClick = {
                            navController.navigate(DestinasiEntry.route)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            "Tambah Menu +",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }

                    BottomIconWithIndicator(
                        icon = Icons.Default.Edit,
                        isActive = currentRoute?.startsWith(DestinasiEdit.route) == true,
                        onClick = {
                            // optional: kalau mau klik icon edit
                        }
                    )

                }
            }
        }
    }

    /* ================= DETAIL POPUP ================= */
    selectedMenu?.let { menu ->
        DetailMenuDialog(
            menu = menu,
            onDismiss = {
                selectedMenu = null
            },
            onEdit = { selected ->
                navController.navigate(
                    DestinasiEdit.routeWithId(selected.id)
                )
                selectedMenu = null
            },
            onDelete = { selected ->
                viewModel.deleteMenu(selected.id)
                selectedMenu = null
            }
        )
    }
}


@Composable
fun BottomIconWithIndicator(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isActive) Color.Black else Color.Black.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 🔥 OUTLINE / INDICATOR
        if (isActive) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Black)
            )
        }
    }
}



    /* ================= MENU CARD ================= */
@Composable
fun MenuCard(
    menu: Menu,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3EEF9)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = menu.photo,
                contentDescription = menu.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                    Text(
                        text = menu.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Rp ${menu.price}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2962FF)
                    )
                }

                StatusBadge(available = menu.available)
            }
        }
    }
}


/* ===== STATUS BADGE ===== */
@Composable
private fun StatusBadge(available: Boolean) {
    val bg = if (available) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (available) Color(0xFF2E7D32) else Color(0xFFC62828)
    val text = if (available) "Tersedia" else "Habis"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}
