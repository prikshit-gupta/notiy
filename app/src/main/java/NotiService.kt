// NotiService.kt

package com.notiy.app

import android.app.Notification
import android.content.Context
import android.os.Environment
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NotiService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        try {

            val prefs =
                getSharedPreferences(
                    "NotiY",
                    Context.MODE_PRIVATE
                )

            val packageName = sbn.packageName

            val enabled =
                prefs.getBoolean(packageName, true)

            val extras = sbn.notification.extras

            val title =
                extras.getString(Notification.EXTRA_TITLE)
                    ?: "No Title"

            val text =
                extras.getCharSequence(Notification.EXTRA_TEXT)
                    ?.toString()
                    ?: "No Text"

            val time = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            val log = """

Time: $time
App: $packageName
Title: $title
Text: $text
Blocked: $enabled

=================================

            """.trimIndent()

            saveNotification(log)

            if (enabled) {

                cancelNotification(sbn.key)

            }

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun saveNotification(data: String) {

        try {

            val folder = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                "NotiY"
            )

            if (!folder.exists()) {

                folder.mkdirs()
            }

            val file =
                File(folder, "notifications.txt")

            file.appendText(data + "\n")

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}
