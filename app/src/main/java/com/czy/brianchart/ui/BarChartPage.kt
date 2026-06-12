package com.czy.brianchart.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.chart.compose.view.chart.Axis
import com.brian.chart.compose.view.chart.BarChart
import com.brian.chart.compose.view.chart.BarChartData
import com.brian.chart.compose.view.chart.BarData
import com.brian.chart.compose.view.chart.BarDataSet
import com.brian.chart.compose.view.chart.BarEntry
import com.brian.chart.compose.view.chart.Chunk
import com.brian.chart.compose.view.chart.LimitLine
import com.czy.brianchart.ui.components.TopBar
import com.czy.brianchart.ui.navigation.ChartNavigationActions
import com.czy.brianchart.ui.theme.BrianChartTheme
import java.text.SimpleDateFormat
import kotlin.math.abs

@Composable
fun BarChartPage(navigationActions: ChartNavigationActions? = null) {
    val barChartViewModel: BarChartViewModel = viewModel()
    val barChartUIState by barChartViewModel.barChartUIState.collectAsStateWithLifecycle()
    BarChartView(barChartUIState = barChartUIState, modifier = Modifier.fillMaxSize(), backClick = {
        navigationActions?.navigateBack()
    })
}

@Composable
fun BarChartView(modifier: Modifier, barChartUIState: BarChartUIState, backClick: () -> Unit?) {
    Surface(modifier = modifier) {
        Column(Modifier.fillMaxSize()) {
            TopBar(modifier = Modifier.fillMaxWidth().padding(top = 28.dp).height(48.dp), title = "BarChart") { backClick?.invoke() }
            HorizontalDivider(thickness = 1.dp)
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                BarChart1(modifier = Modifier.fillMaxWidth().height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart2(modifier = Modifier.fillMaxWidth().height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart3(modifier = Modifier.fillMaxWidth().height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart4(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp).height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart5(modifier = Modifier.fillMaxWidth().height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart6(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp).height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChart7(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp).height(200.dp))
                HorizontalDivider(thickness = 8.dp)
                BarChartWithTouch(modifier = Modifier.padding(bottom = 20.dp).height(300.dp))
            }
        }
    }
}

@Composable
fun BarChart1(modifier: Modifier) {
    val xLimitLineList = getBarTestXLimitLineList()
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1))),
        xAxis = Axis(max = 5f, labelInterval = 10f, scaleInterval = 10f, limitLineList = xLimitLineList, name = "x轴", settingLabelValue = ::settingLabelValue),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "y轴", limitLineList = xLimitLineList)))
}

@Composable
fun BarChart2(modifier: Modifier) {
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, valueColor = Color.Red, name = "CHO", settingValueText = ::settingValueText2),
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f), BarEntry(2f, 250f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText2))),
        xAxis = Axis(max = 5f, name = ""),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "")))
}

@Composable
fun BarChart3(modifier: Modifier) {
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, name = "CHO", settingValueText = ::settingValueText),
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f), BarEntry(2f, 250f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText),
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 50f), BarEntry(2f, 150f)), color = Color.Gray, background = background3, name = "PRO", settingValueText = ::settingValueText))),
        xAxis = Axis(max = 2.9f, scaleInterval = 1f, labelInterval = 1f, name = "", settingLabelValue = ::settingLabelValue2),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", settingLabelValue = ::settingLabelValue)))
}

@Composable
fun BarChart4(modifier: Modifier) {
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f)), color = Color.Gray, background = background1, name = "CHO", settingValueText = ::settingValueText3),
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText3),
            BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 50f)), color = Color.Gray, background = background3, name = "PRO", settingValueText = ::settingValueText3)),
            width = 40.dp, dataSetPadding = 40.dp, weight = 1f),
        xAxis = Axis(max = 3f, scaleInterval = 1f, labelInterval = 1f, name = "", settingLabelValue = ::settingLabelValue2),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", settingLabelValue = ::settingLabelValue)))
}

@Composable
fun BarChart5(modifier: Modifier) {
    val xLimitLineList = getBarTestXLimitLineList()
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, showValue = false))),
        xAxis = Axis(max = 5f, scaleInterval = 10f, labelInterval = 10f, limitLineList = xLimitLineList, name = "x轴", settingLabelValue = ::settingLabelValue),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "y轴", limitLineList = xLimitLineList)))
}

@Composable
fun BarChart6(modifier: Modifier) {
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, showValue = false))),
        xAxis = Axis(max = 5f, isDrawAxis = false, isDrawLabel = false),
        yLeftAxis = Axis(max = 200f, isDrawAxis = false, isDrawLabel = false)))
}

@Composable
fun BarChart7(modifier: Modifier) {
    BarChart(modifier = modifier, data = BarChartData(
        barData = BarData(barDataSetList = mutableListOf(
            BarDataSet(barEntryList = mutableListOf(
                BarEntry(1f, 100f, stackValues = listOf(50f, 30f, 20f)),
                BarEntry(2f, 170f, stackValues = listOf(80f, 50f, 40f)),
                BarEntry(3f, 160f, stackValues = listOf(60f, 70f, 30f)),
                BarEntry(4f, 190f, stackValues = listOf(90f, 40f, 60f))),
                color = Color.Blue, background = background2, showValue = true, valueColor = Color.White, name = "销售额",
                stackColors = listOf(Color.Blue.copy(alpha = 0.8f), Color.Red.copy(alpha = 0.8f), Color.Yellow.copy(alpha = 0.8f)),
                stackValueColors = listOf(Color.White, Color.White, Color.Black),
                settingValueText = { _, value -> "${value.toInt()}" }),
            BarDataSet(barEntryList = mutableListOf(
                BarEntry(1f, 10f, stackValues = listOf(40f, 20f, 10f)),
                BarEntry(2f, 15f, stackValues = listOf(60f, 30f, 15f)),
                BarEntry(3f, -10f, stackValues = listOf(50f, 40f, 20f)),
                BarEntry(4f, 30f, stackValues = listOf(-20f, -25f, -15f))),
                color = Color.Green, background = background2, showValue = true, valueColor = Color.White, name = "利润",
                stackColors = listOf(Color(0xFF4CAF50).copy(alpha = 0.8f), Color(0xFFF44336).copy(alpha = 0.8f), Color(0xFFFF9800).copy(alpha = 0.8f)),
                stackValueColors = listOf(Color.White, Color.White, Color.White),
                settingValueText = { _, value -> "${value.toInt()}" })),
            width = 80.dp),
        xAxis = Axis(max = 5f, scaleInterval = 1f, labelInterval = 1f, position = 0f, name = "月份"),
        yLeftAxis = Axis(max = 200f, min = -50f, scaleInterval = 50f, labelInterval = 50f, name = "金额")))
}

val background1: ((DrawScope, Color, Offset, Size) -> Unit) = { ds, color, offset, size ->
    ds.run { drawRoundRect(color = color, topLeft = offset, size = size, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round), cornerRadius = CornerRadius(2f, 2f)) }
}

val background2: ((DrawScope, Color, Offset, Size) -> Unit) = { ds, color, offset, size ->
    ds.run {
        drawRoundRect(color = color, topLeft = offset, size = size, cornerRadius = CornerRadius(2f, 2f))
        drawRoundRect(color = color, topLeft = offset, size = size, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round), cornerRadius = CornerRadius(2f, 2f))
    }
}

val background3: ((DrawScope, Color, Offset, Size) -> Unit) = { ds, color, offset, size ->
    ds.run {
        val width = 2.dp.toPx()
        drawRoundRect(color = color, topLeft = offset, size = size, style = Stroke(width = width, cap = StrokeCap.Round), cornerRadius = CornerRadius(2f, 2f))
        var heighTemp = size.height
        val coefficient = if (heighTemp > 0) 1 else -1
        for (i in 0..abs(heighTemp).toInt() step 20) {
            drawLine(start = Offset(x = offset.x, y = offset.y + i * coefficient), end = Offset(x = offset.x + size.width, y = offset.y + i * coefficient), color = color, strokeWidth = width)
        }
    }
}

fun settingValueText(name: String, value: Float) = if (name.isNullOrEmpty()) "$${value}" else "${name}:${value}"
fun settingValueText2(name: String, value: Float) = if (name.isNullOrEmpty()) "$${value}" else "${name}=${value}"
fun settingValueText3(name: String, value: Float) = if (name.isNullOrEmpty()) "${value}" else "${name}\n${value}"

fun settingLabelValue(value: Float): String {
    return when { value.toInt().toFloat() == value -> "${value.toInt()}"; else -> "${value}" }
}

fun settingLabelValue2(value: Float): String {
    val time = System.currentTimeMillis()
    var dateFormatYMD = "yyyy-MM-dd"
    return if (value > 0) getStringByFormat((time + value * 24 * 60 * 60 * 1000).toLong(), dateFormatYMD).toString() else ""
}

fun getStringByFormat(milliseconds: Long, format: String?): String? {
    return try { SimpleDateFormat(format).format(milliseconds) } catch (e: Exception) { e.printStackTrace(); null }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 1096, heightDp = 250)
fun BarChartPreview4() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f)), color = Color.Gray, background = background1, name = "CHO", settingValueText = ::settingValueText3),
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText3),
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 50f)), color = Color.Gray, background = background3, name = "PRO", settingValueText = ::settingValueText3)),
                width = 40.dp, dataSetPadding = 40.dp, weight = 1f),
            xAxis = Axis(max = 3f, scaleInterval = 1f, labelInterval = 1f, name = "", settingLabelValue = ::settingLabelValue2),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", settingLabelValue = ::settingLabelValue)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartPreview3() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, name = "CHO", settingValueText = ::settingValueText),
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f), BarEntry(2f, 250f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText),
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 50f), BarEntry(2f, 150f)), color = Color.Gray, background = background3, name = "PRO", settingValueText = ::settingValueText))),
            xAxis = Axis(max = 2.9f, scaleInterval = 1f, labelInterval = 1f, name = "", settingLabelValue = ::settingLabelValue2),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", settingLabelValue = ::settingLabelValue)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartPreview2() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, valueColor = Color.Red, background = background1, name = "CHO", settingValueText = ::settingValueText2),
                BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 80f), BarEntry(2f, 250f)), color = Color.Gray, background = background2, name = "FAT", settingValueText = ::settingValueText2))),
            xAxis = Axis(max = 5f, name = ""),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartPreview1() {
    MaterialTheme { Surface {
        val xLimitLineList = getBarTestXLimitLineList()
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1))),
            xAxis = Axis(max = 5f, scaleInterval = 10f, labelInterval = 10f, limitLineList = xLimitLineList, name = "x轴", settingLabelValue = ::settingLabelValue),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "y轴", limitLineList = xLimitLineList)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartPreviewNoValue() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, -100f)), color = Color.Gray, background = background1, showValue = false))),
            xAxis = Axis(max = 5f, scaleInterval = 10f, labelInterval = 20f, position = 0f, name = "x轴"),
            yLeftAxis = Axis(max = 200f, min = -200f, scaleInterval = 10f, labelInterval = 100f, name = "y轴")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartPreviewNoValueNoAxis() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(BarEntry(1f, 60f), BarEntry(2f, 200f)), color = Color.Gray, background = background1, showValue = false))),
            xAxis = Axis(max = 5f, isDrawAxis = false, isDrawLabel = false),
            yLeftAxis = Axis(max = 200f, isDrawAxis = false, isDrawLabel = false)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 300)
fun BarChartPreviewRenderer() {
    val density = androidx.compose.ui.platform.LocalDensity.current
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf<BarDataSet>(BarDataSet(barEntryList = mutableListOf(
                BarEntry(1f, 150f, renderer = { ds, color, offset, size, value, name, valueRelativeToXAxis ->
                    with(density) {
                        ds.drawRoundRect(color = Color.Blue.copy(alpha = 0.7f), topLeft = offset, size = size, cornerRadius = CornerRadius(8f, 8f))
                        ds.drawCircle(color = Color.Blue, radius = 8f, center = Offset(offset.x + size.width / 2, offset.y))
                        val label = "${value.toInt()}"; val ts = 10.sp.toPx()
                        val p = android.graphics.Paint().apply { textSize = ts; setColor(Color.White.toArgb()); isAntiAlias = true }
                        ds.drawContext.canvas.nativeCanvas.drawText(label, offset.x + size.width / 2 - (label.length * ts) / 4, offset.y + size.height / 2 + ts / 3, p)
                    }
                }),
                BarEntry(2f, 100f),
                BarEntry(3f, -80f, renderer = { ds, color, offset, size, value, name, valueRelativeToXAxis ->
                    with(density) {
                        ds.drawRoundRect(color = Color.Red.copy(alpha = 0.6f), topLeft = offset, size = size, cornerRadius = CornerRadius(4f, 4f))
                        val label = "${value.toInt()}"; val ts = 10.sp.toPx()
                        val p = android.graphics.Paint().apply { textSize = ts; setColor(Color.Red.toArgb()); isAntiAlias = true }
                        ds.drawContext.canvas.nativeCanvas.drawText(label, offset.x + size.width / 2 - (label.length * ts) / 4, offset.y + size.height + ts + 4f, p)
                    }
                })), color = Color.Gray, showValue = true)), width = 60.dp),
            xAxis = Axis(max = 5f, scaleInterval = 1f, labelInterval = 1f, position = 0f, name = "X轴"),
            yLeftAxis = Axis(max = 200f, min = -200f, scaleInterval = 10f, labelInterval = 100f, name = "Y轴")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 300)
fun BarChartPreviewStacked() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(
                BarEntry(1f, 100f, stackValues = listOf(50f, 30f, 20f)),
                BarEntry(2f, 170f, stackValues = listOf(80f, 50f, 40f)),
                BarEntry(3f, 160f, stackValues = listOf(60f, 70f, 30f)),
                BarEntry(4f, 190f, stackValues = listOf(90f, 40f, 60f))),
                color = Color.Blue, background = background2, showValue = true, valueColor = Color.White, name = "销售额",
                stackColors = listOf(Color.Blue.copy(alpha = 0.8f), Color.Red.copy(alpha = 0.8f), Color.Yellow.copy(alpha = 0.8f)),
                stackValueColors = listOf(Color.White, Color.White, Color.Black),
                settingValueText = { _, value -> "${value.toInt()}" }),
                BarDataSet(barEntryList = mutableListOf(
                    BarEntry(1f, 10f, stackValues = listOf(40f, 20f, 10f)),
                    BarEntry(2f, 15f, stackValues = listOf(60f, 30f, 15f)),
                    BarEntry(3f, -10f, stackValues = listOf(50f, 40f, 20f)),
                    BarEntry(4f, 30f, stackValues = listOf(-20f, -25f, -15f))),
                    color = Color.Green, background = background2, showValue = true, valueColor = Color.White, name = "利润",
                    stackColors = listOf(Color(0xFF4CAF50).copy(alpha = 0.8f), Color(0xFFF44336).copy(alpha = 0.8f), Color(0xFFFF9800).copy(alpha = 0.8f)),
                    stackValueColors = listOf(Color.White, Color.White, Color.White),
                    settingValueText = { _, value -> "${value.toInt()}" })),
                width = 80.dp),
            xAxis = Axis(max = 5f, scaleInterval = 1f, labelInterval = 1f, position = 0f, name = "月份"),
            yLeftAxis = Axis(max = 200f, min = -100f, scaleInterval = 50f, labelInterval = 50f, name = "金额")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 300)
fun BarChartPreviewStackedWithCustomRenderer() {
    MaterialTheme { Surface {
        BarChart(data = BarChartData(
            barData = BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = mutableListOf(
                BarEntry(1f, 100f, stackValues = listOf(50f, 30f, 20f), stackRenderer = { ds, color, offset, size, value, name, valueRelativeToXAxis, stackIndex ->
                    with(ds) {
                        val c = when (stackIndex) { 0 -> Color(0xFF2196F3); 1 -> Color(0xFF4CAF50); else -> Color(0xFFFF9800) }
                        val r = when (stackIndex) { 0 -> 12f; 1 -> 0f; else -> 6f }
                        drawRoundRect(color = c, topLeft = offset, size = size, cornerRadius = CornerRadius(r, r))
                        val label = "${value.toInt()}"; val ts = 10.sp.toPx()
                        val p = android.graphics.Paint().apply { textSize = ts; setColor(Color.White.toArgb()); isAntiAlias = true; textAlign = android.graphics.Paint.Align.CENTER }
                        drawContext.canvas.nativeCanvas.drawText(label, offset.x + size.width / 2, offset.y + size.height / 2 + ts / 3, p)
                    }
                }),
                BarEntry(2f, 150f, stackValues = listOf(60f, 50f, 40f)),
                BarEntry(3f, 120f, stackValues = listOf(40f, 40f, 40f), stackRenderer = { ds, color, offset, size, value, name, valueRelativeToXAxis, stackIndex ->
                    with(ds) {
                        val c = when (stackIndex) { 0 -> Color(0xFF9C27B0); 1 -> Color(0xFFE91E63); else -> Color(0xFFF44336) }
                        drawRoundRect(color = c, topLeft = offset, size = size, cornerRadius = CornerRadius(4f, 4f))
                        drawRoundRect(color = Color.White.copy(alpha = 0.5f), topLeft = offset, size = size, style = Stroke(width = 2f), cornerRadius = CornerRadius(4f, 4f))
                        val label = "${value.toInt()}"; val ts = 10.sp.toPx()
                        val p = android.graphics.Paint().apply { textSize = ts; setColor(Color.White.toArgb()); isAntiAlias = true; textAlign = android.graphics.Paint.Align.CENTER }
                        drawContext.canvas.nativeCanvas.drawText(label, offset.x + size.width / 2, offset.y + size.height / 2 + ts / 3, p)
                    }
                })), color = Color.Blue, showValue = false, name = "自定义堆积", settingValueText = { _, value -> "${value.toInt()}" })), width = 80.dp),
            xAxis = Axis(max = 5f, scaleInterval = 1f, labelInterval = 1f, name = "X轴"),
            yLeftAxis = Axis(max = 200f, scaleInterval = 50f, labelInterval = 50f, name = "Y轴")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun BarChartWithTouchPreview() {
    BrianChartTheme { Surface {
        BarChartWithTouch(modifier = Modifier.padding(bottom = 20.dp).height(300.dp))
    } }
}

@Composable
fun BarChartWithTouch(modifier: Modifier) {
    val barEntryList = mutableListOf(
        BarEntry(1f, 20f), BarEntry(2f, 10f), BarEntry(3f, 30f), BarEntry(4f, 20f),
        BarEntry(5f, 20f), BarEntry(6f, 50f), BarEntry(7f, 20f), BarEntry(8f, 20f), BarEntry(9f, 20f))

    var barData by remember {
        mutableStateOf(BarData(barDataSetList = mutableListOf(BarDataSet(barEntryList = barEntryList))))
    }
    var xAxis by remember {
        mutableStateOf(Axis(max = 10f, min = 0f, scaleInterval = 20f, labelInterval = 50f, name = "时间 (s)", limitLineList = mutableListOf()))
    }
    var yLeftAxis by remember {
        mutableStateOf(Axis(max = 50f, min = 0f, scaleInterval = 25f, labelInterval = 50f, name = "数值"))
    }
    var selectedX by remember { mutableStateOf<Float?>(9f) }

    fun thresholdLinePainter(drawScope: DrawScope, start: Offset, end: Offset, line: LimitLine) {
        drawScope.apply {
            drawLine(brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Green), start = start, end = end),
                start = start, end = end, strokeWidth = line.width.toPx())
            drawCircle(color = Color.Cyan.copy(0.6f), radius = line.width.toPx() * 2, center = end)
            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = end)
        }
    }

    fun updateThresholdLine(x: Float?) {
        val min = xAxis.min; val max = xAxis.max
        val clamped = x?.coerceIn(min, max)
        val list = if (clamped != null) mutableListOf(LimitLine(clamped, color = Color.Red, width = 2.dp, text = "X=%.1f".format(clamped), selfDefinedValue = ::thresholdLinePainter)) else mutableListOf()
        xAxis = xAxis.copy(limitLineList = list)
    }

    androidx.compose.runtime.LaunchedEffect(Unit) { updateThresholdLine(selectedX) }

    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "X：${selectedX}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.CenterHorizontally))
        BarChart(modifier = Modifier.fillMaxWidth().weight(1f), data = BarChartData(barData = barData, xAxis = xAxis, yLeftAxis = yLeftAxis))
    }
}

fun getBarTestXLimitLineList(): MutableList<LimitLine> {
    return mutableListOf(LimitLine(100f, isDashes = true, width = 2.dp, color = Color.Gray, text = "测试"), LimitLine(20f))
}
