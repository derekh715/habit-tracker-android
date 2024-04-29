package edu.cuhk.csci3310.ui.habitDetail.barChart

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shape
import edu.cuhk.csci3310.ui.utils.rangeTo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val datetimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
private val bottomAxisValueFormatter =
    CartesianValueFormatter { x, _, _ ->
        LocalDate.ofYearDay(LocalDate.now().year, x.toInt()).format(
            datetimeFormatter
        )
    }

@Composable
fun BarChart(map: Map<LocalDate, Int>) {
    val endDate = remember { LocalDate.now().plusDays(1) }
    val startDate = remember { endDate.minusDays(50) }
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val range = (startDate..endDate)
    LaunchedEffect(key1 = map) {
        modelProducer.tryRunTransaction {
            columnSeries {
                series(
                    x = range.map { it.dayOfYear },
                    y = range.map {
                        if ((map[it] ?: 0) > 0) {
                            map[it]!!
                        } else {
                            0
                        }
                    }
                )
            }
        }
    }

    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    range.map {
                        rememberLineComponent(
                            thickness = 4.dp,
                            shape = Shape.rounded(1.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = bottomAxisValueFormatter
            ),
        ), modelProducer = modelProducer, scrollState = rememberVicoScrollState(
            initialScroll = Scroll.Absolute.End
        ),
        zoomState = rememberVicoZoomState(
            initialZoom = Zoom.x(5F)
        )
    )
}