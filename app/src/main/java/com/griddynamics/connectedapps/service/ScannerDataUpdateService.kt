package com.griddynamics.connectedapps.service

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.DefaultScannersResponse
import com.griddynamics.connectedapps.model.SpecialScannerResponse
import com.griddynamics.connectedapps.repository.local.LocalStorage
import com.griddynamics.connectedapps.repository.local.LocalStorageImpl
import com.griddynamics.connectedapps.repository.network.firebase.FirebaseAPI
import com.griddynamics.connectedapps.ui.map.bottomsheet.BottomSheetDialogMetricsAdapter


private const val ACTION_START = "com.griddynamics.connectedapps.service.action.START"
private const val ACTION_STOP = "com.griddynamics.connectedapps.service.action.STOP"

private const val TAG: String = "ScannerDataUpdateServic"

class ScannerDataUpdateService : IntentService("ScannerDataUpdateService") {
    private lateinit var database: FirebaseFirestore
    private lateinit var localStorage: LocalStorage
    private val response = SpecialScannerResponse(0f, 0f, 0f, 0f)

    private val metrics = mutableMapOf<String, DefaultScannersResponse>()
    private val adapter =
        BottomSheetDialogMetricsAdapter(metrics.values)
    private lateinit var layoutManager: RecyclerView.LayoutManager


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_START -> {
                handleActionStart()
            }
            ACTION_STOP -> {
                handleActionStop()
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = prepareNotification()
            startForeground(191, notification)
        }
        localStorage = LocalStorageImpl()
        layoutManager = GridLayoutManager(applicationContext, 4)
        val widgetTrackedDevice = localStorage.getWidgetTrackedDevice()
        if (widgetTrackedDevice != null) {
            FirebaseAPI.subscribeForMetrics(widgetTrackedDevice)
                .observeForever {
//                    updateWidget(it)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun prepareNotification(): Notification {
        val CHANNEL_ID = "my_channel_01"
        val channel = NotificationChannel(
            CHANNEL_ID,
            "AIRO widget service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            channel
        )
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.gd_logo)
            .setContentTitle("Scanner updates")
            .setContentText("Updates are enabled").build()

    }


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStop() {
        stopSelf()
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionStart(context: Context) {
            val intent = Intent(context, ScannerDataUpdateService::class.java).apply {
                action = ACTION_START
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionStop(context: Context) {
            val intent = Intent(context, ScannerDataUpdateService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)

        }
    }
}
