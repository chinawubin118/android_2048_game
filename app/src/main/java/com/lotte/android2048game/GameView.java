package com.lotte.android2048game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lotte.android2048game.model.Cell;

import java.util.Random;

/**
 * Created by wubin on 2017/3/13.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback2 {

    private SurfaceHolder holder;

    private float screenWidth;//屏幕宽度
    private float screenHeight;//屏幕高度
    private float borderWidth;//单元格边框的宽度
    private float cellWidth;//单元格的宽度
    private int textSize;//字体的大小

    private Paint bgPaint;//绘制屏幕背景的paint
    private Paint cellPaint;//绘制单元格的paint
    private Paint borderPaint;//绘制边框的paint

    private Cell[][] cellArray;//存放所有的单元格

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        cellArray = new Cell[4][4];

        holder = getHolder();
        holder.addCallback(this);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#CEBFB6"));

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setColor(Color.parseColor("#776B61"));

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.parseColor("#BBACA4"));
    }

    //生成 4*4 个单元格
    private void generateCells() {
        if (null == cellArray[0][0]) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Cell cell = new Cell();
                    cell.setNumber("0");
                    cell.setPoint(new Point(i, j));
                    cellArray[i][j] = cell;
                }
            }

            generateRandom();
            generateRandom();
        }
    }

    //生成随机数
    private void generateRandom() {
        int newPositionX = 0;
        int newPositionY = 0;
        do {
            newPositionX = new Random().nextInt(4);
            newPositionY = new Random().nextInt(4);
        } while (!TextUtils.equals("0", cellArray[newPositionX][newPositionY].getNumber()));

        Log.i("wubin", "newPositionX = " + newPositionX);
        Log.i("wubin", "newPositionY = " + newPositionY);
        cellArray[newPositionX][newPositionY].setNumber("2");
    }

    //绘制边框
    private void drawBoders(Canvas canvas) {
        //绘制横向的boder
        for (int i = 0; i < 5; i++) {
            float startY = ((screenWidth - borderWidth) / 4.0f) * i;
            float stopY = ((screenWidth - borderWidth) / 4.0f) * i + borderWidth;
            canvas.drawRect(0, startY, screenWidth, stopY, borderPaint);
        }

        //绘制纵向的boder
        for (int i = 0; i < 5; i++) {
            float startX = ((screenWidth - borderWidth) / 4.0f) * i;
            float stopX = ((screenWidth - borderWidth) / 4.0f) * i + borderWidth;
            canvas.drawRect(startX, 0, stopX, screenWidth, borderPaint);
        }
    }

    //绘制数字
    private void drawNumber(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float x = cellWidth / 2 + j * (cellWidth + borderWidth);
                float y = cellWidth / 2 + borderWidth + textSize / 2 + i * (cellWidth + borderWidth);
                if (!TextUtils.equals("0", cellArray[i][j].getNumber())) {
                    canvas.drawText(cellArray[i][j].getNumber(), x, y, cellPaint);
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenHeight = getHeight();
        screenWidth = getWidth();
        borderWidth = screenWidth / 40.0f;
        cellWidth = (screenWidth - borderWidth * 5.0f) / 4.0f;
        textSize = 50;
        cellPaint.setTextSize(textSize);
        Log.i("wubin", "screenWidth = " + screenWidth + ",screenHeight = " + screenHeight);

        generateCells();//生成单元格

        Canvas canvas = holder.lockCanvas();

        canvas.drawRect(0, 0, screenWidth, screenWidth, bgPaint);//绘制背景
        drawBoders(canvas);//绘制边框
        drawNumber(canvas);

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

    private Point downPoint = new Point();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = (int) event.getX();
                downPoint.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float disX = downPoint.x - event.getX();
                float disY = downPoint.y - event.getY();

                if (Math.abs(disX) > Math.abs(disY)) {
                    //x方向移动的大
                    if (disX > 0) {//向左滑动
                        moveLeft();
                    } else {//向右滑动
                        moveRight();
                    }
                } else {//y方向移动大
                    if (disY > 0) {//向上滑动
                        moveUp();
                    } else {//向下滑动
                        moveDown();
                    }
                }
                break;
        }
        return true;
    }

    private void moveLeft() {

    }

    private void moveRight() {

    }

    private void moveUp() {

    }

    private void moveDown() {

    }

    private class NewThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
