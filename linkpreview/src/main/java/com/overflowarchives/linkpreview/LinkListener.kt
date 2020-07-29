package com.overflowarchives.linkpreview

import android.view.View

interface LinkListener {
    fun onClicked(view: View?, meta: PreviewMetaData?)
}