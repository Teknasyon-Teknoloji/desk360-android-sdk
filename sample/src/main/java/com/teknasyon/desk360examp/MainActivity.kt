package com.teknasyon.desk360examp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import org.json.JSONException
import org.json.JSONObject



class MainActivity : AppCompatActivity(), LifecycleOwner {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openContact = findViewById(R.id.openContactUs)
        getObj()
        openContact?.setOnClickListener { setupNavigation() }
    }

    private fun setupNavigation() {
        Desk360Config.instance.context = this


        Desk360Constants.desk360CurrentTheme(1)
        Desk360Constants.desk360Config(
            app_key = BuildConfig.APP_KEY,
            app_version = BuildConfig.VERSION_NAME,
            baseURL = "http://52.59.142.138:10380/",
            device_token = "deskt36012"
        )

        startActivity(Intent(this, Desk360BaseActivity::class.java))
    }

    private fun getObj(): JSONObject {
        val student1 = JSONObject()
        try {
            student1.put("id", "3")
            student1.put("name", "NAME OF STUDENT")
            student1.put("year", "3rd")
            student1.put("curriculum", "Arts")
            student1.put("birthday", "5/5/1993")
        } catch (e: JSONException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return student1

    }

}