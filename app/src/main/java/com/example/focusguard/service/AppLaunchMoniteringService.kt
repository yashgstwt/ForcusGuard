package com.example.focusguard.service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.example.focusguard.R


class AppLaunchMonitoringService:Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//     if (intent.action){
//
//     }
        startForeground()
        Log.d("FocusGuardService","Service Started  and onStartCommand is executed ")
        return super.onStartCommand(intent, flags, startId)

    }


    private fun startForeground() {
        // Before starting the service as foreground check that the app has the
        // appropriate runtime permissions. In this case, verify that the user has
        // granted the FOREGROUND permission.
        val foreGroundPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)

        if (foreGroundPermission != PermissionChecker.PERMISSION_GRANTED) {
            // Without FOREGROUND permissions the service cannot run in the foreground
            // Consider informing user or updating your app UI if visible.
            stopSelf()
            return
        }

        try {
            val notification = NotificationCompat.Builder(this, "RUNNING_CHANNEL")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("the service is running ")
                .setContentTitle("FocusGuard")
                .build()

            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 10,
                /* notification = */ notification,
                /* foregroundServiceType = */
                if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            Log.d("ServiceError","ForegroundServiceStartNotAllowedException **************")
            }
            // ...
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            while(true){
//            delay(1000)
//            Log.d("FocusGuardService","Service is running")
//        }

    }
}