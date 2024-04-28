package edu.cuhk.csci3310.ui.addGroup

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.nav.Screen
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import edu.cuhk.csci3310.ui.utils.mutableStateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddGroupViewModel
@Inject
constructor(
    private val groupDao: GroupDao, application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(
    application,
) {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()
    private val prefillGroup = flow {
        emit(savedStateHandle.get<Long>(key = "groupId") ?: -1)
    }.flatMapLatest { id -> groupDao.getGroup(id) }

    val prefilledId = prefillGroup.map { it?.groupId }.stateIn(
        scope = viewModelScope,
        initialValue = null,
        started = SharingStarted.WhileSubscribed()
    )

    private fun nameInputInfo(group: Group?): TextInputInfo {
        return TextInputInfo(
            value = group?.name ?: "",
            label = "Group Name",
            placeholder = "Default Group",
            icon = Icons.Filled.Folder,
        )
    }

    private val _name = prefillGroup.map {
        nameInputInfo(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = nameInputInfo(null)
    )
    val name = _name.asStateFlow()

    private fun descriptionInputInfo(group: Group?): TextInputInfo {
        return TextInputInfo(
            value = group?.description ?: "",
            icon = Icons.Filled.Description,
            label = "Description",
            placeholder =
            "For ungrouped habits",
        )
    }

    private val _description = prefillGroup.map { descriptionInputInfo(it) }.mutableStateIn(
        scope = viewModelScope,
        initialValue = descriptionInputInfo(null)
    )
    val description = _description.asStateFlow()

    private val _colour = prefillGroup.map {
        it?.colour ?: Color.Blue.toArgb()
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = Color.Blue.toArgb()
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
            if (_name.value.value.isEmpty()) {
                sendEvent(CommonUiEvent.ShowToast("Group name is empty!"))
                return@launch
            }
            val group =
                Group(
                    name = _name.value.value,
                    description = _description.value.value,
                    colour = _colour.value,
                    groupId = prefilledId.value
                )
            groupDao.insertGroup(group)
            sendEvent(CommonUiEvent.Navigate(Screen.GroupList.route))
        }
    }

    fun onEvent(event: AddGroupEvent) {
        when (event) {
            is AddGroupEvent.AddGroup -> addGroup()
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiChannel.send(event)
        }
    }
}
