package edu.cuhk.csci3310.ui.nav

sealed class Graph(
    val name: String,
) {
    data object Habits : Graph("habits")
    data object Groups : Graph("groups")
}
