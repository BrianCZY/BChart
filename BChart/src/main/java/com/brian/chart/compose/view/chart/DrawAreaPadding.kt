package com.brian.chart.compose.view.chart

import androidx.compose.ui.unit.Dp

/**
 *@author Brian
 *@Description:绘图区域的padding
 * 这个padding空间通常是label scale
 */
class DrawAreaPadding(
    val start: Dp? = null,
    val top: Dp? = null,
    val end: Dp? = null,
    val bottom: Dp? = null,
) {
    companion object {
        /**
         * 创建一个所有边都相同的padding
         */
        fun all(all: Dp): DrawAreaPadding = DrawAreaPadding(
            start = all,
            top = all,
            end = all,
            bottom = all
        )

        /**
         * 创建水平和垂直方向相同的padding
         */
        fun horizontal(horizontal: Dp): DrawAreaPadding = DrawAreaPadding(
            start = horizontal,
            end = horizontal
        )

        /**
         * 创建垂直方向相同的padding
         */
        fun vertical(vertical: Dp): DrawAreaPadding = DrawAreaPadding(
            top = vertical,
            bottom = vertical
        )

        /**
         * 为各个方向单独指定padding值
         */
        fun only(
            start: Dp? = null,
            top: Dp? = null,
            end: Dp? = null,
            bottom: Dp? = null
        ): DrawAreaPadding = DrawAreaPadding(
            start = start,
            top = top,
            end = end,
            bottom = bottom
        )
    }

    /**
     * 修改当前padding的某个方向的值
     */
    fun copy(
        start: Dp? = this.start,
        top: Dp? = this.top,
        end: Dp? = this.end,
        bottom: Dp? = this.bottom
    ): DrawAreaPadding = DrawAreaPadding(
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}

// 扩展函数，用于更自然的DSL风格
fun DrawAreaPadding?.orDefault(): DrawAreaPadding = this ?: DrawAreaPadding()

// 便捷的顶层函数
fun DrawAreaPadding(all: Dp) = DrawAreaPadding.all(all)
fun DrawAreaPadding(horizontal: Dp, vertical: Dp) = DrawAreaPadding(
    start = horizontal,
    top = vertical,
    end = horizontal,
    bottom = vertical
)