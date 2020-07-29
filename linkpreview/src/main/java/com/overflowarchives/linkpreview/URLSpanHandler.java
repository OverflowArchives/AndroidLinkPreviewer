package com.overflowarchives.linkpreview;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanHandler extends URLSpan {
    public URLSpanHandler(String p_Url) {
        super(p_Url);
    }

    public void updateDrawState(TextPaint p_DrawState) {
        super.updateDrawState(p_DrawState);
        p_DrawState.setUnderlineText(false);
    }
}
