package com.griddynamics.connectedapps.di.ui.cutom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import com.griddynamics.connectedapps.R
import com.progress.progressview.ProgressView
import kotlin.math.max

private const val TAG: String = "ColorAdaptedProgressVie"

class ColorAdaptedProgressView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ProgressView(context, attributeSet, defStyleAttr) {
    var maxValue = 1f
    fun setPvProgress(progress: Float) {
        setProgress(progress)
    }
    override fun setProgress(progress: Float) {
        var tempProgress = progress
        if (progress > maxValue) {
            Log.e(
                TAG,
                "ColorAdaptedProgressView: ",
                IllegalArgumentException("Progress [$progress] should not overhead maxValue [$maxValue]")
            )
            tempProgress = maxValue
        }
        val normalizedProgress: Float = 1 - (tempProgress / maxValue)
        val colors: IntArray = when (normalizedProgress) {
            in 0.1f..0.25f -> intArrayOf(Color.LTGRAY, Color.BLUE)
            in 0.25f..0.5f -> intArrayOf(Color.BLUE, Color.GREEN)
            in 0.5f..0.75f -> intArrayOf(Color.GREEN, Color.YELLOW)
            in 0.75f..1f -> intArrayOf(Color.YELLOW, Color.RED)
            else -> intArrayOf(
                context.getColor(R.color.transparent),
                context.getColor(R.color.transparent)
            )
        }
        super.setProgress(normalizedProgress)
        applyGradient(colors)
    }
}