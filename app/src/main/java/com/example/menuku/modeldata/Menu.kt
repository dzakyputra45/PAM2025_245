package com.example.menuku.modeldata


data class Menu(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val category: String = "",
    val available: Boolean = true,
    val photo: String = ""
)
