package id.standherealone.yuubaca.core.components

import android.os.Build
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.telephony.TelephonyManager
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

object DeviceInfo {
    fun getAllDeviceInfo(context: Context, fromDialog: Boolean): String {
        var stringBuilder = StringBuilder()
        if (!fromDialog) stringBuilder = StringBuilder("\n\n ==== SYSTEM-INFO ===\n\n")
        stringBuilder.append(
            """
 Perangkat: ${getDeviceInfo(context, Device.DEVICE_SYSTEM_VERSION)}"""
        )
        stringBuilder.append(
            """
 Versi SDK: ${getDeviceInfo(context, Device.DEVICE_VERSION)}"""
        )
        stringBuilder.append(
            """
 Versi Aplikasi: ${getAppVersion(context)}"""
        )
        stringBuilder.append(
            """
 Bahasa: ${getDeviceInfo(context, Device.DEVICE_LANGUAGE)}"""
        )
        stringBuilder.append(
            """
 Waktu: ${getDeviceInfo(context, Device.DEVICE_TIME_ZONE)}"""
        )
        stringBuilder.append(
            """
 Total Memori: ${getDeviceInfo(context, Device.DEVICE_TOTAL_MEMORY)}"""
        )
        stringBuilder.append(
            """
 Memori Bebas: ${getDeviceInfo(context, Device.DEVICE_FREE_MEMORY)}"""
        )
        stringBuilder.append(
            """
 Tipe Perangkat: ${getDeviceInfo(context, Device.DEVICE_TYPE)}"""
        )
        stringBuilder.append(
            """
 Tipe Data: ${getDataType(context)}"""
        )
        return stringBuilder.toString()
    }

    fun getDeviceInfo(activity: Context, device: Device?): String {
        try {
            when (device) {
                Device.DEVICE_LANGUAGE -> return Locale.getDefault().displayLanguage
                Device.DEVICE_TIME_ZONE -> return TimeZone.getDefault().id //(false, TimeZone.SHORT);
                Device.DEVICE_TOTAL_MEMORY -> {
                    return if (Build.VERSION.SDK_INT >= 16) getTotalMemory(activity).toString() else getFreeMemory(
                        activity
                    ).toString()
                }
                Device.DEVICE_FREE_MEMORY -> return getFreeMemory(activity).toString()
                Device.DEVICE_SYSTEM_VERSION -> return deviceName
                Device.DEVICE_VERSION -> return "SDK " + Build.VERSION.SDK_INT
                Device.DEVICE_TYPE -> return if (isTablet(activity)) {
                    if (getDeviceMoreThan5Inch(activity)) {
                        "Tablet"
                    } else "Mobile"
                } else {
                    "Mobile"
                }
                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("NewApi")
    private fun getTotalMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.totalMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getFreeMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.availMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private val deviceName: String
        private get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getDeviceMoreThan5Inch(activity: Context): Boolean {
        return try {
            val displayMetrics = activity.resources.displayMetrics
            // int width = displayMetrics.widthPixels;
            // int height = displayMetrics.heightPixels;
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
            // 5inch device or bigger
            // smaller device
            diagonalInches >= 7
        } catch (e: Exception) {
            false
        }
    }

    fun getDataType(activity: Context): String {
        var type = "Mobile Data"
        val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (tm.networkType) {
            TelephonyManager.NETWORK_TYPE_HSDPA -> {
                type = "Mobile Data 3G"
                Log.d("Type", "3g")
            }
            TelephonyManager.NETWORK_TYPE_HSPAP -> {
                type = "Mobile Data 4G"
                Log.d("Type", "4g")
            }
            TelephonyManager.NETWORK_TYPE_GPRS -> {
                type = "Mobile Data GPRS"
                Log.d("Type", "GPRS")
            }
            TelephonyManager.NETWORK_TYPE_EDGE -> {
                type = "Mobile Data EDGE 2G"
                Log.d("Type", "EDGE 2g")
            }
        }
        return type
    }

    fun getAppVersion(context: Context): String {
        var pInfo: PackageInfo? = null
        var version = " "
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    enum class Device {
        DEVICE_TYPE, DEVICE_VERSION, DEVICE_SYSTEM_VERSION,

        /**
         *
         */
        DEVICE_LANGUAGE, DEVICE_TIME_ZONE,

        /**
         *
         */
        DEVICE_TOTAL_MEMORY, DEVICE_FREE_MEMORY
    }
}