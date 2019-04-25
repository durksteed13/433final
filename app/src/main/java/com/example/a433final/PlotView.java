package com.example.a433final;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class PlotView extends View {

    ArrayList<Float> values = new ArrayList<Float>();
    ArrayList<Double> timeCount = new ArrayList<Double>();
    Paint p = new Paint();
    Paint linep = new Paint();
    Paint textp = new Paint();
    Paint circlep = new Paint();
    float scaler;

    public PlotView(Context context) {
        super(context);
    }

    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setColors();

        if (timeCount.isEmpty()) {
            for(double i = 0; i < 7; i+= 1) {
                timeCount.add(i);
            }
        }

        /*
        if(values.isEmpty()) {
            values.add((float) 0);
        }
        */

        float yspace = (getHeight()-15)/5;
        float xspace = (getWidth()-15)/7;
        float yinc = yspace;
        float xinc = xspace;
        float maxy = maxYPlot();
        float yaxis = maxy/5;
        int yaxtext = (int) yaxis;

        scaler = (getHeight() - 15)/maxYPlot();

        //10dp padding for x
        //15dp padding for y
        //draw lines on graph
        canvas.drawText("DATE", 0, (float) getHeight(), textp);
        for(int i = 0; i < 7; i++) {
            //canvas.drawLine(xinc + 10, (float) getHeight() - 15, xinc + 10, 0, p);
            canvas.drawText(Double.toString(timeCount.get(i)), (xinc) +3, (float) getHeight(), p);

            if(i < 5) {
                //canvas.drawLine(10, (float) getHeight() - (15 + yinc) + 15,  (float)getWidth()+10, (float) getHeight() - (15 + yinc) + 15, p);
                //canvas.drawText(Integer.toString(yaxtext), 0, (float) getHeight() - (15 + yinc) + 30, p);
            }
            yinc += yspace; yaxtext += yaxis; xinc += xspace;
        }

        float plotx = xspace;
        float prevxv = 0; float prevyv = 0;
        for(int i = 0; i < values.size(); i++) {
            canvas.drawCircle(plotx + 10, scaleY(values.get(i)), 10, circlep);
            canvas.drawText("(" +values.get(i)+")", plotx, scaleY(values.get(i)), p);
            if(i != 0) {
                canvas.drawLine(prevxv, prevyv, plotx + 10, scaleY(values.get(i)), linep);
            }
            prevxv = plotx + 10; prevyv = scaleY(values.get(i));
            plotx += xspace;
        }
    }

    public void setColors() {

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setTextSize(30);

        circlep.setColor(Color.WHITE);
        circlep.setStyle(Paint.Style.STROKE);
        circlep.setTextSize(30);
        circlep.setStrokeWidth(3);

        linep.setColor(Color.WHITE);
        linep.setStyle(Paint.Style.FILL);
        linep.setTextSize(20);
        linep.setStrokeWidth(5);

        textp.setColor(Color.WHITE);
        textp.setStyle(Paint.Style.FILL);
        textp.setTextSize(40);

    }

    public float scaleY(float toScale) {
        if(toScale == 0) {
            return getHeight() - 15;
        }
        if(toScale == getHeight()) {
            return 15;
        }
        if(toScale < 1) {
            return getHeight() - 15 - toScale;
        }
        else {
            return (float) getHeight() - (toScale * scaler);
        }
    }

    public float maxYPlot() {
        float maxy = 0;
        for(int i = 0; i < values.size(); i++) {
            if(values.get(i) > maxy) {
                maxy = (float)(double)values.get(i);
            }
        }
        if(maxy < 1) {
            return (float)(maxy + .1);
        }
        else if(maxy < 10) {
            return maxy + 1;
        }
        else if(maxy < 50) {
            return maxy + 5;
        }
        else {
            return maxy + 10;
        }
    }

    public void addPoint(float f) {
        values.add(f);
        if(values.size() == 8) {
            values.remove(0);
            timeCount.add(timeCount.get(timeCount.size()-1) +0.5);
            timeCount.remove(0);
        }
        //addMean();
    }

}
