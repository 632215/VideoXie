package com.weis.videoxie.moudle.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.weis.videoxie.R;


/**
 * Created by Administrator on 2018/8/2.
 */

@SuppressLint("AppCompatCustomView")
public class CustomRadioButton extends RadioButton {
    private int num = 0;
    private Paint mPanit = new Paint();
    private Paint mNumPanit = new Paint();

    private float width = 0;
    private float height = 0;

    public CustomRadioButton(Context context) {
        super(context);
        setPaint();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint();
    }

    private void setPaint() {
        mPanit.setColor(getResources().getColor(R.color.color_red));
        mPanit.setAntiAlias(true);
        mNumPanit.setColor(getResources().getColor(R.color.color_white));
        mNumPanit.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec) / 2;
        height = MeasureSpec.getSize(heightMeasureSpec) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num > 0 && num <= 10) {
            canvas.drawOval(new RectF(width + 15, 5, width + 55, 45), mPanit);
            mNumPanit.setTextSize(28);
            canvas.drawText(String.valueOf(num), width + 28, 35, mNumPanit);
        } else if (num > 10 && num < 100) {
            canvas.drawOval(new RectF(width + 15, 5, width + 55, 45), mPanit);
            mNumPanit.setTextSize(24);
            canvas.drawText(String.valueOf(num), width + 22, 34, mNumPanit);
        } else if (num > 99) {
            canvas.drawOval(new RectF(width + 15, 5, width + 55, 45), mPanit);
            mNumPanit.setTextSize(20);
            canvas.drawText("99+", width + 20, 32, mNumPanit);
        }
        canvas.save();
    }

    //设置红色小点的可见性
    public void setNum(int num) {
        this.num = num;
        invalidate();
    }
}