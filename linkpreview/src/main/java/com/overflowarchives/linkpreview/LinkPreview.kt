package com.overflowarchives.linkpreview

import android.webkit.URLUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class LinkPreview(private val responseListener: ResponseListener) {

    fun getPreview(url: String) = CoroutineScope(Dispatchers.IO).launch {
        when {
            URLUtil.isValidUrl(url) -> {
                val doc = try {
                    Jsoup.connect(url).get()
                } catch (e: Exception) {
                    responseListener.onError(e)
                    return@launch
                }

                var previewMetaData = PreviewMetaData()

                // Get title
                var title = doc.select("meta[property=og:title]").attr("content")
                if (title.isNullOrEmpty()) title = doc.title()

                previewMetaData = previewMetaData.copy(title = title)

                // Get description
                var description = doc.select("meta[name=description]").attr("content")
                if (description.isNullOrEmpty()) description =
                    doc.select("meta[property=og:description]").attr("content")
                previewMetaData = previewMetaData.copy(description = description)

                // Get media type
                val mediaTypes = doc.select("meta[name=medium]")
                val type: String = when {
                    mediaTypes.isNotEmpty() -> {
                        val media = mediaTypes.attr("content")
                        if (media == "image") "photo" else media
                    }

                    else -> doc.select("meta[property=og:type]").attr("content")
                }

                previewMetaData = previewMetaData.copy(mediatype = type)

                // Get images
                val imageElements = doc.select("meta[property=og:image]")

                when {
                    imageElements.isNotEmpty() -> {
                        val image = imageElements.attr("content")
                        if (image.isNotEmpty()) previewMetaData =
                            previewMetaData.copy(imageurl = resolveURL(url, image))
                    }

                    previewMetaData.imageurl.isEmpty() -> {
                        val src = doc.select("link[rel=image_src]").attr("href")
                        if (src.isNotEmpty()) previewMetaData =
                            previewMetaData.copy(imageurl = resolveURL(url, src))
                        else {
                            val icon = doc.select("link[rel=icon]").attr("href")
                            if (icon.isNotEmpty()) previewMetaData = previewMetaData.copy(
                                imageurl = resolveURL(url, icon), favicon = resolveURL(url, icon)
                            )
                        }
                    }
                }

                // Get favicon
                val favicon = doc.select("link[rel=icon]").attr("href")
                if (favicon.isNotEmpty()) previewMetaData =
                    previewMetaData.copy(favicon = resolveURL(url, favicon))

                doc.getElementsByTag("meta").forEach { element ->
                    if (element.hasAttr("property")) {
                        val property = element.attr("property")
                        when (property) {
                            "og:url" -> previewMetaData =
                                previewMetaData.copy(url = element.attr("content"))

                            "og:site_name" -> previewMetaData =
                                previewMetaData.copy(sitename = element.attr("content"))
                        }
                    }
                }

                if (previewMetaData.url.isEmpty()) previewMetaData =
                    previewMetaData.copy(url = doc.location())

                withContext(Dispatchers.Main) { responseListener.onData(previewMetaData) }
            }

            else -> withContext(Dispatchers.Main) { responseListener.onError(Exception("url is not valid")) }
        }
    }

    private fun resolveURL(url: String, part: String): String {
        return when {
            part.startsWith("http://") || part.startsWith("https://") -> part
            else -> {
                val baseURL = url.substringBeforeLast('/')
                "$baseURL/$part"
            }
        }
    }
}