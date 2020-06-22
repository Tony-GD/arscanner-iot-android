package com.griddynamics.connectedapps.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.repository.local.LocalStorageImpl
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.util.getColorByProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent)
    }
}

internal class StackRemoteViewsFactory(
    private val mContext: Context,
    intent: Intent
) :
    RemoteViewsFactory {
    private val localStorage = LocalStorageImpl()
    private val items = mutableMapOf<String, DefaultScannersResponse>()
    private val mAppWidgetId: Int = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )

    override fun onCreate() {
        subscribeFirebase()
    }

    private fun subscribeFirebase() {
        localStorage.getWidgetTrackedDevice()?.let { trackId ->
            FirebaseAPI.subscribeForMetrics(trackId).observeForever {
                items[it.metricName] = it
            }
        }
    }

    override fun onDestroy() {
        items.clear()
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= items.size) {
            return RemoteViews(mContext.packageName, R.layout.widget_item)
        }
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val defaultScannersResponse = items.values.toTypedArray()[position]
        val normalized: Float =
            1 - (defaultScannersResponse.value / defaultScannersResponse.maxValue)
        rv.setTextViewText(R.id.widget_item_label, defaultScannersResponse.metricName)
        rv.setTextColor(
            R.id.widget_item_value, getColorByProgress(mContext, normalized).last()
        )
        rv.setTextViewText(
            R.id.widget_item_value,
            defaultScannersResponse.value.toString()
        )
        val extras = Bundle()
        extras.putInt(StackWidgetProvider.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.tv_progress_text, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                subscribeFirebase()
            }
        }
    }

    companion object {
        private const val mCount = 9
    }

}