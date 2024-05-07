package edu.cuhk.csci3310.ui.utils

// base class for all ui events
abstract class UiEvent

sealed class CommonUiEvent : UiEvent() {
    //    data class ShowSnackBar(val content: String, val action: String) : UiEvent()
    data class ShowToast(val content: String) : UiEvent()

    data class Navigate(val route: String) : UiEvent()
    data object NavigateBack : UiEvent()
}
