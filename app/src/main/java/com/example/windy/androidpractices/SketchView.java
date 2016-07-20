package com.example.windy.androidpractices;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by windog on 2016/7/20.
 */
public class SketchView extends View {

    private int custom_size;
    private int custom_background;

    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private float scale = 1f;

    private final int SIZE = 15;
    private final int DEFAULT_COLOR = Color.BLUE;

    /* 自定义 View 一般会有三个构造方法，第一个构造方法如下， 一般是在 Java 代码中 new 一个 View 时会被系统自动调用。
       如：SketchView sketchView = new SketchView(this);

       本例中，此方法中调用了第二个构造函数
    * */
    public SketchView(Context context) {
        this(context, null);
    }

    /* 第二个构造函数，一般是在 xml 中添加 View 时会被系统自动调用，因为多了个 AttributeSet 参数。
            xml 中要定义的各种属性（布局属性，宽高属性以及margin属性等）就存在这个参数中

        本例中，此方法中调用了第三个构造函数
    * */
    public SketchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* 第三个构造函数 ，又多了一个 int 型的自定义属性的参数，我们需要写自己的完全定制的 View 时就会在这个函数中写。
        然后，在前两个构造函数中，调用这个自己写的构造函数。
        因为系统不会自动调用这个完全自定义的函数
    * */
    public SketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SketchView, defStyleAttr, R.style.AppTheme);

        custom_size = a.getDimensionPixelSize(R.styleable.SketchView_size, SIZE);
        custom_background = a.getColor(R.styleable.SketchView_background_color, DEFAULT_COLOR);

        a.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(custom_background);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //这行是默认实现，其实可以删掉，因为里面是空的。
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredHeight, measuredWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        } else {
            measuredWidth = SIZE;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else {
            measuredHeight = SIZE;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mWidth/2, mHeight/2, custom_size * scale, mPaint);
    }

    private ValueAnimator mAnimator;

    public void startAnimation() {
        mAnimator = ValueAnimator.ofFloat(1, 2);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        // 重复次数 -1表示无限循环
        mAnimator.setRepeatCount(-1);

        // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);

        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 关闭动画
        mAnimator.end();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
