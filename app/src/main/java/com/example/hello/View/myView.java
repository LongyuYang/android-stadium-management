package com.example.hello.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import util.State;

public class myView extends View {

    private Paint backgroundPaint;        //画笔
    private Rect[] backgroundRects;       //待绘制的矩形区域
    private float[] weights;              //每个矩形区域的权重
    private int[] colors;                 //每个矩形区域的颜色
    private float totalWeight;            //总的权重
    public  int[] DEF_COLORS;             //外部可访问的颜色数组
    public  float[] DEF_WEIGHTS;          //外部可访问的每段对应的权重

    public myView(Context context) {
        super(context);
        init();
    }

    public myView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public myView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        DEF_COLORS = new int[]{
                Color.parseColor("#D3D3D3"),

        };
        DEF_WEIGHTS = new float[]{
                1
        };
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setColors(DEF_COLORS, DEF_WEIGHTS);
    }


    private void setColors(int[] colors, float[] weights) {
        backgroundRects = new Rect[colors.length];
        this.colors = colors;
        this.weights = weights;
        totalWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            totalWeight += weights[i];
            backgroundRects[i] = new Rect();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取State中的颜色数组和权值数组
        if (State.getState().DEF_COLORS!= null && State.getState().DEF_WEIGHTS != null) {
            DEF_COLORS = State.getState().DEF_COLORS;
            DEF_WEIGHTS = State.getState().DEF_WEIGHTS;
            setColors(DEF_COLORS, DEF_WEIGHTS);
        }

        //绘制颜色块
        int x = 0, y = getHeight();
        for (int i = 0; i < colors.length; i++) {
            Rect rect = backgroundRects[i];
            backgroundPaint.setColor(colors[i]);
            int width = (int) (getWidthForWeight(weights[i], totalWeight));
            rect.set(x, 0, x + width, y);
            x += width;//计算下一个的开始位置
            canvas.drawRect(rect, backgroundPaint);//绘制矩形
        }
    }

    private float getWidthForWeight(float weight, float totalWeight) {
        return getWidth() * (weight / totalWeight) + 0.5f;
    }
}
