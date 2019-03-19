package com.weis.videoxie.moudle.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView {
    private Bitmap bitmap = null;
    private float width;
    private float height;

    public CustomSurfaceView(Context context) {
        super(context);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         width = MeasureSpec.getSize(widthMeasureSpec);
         height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        if (this.bitmap != null) ;
        {
            canvas.drawBitmap(this.bitmap,0,0, paint);
        }
    }
}
