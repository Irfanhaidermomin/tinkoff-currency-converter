package ru.tinkoff.converter.feature.converter.drawable

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable

class BackgroundDrawable(color: Int) : ColorDrawable(color) {
    override fun draw(canvas: Canvas) {
        val bounds = bounds
        canvas.save()
        canvas.clipRect(bounds.left, bounds.top, bounds.right, bounds.bottom / 2)
        super.draw(canvas)
        canvas.restore()
    }
}