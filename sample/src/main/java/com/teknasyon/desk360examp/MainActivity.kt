package com.teknasyon.desk360examp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity(), LifecycleOwner {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openContact = findViewById(R.id.openContactUs)
        openContact?.setOnClickListener {
            setupNavigation()
        }

        startService(Intent(this, MyService::class.java))
    }

    private fun setupNavigation() {

        Desk360Config.instance.context = this

        Desk360Constants.configure(
            appVersion = BuildConfig.VERSION_NAME,
            deviceToken = "deskt36012",
            appKey = "NjuP7NKerJYj5aWPHIFf72OQpXfmSlXW",
            appLanguage = "tr",
            isTest = true
        )

        Desk360Constants.openDesk360(this)
    }

    private fun getObj(): JSONObject {
        val student1 = JSONObject()
        try {
            student1.put("id", "3")
            student1.put("name", "NAME OF STUDENT")
            student1.put("year", "3rd")
            student1.put("curriculum", "Arts")
            student1.put("birthday", "5/5/1993")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return student1

    }

}