package com.example.focusguard.UiLayer.Screens.utility

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap


fun getAppName(packageName: String , packageManager: PackageManager): String {


    try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        return packageManager.getApplicationLabel(appInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        return packageName
    }
}

fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    val hours = (milliseconds / (1000 * 60 * 60)) % 24
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun getLauncherApps(context: Context): List<ApplicationInfo> {
    val packageManager = context.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0)
    val launcherApps = mutableListOf<ApplicationInfo>()
    for (resolveInfo in resolveInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            launcherApps.add(appInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            // Handle the case where the package is not found
        }
    }
    return launcherApps
}

data class AppCardData (val logo:ImageBitmap , val appName : String , val appUsageDuration:String , val packageName:String)