package com.overflowarchives.linkpreviewdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.overflowarchives.linkpreview.ViewListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

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

        link_preview_plain_link_preview.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })

        link_preview_whatsapp.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })

        link_preview_skype.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })

        link_preview_telegram.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })

        link_preview_twitter.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })
    }
}