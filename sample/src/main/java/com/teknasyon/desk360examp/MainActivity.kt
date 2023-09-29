package com.teknasyon.desk360examp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360examp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.btnOpenDesk360.setOnClickListener {
            Desk360SDKHelper.setup(
                this,
                binding.txtCountryCode.text.toString(),
                binding.txtLanguageCode.text.toString()
            )
            Desk360SDK.start()
        }

        binding.btnOpenDesk360WithTopic.setOnClickListener {
            Desk360SDKHelper.setup(
                this,
                binding.txtCountryCode.text.toString(),
                binding.txtLanguageCode.text.toString()
            )
            Desk360SDK.startWithTopic(binding.txtTopicId.text.toString())
        }
    }
}