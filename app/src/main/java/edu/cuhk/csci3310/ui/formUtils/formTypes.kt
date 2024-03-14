package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.ui.graphics.vector.ImageVector

data class ToggleableInfo(
    var toggled: Boolean,
    val text: String,
) {
    companion object {
        fun update(
            list: List<ToggleableInfo>,
            selected: String,
        ): List<ToggleableInfo> {
            return list.map {
                ToggleableInfo(
                    toggled = selected == it.text,
                    text = it.text,
                )
            }
        }

        fun update(
            list: MutableList<ToggleableInfo>,
            selected: String,
        ) {
            list.replaceAll {
                ToggleableInfo(
                    toggled = selected == it.text,
                    text = it.text,
                )
            }
        }
    }
}

data class TextInputInfo(
    val value: String = "",
    val errorMessage: String = "",
    val showError: Boolean = false,
    val label: String = "",
    val placeholder: String = "",
    val icon: ImageVector? = null,
    val contentDescription: String = "icon",
    val isNumberInput: Boolean = false,
)
