package com.czy.brianchart.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.chart.compose.view.chart.Axis
import com.brian.chart.compose.view.chart.AxisType
import com.brian.chart.compose.view.chart.Chunk
import com.brian.chart.compose.view.chart.GridLine
import com.brian.chart.compose.view.chart.LimitLine
import com.brian.chart.compose.view.chart.Line
import com.brian.chart.compose.view.chart.LineChart
import com.brian.chart.compose.view.chart.LineChartData
import com.brian.chart.compose.view.chart.Point
import com.brian.chart.compose.view.chart.Renderer
import com.brian.chart.compose.view.chart.TouchEventData
import com.brian.chart.compose.view.chart.TouchEventType
import com.czy.brianchart.ui.components.TopBar
import com.czy.brianchart.ui.navigation.ChartNavigationActions
import com.czy.brianchart.ui.theme.BrianChartTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun LineChartPage(navigationActions: ChartNavigationActions? = null) {
    val lineChartViewModel: LineChartViewModel = viewModel()
    val lineChartUIState by lineChartViewModel.lineChartUIState.collectAsStateWithLifecycle()
    LineChartView(
        modifier = Modifier.fillMaxSize(), lineChartUIState = lineChartUIState, backClick = {
            navigationActions?.navigateBack()
        })
}

@Composable
fun LineChartView(modifier: Modifier, lineChartUIState: LineChartUIState, backClick: () -> Unit?) {
    Surface(modifier = modifier) {
        Column {
            TopBar(
                modifier = Modifier.fillMaxWidth().padding(top = 28.dp).height(48.dp),
                title = "LineChart"
            ) { backClick?.invoke() }
            HorizontalDivider(thickness = 1.dp)
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("动态示例", modifier = Modifier.height(40.dp).fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                LineChartWithTimer(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Text("静态示例", modifier = Modifier.height(40.dp).fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Chart1(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart2(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart3(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart4(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart5(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart6(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart7(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart8(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart9(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Chart10(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                ChartPading(modifier = Modifier.height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                ChartSelfDefine(modifier = Modifier.padding(bottom = 40.dp).height(220.dp))
                HorizontalDivider(thickness = 8.dp)
                Text("触摸交互示例", modifier = Modifier.height(40.dp).fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                ChartWithTouch(modifier = Modifier.padding(bottom = 20.dp).height(300.dp))
                HorizontalDivider(thickness = 8.dp)
            }
        }
    }
}

@Composable
fun LineChartWithTimer(modifier: Modifier) {
    var lineData by remember {
        mutableStateOf(LineChartData(
            lineList = listOf(Line(pointList = emptyList(), color = Color(0xff50E3C2))),
            xAxis = Axis(max = 10000f, labelInterval = 1000f, scaleInterval = 1000f),
            yLeftAxis = Axis(max = 500f, min = -500f, labelInterval = 100f, scaleInterval = 100f),
        ))
    }
    val scope = rememberCoroutineScope()
    val timerFlow = remember { flow { var i = 0; while (true) { emit(i++); delay(10) } } }
    var isRunning by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    Box(modifier = modifier) {
        Button(modifier = Modifier.align(Alignment.TopEnd), onClick = {
            isRunning = !isRunning
            if (isRunning) {
                lineData = lineData.copy(lineList = lineData.lineList?.map { it.copy(pointList = emptyList()) })
                job = scope.launch {
                    val amplitude = 200.0; val frequency = 0.2
                    timerFlow.take(1000).collect { i ->
                        val newPoints = (0 until 10).map { j ->
                            val x = (i * 10 + j).toDouble()
                            Point(x.toFloat(), (amplitude * sin(2 * Math.PI * frequency * x / 100.0)).toFloat())
                        }
                        lineData = lineData.copy(lineList = lineData.lineList?.map { it.copy(pointList = it.pointList + newPoints) })
                    }
                }
            } else job?.cancel()
        }) { Text(if (isRunning) "Stop" else "Start") }
        Text("Points: ${lineData.lineList?.first()?.pointList?.size}",
            modifier = Modifier.align(Alignment.TopEnd).height(40.dp).wrapContentHeight(Alignment.CenterVertically).padding(end = 100.dp),
            color = Color.Blue)
        LineChart(data = lineData)
    }
}

@Composable
fun ChartPading(modifier: Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        LineChart(data = LineChartData(lineList = null,
            xAxis = Axis(max = 6f, min = 0f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 1f, labelTextSize = 14.sp, isDrawLabel = false),
            yLeftAxis = Axis(max = 4.0f, min = -4f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 2f, labelInterval = 2f, labelTextSize = 14.sp, position = 3f),
        ), modifier = Modifier.weight(1f))
        LineChart(data = LineChartData(lineList = null,
            xAxis = Axis(max = 6f, min = 0f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 1f, labelTextSize = 14.sp, isDrawLabel = false),
            yLeftAxis = Axis(max = 4.0f, min = -4f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 2f, labelInterval = 2f, labelTextSize = 14.sp, position = 3f),
        ), modifier = Modifier.weight(1f))
    }
}

@Composable
fun ChartSelfDefine(modifier: Modifier) {
    val context = LocalContext.current
    LineChart(modifier = modifier, data = LineChartData(lineList = getTestLineListSelfDefined(context),
        xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, name = "", gridLine = GridLine(10f, width = 0.5.dp)),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", gridLine = GridLine(10f, width = 0.5.dp)),
        isScroll = true))
}

@Composable
fun Chart10(modifier: Modifier) {
    val list = getTestLineList().map { it.copy(isDrawArea = true) }.toMutableList()
    LineChart(modifier = modifier.padding(2.dp), data = LineChartData(lineList = list,
        xAxis = Axis(max = 500f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false),
        yLeftAxis = Axis(max = 300f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false)))
}

@Composable
fun Chart9(modifier: Modifier) {
    LineChart(modifier = modifier.padding(2.dp), data = LineChartData(lineList = getTestLineList(), isSelfAdaptation = true,
        xAxis = Axis(max = 500f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false),
        yLeftAxis = Axis(max = 300f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false)))
}

@Composable
fun Chart8(modifier: Modifier) {
    LineChart(modifier = modifier.padding(2.dp), data = LineChartData(
        xAxis = Axis(max = 8f, min = -0f, position = 0f, scaleInterval = 1f, labelInterval = 1f, name = ""),
        yLeftAxis = Axis(max = 0f, scaleInterval = 10f, labelInterval = 10f, position = 0f, name = "", min = -80f)))
}

@Composable
fun Chart7(modifier: Modifier) {
    LineChart(modifier = modifier.padding(2.dp), data = LineChartData(
        xAxis = Axis(max = 0.833f, min = -0f, position = 0f, scaleInterval = 0.1f, labelInterval = 0.1f, name = ""),
        yLeftAxis = Axis(max = 0.02f, scaleInterval = 0.003f, labelInterval = 0.003f, position = 0f, name = "", min = 0f)))
}

@Composable
fun Chart6(modifier: Modifier) {
    LineChart(modifier = modifier, data = LineChartData(lineList = getTestPointLineList(),
        xAxis = Axis(max = 800f, min = -400f, position = 0f, scaleInterval = 100f, labelInterval = 100f, name = ""),
        yLeftAxis = Axis(max = 200f, scaleInterval = 50f, labelInterval = 50f, position = 0f, name = "", min = -300f)))
}

@Composable
fun Chart5(modifier: Modifier) {
    val limitLineList = getTestPlusOrMinusLimitLineList()
    LineChart(modifier = modifier, data = LineChartData(lineList = getTestPlusOrMinusLineList(),
        xAxis = Axis(max = 800f, min = -400f, position = 0f, scaleInterval = 100f, labelInterval = 100f, name = "", limitLineList = limitLineList),
        yLeftAxis = Axis(max = 200f, scaleInterval = 50f, labelInterval = 50f, position = 0f, name = "", min = -300f, limitLineList = limitLineList)))
}

@Composable
fun Chart4(modifier: Modifier) {
    LineChart(modifier = modifier.padding(2.dp).background(Color(0xffaabbcc)), data = LineChartData(lineList = getTestLineList(),
        xAxis = Axis(max = 500f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false),
        yLeftAxis = Axis(max = 300f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false)))
}

@Composable
fun Chart3(modifier: Modifier) {
    val list = getTestLineList(); val listChunk = getTestChunkList(); val xLimitLineList = getTestXLimitLineList()
    val limitLineList = getTestLimitLineList()
    LineChart(modifier = modifier, data = LineChartData(lineList = list,
        xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, limitLineList = xLimitLineList, chunkList = listChunk, name = "",
            settingLabelValue = ::settingLineChartLabelValue, gridLine = GridLine(10f)),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "",
            chunkList = listChunk, limitLineList = limitLineList, settingLabelValue = ::settingLineChartLabelValue, gridLine = GridLine(10f))))
}

@Composable
fun Chart2(modifier: Modifier) {
    val list = getTestLineList(); val listChunk = getTestChunkList(); val xLimitLineList = getTestXLimitLineList()
    val limitLineList = getTestLimitLineList()
    LineChart(modifier = modifier, data = LineChartData(lineList = list,
        xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, limitLineList = xLimitLineList, chunkList = listChunk, name = ""),
        yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", chunkList = listChunk, limitLineList = limitLineList)))
}

@Composable
fun Chart1(modifier: Modifier) {
    val list = getTestLineList2(); val listChunkX = getTestXChunkList()
    val listChunk1 = getTestChunkList1(); val listChunk2 = getTestChunkList2(); val listChunk3 = getTestChunkList3()
    val xLimitLineList1 = getTestXLimitLineList1(); val yLimitLineList1 = getTestYLimitLineList1()
    val yLimitLineList2 = getTestYLimitLineList2(); val yLimitLineList3 = getTestYLimitLineList3()
    LineChart(data = LineChartData(lineList = list,
        xAxis = Axis(min = 10f, max = 40f, scaleInterval = 5f, labelInterval = 10f, limitLineList = xLimitLineList1, chunkList = listChunkX, name = "x轴"),
        yLeftInsideAxis = Axis(max = 200f, scaleInterval = 25f, labelInterval = 25f, name = "Load\nW", color = Color(0XFF18D276), chunkList = listChunk1, limitLineList = yLimitLineList1),
        yLeftAxis = Axis(max = 2000f, scaleInterval = 100f, labelInterval = 500f, name = "  VO2\nml/min", color = Color(0XFFFF4E87), chunkList = listChunk2, limitLineList = yLimitLineList2),
        yRightAxis = Axis(max = 2000f, scaleInterval = 100f, labelInterval = 500f, name = "  VCO2\nml/min", color = Color(0XFF058BF6), chunkList = listChunk3, limitLineList = yLimitLineList3)
    ), modifier = modifier)
}

@Composable @Preview fun ChartViewPreview() { BrianChartTheme { LineChartPage() } }
@Composable @Preview(heightDp = 2000) fun ChartViewLongPreview() { BrianChartTheme { LineChartPage() } }

fun getTestChunkList() = mutableListOf(Chunk(40f, 60f), Chunk(10f, 20f))
fun getTestXChunkList() = mutableListOf(Chunk(20f, 25f))
fun getTestChunkList1() = mutableListOf(Chunk(25f, 50f, color = Color(0X2218D276)))
fun getTestChunkList2() = mutableListOf(Chunk(800f, 1000f, color = Color(0X222FF4E87)))
fun getTestChunkList3() = mutableListOf(Chunk(1500f, 1800f, color = Color(0X22058BF6)))

fun getTestLimitLineList() = mutableListOf(LimitLine(50f, isDashes = true, width = 2.dp, color = Color.Gray, text = "测试"), LimitLine(15f))
fun getTestPlusOrMinusLimitLineList() = mutableListOf(LimitLine(50f, isDashes = true, width = 2.dp, color = Color.Gray, text = "测试"), LimitLine(-25f))
fun getTestXLimitLineList() = mutableListOf(LimitLine(100f, isDashes = true, width = 2.dp, color = Color.Gray, text = "测试"), LimitLine(20f))
fun getTestXLimitLineList1() = mutableListOf(LimitLine(10f, isDashes = true, width = 2.dp, color = Color.Gray, text = "测试"), LimitLine(20f))
fun getTestYLimitLineList1() = mutableListOf(LimitLine(10f, isDashes = true, width = 2.dp, color = Color(0XFF18D276), text = "限制线"))
fun getTestYLimitLineList2() = mutableListOf(LimitLine(600f, isDashes = true, width = 2.dp, color = Color(0XFFFF4E87), text = "限制线"))
fun getTestYLimitLineList3() = mutableListOf(LimitLine(1200f, isDashes = true, width = 2.dp, color = Color(0XFF058BF6), text = "限制线"))

fun getTestLineList(): MutableList<Line> {
    val p1 = listOf(Point(10f, 210f), Point(50f, 150f), Point(100f, 130f), Point(150f, 200f), Point(200f, 80f), Point(250f, 240f), Point(300f, 20f), Point(350f, 150f), Point(400f, 50f), Point(450f, 240f), Point(500f, 140f))
    return mutableListOf(
        Line(p1, color = Color(0xff4A90E2), isDrawCubic = true, isDrawArea = true,
            drawAreaBrush = Brush.linearGradient(colors = listOf(Color(0xff4A90E2), Color(0x204A90E2)), start = Offset(0f, 0f), end = Offset(0f, Float.POSITIVE_INFINITY))),
        Line(p1, color = Color(0xffFF90E2)))
}

fun getTestLineListSelfDefined(context: Context): MutableList<Line> {
    val pts = mutableListOf(
        Point(100f, 50f, selfDefinedValue = { ds, o -> drawSelfDefinedTextAndShape(ds, o, 100f, 50f, Color.Green) }),
        Point(200f, 120f, selfDefinedValue = { ds, o -> drawSelfDefinedTextAndShape(ds, o, 200f, 120f, Color.Red) }),
        Point(300f, 220f, selfDefinedValue = { ds, o -> drawSelfDefinedText(ds, o, 300f, 220f, Color.Black) }),
        Point(400f, 80f, selfDefinedValue = { ds, o -> drawSelfDefinedBitmap(ds, BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_edit).asImageBitmap(), o) }),
        Point(500f, 200f, selfDefinedValue = { ds, o -> drawSelfDefinedBitmap(ds, BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_edit).asImageBitmap(), o) }))
    return mutableListOf(Line(pts, color = Color(0xff50E3C2), isDashes = true,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 12f), 2f),
        renderer = { ds, line, offsetList -> line?.pointList?.forEachIndexed { i, pt -> offsetList?.getOrNull(i)?.let { pt.selfDefinedValue?.invoke(ds, it) } } }))
}

fun drawSelfDefinedBitmap(ds: DrawScope, bitmap: ImageBitmap, offset: Offset) {
    ds.run { drawImage(image = bitmap, topLeft = Offset(offset.x - bitmap.width / 2, offset.y - bitmap.height / 2)) }
}

fun drawSelfDefinedTextAndShape(ds: DrawScope, offset: Offset, x: Float, y: Float, color: Color) {
    ds.run {
        val ts = 12.sp
        drawRoundRect(color = color, topLeft = Offset(offset.x - 20f, offset.y - 20f), size = Size(40f, 40f), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round), cornerRadius = CornerRadius(2f, 2f))
        drawContext.canvas.nativeCanvas.apply { val p = Paint().apply { textSize = ts.toPx(); this.color = color.toArgb(); isAntiAlias = true }; drawText("(${x},${y})", offset.x - 80f, offset.y - ts.toPx(), p) }
    }
}

fun drawSelfDefinedText(ds: DrawScope, offset: Offset, x: Float, y: Float, color: Color) {
    ds.run {
        val ts = 12.sp
        drawContext.canvas.nativeCanvas.apply { val p = Paint().apply { textSize = ts.toPx(); this.color = color.toArgb(); isAntiAlias = true }; drawText("${y.toInt()}次", offset.x - 40f, offset.y - ts.toPx() / 2, p) }
    }
}

fun getTestLineList2(): MutableList<Line> {
    return mutableListOf(
        Line(listOf(Point(0f, 10f), Point(5f, 100f), Point(10f, 30f), Point(15f, 200f), Point(20f, 120f), Point(25f, 10f), Point(30f, 180f), Point(35f, 100f), Point(40f, 10f)), color = Color(0XFF18D276), axisType = AxisType.LEFT_INSIDE),
        Line(listOf(Point(0f, 1000f), Point(5f, 1000f), Point(10f, 2000f), Point(15f, 120f), Point(20f, 1120f), Point(25f, 1000f), Point(30f, 180f), Point(35f, 100f), Point(40f, 1000f)), color = Color(0XFFFF4E87), isDrawCubic = true, isDashes = true),
        Line(listOf(Point(0f, 1200f), Point(5f, 100f), Point(10f, 2200f), Point(15f, 600f), Point(20f, 120f), Point(25f, 1500f), Point(30f, 680f), Point(35f, 200f), Point(40f, 1500f)), color = Color(0XFF058BF6), axisType = AxisType.RIGHT, isDrawCubic = true))
}

fun drawableToBitmap(drawable: Drawable? = null): ImageBitmap? {
    if (drawable == null) return null
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap); drawable.setBounds(0, 0, canvas.width, canvas.height); drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

fun getTestPlusOrMinusLineList(): MutableList<Line> {
    val p1 = mutableListOf<Point>().apply { for (i in 0..20) { add(Point(Random.nextInt(-500, 500).toFloat(), Random.nextInt(-200, 200).toFloat())) } }
    val p2 = mutableListOf<Point>().apply { for (i in -500..500 step 50) { add(Point(i.toFloat(), Random.nextInt(-200, 200).toFloat())) } }
    return mutableListOf(Line(p1, color = Color(0xff50E3C2), isDrawCubic = true), Line(p2, color = Color(0xff4A90E2), isDrawCubic = true))
}

fun getTestPointLineList() = mutableListOf(Line(
    listOf(Point(10f, 210f), Point(50f, 150f), Point(100f, 130f), Point(150f, 200f), Point(200f, 80f), Point(250f, 240f), Point(300f, 20f), Point(350f, 150f), Point(400f, 50f), Point(450f, 240f), Point(500f, 140f)),
    width = 2.dp, color = Color(0xff4A90E2), isDrawCubic = true, isPoints = true, isDrawPath = false))

fun settingLineChartLabelValue(value: Float): String = "${if (value.toInt().toFloat() == value) value.toInt() else value}T"

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview() {
    BrianChartTheme { Surface {
        val list = getTestLineList2(); val listChunkX = getTestXChunkList()
        val listChunk1 = getTestChunkList1(); val listChunk2 = getTestChunkList2(); val listChunk3 = getTestChunkList3()
        val xLimitLineList1 = getTestXLimitLineList1(); val yLimitLineList1 = getTestYLimitLineList1()
        val yLimitLineList2 = getTestYLimitLineList2(); val yLimitLineList3 = getTestYLimitLineList3()
        LineChart(data = LineChartData(lineList = list,
            xAxis = Axis(min = 10f, max = 40f, scaleInterval = 5f, labelInterval = 10f, limitLineList = xLimitLineList1, chunkList = listChunkX, name = "x轴"),
            yLeftInsideAxis = Axis(max = 200f, scaleInterval = 25f, labelInterval = 25f, name = "Load\nW", color = Color(0XFF18D276), chunkList = listChunk1, limitLineList = yLimitLineList1),
            yLeftAxis = Axis(max = 2000f, scaleInterval = 100f, labelInterval = 500f, name = "  VO2\nml/min", color = Color(0XFFFF4E87), chunkList = listChunk2, limitLineList = yLimitLineList2),
            yRightAxis = Axis(max = 2000f, scaleInterval = 100f, labelInterval = 500f, name = "  VCO2\nml/min", color = Color(0XFF058BF6), chunkList = listChunk3, limitLineList = yLimitLineList3)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview2() {
    BrianChartTheme { Surface {
        val list = getTestLineList(); val listChunk = getTestChunkList(); val xLimitLineList = getTestXLimitLineList(); val limitLineList = getTestLimitLineList()
        LineChart(data = LineChartData(lineList = list,
            xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, limitLineList = xLimitLineList, chunkList = listChunk, name = ""),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", chunkList = listChunk, limitLineList = limitLineList)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview3() {
    BrianChartTheme { Surface {
        val list = getTestLineList(); val listChunk = getTestChunkList(); val xLimitLineList = getTestXLimitLineList(); val limitLineList = getTestLimitLineList()
        LineChart(data = LineChartData(lineList = list,
            xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, limitLineList = xLimitLineList, chunkList = listChunk, name = "", settingLabelValue = ::settingLineChartLabelValue, gridLine = GridLine(10f)),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", chunkList = listChunk, limitLineList = limitLineList, settingLabelValue = ::settingLineChartLabelValue, gridLine = GridLine(10f))))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview4() {
    BrianChartTheme { Surface {
        LineChart(modifier = Modifier.padding(2.dp).background(Color(0xffaabbcc)), data = LineChartData(lineList = getTestLineList(),
            xAxis = Axis(max = 500f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false),
            yLeftAxis = Axis(max = 300f, gridLine = GridLine(10f, width = 0.5.dp), isDrawLabel = false, isDrawAxis = false)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview5() {
    BrianChartTheme { Surface {
        val limitLineList = getTestPlusOrMinusLimitLineList()
        LineChart(data = LineChartData(lineList = getTestPlusOrMinusLineList(),
            xAxis = Axis(max = 800f, min = -400f, position = 0f, scaleInterval = 100f, labelInterval = 100f, name = "", limitLineList = limitLineList),
            yLeftAxis = Axis(max = 200f, scaleInterval = 50f, labelInterval = 50f, position = 0f, name = "", min = -300f, limitLineList = limitLineList)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview6() {
    BrianChartTheme { Surface {
        LineChart(data = LineChartData(lineList = getTestPointLineList(),
            xAxis = Axis(max = 800f, min = -400f, position = 0f, scaleInterval = 100f, labelInterval = 100f, name = ""),
            yLeftAxis = Axis(max = 200f, scaleInterval = 50f, labelInterval = 50f, position = 0f, name = "", min = -300f)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview7() {
    BrianChartTheme { Surface {
        LineChart(modifier = Modifier.padding(2.dp), data = LineChartData(
            xAxis = Axis(max = 0.833f, min = -0f, position = 0f, scaleInterval = 0.1f, labelInterval = 0.1f, name = ""),
            yLeftAxis = Axis(max = 0.02f, scaleInterval = 0.003f, labelInterval = 0.003f, position = 0f, name = "", min = 0f)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreview8() {
    BrianChartTheme { Surface {
        LineChart(modifier = Modifier.padding(2.dp), data = LineChartData(
            xAxis = Axis(max = 8f, min = -0f, position = 0f, scaleInterval = 1f, labelInterval = 1f, name = ""),
            yLeftAxis = Axis(max = 0f, scaleInterval = 10f, labelInterval = 10f, position = 0f, name = "", min = -80f)))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartSelfAdaptationPreview() {
    BrianChartTheme { Surface {
        val list = getTestPlusOrMinusLineList()
        LineChart(data = LineChartData(lineList = list, xAxis = Axis(position = 0f, scaleInterval = 100f, labelInterval = 100f, name = ""),
            yLeftAxis = Axis(scaleInterval = 50f, labelInterval = 50f, position = 0f, name = ""), isSelfAdaptation = true))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPadingPreview() {
    BrianChartTheme { Surface {
        Row(modifier = Modifier.padding(8.dp)) {
            LineChart(data = LineChartData(lineList = null,
                xAxis = Axis(max = 6f, min = 0f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 1f, labelTextSize = 14.sp, isDrawLabel = false),
                yLeftAxis = Axis(max = 4.0f, min = -4f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 2f, labelInterval = 2f, labelTextSize = 14.sp, position = 3f),
            ), modifier = Modifier.weight(1f))
            LineChart(data = LineChartData(lineList = null,
                xAxis = Axis(max = 6f, min = 0f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 1f, labelTextSize = 14.sp, isDrawLabel = false),
                yLeftAxis = Axis(max = 4.0f, min = -4f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 2f, labelInterval = 2f, labelTextSize = 14.sp, position = 3f),
            ), modifier = Modifier.weight(1f))
        }
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPadingSelfDefinePreview() {
    BrianChartTheme { Surface {
        Row(modifier = Modifier.padding(8.dp)) {
            LineChart(data = LineChartData(lineList = null,
                xAxis = Axis(max = 6f, min = 0f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 1f, labelInterval = 1f, labelTextSize = 14.sp),
                yLeftAxis = Axis(max = 4.0f, min = -4f, color = MaterialTheme.colorScheme.onSurfaceVariant, scaleInterval = 2f, labelInterval = 2f, labelTextSize = 14.sp),
            ), modifier = Modifier.weight(1f).background(Color(0x10000000)))
        }
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreviewSelfDefined() {
    BrianChartTheme { Surface {
        val context = LocalContext.current
        LineChart(data = LineChartData(lineList = getTestLineListSelfDefined(context),
            xAxis = Axis(max = 500f, scaleInterval = 20f, labelInterval = 100f, name = "", gridLine = GridLine(10f, width = 0.5.dp)),
            yLeftAxis = Axis(max = 300f, scaleInterval = 10f, labelInterval = 50f, name = "", gridLine = GridLine(10f, width = 0.5.dp)),
            isScroll = true))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun LineChartPreviewChunk() {
    BrianChartTheme { Surface {
        LineChart(data = LineChartData(lineList = null,
            xAxis = Axis(max = 500f, min = 200f, scaleInterval = 20f, labelInterval = 100f,
                chunkList = mutableListOf(Chunk(200f, 300f, Color.Red.copy(alpha = 0.5f)), Chunk(400f, 500f, Color.Blue.copy(alpha = 0.5f))), name = ""),
            yLeftAxis = Axis(max = 800f, min = 300f, scaleInterval = 10f, labelInterval = 50f,
                chunkList = mutableListOf(Chunk(400f, 420f, Color.Red.copy(alpha = 0.5f)), Chunk(600f, 700f, Color.Blue.copy(alpha = 0.5f))), name = "")))
    } }
}

@Composable @Preview(showSystemUi = false, showBackground = true, widthDp = 500, heightDp = 250)
fun ChartWithTouchPreview() {
    BrianChartTheme { Surface {
        ChartWithTouch(modifier = Modifier.padding(bottom = 20.dp).height(300.dp))
    } }
}

@Composable
fun ChartWithTouch(modifier: Modifier) {
    var config by remember {
        mutableStateOf(LineChartData(
            lineList = listOf(Line(pointList = mutableListOf(
                Point(0f, 10f), Point(25f, 80f), Point(50f, 40f), Point(75f, 120f),
                Point(100f, 90f), Point(125f, 160f), Point(150f, 130f), Point(175f, 200f), Point(200f, 170f)),
                color = Color(0xff4A90E2), isDrawCubic = true, isDrawPath = true)),
            xAxis = Axis(max = 200f, min = 0f, scaleInterval = 20f, labelInterval = 50f, name = "时间 (s)", limitLineList = mutableListOf()),
            yLeftAxis = Axis(max = 250f, min = 0f, scaleInterval = 25f, labelInterval = 50f, name = "数值")))
    }
    var selectedX by remember { mutableStateOf<Float?>(200f) }

    fun thresholdLinePainter(drawScope: DrawScope, start: Offset, end: Offset, line: LimitLine) {
        drawScope.apply {
            drawLine(brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Green), start = start, end = end),
                start = start, end = end, strokeWidth = line.width.toPx())
            drawCircle(color = Color.Cyan.copy(0.6f), radius = line.width.toPx() * 2, center = end)
            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = end)
        }
    }

    fun updateThresholdLine(x: Float?) {
        val min = config.xAxis.min; val max = config.xAxis.max
        val clamped = x?.coerceIn(min, max)
        val list = if (clamped != null) mutableListOf(LimitLine(clamped, color = Color.Red, width = 2.dp, text = "X=%.1f".format(clamped), selfDefinedValue = ::thresholdLinePainter)) else mutableListOf()
        config = config.copy(xAxis = config.xAxis.copy(limitLineList = list))
    }

    LaunchedEffect(Unit) { updateThresholdLine(selectedX) }

    var selectedPoint by remember { mutableStateOf<Point?>(null) }
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "X：${selectedX} selectedPoint：${selectedPoint}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.CenterHorizontally))
        LineChart(modifier = Modifier.fillMaxWidth().weight(1f), data = config.copy(onTouch = { payload: TouchEventData ->
            updateThresholdLine(payload.dataX); selectedPoint = getClosestLinePoint(config.lineList, payload.dataX); selectedX = payload.dataX
            when (payload.eventType) {
                TouchEventType.TAP -> updateThresholdLine(selectedPoint?.x ?: payload.dataX)
                TouchEventType.MOVE -> updateThresholdLine(payload.dataX)
                TouchEventType.UP -> updateThresholdLine(selectedPoint?.x ?: payload.dataX)
                TouchEventType.DOWN -> {}
            }
        }))
    }
}

fun getClosestLinePoint(lineList: List<Line>? = null, dataX: Float): Point? {
    return lineList?.flatMap { it.pointList }?.minByOrNull { abs(it.x - dataX) }
}
