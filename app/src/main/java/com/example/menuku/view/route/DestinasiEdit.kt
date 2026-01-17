package com.example.menuku.view.route

object DestinasiEdit : DestinasiNavigasi {

    override val route = "edit_menu"
    override val title = "Edit Menu"

    const val MENU_ID = "menuId"

    fun routeWithId(menuId: String): String {
        return "$route?$MENU_ID=$menuId"
    }
}

