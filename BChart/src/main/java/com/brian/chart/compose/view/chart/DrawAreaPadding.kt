package com.brian.chart.compose.view.chart

import androidx.compose.ui.unit.Dp

/**
 *@author Brian
 *@Description:绘图区域的padding
 * 这个padding空间通常是label scale
 */
class DrawAreaPadding(
    var start: Dp? = null,
    var top: Dp? = null,
    var end: Dp? = null,
    var bottom: Dp? = null,
) {

    fun padding(all: Dp) = DrawAreaPadding(
        start = all,
        top = all,
        end = all,
        bottom = all
    )

    fun padding(start: Dp?, top: Dp?, end: Dp?, bottom: Dp?) = DrawAreaPadding(
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}
