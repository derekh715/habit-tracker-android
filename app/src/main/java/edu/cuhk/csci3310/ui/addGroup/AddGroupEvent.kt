package edu.cuhk.csci3310.ui.addGroup

sealed class AddGroupEvent {
    data object AddGroup : AddGroupEvent()
}
