package com.alamkanak.weekview

import android.graphics.Canvas
import android.graphics.Paint

internal interface Updater {
    fun update(drawingContext: DrawingContext)
}

internal interface Drawer {
    fun draw(
        drawingContext: DrawingContext,
        canvas: Canvas,
        paint: Paint
    ) = Unit
}

internal interface CachingDrawer {
    fun clear()
}