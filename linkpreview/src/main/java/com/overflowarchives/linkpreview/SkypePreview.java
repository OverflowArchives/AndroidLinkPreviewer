package com.overflowarchives.linkpreview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;



public class SkypePreview extends RelativeLayout {

    private View view;
    Context context;
    private PreviewMetaData meta;

    RelativeLayout relativeLayout;
    ImageView imageView;
    ImageView imageViewFavIcon;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private LinkListener linkListener;


    public SkypePreview(Context context) {
        super(context);
        this.context = context;
    }

    public SkypePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SkypePreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SkypePreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void initView() {

        if(findRelativeLayoutChild() != null) {
            this.view = findRelativeLayoutChild();
        } else  {
            this.view = this;
            inflate(context, R.layout.skype_link_layout,this);
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.skype_preview_link_card);
        imageView = (ImageView) findViewById(R.id.skype_preview_link_image);
        imageViewFavIcon = (ImageView) findViewById(R.id.skype_link_favicon);
        textViewTitle = (TextView) findViewById(R.id.skype_preview_link_title);
        textViewDesp = (TextView) findViewById(R.id.skype_preview_link_desc);
        textViewUrl = (TextView) findViewById(R.id.skype_preview_link_url);


        if(meta.getImageurl().equals("") || meta.getImageurl().isEmpty()) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            Glide.with(this).load(meta.getImageurl()).into(imageView);
        }

        if(meta.getFavicon().equals("") || meta.getFavicon().isEmpty()) {
            imageViewFavIcon.setVisibility(GONE);
        } else {
            imageViewFavIcon.setVisibility(VISIBLE);
            Glide.with(this).load(meta.getFavicon()).into(imageViewFavIcon);
        }

        if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
            textViewTitle.setVisibility(GONE);
        } else {
            textViewTitle.setVisibility(VISIBLE);
            textViewTitle.setText(meta.getTitle());
        }
        if(meta.getUrl().isEmpty() || meta.getUrl().equals("")) {
            textViewUrl.setVisibility(GONE);
        } else {
            textViewUrl.setVisibility(VISIBLE);
            textViewUrl.setText(meta.getUrl());
        }
        if(meta.getDescription().isEmpty() || meta.getDescription().equals("")) {
            textViewDesp.setVisibility(GONE);
        } else {
            textViewDesp.setVisibility(VISIBLE);
            textViewDesp.setText(meta.getDescription());
        }


        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDefaultClick) {
                    onLinkClicked();
                } else {
                    if(linkListener != null) {
                        linkListener.onClicked(view, meta);
                    } else {
                        onLinkClicked();
                    }
                }
            }
        });

    }

    private void onLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }

    protected RelativeLayout findRelativeLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (RelativeLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(PreviewMetaData previewMetaData) {
        meta = previewMetaData;
        initView();
    }

    public PreviewMetaData getMetaData() {
        return meta;
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(LinkListener linkListener1) {
        linkListener = linkListener1;
    }


    public void loadUrl(String url, final ViewListener viewListener) {
        main_url = url;
        LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
            @Override
            public void onData(PreviewMetaData metaData) {
                meta = metaData;

                if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
                    viewListener.onPreviewSuccess(true);
                }

                initView();
            }

            @Override
            public void onError(Exception e) {
                viewListener.onFailedToLoad(e);
            }
        });
        linkPreview.getPreview(url);
    }


}
