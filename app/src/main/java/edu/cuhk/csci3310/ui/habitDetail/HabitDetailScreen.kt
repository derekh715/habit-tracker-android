package edu.cuhk.csci3310.ui.habitDetail

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import edu.cuhk.csci3310.ui.habitDetail.barChart.BarChart
import edu.cuhk.csci3310.ui.habitDetail.customHeatmap.MyHeatMapCalendar
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import java.io.OutputStream

fun getImageUri(bitmap: Bitmap, context: Context): Uri {
    val filename = "habit_tracker_temp_screenshot.jpg"
    var fos: OutputStream?
    var imageUri: Uri
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    //use application context to get contentResolver
    val contentResolver = context.contentResolver

    contentResolver.also { resolver ->
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        fos = resolver.openOutputStream(imageUri)
    }

    fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    contentResolver.update(imageUri, contentValues, null, null)

    return imageUri
}

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val habit = viewModel.habit.collectAsState(initial = null)
    val groups = viewModel.groups.collectAsState(initial = listOf())
    val map = viewModel.dateMap.collectAsState(initial = mutableMapOf())

    if (habit.value == null || groups.value == null) {
        return
    }
    val records = viewModel.records.collectAsState()
    val state = rememberUseCaseState(visible = false)
    val screenshotState = rememberScreenshotState()

    val imageResult = screenshotState.imageState.value
    val context = LocalContext.current

    // Show dialog only when ImageResult is success or error
    LaunchedEffect(key1 = imageResult) {
        if (imageResult is ImageResult.Success) {
            // share
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            val uri = getImageUri(imageResult.data, context)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            val chooser = Intent.createChooser(intent, "Share Image")
            startActivity(context, chooser, null)
        } else if (imageResult is ImageResult.Error) {
            Toast.makeText(
                context,
                "Could not take a picture of the statistics because of some unknown errors",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is HabitDetailScreenUiEvent.ShowAddToGroupDialog -> {
                    state.show()
                }

                is CommonUiEvent.Navigate -> {
                    navController.navigate(event.route)
                }

                is CommonUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    })

    ScreenshotBox(screenshotState = screenshotState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HabitHeader(habit = habit.value!!, takePicture = {
                screenshotState.capture()
            })
            Spacer(modifier = Modifier.height(16.dp))
            HabitDayEntries(habit = habit.value!!, records = records.value,
                changeStatus = { index, it ->
                    viewModel.onEvent(
                        HabitDetailEvent.ChangeRecordStatus(
                            index = index,
                            newStatus = it
                        )
                    )
                },
                changeTimes = { r, newTimes ->
                    viewModel.onEvent(
                        HabitDetailEvent.ChangeRecord(
                            r.copy(
                                times = newTimes
                            )
                        )
                    )
                },
                changeReason = { r, newReason ->
                    viewModel.onEvent(
                        HabitDetailEvent.ChangeRecord(
                            r.copy(
                                reason = newReason
                            )
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitGroupListing(
                groups = groups.value!!, state = state,
                onCreateDialog = {
                    viewModel.onEvent(HabitDetailEvent.AddToGroupDialog)
                },
                onSelect = { newlyAdded, newlyRemoved ->
                    viewModel.onEvent(
                        HabitDetailEvent.AddToGroup(
                            newlyAdded,
                            newlyRemoved
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            MyHeatMapCalendar(heatMap = map.value)
            Spacer(modifier = Modifier.height(16.dp))
            BarChart(map = map.value)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { viewModel.onEvent(HabitDetailEvent.ChangeHabit(habit.value!!)) }) {
                    Text(text = "Change Habit")
                }
                Button(onClick = { viewModel.onEvent(HabitDetailEvent.RemoveHabit(habit.value!!)) }) {
                    Text(text = "Delete Habit")
                }
            }
        }
    }

}
