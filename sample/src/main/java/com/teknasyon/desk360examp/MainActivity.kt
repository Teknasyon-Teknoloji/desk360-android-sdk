package com.teknasyon.desk360examp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.teknasyon.desk360.view.fragment.BaseFragment

class MainActivity : AppCompatActivity() {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)
        openContact = findViewById(R.id.openContactUs)
        openContact?.setOnClickListener {
            setupNavigation()
        }
    }

    private fun setupNavigation() {
        val fragment = BaseFragment.newInstance() as Fragment
        val tag = fragment.javaClass.name
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }
}
