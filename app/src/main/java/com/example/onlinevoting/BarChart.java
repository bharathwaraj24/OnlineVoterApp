package com.example.onlinevoting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class BarChart extends View {

    private List<Integer> dataValues;
    private List<String> dataLabels;
    private int maxValue;

    private Paint barPaint;
    private Paint textPaint;

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36);
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
        textPaint.setTypeface(typeface);
    }

    public void setData(List<Integer> values, List<String> labels) {
        this.dataValues = values;
        this.dataLabels = labels;
        this.maxValue = calculateMaxValue();
        invalidate(); // Refresh the view
    }

    private int calculateMaxValue() {
        int max = Integer.MIN_VALUE;
        for (int value : dataValues) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataValues == null || dataLabels == null || dataValues.size() != dataLabels.size()) {
            return;
        }

        int barWidth = getWidth() / (2 * dataValues.size()); // Adjust for padding
        int barHeight;
        int x = 0;

        for (int i = 0; i < dataValues.size(); i++) {
            barHeight = getHeight() * dataValues.get(i) / maxValue;
            canvas.drawRect(x, getHeight() - barHeight, x + barWidth, getHeight(), barPaint);
            canvas.drawText(dataLabels.get(i), x, getHeight() - barHeight - 10, textPaint); // Label below bar
            x += 2 * barWidth; // Adjust for padding
        }
    }
}