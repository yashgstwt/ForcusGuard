package com.example.focusguard

import android.Manifest
import android.app.usage.UsageStatsManager
import android.content.Intent

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.focusguard.UiLayer.Screens.utility.AppCardData
import com.example.focusguard.UiLayer.Screens.utility.formatTime
import com.example.focusguard.UiLayer.Screens.utility.getAppName
import com.example.focusguard.UiLayer.Screens.utility.getLauncherApps
import com.example.focusguard.UiLayer.UIComponents.AppCard
import com.example.focusguard.service.AppLaunchMonitoringService
import com.example.focusguard.ui.theme.FocusGuardTheme
import java.util.Calendar
import java.util.TimeZone


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.POST_NOTIFICATIONS ) ,0)
        }

        val intent = Intent(this, AppLaunchMonitoringService::class.java)
        startService(intent)

        setContent {

            FocusGuardTheme {
//                Go to Settings: Open the device's settings app.
//                Find Usage Access: Navigate to the "Security" or "Privacy" section (the exact location may vary depending on the Android version and device manufacturer). Look for an option called "Usage access" or "Apps with usage access."
//                Grant Permission: Find your app in the list and enable the toggle to grant it access to usage statistics.


                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)) { innerPadding ->

                    val usageStatsManager: UsageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.set(Calendar.HOUR_OF_DAY,0)
                    calendar.set(Calendar.MINUTE,0)
                    calendar.set(Calendar.SECOND,0)

                    val startTime = calendar.timeInMillis
//                    Log.d("AppUsageyash","start:${formatTime(startTime)}")
                    val endTime = System.currentTimeMillis()
//                    Log.d("AppUsageyash","end:${formatTime(endTime)}")


                    val packageManager =  applicationContext.packageManager
                    val launcherApps = getLauncherApps(applicationContext)
                    val launcherAppsMap = launcherApps.associateBy { it.packageName }.toMap()
                    val usageStatsList = usageStatsManager.queryUsageStats( UsageStatsManager.INTERVAL_DAILY,startTime,endTime).associateBy { it.packageName }


                    val appListUi:MutableList<AppCardData> = mutableListOf()

                   // Log.d("AppUsageyash","keys:${launcherAppsMap.keys }\n total:${launcherAppsMap.size} \n values:${launcherAppsMap.values}")


                    for (stats in  launcherAppsMap.keys) {

                        launcherAppsMap[stats]?.let{

                            val logo = packageManager.getApplicationIcon(it.packageName).toBitmap(300,300).asImageBitmap()
                            val packageName = it.packageName
                            val appName = getAppName(packageName,packageManager)
                            val usageTimeMillis = usageStatsList[stats]?.totalTimeInForeground ?: 0
                            val usageTimeString = formatTime(usageTimeMillis)

//                            Log.d(
//                                "AppUsageyash",
//                                " name : $appName , package name : $packageName  , usage time : $usageTimeString"
//                            )
                            appListUi.add(AppCardData(logo,appName,usageTimeString,packageName))

                        }

                    }

                    appListUi.sortByDescending { it.appUsageDuration }
                    LazyColumn(modifier = Modifier.padding(innerPadding)
                        .fillMaxSize()
                        .background(Color.Black)) {
                        items( items = appListUi , key = {app-> app.packageName}){
                                app->

                            AppCard(app.appName,app.logo,app.appUsageDuration)

                        }
                    }
                }
            }
        }
    }
}

