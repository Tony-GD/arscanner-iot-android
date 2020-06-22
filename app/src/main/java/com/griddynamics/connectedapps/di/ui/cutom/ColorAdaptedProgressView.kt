package com.griddynamics.connectedapps.di.ui.cutom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.griddynamics.connectedapps.util.getColorByProgress
import com.progress.progressview.ProgressView

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
        val colors: IntArray = getColorByProgress(context, normalizedProgress)
        super.setProgress(normalizedProgress)
        applyGradient(colors)
    }
}