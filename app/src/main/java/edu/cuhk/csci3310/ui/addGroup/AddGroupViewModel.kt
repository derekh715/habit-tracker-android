package edu.cuhk.csci3310.ui.addGroup

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
@HiltViewModel
class AddGroupViewModel
    @Inject
    constructor(private val groupDao: GroupDao, application: Application) : AndroidViewModel(
            application,
        ) {
        private val _name =
            MutableStateFlow(
                TextInputInfo(
                    value = "Test",
                    label = "Group Name",
                    placeholder = "Default Group",
                    icon = Icons.Filled.Folder,
                ),
            )
        val name = _name.asStateFlow()

        private val _description =
            MutableStateFlow(
                TextInputInfo(
                    value = "Test Description",
                    icon = Icons.Filled.Description,
                    label = "Description",
                    placeholder =
                        "For ungrouped habits",
                ),
            )
        val description = _description.asStateFlow()

        // colour picker doesn't use Color type, it uses int with ARGB format
        private val _colour =
            MutableStateFlow(
                Color.Blue.toArgb(),
            )
        val colour = _colour.asStateFlow()

        fun changeName(newName: String) {
            viewModelScope.launch {
                _name.emit(
                    _name.value.copy(
                        value = newName,
                    ),
                )
            }
        }

        fun changeDescription(newDesc: String) {
            viewModelScope.launch {
                _description.emit(
                    _description.value.copy(
                        value = newDesc,
                    ),
                )
            }
        }

        fun changeColour(newColour: Int) {
            viewModelScope.launch {
                _colour.emit(
                    newColour,
                )
            }
        }

        private fun addGroup() {
            viewModelScope.launch {
                val group =
                    Group(
                        name = _name.value.value,
                        description = _description.value.value,
                        colour = _colour.value.toString(),
                    )
                groupDao.insertGroup(group)
            }
        }

        fun onEvent(event: AddGroupEvent) {
            when (event) {
                is AddGroupEvent.AddGroup -> addGroup()
            }
        }
    }
