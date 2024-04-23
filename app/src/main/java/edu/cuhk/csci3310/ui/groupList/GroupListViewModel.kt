package edu.cuhk.csci3310.ui.groupList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.GroupDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel
    @Inject
    constructor(
        private val groupDao: GroupDao,
    ) : ViewModel() {
        fun onEvent(event: GroupListEvent) {
            when (event) {
                is GroupListEvent.AddGroup -> {
                    insertGroup(event.group)
                }
                is GroupListEvent.ChangeGroup -> {
                }
                is GroupListEvent.RemoveGroup -> {
                    removeGroup(event.group)
                }
                is GroupListEvent.AddDummyGroup -> {
                }
                is GroupListEvent.AddHabitToGroup -> {
                }
            }
        }

        val groupList = groupDao.getGroups()
        private var lastDeletedGroup: Group? = null

        private fun insertGroup(group: Group) {
            viewModelScope.launch {
                groupDao.insertGroup(group)
                // TODO: add ui event channel
            }
        }

        private fun removeGroup(group: Group) {
            viewModelScope.launch {
                lastDeletedGroup = group
                groupDao.deleteGroup(group)
            }
        }
    }
