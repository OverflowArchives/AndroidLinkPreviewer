# Android Link Previewer
Link Preview Library for Android

[![](https://jitpack.io/v/OverflowArchives/AndroidLinkPreviewer.svg)](https://jitpack.io/#OverflowArchives/AndroidLinkPreviewer)

## Demo
<img src="https://github.com/OverflowArchives/AndroidLinkPreviewer/blob/master/app/screenshot_1.PNG?raw=true" width="300" alt="ScreenShot">
<img src="https://github.com/OverflowArchives/AndroidLinkPreviewer/blob/master/app/screenshot_2.PNG?raw=true" width="300" alt="ScreenShot">

#### Gradle setup

Step 1: Add it in your root build.gradle at the end of repositories:

~~~gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
~~~

Step 2: Add the dependency
~~~gradle
dependencies {
	        implementation 'com.github.OverflowArchives:AndroidLinkPreviewer:0.01'
	}
~~~

#### Usage

Add below code in your xml layout

~~~xml
<!-- whatsapp -->
<com.overflowarchives.linkpreview.WhatsappPreview
    android:id="@+id/link_preview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
<!-- Skype -->
<com.overflowarchives.linkpreview.SkypePreview
    android:id="@+id/link_preview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>

<!-- Telegram -->
<com.overflowarchives.linkpreview.TelegramPreview
    android:id="@+id/link_preview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>

<!-- Twitter -->
<com.overflowarchives.linkpreview.TwitterPreview
    android:id="@+id/link_preview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
~~~

##### Java

~~~java
WhatsappPreview preview = findViewById(R.id.link_preview);
    preview.loadUrl("link", new ViewListener() {
      @Override
      public void onPreviewSuccess(boolean status) {
          // on success
      }
      @Override
      public void onFailedToLoad(@Nullable Exception e) {
        // on preview failed
      }
    });
~~~

#### Kotlin

~~~kotlin

link_preview.loadUrl(loadUrl, object : ViewListener {
            override fun onPreviewSuccess(status: Boolean) {
            }

            override fun onFailedToLoad(e: Exception?) {

            }
        })
~~~
