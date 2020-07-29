package com.overflowarchives.linkpreview

interface ResponseListener {
    fun onData(previewMetaData: PreviewMetaData?)
    fun onError(e: Exception?)
}