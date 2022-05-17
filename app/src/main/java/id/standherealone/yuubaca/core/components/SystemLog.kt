package id.standherealone.yuubaca.core.components

import android.os.Process
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

object SystemLog {
    fun extractLogToString(): String {
        val result = StringBuilder("\n\n ==== SYSTEM-LOG ===\n\n")
        val pid = Process.myPid()
        try {
            val command = String.format("logcat -d -v threadtime *:*")
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var currentLine: String?
            while (reader.readLine().also { currentLine = it } != null) {
                if (currentLine != null && currentLine!!.contains(pid.toString())) {
                    result.append(currentLine)
                    result.append("\n")
                }
            }
            //Runtime.getRuntime().exec("logcat -d -v time -f "+file.getAbsolutePath());
        } catch (e: IOException) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


        //clear the log
        try {
            Runtime.getRuntime().exec("logcat -c")
        } catch (e: IOException) {
            // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return result.toString()
    }
}