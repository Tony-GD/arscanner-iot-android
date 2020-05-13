package com.griddynamics.connectedapps.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.griddynamics.connectedapps.R

/**
 * Implementation of App Widget functionality.
 */
const val AIR_SCANNER_WIDGET_INTENT_ID = "android.appwidget.action.APPWIDGET_UPDATE"
private const val TAG: String = "AirScannerSubscriptionW"

class AirScannerSubscriptionWidget : AppWidgetProvider() {

    companion object {
        private var APP_WIDGET_MANAGER: AppWidgetManager? = null
    }

    private fun Bundle.toShortString(): String {
        val builder = StringBuilder()
        this.keySet().forEach {
            builder.append(get(it)).append("; ")
        }
        return builder.toString()
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate() called with: context = [$context], appWidgetManager = [$appWidgetManager], appWidgetIds = [$appWidgetIds]")
        APP_WIDGET_MANAGER = appWidgetManager
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.air_scanner_subscription_widget)
    views.setTextViewText(R.id.appwidget_humidity_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}