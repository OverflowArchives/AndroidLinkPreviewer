package com.overflowarchives.linkpreviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        load_button.setOnClickListener {
            if(!link_input.text.toString().isEmpty()){
                loadUrl(link_input.text.toString())
            }
        }
    }

    private fun loadUrl(loadUrl: String) {

    }
}