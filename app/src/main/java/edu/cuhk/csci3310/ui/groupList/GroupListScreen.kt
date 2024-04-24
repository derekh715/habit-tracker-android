package edu.cuhk.csci3310.ui.groupList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.cuhk.csci3310.ui.nav.Screen

@Composable
fun GroupListScreen(
    viewModel: GroupListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val groups = viewModel.groupList.collectAsState(initial = listOf())
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(groups.value) { group ->
                GroupItem(
                    group = group,
                    deleteGroup = { viewModel.onEvent(GroupListEvent.RemoveGroup(it)) })
            }
        }
        Button(onClick = {
            navController.navigate(Screen.AddGroup.route)
        }, modifier = Modifier
            .align(Alignment.End)
            .padding(end = 8.dp, bottom = 8.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Add New Group")
            Text(text = "Add New Group")
        }
    }
}
