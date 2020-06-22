package com.griddynamics.connectedapps.util

import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import com.griddynamics.connectedapps.R

fun getMapColorFilter(destinationColor: Int): ColorMatrixColorFilter {
    val inverseMatrix = ColorMatrix(
        floatArrayOf(
            -1.0f, 0.0f, 0.0f, 0.0f, 255f,
            0.0f, -1.0f, 0.0f, 0.0f, 255f,
            0.0f, 0.0f, -1.0f, 0.0f, 255f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
        )
    )


    val lr: Float = (255.0f - Color.red(destinationColor)) / 255.0f
    val lg: Float = (255.0f - Color.green(destinationColor)) / 255.0f
    val lb: Float = (255.0f - Color.blue(destinationColor)) / 255.0f
    val grayscaleMatrix = ColorMatrix(
        floatArrayOf(
            lr, lg, lb, 0f, 0f,
            lr, lg, lb, 0f, 0f,
            lr, lg, lb, 0f, 0f, 0f, 0f, 0f, 0f, 255f
        )
    )
    grayscaleMatrix.preConcat(inverseMatrix)
    val dr: Int = Color.red(destinationColor)
    val dg: Int = Color.green(destinationColor)
    val db: Int = Color.blue(destinationColor)
    val drf = dr / 255f
    val dgf = dg / 255f
    val dbf = db / 255f
    val tintMatrix = ColorMatrix(
        floatArrayOf(
            drf, 0f, 0f, 0f, 0f, 0f, dgf, 0f, 0f, 0f, 0f, 0f, dbf, 0f, 0f, 0f, 0f, 0f, 1f, 0f
        )
    )
    tintMatrix.preConcat(grayscaleMatrix)
    val lDestination = drf * lr + dgf * lg + dbf * lb
    val scale = 1f - lDestination
    val translate = 1 - scale * 0.5f
    val scaleMatrix = ColorMatrix(
        floatArrayOf(
            scale,
            0f,
            0f,
            0f,
            dr * translate,
            0f,
            scale,
            0f,
            0f,
            dg * translate,
            0f,
            0f,
            scale,
            0f,
            db * translate,
            0f,
            0f,
            0f,
            1f,
            0f
        )
    )
    scaleMatrix.preConcat(tintMatrix)
    return ColorMatrixColorFilter(scaleMatrix)
}

fun getColorByProgress(context: Context, normalizedProgress: Float): IntArray {
    return when (normalizedProgress) {
        in 0.1f..0.25f -> intArrayOf(Color.LTGRAY, Color.BLUE)
        in 0.25f..0.5f -> intArrayOf(Color.BLUE, Color.GREEN)
        in 0.5f..0.75f -> intArrayOf(Color.GREEN, Color.YELLOW)
        in 0.75f..1f -> intArrayOf(Color.YELLOW, Color.RED)
        else -> intArrayOf(
            context.getColor(R.color.transparent),
            context.getColor(R.color.colorAccent)
        )
    }
}