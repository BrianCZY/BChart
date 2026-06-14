package com.brian.chart.compose.view.chart.data

import androidx.compose.ui.graphics.Color

//TODO 配置chunk可以渐变色
data class Chunk(var start: Float, var end: Float, var color: Color = Color(0xffEAF1FA))