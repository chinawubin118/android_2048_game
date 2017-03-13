package com.lotte.android2048game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lotte.android2048game.model.Cell;

import java.util.ArrayList;

/**
 * Created by wubin on 2017/3/13.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback2 {

    private SurfaceHolder holder;

    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    private int borderWidth;//单元格边框的宽度

    private Paint bgPaint;//绘制屏幕背景的paint
    private Paint cellPaint;//绘制单元格的paint
    private Paint borderPaint;//绘制边框的paint

    private ArrayList<Cell> cellList;//存放所有的单元格

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        cellList = new ArrayList<>();

        holder = getHolder();
        holder.addCallback(this);

        bgPaint = new Paint();
        bgPaint.setColor(0xCEBFB6);
        cellPaint = new Paint();
        cellPaint.setColor(Color.CYAN);
        borderPaint = new Paint();
        borderPaint.setColor(0xBAABA2);
    }

    //生成 4*4 个单元格
    private void generateCells() {
        if (null == cellList || cellList.size() <= 0) {
            for (int i = 0; i < 16; i++) {
                Cell cell = new Cell();
                cell.setNumber("0");
                cell.setPoint(new Point());
                cellList.add(cell);
            }
        }
    }

    //绘制边框
    private void drawBoders(Canvas canvas) {
        //绘制横向的boder
        for (int i = 0; i < 5; i++) {
            int startY = (screenWidth - borderWidth) / 4;
            canvas.drawLine(0, 0, , , borderPaint);
        }

        //绘制纵向的boder
        for (int i = 0; i < 5; i++) {

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenHeight = getHeight();
        screenWidth = getWidth();
        borderWidth = screenWidth / 40;
        Log.i("wubin", "screenWidth = " + screenWidth + "screenHeight = " + screenHeight);

        Canvas canvas = holder.lockCanvas();

        drawBoders(canvas);//绘制边框

        Paint paint = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        canvas.drawRect(0, 0, screenWidth, screenWidth, bgPaint);

//        canvas.drawLines();

        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("wubin", "format = " + format + ",width = " + width + ",height = " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    private class NewThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
