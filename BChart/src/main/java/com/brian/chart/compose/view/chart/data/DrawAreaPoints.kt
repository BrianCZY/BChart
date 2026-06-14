package com.brian.chart.compose.view.chart.data

import com.brian.chart.compose.view.chart.data.Point

data class DrawAreaPoints(
    val leftBottom: Point = Point(),//左下角
    val rightBottom: Point = Point(),//右下角
    val rightTop: Point = Point(),//右上角
    val leftTop: Point = Point(),//左上角
)