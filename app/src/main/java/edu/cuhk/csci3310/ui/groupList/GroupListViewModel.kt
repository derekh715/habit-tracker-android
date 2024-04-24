package edu.cuhk.csci3310.ui.groupList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.ui.nav.Screen
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel
@Inject
constructor(
    private val groupDao: GroupDao,
) : ViewModel() {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()

    fun onEvent(event: GroupListEvent) {
        when (event) {
            is GroupListEvent.AddGroup -> {
                insertGroup(event.group)
            }

            is GroupListEvent.ChangeGroup -> {
                changeGroup(event.group)
            }

            is GroupListEvent.RemoveGroup -> {
                removeGroup(event.group)
            }
        }
    }

    val groupList = groupDao.getGroups()
    private var lastDeletedGroup: Group? = null

    private fun insertGroup(group: Group) {
        viewModelScope.launch {
            groupDao.insertGroup(group)
        }
    }

    private fun removeGroup(group: Group) {
        viewModelScope.launch {
            lastDeletedGroup = group
            groupDao.deleteGroup(group)
        }
    }

    private fun changeGroup(group: Group) {
        sendEvent(CommonUiEvent.Navigate(Screen.AddGroup.route + "?groupId=${group.groupId}"))
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiChannel.send(event)
        }
    }
}
