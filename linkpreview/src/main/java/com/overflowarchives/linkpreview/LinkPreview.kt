package com.overflowarchives.linkpreview;

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class LinkPreview {
private val previewMetaData = PreviewMetaData()
private var responseListener: ResponseListener
private lateinit var url: String

constructor(responseListener: ResponseListener) {
    this.responseListener = responseListener
}

fun getPreview(url: String) {
    this.url = url
    CoroutineScope(Dispatchers.IO).launch {
        val doc = try {
            Jsoup.connect(url).get()
        } catch (e: Exception) {
            responseListener.onError(e)
            return@launch
        }

        //Get title
        var title = doc.select("meta[property=og:title]").attr("content")
        if (title.isNullOrEmpty()) {
            title = doc.title()
        }
        previewMetaData.setTitle(title)

        //Get description
        var description = doc.select("meta[name=description]").attr("content")
        if (description.isNullOrEmpty()) {
            description = doc.select("meta[property=og:description]").attr("content")
        }
        if (description.isNullOrEmpty()) {
            description = ""
        }
        previewMetaData.setDescription(description)

        //Get media type
        val mediaTypes = doc.select("meta[name=medium]")
        var type = ""
        if (mediaTypes.size > 0) {
            val media = mediaTypes.attr("content")
            type = if (media == "image") "photo" else media
        } else {
            type = doc.select("meta[property=og:type]").attr("content")
        }
        previewMetaData.setMediatype(type)

        //Get images
        val imageElements = doc.select("meta[property=og:image]")
        if (imageElements.size > 0) {
            val image = imageElements.attr("content")
            if (image.isNotEmpty()) {
                previewMetaData.setImageurl(resolveURL(url, image))
            }
        }
        if (previewMetaData.getImageurl().isEmpty()) {
            val src = doc.select("link[rel=image_src]").attr("href")
            if (src.isNotEmpty()) {
                previewMetaData.setImageurl(resolveURL(url, src))
            } else {
                val icon = doc.select("link[rel=icon]").attr("href")
                if (icon.isNotEmpty()) {
                    previewMetaData.setImageurl(resolveURL(url, icon))
                    previewMetaData.setFavicon(resolveURL(url, icon))
                }
            }
        }

        //Get favicon
        val favicon = doc.select("link[rel=icon]").attr("href")
        if (favicon.isNotEmpty()) {
            previewMetaData.setFavicon(resolveURL(url, favicon))
        }

        for (element in doc.getElementsByTag("meta")) {
            if (element.hasAttr("property")) {
                val property = element.attr("property")
                when (property) {
                    "og:url" -> previewMetaData.setUrl(element.attr("content"))
                    "og:site_name" -> previewMetaData.setSitename(element.attr("content"))
                }
            }
        }

        if (previewMetaData.getUrl().isEmpty()) {
            previewMetaData.setUrl(doc.location())
        }

        withContext(Dispatchers.Main) {
            responseListener.onData(previewMetaData)
        }
    }
}

private fun resolveURL(url: String, part: String): String {
    return if (part.startsWith("http://") || part.startsWith("https://")) {
        part
    } else {
        val baseURL = url.substringBeforeLast('/')
        "$baseURL/$part"
    }
}
}