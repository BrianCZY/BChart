package com.brian.chart.compose.view.chart

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.brian.chart.compose.view.chart.data.Axis
import com.brian.chart.compose.view.chart.data.Chunk
import com.brian.chart.compose.view.chart.data.DrawAreaPoints
import com.brian.chart.compose.view.chart.data.GridLine
import com.brian.chart.compose.view.chart.data.LimitLine
import com.brian.chart.compose.view.chart.data.Point
import java.math.BigDecimal


/**
 * @author Brian
 * @Description:鐢婚鑹插潡
 */
fun drawXChunk(
    drawScope: DrawScope,
    chunkList: MutableList<Chunk>?,
    drawAreaPoints: DrawAreaPoints,
    axisMin: Float,
    axisMax: Float,

    ) {
    drawScope.run {


        val oneDataYPx =
            (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (axisMax - axisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        chunkList?.forEachIndexed { index, chunk ->
            val X1 = drawAreaPoints.leftBottom.x + (chunk.start - axisMin) * oneDataYPx
            val X2 = drawAreaPoints.leftBottom.x + (chunk.end - axisMin) * oneDataYPx
            val Y1 = drawAreaPoints.leftBottom.y  //
            val Y2 = drawAreaPoints.leftTop.y  //

            drawRect(
                color = chunk.color,
                topLeft = Offset(x = X1, y = Y1),
                size = Size(X2 - X1, Y2 - Y1)
            )
        }


    }


}


fun drawChunk(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
) {
    drawScope.run {
        xAxis.let {
            it.chunkList?.let { chunkList ->
                drawXChunk(
                    drawScope = this,
                    chunkList = chunkList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                )
            }
        }
        yLeftInsideAxis?.let {
            it.chunkList?.let { chunkList ->
                drawYChunk(
                    drawScope = this,
                    yChunkList = chunkList,
                    drawAreaPoints = drawAreaPoints,
                    yAxisMax = it.max,
                    yAxisMin = it.min,
                )
            }
        }
        yLeftAxis?.let {
            it.chunkList?.let { chunkList ->
                drawYChunk(
                    drawScope = this,
                    yChunkList = chunkList,
                    drawAreaPoints = drawAreaPoints,
                    yAxisMax = it.max,
                    yAxisMin = it.min,
                )
            }


        }
        yRightInsideAxis?.let {
            it.chunkList?.let { chunkList ->
                drawYChunk(
                    drawScope = this,
                    yChunkList = chunkList,
                    drawAreaPoints = drawAreaPoints,
                    yAxisMax = it.max,
                    yAxisMin = it.min,
                )
            }


        }
        yRightAxis?.let {
            it.chunkList?.let { chunkList ->
                drawYChunk(
                    drawScope = this,
                    yChunkList = chunkList,
                    drawAreaPoints = drawAreaPoints,
                    yAxisMax = it.max,
                    yAxisMin = it.min,
                )
            }


        }
    }
}

fun drawXYAxis(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
) {
    drawScope.run {

        val oneDataXPx =
            (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (xAxis.max - xAxis.min) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        var oneDataYPx = 0f
        var yOffset = 0f
        when {
            yLeftAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yLeftAxis.max - yLeftAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yLeftAxis.min) * oneDataYPx
                }

            }

            yLeftInsideAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yLeftInsideAxis.max - yLeftInsideAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yLeftInsideAxis.min) * oneDataYPx
                }
            }

            yRightAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yRightAxis.max - yRightAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yRightAxis.min) * oneDataYPx
                }
            }
        }


        xAxis.let {
            //缁樺埗 X杞?
            if (it.isDrawAxis) {

                drawLine(
                    start = Offset(drawAreaPoints.leftBottom.x, drawAreaPoints.leftBottom.y - yOffset),
                    end = Offset(drawAreaPoints.rightBottom.x, drawAreaPoints.rightBottom.y - yOffset),
                    color = it.color,
                    strokeWidth = it.strokeSize.toPx()
                )
            }
        }
        //缁樺埗 Y杞?宸﹀唴
        yLeftInsideAxis?.let {
            if (it.isDrawAxis) {

                val xOffset = it.position?.let { (it - xAxis.min) * oneDataXPx } ?: 0f
                drawLine(
                    start = Offset(drawAreaPoints.leftBottom.x + xOffset, drawAreaPoints.leftBottom.y),
                    end = Offset(drawAreaPoints.leftTop.x + xOffset, drawAreaPoints.leftTop.y),
                    color = it.color,
                    strokeWidth = it.strokeSize.toPx()
                )
            }
        }
        //缁樺埗 Y杞?宸?
        yLeftAxis?.let {
            if (it.isDrawAxis) {
                val xOffset = it.position?.let { (it - xAxis.min) * oneDataXPx } ?: 0f
                drawLine(
                    start = Offset(drawAreaPoints.leftBottom.x + xOffset, drawAreaPoints.leftBottom.y),
                    end = Offset(drawAreaPoints.leftTop.x + xOffset, drawAreaPoints.leftTop.y),
                    color = it.color,
                    strokeWidth = it.strokeSize.toPx()
                )
            }
        }
        //缁樺埗 Y杞?鍙冲唴
        yRightInsideAxis?.let {
            if (it.isDrawAxis) {
                val xOffset = it.position?.let { (it - xAxis.max) * oneDataXPx } ?: 0f
                drawLine(
                    start = Offset(drawAreaPoints.rightBottom.x + xOffset, drawAreaPoints.rightBottom.y),
                    end = Offset(drawAreaPoints.rightTop.x + xOffset, drawAreaPoints.rightTop.y),
                    color = it.color,
                    strokeWidth = it.strokeSize.toPx()
                )
            }
        }
        //缁樺埗 Y杞?鍙?
        yRightAxis?.let {
            if (it.isDrawAxis) {
                val xOffset = it.position?.let { (it - xAxis.max) * oneDataXPx } ?: 0f
                drawLine(
                    start = Offset(drawAreaPoints.rightBottom.x + xOffset, drawAreaPoints.rightBottom.y),
                    end = Offset(drawAreaPoints.rightTop.x + xOffset, drawAreaPoints.rightTop.y),
                    color = it.color,
                    strokeWidth = it.strokeSize.toPx()
                )
            }
        }
    }

}


fun getScaleLengSize(axis: Axis?, currentDensity: Density): Float {

    return if (axis?.scaleInterval != null) {
        with(currentDensity) {
            axis.scaleLengSize.toPx()
        }

    } else {
        0f
    }//宸﹁竟鍒诲害鐨勯暱搴?

}

fun drawLable(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
    scale: Float = 1f
) {
    drawScope.run {
        val oneDataXPx =
            (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (xAxis.max - xAxis.min) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        var oneDataYPx = 0f
        var yOffset = 0f
        when {
            yLeftAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yLeftAxis.max - yLeftAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yLeftAxis.min) * oneDataYPx
                }

            }

            yLeftInsideAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yLeftInsideAxis.max - yLeftInsideAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yLeftInsideAxis.min) * oneDataYPx
                }
            }

            yRightAxis != null -> {
                oneDataYPx =
                    (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yRightAxis.max - yRightAxis.min) // Y杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
                xAxis.position?.let {
                    yOffset = (it - yRightAxis.min) * oneDataYPx
                }
            }
        }
        xAxis.let {
            it.scaleInterval?.let { scaleInterval ->
                drawXaxisBottomScale(
                    drawScope = this,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    defaultXAxisMin = it.min,
                    axisColor = it.color,
                    scaleLengSize = it.scaleLengSize.toPx(),
                    axisStrokeSize = it.strokeSize.toPx(),
                    scaleInterval = scaleInterval,
                    scale = scale,
                    yOffset = yOffset
                )

            }
            if (it.isDrawLabel) {
                it.labelInterval?.let { labelInterval ->

                    drawXaxisBottomLabel(
                        drawScope = this,
                        drawAreaPoints = drawAreaPoints,
                        defaultXAxisMax = it.max,
                        defaultXAxisMin = it.min,
                        labelColor = it.color,
                        labelInterval = labelInterval,
                        labelTextSizePx = it.labelTextSize.toPx(),
                        scale = scale,
                        settingLabelValue = it.settingLabelValue,
                        yOffset = yOffset
                    )
                }
            }

        }
        yLeftInsideAxis?.let {
            val xOffset = it.position?.let { (it - xAxis.min) * oneDataXPx } ?: 0f
            it.scaleInterval?.let { scaleInterval ->
                drawYAxisLeftInsideScale(
                    drawScope = this,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    defaultXAxisMin = it.min,
                    axisColor = it.color,
                    scaleLengSize = it.scaleLengSize.toPx(),
                    axisStrokeSize = it.strokeSize.toPx(),
                    scaleInterval = scaleInterval,
                    scale = scale,
                    xOffset = xOffset
                )
            }
            if (it.isDrawLabel) {
                it.labelInterval?.let { labelInterval ->
                    drawYAxisLeftInsideLabel(
                        drawScope = this,
                        drawAreaPoints = drawAreaPoints,
                        defaultYAxisMax = it.max,
                        defaultYAxisMin = it.min,
                        labelColor = it.color,
                        labelInterval = labelInterval,
                        labelTextSizePx = it.labelTextSize.toPx(),
                        scale = scale,
                        settingLabelValue = it.settingLabelValue,
                        xOffset = xOffset
                    )
                }
            }
        }
        yLeftAxis?.let {
            val xOffset = it.position?.let { (it - xAxis.min) * oneDataXPx } ?: 0f
            it.scaleInterval?.let { scaleInterval ->
                drawYAxisLeftScale(
                    drawScope = this,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    defaultXAxisMin = it.min,
                    axisColor = it.color,
                    scaleLengSize = it.scaleLengSize.toPx(),
                    axisStrokeSize = it.strokeSize.toPx(),
                    scaleInterval = scaleInterval,
                    scale = scale,
                    xOffset = xOffset
                )
            }
            if (it.isDrawLabel) {
                it.labelInterval?.let { labelInterval ->
                    drawYAxisLeftLabel(
                        drawScope = this,
                        drawAreaPoints = drawAreaPoints,
                        defaultYAxisMax = it.max,
                        defaultYAxisMin = it.min,
                        labelColor = it.color,
                        labelInterval = labelInterval,
                        labelTextSizePx = it.labelTextSize.toPx(),
                        scale = scale,
                        settingLabelValue = it.settingLabelValue,
                        xOffset = xOffset
                    )
                }
            }
        }
        yRightInsideAxis?.let {
            val xOffset = it.position?.let { (it - xAxis.max) * oneDataXPx } ?: 0f
            it.scaleInterval?.let { scaleInterval ->
                drawYAxisRightInsideScale(
                    drawScope = this,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    defaultXAxisMin = it.min,
                    axisColor = it.color,
                    scaleLengSize = it.scaleLengSize.toPx(),
                    axisStrokeSize = it.strokeSize.toPx(),
                    scaleInterval = scaleInterval,
                    scale = scale,
                    xOffset = xOffset
                )
            }
            if (it.isDrawLabel) {
                it.labelInterval?.let { labelInterval ->
                    drawYAxisRightInsideLabel(
                        drawScope = this,
                        drawAreaPoints = drawAreaPoints,
                        defaultYAxisMax = it.max,
                        defaultYAxisMin = it.min,
                        labelColor = it.color,
                        labelInterval = labelInterval,
                        labelTextSizePx = it.labelTextSize.toPx(),
                        scale = scale,
                        settingLabelValue = it.settingLabelValue,
                        xOffset = xOffset
                    )
                }
            }
        }
        yRightAxis?.let {
            val xOffset = it.position?.let { (it - xAxis.max) * oneDataXPx } ?: 0f
            it.scaleInterval?.let { scaleInterval ->
                drawYAxisRightScale(
                    drawScope = this,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    defaultXAxisMin = it.min,
                    axisColor = it.color,
                    scaleLengSize = it.scaleLengSize.toPx(),
                    axisStrokeSize = it.strokeSize.toPx(),
                    scaleInterval = scaleInterval,
                    scale = scale,
                    xOffset = xOffset
                )
            }
            if (it.isDrawLabel) {
                it.labelInterval?.let { labelInterval ->
                    drawYAxisRightLabel(
                        drawScope = this,
                        drawAreaPoints = drawAreaPoints,
                        defaultYAxisMax = it.max,
                        defaultYAxisMin = it.min,
                        labelColor = it.color,
                        labelInterval = labelInterval,
                        labelTextSizePx = it.labelTextSize.toPx(),
                        scale = scale,
                        settingLabelValue = it.settingLabelValue,
                        xOffset = xOffset
                    )
                }
            }

        }
    }


}

fun drawLimitLine(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
    scale: Float
) {
    drawScope.run {
        xAxis.let {
            it.limitLineList?.let { limitLineList ->
                drawXLimitLine(
                    drawScope = this,
                    xLimitLineList = limitLineList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                    scale = scale,
                )

            }


        }
        yLeftInsideAxis?.let {
            it.limitLineList?.let { limitLineList ->
                drawYLimitLine(
                    drawScope = this,
                    yLimitLineList = limitLineList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                    scale = scale,
                )

            }
        }
        yLeftAxis?.let {
            it.limitLineList?.let { limitLineList ->
                drawYLimitLine(
                    drawScope = this,
                    yLimitLineList = limitLineList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                    scale = scale,
                )

            }

        }
        yRightInsideAxis?.let {
            it.limitLineList?.let { limitLineList ->
                drawYLimitLine(
                    drawScope = this,
                    yLimitLineList = limitLineList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                    scale = scale,
                )

            }

        }
        yRightAxis?.let {
            it.limitLineList?.let { limitLineList ->
                drawYLimitLine(
                    drawScope = this,
                    yLimitLineList = limitLineList,
                    drawAreaPoints = drawAreaPoints,
                    axisMax = it.max,
                    axisMin = it.min,
                    scale = scale,
                )

            }
        }
    }

}

fun drawAxisName(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
    scale: Float
) {
    drawScope.run {
        xAxis.let {
            it.name?.let { name ->
                drawXaxisBottomName(
                    drawScope = this,
                    name = name,
                    drawAreaPoints = drawAreaPoints,
                    defaultXAxisMax = it.max,
                    labelColor = it.color,
                    labelTextSizePx = it.labelTextSize.toPx(),
                    scale = scale,
                )

            }


        }
        yLeftInsideAxis?.let {
            it.name?.let { name ->
                drawYAxisLeftInsideName(
                    drawScope = this,
                    name = name,
                    drawAreaPoints = drawAreaPoints,

                    labelColor = it.color,
                    labelTextSizePx = it.labelTextSize.toPx(),
                    scale = scale,
                )

            }
        }
        yLeftAxis?.let {
            it.name?.let { name ->
                drawYAxisLeftName(
                    drawScope = this,
                    name = name,
                    drawAreaPoints = drawAreaPoints,
                    labelColor = it.color,
                    labelTextSizePx = it.labelTextSize.toPx(),
                    scale = scale,
                )

            }
        }
        yRightInsideAxis?.let {
            it.name?.let { name ->
                drawYAxisRightInsideName(
                    drawScope = this,
                    name = name,
                    drawAreaPoints = drawAreaPoints,

                    labelColor = it.color,
                    labelTextSizePx = it.labelTextSize.toPx(),
                    scale = scale,
                )

            }
        }
        yRightAxis?.let {
            it.name?.let { name ->
                drawYAxisRightName(
                    drawScope = this,
                    name = name,
                    drawAreaPoints = drawAreaPoints,

                    labelColor = it.color,
                    labelTextSizePx = it.labelTextSize.toPx(),
                    scale = scale,
                )

            }

        }
    }
}

fun drawXaxisBottomScale(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    axisColor: Color,
    scaleLengSize: Float,
    axisStrokeSize: Float,
    scaleInterval: Float,
    scale: Float,
    yOffset: Float
) {
    drawScope.run {
//        val scaleInterval = 2f //鍒诲害涔嬮棿鐨勯棿闅?瀹為檯鏁版嵁闂撮殧
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / scaleInterval//鍒诲害涓暟
        val scaleIntervalSize = (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / scaleNum//鍒诲害闂撮殧锛屾崲绠楁垚px
        for (i in 0..scaleNum.toInt()) {
            val x = (drawAreaPoints.leftBottom.x + i * scaleIntervalSize)
            drawLine(
                start = Offset(x * scale, drawAreaPoints.leftBottom.y - yOffset),
                end = Offset(
                    x * scale,
                    drawAreaPoints.leftBottom.y + scaleLengSize - yOffset
                ),
                color = axisColor,
                strokeWidth = axisStrokeSize
            )
        }

    }
}

fun drawYAxisLeftInsideScale(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    axisColor: Color,
    scaleLengSize: Float,
    axisStrokeSize: Float,
    scaleInterval: Float,
    scale: Float,
    xOffset: Float
) {
    drawScope.run {
//        val scaleInterval = 2f //鍒诲害涔嬮棿鐨勯棿闅?瀹為檯鏁版嵁闂撮殧
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / scaleInterval//鍒诲害涓暟
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum//鍒诲害闂撮殧锛屾崲绠楁垚px
        for (i in 0..scaleNum.toInt()) {
            val y = (drawAreaPoints.leftBottom.y - i * scaleIntervalSize)
            drawLine(
                start = Offset(drawAreaPoints.leftBottom.x + xOffset, y),
                end = Offset(
                    drawAreaPoints.leftBottom.x + scaleLengSize * scale + xOffset,
                    y
                ),
                color = axisColor,
                strokeWidth = axisStrokeSize
            )
        }

    }
}

fun drawYAxisLeftScale(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    axisColor: Color,
    scaleLengSize: Float,
    axisStrokeSize: Float,
    scaleInterval: Float,
    scale: Float,
    xOffset: Float
) {
    drawScope.run {
//        val scaleInterval = 2f //鍒诲害涔嬮棿鐨勯棿闅?瀹為檯鏁版嵁闂撮殧
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / scaleInterval//鍒诲害涓暟
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum//鍒诲害闂撮殧锛屾崲绠楁垚px
        for (i in 0..scaleNum.toInt()) {
            val y = (drawAreaPoints.leftBottom.y - i * scaleIntervalSize)
            drawLine(
                start = Offset(drawAreaPoints.leftBottom.x + xOffset, y),
                end = Offset(
                    drawAreaPoints.leftBottom.x - scaleLengSize * scale + xOffset,
                    y
                ),
                color = axisColor,
                strokeWidth = axisStrokeSize
            )
        }

    }
}

fun drawYAxisRightScale(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    axisColor: Color,
    scaleLengSize: Float,
    axisStrokeSize: Float,
    scaleInterval: Float,
    scale: Float,
    xOffset: Float
) {
    drawScope.run {
//        val scaleInterval = 2f //鍒诲害涔嬮棿鐨勯棿闅?瀹為檯鏁版嵁闂撮殧
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / scaleInterval//鍒诲害涓暟
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum//鍒诲害闂撮殧锛屾崲绠楁垚px
        for (i in 0..scaleNum.toInt()) {
            val y = (drawAreaPoints.leftBottom.y - i * scaleIntervalSize)
            drawLine(
                start = Offset(drawAreaPoints.rightBottom.x + xOffset, y),
                end = Offset(
                    drawAreaPoints.rightBottom.x + scaleLengSize * scale + xOffset,
                    y
                ),
                color = axisColor,
                strokeWidth = axisStrokeSize
            )
        }

    }
}

fun drawYAxisRightInsideScale(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    axisColor: Color,
    scaleLengSize: Float,
    axisStrokeSize: Float,
    scaleInterval: Float,
    scale: Float,
    xOffset: Float
) {
    drawScope.run {
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / scaleInterval
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum
        for (i in 0..scaleNum.toInt()) {
            val y = (drawAreaPoints.leftBottom.y - i * scaleIntervalSize)
            drawLine(
                start = Offset(drawAreaPoints.rightBottom.x + xOffset, y),
                end = Offset(
                    drawAreaPoints.rightBottom.x - scaleLengSize * scale + xOffset,
                    y
                ),
                color = axisColor,
                strokeWidth = axisStrokeSize
            )
        }

    }
}


fun drawXaxisBottomLabel(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMin: Float,
    defaultXAxisMax: Float,
    labelColor: Color,
    labelInterval: Float,
    labelTextSizePx: Float = 24f,
    scale: Float,
    settingLabelValue: ((value: Float) -> String)?,
    yOffset: Float,
) {
    with(drawScope) {
        val scaleNum = (defaultXAxisMax - defaultXAxisMin) / labelInterval
        val scaleIntervalSize = (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / scaleNum

        // Create paint once
        val nativePaint = android.graphics.Paint().apply {
            textSize = labelTextSizePx
            color = labelColor.toArgb()
            isAntiAlias = true
        }

        val baseY = drawAreaPoints.leftBottom.y + labelTextSizePx + 4.dp.toPx() - yOffset

        // Precompute all text positions and labels
        val textEntries = (0..scaleNum.toInt()).map { i ->
            val labelValue =
                BigDecimal(defaultXAxisMin.toString()).add(
                    BigDecimal(labelInterval.toString()).multiply(
                        BigDecimal(i)
                    )
                ).toFloat()
            val labelText = settingLabelValue?.invoke(labelValue) ?: run {
                if (labelValue.isInteger()) labelValue.toInt().toString()
                else labelValue.toString()
            }
            val x =
                (drawAreaPoints.leftBottom.x + i * scaleIntervalSize - labelText.length * labelTextSizePx / 2 * 0.6f) * scale
            Pair(labelText, x)
        }

        // Draw all texts in one operation
        drawContext.canvas.nativeCanvas.apply {
            textEntries.forEach { (text, x) ->
                drawText(text, x, baseY, nativePaint)
            }
        }
    }
}

private fun Float.isInteger(): Boolean = this == toInt().toFloat()

fun drawXaxisBottomName(
    drawScope: DrawScope,
    name: String,
    drawAreaPoints: DrawAreaPoints,
    defaultXAxisMax: Float,
    labelColor: Color,
    labelTextSizePx: Float = 24f,
    scale: Float
) {
    drawScope.run {
        val nativePaint = Paint().let {
            it.apply {
                textSize = labelTextSizePx
                color = labelColor.toArgb()
                isAntiAlias = true//鎶楅敮榻?
            }
        }
        val label = when {
            defaultXAxisMax.toInt().toFloat() == defaultXAxisMax -> {//涓烘暣鏁版诞鐐规暟
                "${defaultXAxisMax.toInt()}"
            }

            else -> {//涓哄皬鏁版诞鐐规暟
                "${defaultXAxisMax}"
            }
        }
        val labelWidth = label.length * labelTextSizePx
        val offset = labelWidth / 2

        val x = drawAreaPoints.rightBottom.x + offset
        var y = drawAreaPoints.leftBottom.y + labelTextSizePx + 4.dp.toPx()
        val nameList = name.split("\n")
        nameList.forEach {
            drawContext.canvas.nativeCanvas.drawText(
                it,
                x,
                y,
                nativePaint
            )
            y += labelTextSizePx
        }


    }
}

fun drawYAxisLeftInsideName(
    drawScope: DrawScope,
    name: String,
    drawAreaPoints: DrawAreaPoints,
    labelColor: Color,
    labelTextSizePx: Float = 24f,
    scale: Float
) {

    drawScope.run {
        val nativePaint = Paint().let {
            it.apply {
                textSize = labelTextSizePx
                color = labelColor.toArgb()
                isAntiAlias = true//鎶楅敮榻?
            }
        }


        val x = drawAreaPoints.leftBottom.x + 8.dp.toPx()
        var y = drawAreaPoints.leftTop.y - labelTextSizePx
        val nameList = name.split("\n")
        for (i in nameList.size - 1 downTo 0) {
            drawContext.canvas.nativeCanvas.drawText(
                nameList[i],
                x,
                y,
                nativePaint
            )
            y -= labelTextSizePx
        }


    }
}

fun drawYAxisLeftName(
    drawScope: DrawScope,
    name: String,
    drawAreaPoints: DrawAreaPoints,
    labelColor: Color,
    labelTextSizePx: Float = 24f,
    scale: Float
) {

    drawScope.run {
        val nativePaint = Paint().let {
            it.apply {
                textSize = labelTextSizePx
                color = labelColor.toArgb()
                isAntiAlias = true//鎶楅敮榻?
            }
        }


        val x = 0f + 2.dp.toPx()
        var y = drawAreaPoints.leftTop.y - labelTextSizePx
        val nameList = name.split("\n")
        for (i in nameList.size - 1 downTo 0) {
            drawContext.canvas.nativeCanvas.drawText(
                nameList[i],
                x,
                y,
                nativePaint
            )
            y -= labelTextSizePx
        }

    }
}

fun drawYAxisRightName(
    drawScope: DrawScope,
    name: String,
    drawAreaPoints: DrawAreaPoints,
    labelColor: Color,
    labelTextSizePx: Float = 24f,
    scale: Float
) {

    drawScope.run {
        val nativePaint = Paint().let {
            it.apply {
                textSize = labelTextSizePx
                color = labelColor.toArgb()
                isAntiAlias = true//鎶楅敮榻?
            }
        }
        val x = drawAreaPoints.rightTop.x + 2.dp.toPx()
        var y = drawAreaPoints.rightTop.y - labelTextSizePx
        val nameList = name.split("\n")
        for (i in nameList.size - 1 downTo 0) {
            drawContext.canvas.nativeCanvas.drawText(
                nameList[i],
                x,
                y,
                nativePaint
            )
            y -= labelTextSizePx
        }


    }
}

fun drawYAxisRightInsideName(
    drawScope: DrawScope,
    name: String,
    drawAreaPoints: DrawAreaPoints,
    labelColor: Color,
    labelTextSizePx: Float = 24f,
    scale: Float
) {

    drawScope.run {
        val nativePaint = Paint().let {
            it.apply {
                textSize = labelTextSizePx
                color = labelColor.toArgb()
                isAntiAlias = true//鎶楅敮榻?
            }
        }
        val x = drawAreaPoints.rightTop.x - 8.dp.toPx() - name.length * labelTextSizePx * 0.6f
        var y = drawAreaPoints.rightTop.y - labelTextSizePx
        val nameList = name.split("\n")
        for (i in nameList.size - 1 downTo 0) {
            drawContext.canvas.nativeCanvas.drawText(
                nameList[i],
                x,
                y,
                nativePaint
            )
            y -= labelTextSizePx
        }


    }
}

fun drawYAxisLeftInsideLabel(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultYAxisMin: Float,
    defaultYAxisMax: Float,
    labelColor: Color,
    labelInterval: Float,
    labelTextSizePx: Float = 24f,
    scale: Float,
    settingLabelValue: ((value: Float) -> String)?,
    xOffset: Float,
) {
    with(drawScope) {
        val scaleNum = (defaultYAxisMax - defaultYAxisMin) / labelInterval
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum

        // 鍒涘缓骞堕厤缃甈aint瀵硅薄锛堝彧鍒涘缓涓€娆★級
        val textPaint = android.graphics.Paint().apply {
            textSize = labelTextSizePx
            color = labelColor.toArgb()
            isAntiAlias = true
        }

        // 棰勮绠楁墍鏈夋爣绛句綅缃?
        (0..scaleNum.toInt()).map { i ->
            val labelValue =
                BigDecimal(defaultYAxisMin.toString()).add(
                    BigDecimal(labelInterval.toString()).multiply(
                        BigDecimal(i)
                    )
                ).toFloat()
            val labelText = settingLabelValue?.invoke(labelValue) ?: formatLabel(labelValue)
            val x = drawAreaPoints.leftBottom.x + xOffset + 8.dp.toPx()
            val y = drawAreaPoints.leftBottom.y - i * scaleIntervalSize + labelTextSizePx / 4
            labelText to Point(x, y)
        }.let {
            drawContext.canvas.nativeCanvas.apply {
                it.forEach { (text, pos) ->
                    drawText(text, pos.x, pos.y, textPaint)
                }
            }
        }
    }
}

private fun formatLabel(value: Float): String {


    return if (value.isInteger()) value.toInt().toString() else value.toString()
}

fun drawYAxisLeftLabel(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultYAxisMin: Float,
    defaultYAxisMax: Float,
    labelColor: Color,
    labelInterval: Float,
    labelTextSizePx: Float = 24f,
    scale: Float,
    settingLabelValue: ((value: Float) -> String)?,
    xOffset: Float,
) {
    with(drawScope) {
        val scaleNum = (defaultYAxisMax - defaultYAxisMin) / labelInterval
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum

        val textPaint = android.graphics.Paint().apply {
            textSize = labelTextSizePx
            color = labelColor.toArgb()
            isAntiAlias = true
        }

        (0..scaleNum.toInt()).map { i ->
            val labelValue =
                BigDecimal(defaultYAxisMin.toString()).add(
                    BigDecimal(labelInterval.toString()).multiply(
                        BigDecimal(i)
                    )
                ).toFloat()
            val labelText = settingLabelValue?.invoke(labelValue) ?: formatLabel(
                labelValue
            )
            val textWidth = labelText.length * labelTextSizePx
            val x = drawAreaPoints.leftBottom.x + xOffset - 8.dp.toPx() - textWidth / 2
            val y = drawAreaPoints.leftBottom.y - i * scaleIntervalSize + labelTextSizePx * 0.3f
            labelText to Point(x, y)
        }.let {
            drawContext.canvas.nativeCanvas.apply {
                it.forEach { (text, pos) ->
                    drawText(text, pos.x, pos.y, textPaint)
                }
            }
        }


    }
}

fun drawYAxisRightLabel(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultYAxisMin: Float,
    defaultYAxisMax: Float,
    labelColor: Color,
    labelInterval: Float,
    labelTextSizePx: Float = 24f,
    scale: Float,
    settingLabelValue: ((value: Float) -> String)?,
    xOffset: Float,
) {
    with(drawScope) {
        val scaleNum = (defaultYAxisMax - defaultYAxisMin) / labelInterval
        val scaleIntervalSize = (drawAreaPoints.rightBottom.y - drawAreaPoints.rightTop.y) / scaleNum

        val textPaint = android.graphics.Paint().apply {
            textSize = labelTextSizePx
            color = labelColor.toArgb()
            isAntiAlias = true
        }

        (0..scaleNum.toInt()).map { i ->
            val labelValue =
                BigDecimal(defaultYAxisMin.toString()).add(
                    BigDecimal(labelInterval.toString()).multiply(
                        BigDecimal(i)
                    )
                ).toFloat()
            val labelText = settingLabelValue?.invoke(labelValue) ?: formatLabel(
                labelValue
            )
            val x = drawAreaPoints.rightBottom.x + xOffset + 8.dp.toPx()
            val y = drawAreaPoints.rightBottom.y - i * scaleIntervalSize + labelTextSizePx * 0.3f
            labelText to Point(x, y)
        }.let {
            drawContext.canvas.nativeCanvas.apply {
                it.forEach { (text, pos) ->
                    drawText(text, pos.x, pos.y, textPaint)
                }
            }
        }

    }
}

fun drawYAxisRightInsideLabel(
    drawScope: DrawScope,
    drawAreaPoints: DrawAreaPoints,
    defaultYAxisMin: Float,
    defaultYAxisMax: Float,
    labelColor: Color,
    labelInterval: Float,
    labelTextSizePx: Float = 24f,
    scale: Float,
    settingLabelValue: ((value: Float) -> String)?,
    xOffset: Float,
) {
    with(drawScope) {
        val scaleNum = (defaultYAxisMax - defaultYAxisMin) / labelInterval
        val scaleIntervalSize = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / scaleNum

        val textPaint = android.graphics.Paint().apply {
            textSize = labelTextSizePx
            color = labelColor.toArgb()
            isAntiAlias = true
        }

        (0..scaleNum.toInt()).map { i ->
            val labelValue =
                BigDecimal(defaultYAxisMin.toString()).add(
                    BigDecimal(labelInterval.toString()).multiply(
                        BigDecimal(i)
                    )
                ).toFloat()
            val labelText = settingLabelValue?.invoke(labelValue) ?: formatLabel(
                labelValue
            )
            val textWidth = labelText.length * labelTextSizePx
            val x = drawAreaPoints.rightBottom.x + xOffset - 8.dp.toPx() - textWidth / 2
            val y = drawAreaPoints.leftBottom.y - i * scaleIntervalSize + labelTextSizePx * 0.3f
            labelText to Point(x, y)
        }.let {
            drawContext.canvas.nativeCanvas.apply {
                it.forEach { (text, pos) ->
                    drawText(text, pos.x, pos.y, textPaint)
                }
            }
        }

    }
}


/**
 * @author Brian
 * @Description:鐢婚鑹插潡
 */
fun drawYChunk(
    drawScope: DrawScope,
    yChunkList: MutableList<Chunk>?,
    drawAreaPoints: DrawAreaPoints,
    yAxisMin: Float,
    yAxisMax: Float,

    ) {
    drawScope.run {


        val oneDataYPx =
            (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yAxisMax - yAxisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        yChunkList?.forEachIndexed { index, chunk ->
            val X1 = drawAreaPoints.leftBottom.x
            val X2 = drawAreaPoints.rightBottom.x
            val Y1 = drawAreaPoints.leftBottom.y - (chunk.start - yAxisMin) * oneDataYPx //
            val Y2 = drawAreaPoints.leftBottom.y - (chunk.end - yAxisMin) * oneDataYPx //

            drawRect(
                color = chunk.color,
                topLeft = Offset(x = drawAreaPoints.leftBottom.x, y = Y1),
                size = Size(X2 - X1, Y2 - Y1)
            )
        }


    }


}


/**
 * @author Brian
 * @Description:鐢荤嚎
 */
fun drawXLimitLine(
    drawScope: DrawScope,

    xLimitLineList: MutableList<LimitLine>? = null,//Y杞翠笂鐢荤嚎
    drawAreaPoints: DrawAreaPoints,
    axisMin: Float,
    axisMax: Float,
    scale: Float

) {
    drawScope.run {

        val oneDataXPx =
            (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (axisMax - axisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?

        xLimitLineList?.forEachIndexed { index, limitLine ->
            val X1 = drawAreaPoints.leftBottom.x + (limitLine.value - axisMin) * oneDataXPx //杞崲涓哄搴旂殑X Px
            val Y1 = drawAreaPoints.leftTop.y
            val Y2 = drawAreaPoints.leftBottom.y
            val widthPx = limitLine.width.toPx()
            val dashPathEffect = if (limitLine.isDashes) {
                PathEffect.dashPathEffect(floatArrayOf(10f, 4f), 4f)
            } else {
                null
            }
            val lineX = (X1 - widthPx / 4) * scale
            if (limitLine.selfDefinedValue != null) {
                limitLine.selfDefinedValue?.invoke(
                    drawScope,
                    Offset(x = lineX, y = Y1),
                    Offset(x = lineX, y = Y2),
                    limitLine
                )
            } else {

                drawLine(
                    start = Offset(x = lineX, y = Y1),
                    end = Offset(x = lineX, y = Y2),
                    color = limitLine.color,
                    pathEffect = dashPathEffect,
                    strokeWidth = widthPx
                )
                //鏂囧瓧
                var textSizePx = limitLine.textSize.toPx()
                val nativePaint = Paint().let {
                    it.apply {
                        textSize = textSizePx
                        color = limitLine.color.toArgb()
                        isAntiAlias = true//鎶楅敮榻?
                    }
                }


                drawContext.canvas.nativeCanvas.drawText(
                    limitLine.text,
                    X1 - (textSizePx / 2) * limitLine.text.length - widthPx - 4f,
                    Y1,
                    nativePaint
                )
            }

        }


    }


}


fun drawYLimitLine(
    drawScope: DrawScope,
    yLimitLineList: MutableList<LimitLine>? = null,//Y杞翠笂鐢荤嚎
    drawAreaPoints: DrawAreaPoints,
    axisMin: Float,
    axisMax: Float,
    scale: Float
) {
    drawScope.run {

        val oneDataYPx =
            (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (axisMax - axisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?

        yLimitLineList?.forEachIndexed { index, limitLine ->
            val X1 = drawAreaPoints.leftBottom.x
            val X2 = drawAreaPoints.rightBottom.x
            val Y1 = drawAreaPoints.leftBottom.y - (limitLine.value - axisMin) * oneDataYPx //杞崲涓哄搴旂殑Y Px

            //鐩寸嚎
            val dashPathEffect = if (limitLine.isDashes) {
                PathEffect.dashPathEffect(floatArrayOf(10f, 4f), 4f)
            } else {
                null
            }
            val widthPx = limitLine.width.toPx()
            val linY = Y1 - widthPx / 4

            if (limitLine.selfDefinedValue != null) {
                limitLine.selfDefinedValue?.invoke(
                    drawScope,
                    Offset(x = X1, y = linY),
                    Offset(x = X2, y = linY),
                    limitLine
                )
            } else {

                drawLine(
                    start = Offset(x = X1, y = linY),
                    end = Offset(x = X2, y = linY),
                    color = limitLine.color,
                    pathEffect = dashPathEffect,
                    strokeWidth = widthPx


                )
                //鏂囧瓧
                var textSizePx = limitLine.textSize.toPx()
                val nativePaint = Paint().let {
                    it.apply {
                        textSize = textSizePx
                        color = limitLine.color.toArgb()
                        isAntiAlias = true//鎶楅敮榻?
                    }
                }


                drawContext.canvas.nativeCanvas.drawText(
                    limitLine.text,
                    X2 - textSizePx * limitLine.text.length,
                    Y1 - widthPx / 4 - textSizePx / 2,
                    nativePaint
                )
            }
        }


    }
}

/**
 * @author Brian
 * @Description:鐢荤嚎
 */
fun drawXGridLine(
    drawScope: DrawScope,
    gridLine: GridLine? = null,//Y杞翠笂鐢荤嚎
    drawAreaPoints: DrawAreaPoints,
    xAxisMin: Float,
    xAxisMax: Float,
    scale: Float

) {
    drawScope.run {

        val oneDataXPx =
            (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (xAxisMax - xAxisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        gridLine?.let {
            val gridNum = ((xAxisMax - xAxisMin) / it.interval).toInt()
            it.interval * oneDataXPx
            for (i in 0..gridNum) {
                val X1 = drawAreaPoints.leftBottom.x + i * it.interval * oneDataXPx //杞崲涓哄搴旂殑X Px
                val Y1 = drawAreaPoints.leftTop.y
                val Y2 = drawAreaPoints.leftBottom.y
                val widthPx = it.width.toPx()
                val dashPathEffect = if (it.isDashes) {
                    PathEffect.dashPathEffect(floatArrayOf(10f, 4f), 4f)
                } else {
                    null
                }
                val lineX = (X1 - widthPx / 4) * scale
                drawLine(
                    start = Offset(x = lineX, y = Y1),
                    end = Offset(x = lineX, y = Y2),
                    color = it.color,
                    pathEffect = dashPathEffect,
                    strokeWidth = widthPx
                )


            }

        }


    }


}

fun drawYGridLine(
    drawScope: DrawScope,
    gridLine: GridLine? = null,//Y杞翠笂鐢荤嚎
    drawAreaPoints: DrawAreaPoints,
    xAxisMin: Float,
    xAxisMax: Float,
    scale: Float
) {
    drawScope.run {

        val oneDataYPx =
            (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (xAxisMax - xAxisMin) // X杞翠笂 1f鍗曚綅鏁版嵁鐐瑰搴旂殑px鏁?
        gridLine?.let {
            val gridNum = ((xAxisMax - xAxisMin) / it.interval).toInt()
            for (i in 0..gridNum) {
                val X1 = drawAreaPoints.leftBottom.x
                val X2 = drawAreaPoints.rightBottom.x
                val Y1 = drawAreaPoints.leftBottom.y - i * it.interval * oneDataYPx //杞崲涓哄搴旂殑Y Px

                //鐩寸嚎
                val dashPathEffect = if (it.isDashes) {
                    PathEffect.dashPathEffect(floatArrayOf(10f, 4f), 4f)
                } else {
                    null
                }
                val widthPx = it.width.toPx()
                drawLine(
                    start = Offset(x = X1, y = Y1 - widthPx / 4),
                    end = Offset(x = X2, y = Y1 - widthPx / 4),
                    color = it.color,
                    pathEffect = dashPathEffect,
                    strokeWidth = widthPx
                )

            }

        }


    }
}


fun drawGrideLine(
    drawScope: DrawScope,
    xAxis: Axis,
    yLeftInsideAxis: Axis? = null,
    yLeftAxis: Axis? = null,
    yRightInsideAxis: Axis? = null,
    yRightAxis: Axis? = null,
    drawAreaPoints: DrawAreaPoints,
    scale: Float
) {
    drawScope.run {
        xAxis.let {
            it.gridLine?.let { gridLine ->
                drawXGridLine(
                    drawScope = this,
                    gridLine = gridLine,
                    drawAreaPoints = drawAreaPoints,
                    xAxisMax = it.max,
                    xAxisMin = it.min,
                    scale = scale,
                )

            }


        }
        yLeftInsideAxis?.let {
            it.gridLine?.let { gridLine ->
                drawYGridLine(
                    drawScope = this,
                    gridLine = gridLine,
                    drawAreaPoints = drawAreaPoints,
                    xAxisMax = it.max,
                    xAxisMin = it.min,
                    scale = scale,
                )

            }
        }
        yLeftAxis?.let {
            it.gridLine?.let { gridLine ->
                drawYGridLine(
                    drawScope = this,
                    gridLine = gridLine,
                    drawAreaPoints = drawAreaPoints,
                    xAxisMax = it.max,
                    xAxisMin = it.min,
                    scale = scale,
                )

            }

        }
        yRightInsideAxis?.let {
            it.gridLine?.let { gridLine ->
                drawYGridLine(
                    drawScope = this,
                    gridLine = gridLine,
                    drawAreaPoints = drawAreaPoints,
                    xAxisMax = it.max,
                    xAxisMin = it.min,
                    scale = scale,
                )

            }

        }
        yRightAxis?.let {
            it.gridLine?.let { gridLine ->
                drawYGridLine(
                    drawScope = this,
                    gridLine = gridLine,
                    drawAreaPoints = drawAreaPoints,
                    xAxisMax = it.max,
                    xAxisMin = it.min,
                    scale = scale,
                )

            }
        }
    }

}


/**
 * 灏嗗儚绱燲鍧愭爣杞崲涓烘暟鎹甔鍧愭爣
 */
fun convertPixelToDataX(
    pixelX: Float,
    drawAreaPoints: DrawAreaPoints,
    xAxisMin: Float,
    xAxisMax: Float,
    scale: Float
): Float {
    val oneDataXPx = (drawAreaPoints.rightBottom.x - drawAreaPoints.leftBottom.x) / (xAxisMax - xAxisMin)
    val offsetXPx = xAxisMin * oneDataXPx
    val raw = (pixelX - drawAreaPoints.leftBottom.x + offsetXPx) / (oneDataXPx * scale)
    // 闄愬埗鍦?xAxis 鐨勮寖鍥村唴锛岄伩鍏嶈秺鐣岃Е鎽稿甫鏉ヨ秴鍑鸿酱鑼冨洿鐨?data 鍊?
    return raw.coerceIn(xAxisMin, xAxisMax)
}

/**
 * 灏嗗儚绱燳鍧愭爣杞崲涓烘暟鎹甕鍧愭爣锛堝崟涓猋杞达級
 */
fun convertPixelToDataY(
    pixelY: Float,
    drawAreaPoints: DrawAreaPoints,
    yLeftInsideAxis: Axis?,
    yLeftAxis: Axis?,
    yRightInsideAxis: Axis?,
    yRightAxis: Axis?
): Float {
    // 浼樺厛浣跨敤宸﹀唴杞达紝鍏舵宸﹀杞达紝鏈€鍚庡彸杞?
    val yAxis = yLeftInsideAxis ?: yLeftAxis ?: yRightInsideAxis ?: yRightAxis ?: return 0f
    val oneDataYPx = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yAxis.max - yAxis.min)
    val offsetYPx = yAxis.min * oneDataYPx
    val raw = (drawAreaPoints.leftBottom.y - pixelY + offsetYPx) / oneDataYPx
    // 闄愬埗鍦ㄥ搴?y 杞磋寖鍥村唴
    return raw.coerceIn(yAxis.min, yAxis.max)
}
/**
 * 灏嗗儚绱燳鍧愭爣杞崲涓烘暟鎹甕鍧愭爣锛堝崟涓猋杞达級
 */
fun convertPixelToDataY(
    pixelY: Float,
    drawAreaPoints: DrawAreaPoints,
    yLeftAxis: Axis?,
): Float {
    // 浼樺厛浣跨敤宸﹀唴杞达紝鍏舵宸﹀杞达紝鏈€鍚庡彸杞?
    val yAxis = yLeftAxis ?: return 0f
    val oneDataYPx = (drawAreaPoints.leftBottom.y - drawAreaPoints.leftTop.y) / (yAxis.max - yAxis.min)
    val offsetYPx = yAxis.min * oneDataYPx
    val raw = (drawAreaPoints.leftBottom.y - pixelY + offsetYPx) / oneDataYPx
    // 闄愬埗鍦ㄥ搴?y 杞磋寖鍥村唴
    return raw.coerceIn(yAxis.min, yAxis.max)
}
