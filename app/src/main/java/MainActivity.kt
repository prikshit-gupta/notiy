// MainActivity.kt

package com.notiy.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("NotiY", Context.MODE_PRIVATE)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL

        val accessButton = Button(this)
        accessButton.text = "Enable Notification Access"

        accessButton.setOnClickListener {

            startActivity(
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            )
        }

        root.addView(accessButton)

        val scrollView = ScrollView(this)

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL

        val pm = packageManager

        val apps = pm.getInstalledApplications(
            PackageManager.GET_META_DATA
        )

        for (app in apps) {

            if (pm.getLaunchIntentForPackage(app.packageName) == null)
                continue

            val appName =
                pm.getApplicationLabel(app).toString()

            val packageName = app.packageName

            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            val text = TextView(this)
            text.text = appName

            text.layoutParams =
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

            val toggle = Switch(this)

            val enabled =
                prefs.getBoolean(packageName, true)

            toggle.isChecked = enabled

            toggle.setOnCheckedChangeListener { _, isChecked ->

                prefs.edit()
                    .putBoolean(packageName, isChecked)
                    .apply()
            }

            row.addView(text)
            row.addView(toggle)

            container.addView(row)
        }

        scrollView.addView(container)

        root.addView(scrollView)

        setContentView(root)
    }
}
