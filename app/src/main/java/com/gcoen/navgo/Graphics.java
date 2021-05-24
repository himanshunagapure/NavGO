package com.gcoen.navgo;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Graphics extends GraphicOverlay.Graphic {

//    private static final int TEXT_COLOR1 = Color.BLUE;
    private static final int TEXT_COLOR1 = Color.rgb(48,154,211);
    private static Paint sRectPaint;

    float currImgXAxis;
    float currImgYAxis;
    float destImgXAxis;
    float destImgYAxis;

    @SuppressLint("ResourceAsColor")
    public Graphics(GraphicOverlay overlay, float currImgXAxis, float currImgYAxis, float destImgXAxis, float destImgYAxis) {
        super(overlay);

        this.currImgXAxis=currImgXAxis;
        this.currImgYAxis=currImgYAxis;
        this.destImgXAxis=destImgXAxis;
        this.destImgYAxis=destImgYAxis;

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(TEXT_COLOR1);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(18f);
        }

        //Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    /**
     * Draws the text block annotations for position, size, and raw value on thesupplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        float x = currImgXAxis+25;
        float y = currImgYAxis-220;
        float x1 = destImgXAxis+10;
        float y1 = destImgYAxis-220;
        canvas.drawLine(x,y,x1,y1,sRectPaint);
    }

}