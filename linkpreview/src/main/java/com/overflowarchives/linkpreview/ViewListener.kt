package com.overflowarchives.linkpreview

interface ViewListener {
    fun onPreviewSuccess(status: Boolean)
    fun onFailedToLoad(e: Exception?)
}