package com.example.focusguard.UiLayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AppLaunchBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_MAIN && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            // Handle app launch event here
            val launchedPackageName = intent.data?.schemeSpecificPart
            Log.d("AppLaunch", "App Launched: $launchedPackageName")
        }
    }
}