package com.brian.chart.compose.view.chart.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
*@author Brian
*@Description: 网格线
*/
data class GridLine(
    var interval: Float = 1f,//间隔
    var isDashes: Boolean = false, //是否虚线
    var color: Color = Color.Companion.LightGray,//颜色
    var width: Dp = 1.dp,//线条宽度

    )