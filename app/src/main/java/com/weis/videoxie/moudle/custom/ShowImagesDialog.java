package com.weis.videoxie.moudle.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.weis.videoxie.R;
import com.weis.videoxie.moudle.adapter.ShowImagesAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImagesDialog extends Dialog {
    private View mView;
    private Context mContext;
    private ShowImagesViewPager mViewPager;
    private TextView txIndex;
    private List<String> mImgUrls;
    private List<String> mTitles;
    private List<View> mViews;
    private ShowImagesAdapter mAdapter;
    private DisplayMetrics metrics = null;
    private int position = -1;

    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls, int position, DisplayMetrics metrics) {
        super(context, R.style.ShowImageTheme);
        this.mContext = context;
        this.mImgUrls = imgUrls;
        this.position = position;
        this.metrics = metrics;
        initView();
        initData();
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.dialog_image_show, null);
        mViewPager = (ShowImagesViewPager) mView.findViewById(R.id.show_img_view_page);
        txIndex = (TextView) mView.findViewById(R.id.tx_index);
        mTitles = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = metrics.heightPixels;
        wl.width = metrics.widthPixels;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }

    private void initData() {
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }
        };
        for (int i = 0; i < mImgUrls.size(); i++) {
            final PhotoView photoView = new PhotoView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            photoView.setOnPhotoTapListener(listener);
            Glide.with(mContext)
                    .load(mImgUrls.get(i))
                    .placeholder(R.mipmap.icon_camera_offline)
                    .error(R.mipmap.icon_camera_offline)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            photoView.setImageDrawable(resource);
                        }
                    });
            mViews.add(photoView);
            mTitles.add(i + "");
        }
        mAdapter = new ShowImagesAdapter(mViews, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                txIndex.setText(position + 1 + "/" + mImgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
