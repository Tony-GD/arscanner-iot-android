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
import java.util.*


/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private val mWidgetItems: MutableList<DefaultScannersResponse> =
        ArrayList<DefaultScannersResponse>()
    private val items = mutableMapOf<String, DefaultScannersResponse>()
    private val mAppWidgetId: Int
    override fun onCreate() {

        FirebaseAPI.subscribeForMetrics("LrzW8eDJbBJu3FN830K4").observeForever {
            items[it.metricName] = it
        }
    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear()
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getViewAt(position: Int): RemoteViews {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
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
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        val extras = Bundle()
        extras.putInt(StackWidgetProvider.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.tv_progress_text, fillInIntent)
        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            println("Loading view $position")
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        // Return the remote views object.
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
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
                FirebaseAPI.subscribeForMetrics("LrzW8eDJbBJu3FN830K4").observeForever {
                    items[it.metricName] = it
                }
            }
        }
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }

    companion object {
        private const val mCount = 9
    }

    init {
        mAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }
}