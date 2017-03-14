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
    private Canvas canvas;//lockCanas得到的画布

    private float screenWidth;//屏幕宽度
    private float screenHeight;//屏幕高度
    private float borderWidth;//单元格边框的宽度
    private float cellWidth;//单元格的宽度
    private float textSize;//字体的大小

    private Paint bgPaint;//绘制屏幕背景的paint
    private Paint cellPaint;//绘制单元格的paint
    private Paint borderPaint;//绘制边框的paint

    private Cell[][] cellArray;//存放所有的单元格
    private boolean isFirstGame;//是否是第一次进入游戏,第一次的话需要生成两个位置的随机数
    private int score;//分数

    public GameView(Context context) {
        super(context);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGame();
    }

    public void initGame() {
        isFirstGame = true;
        cellArray = new Cell[4][4];
        generateCells();//生成单元格
        score = 0;//分数

        holder = getHolder();
        holder.addCallback(this);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#CEBFB6"));

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setColor(Color.parseColor("#776B61"));
        if (screenWidth != 0) {//可以认为是再次开始游戏
            textSize = (int) (screenWidth / 15);//字体大小为屏幕宽度的1/15
            cellPaint.setTextSize(textSize);
        }

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

            if (isFirstGame) {//第一次进入游戏,随机在两位位置生成两个数字
                generateRandom();
                generateRandom();
                generateRandom();
                generateRandom();
                isFirstGame = false;
            }
        }
    }

    //生成随机数
    private void generateRandom() {
        if (!isHasSpace()) {//没有空余的单元格了,不再生成随机位置,防止没有位置的时候,界面卡死
            return;
        }
        int newPositionX = 0;
        int newPositionY = 0;
        do {
            newPositionX = new Random().nextInt(4);
            newPositionY = new Random().nextInt(4);
        } while (!TextUtils.equals("0", cellArray[newPositionY][newPositionX].getNumber()));

        Log.i("wubin", "newPositionX = " + newPositionX);
        Log.i("wubin", "newPositionY = " + newPositionY);
        cellArray[newPositionY][newPositionX].setNumber("2");
    }

    //是否还有空间(空余的单元格)
    private boolean isHasSpace() {
        for (int y0 = 0; y0 < 4; y0++) {
            for (int x0 = 0; x0 < 4; x0++) {
                if (TextUtils.equals("0", cellArray[y0][x0].getNumber())) {
                    return true;
                }
            }
        }
        return false;
    }

    //游戏是否胜利
    private boolean isGameFailed() {
        int maxNum = getCurrentMaxNum();
        if (maxNum >= 2048) {//游戏胜利
            return false;
        }
        if (!isHasSpace() && maxNum < 2048 && !canMoveLeft() && !canMoveRight() && !canMoveUp()
                && !canMoveDown()) {//没有空间,最大值小于2048,并且不能左右上下移动了,游戏失败
            return true;
        }
        return false;
    }

    //获取当前最大值
    public int getCurrentMaxNum() {
        int maxNum = Integer.parseInt(cellArray[0][0].getNumber());//当前的最大数字
        for (int y0 = 0; y0 < 4; y0++) {
            for (int x0 = 0; x0 < 4; x0++) {
                maxNum = Integer.parseInt(cellArray[y0][x0].getNumber()) > maxNum ?
                        Integer.parseInt(cellArray[y0][x0].getNumber()) : maxNum;
            }
        }
        return maxNum;
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
        for (int y0 = 0; y0 < 4; y0++) {
            for (int x0 = 0; x0 < 4; x0++) {

                String drawNum = cellArray[y0][x0].getNumber();//需要绘制的数字

                float offsetX = 0;//绘制数字x轴方向的偏移量
                if (drawNum.length() == 2) {
                    offsetX = -cellWidth / 10;
                } else if (drawNum.length() == 3) {
                    offsetX = -cellWidth / 8;
                } else if (drawNum.length() == 4) {
                    offsetX = -cellWidth / 5;
                }

                float x = cellWidth / 2 + x0 * (cellWidth + borderWidth) + offsetX;
                float y = cellWidth / 2 + borderWidth + textSize / 2 + y0 * (cellWidth + borderWidth);

                if (!TextUtils.equals("0", drawNum)) {
                    Log.i("wubin", drawNum + ",位置是:" + y0 + "," + x0 + "画数字.....");
                    canvas.drawText(drawNum, x, y, cellPaint);
                }
            }
        }
    }

    private void drawBottom(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#F9F9EF"));
        canvas.drawRect(0, screenWidth, screenWidth, screenHeight, paint);
    }

    //画分数
    private void drawScore() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setTextSize(screenWidth / 15);//字体大小
        canvas.drawText("分数:" + score, screenWidth / 15, getWidth() + cellWidth / 2, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenHeight = getHeight();
        screenWidth = getWidth();
        borderWidth = screenWidth / 40.0f;
        cellWidth = (screenWidth - borderWidth * 5.0f) / 4.0f;
        textSize = screenWidth / 15;//字体大小为屏幕宽度的1/15
        cellPaint.setTextSize(textSize);
        Log.i("wubin", "screenWidth = " + screenWidth + ",screenHeight = " + screenHeight);

        drawElements();//界面有改变,重新绘制所有元素
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
//            case MotionEvent.ACTION_CANCEL:

                float disX = downPoint.x - event.getX();
                float disY = downPoint.y - event.getY();
                //手指滑动超过一定距离时才有效(屏幕宽度的1/8)
                if (Math.abs(disX) > screenWidth / 8 || Math.abs(disY) > screenWidth / 8) {

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

                    generateRandom();
                    drawElements();//界面有改变,重新绘制所有元素

                    if (null != onGameStateChangeListener) {
                        if (isGameFailed()) {
                            onGameStateChangeListener.onGameOver(this, score, false);
                        } else {//游戏没有失败(可能还有空间或者没有空间但是游戏分数大于2048)
                            if (isHasSpace()) {
                                onGameStateChangeListener.onCellChange();
                            } else {
                                if (getCurrentMaxNum() >= 2048 && !canMoveLeft() && !canMoveRight() && !canMoveUp()
                                        && !canMoveDown()) {
                                    //没有空间了,分数达到2048,不能移动了
                                    onGameStateChangeListener.onGameOver(this, score, true);
                                }
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void moveLeft() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x][y1].getNumber());//当前位置右边的数字
                    if (rightNum > 0) {//右边位置不是0
                        if (currNum == 0) {//当前位置是0
                            //此时需要将右边的值放到当前位置,右边的位置设为0
                            cellArray[x][y1].setNumber("0");
                            cellArray[x][y].setNumber(rightNum + "");

//                            y--;
                        } else if (currNum == rightNum) {//右边不是0,并且当前位置和右边的数字相同,此时需要合并数字
                            //此时需要将右边的值*2放到当前位置,右边的位置设为0
                            cellArray[x][y1].setNumber("0");
                            cellArray[x][y].setNumber(rightNum * 2 + "");
                            score += rightNum * 2;
                        }
                    }
                }
            }
        }
    }

    private boolean canMoveLeft() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x][y1].getNumber());//当前位置右边的数字
                    if (rightNum > 0) {//右边位置不是0
                        if (currNum == 0) {//当前位置是0
                            return true;
                        } else if (currNum == rightNum) {//右边不是0,并且当前位置和右边的数字相同,此时需要合并数字
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void moveRight() {
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x][y1].getNumber());//当前位置左边的数字
                    if (rightNum > 0) {//左边位置不是0
                        if (currNum == 0) {//当前位置是0
                            //此时需要将左边的值放到当前位置,左边的位置设为0
                            cellArray[x][y1].setNumber("0");
                            cellArray[x][y].setNumber(rightNum + "");

//                            y++;
                        } else if (currNum == rightNum) {//左边不是0,并且当前位置和左边的数字相同,此时需要合并数字
                            //此时需要将左边的值*2放到当前位置,左边的位置设为0
                            cellArray[x][y1].setNumber("0");
                            cellArray[x][y].setNumber(rightNum * 2 + "");
                            score += rightNum * 2;
                        }
                    }
                }
            }
        }
    }

    private boolean canMoveRight() {
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x][y1].getNumber());//当前位置左边的数字
                    if (rightNum > 0) {//左边位置不是0
                        if (currNum == 0) {//当前位置是0
                            return true;
                        } else if (currNum == rightNum) {//左边不是0,并且当前位置和左边的数字相同,此时需要合并数字
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void moveUp() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x1][y].getNumber());//当前位置下边的数字
                    if (rightNum > 0) {//下边位置不是0
                        if (currNum == 0) {//当前位置是0
                            //此时需要将下边的值放到当前位置,下边的位置设为0
                            cellArray[x1][y].setNumber("0");
                            cellArray[x][y].setNumber(rightNum + "");

//                            x--;
                        } else if (currNum == rightNum) {//下边不是0,并且当前位置和下边的数字相同,此时需要合并数字
                            //此时需要将下边的值*2放到当前位置,下边的位置设为0
                            cellArray[x1][y].setNumber("0");
                            cellArray[x][y].setNumber(rightNum * 2 + "");
                            score += rightNum * 2;
                        }
                    }
                }
            }
        }
    }

    private boolean canMoveUp() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x1][y].getNumber());//当前位置下边的数字
                    if (rightNum > 0) {//下边位置不是0
                        if (currNum == 0) {//当前位置是0
                            return true;
                        } else if (currNum == rightNum) {//下边不是0,并且当前位置和下边的数字相同,此时需要合并数字
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void moveDown() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x1][y].getNumber());//当前位置上边的数字
                    if (rightNum > 0) {//上边位置不是0
                        if (currNum == 0) {//当前位置是0
                            //此时需要将上边的值放到当前位置,上边的位置设为0
                            cellArray[x1][y].setNumber("0");
                            cellArray[x][y].setNumber(rightNum + "");

//                            x++;
                        } else if (currNum == rightNum) {//上边不是0,并且当前位置和上边的数字相同,此时需要合并数字
                            //此时需要将上边的值*2放到当前位置,上边的位置设为0
                            cellArray[x1][y].setNumber("0");
                            cellArray[x][y].setNumber(rightNum * 2 + "");
                            score += rightNum * 2;
                        }
                    }
                }
            }
        }
    }

    private boolean canMoveDown() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {//横向遍历该行的每一个数字
                    int currNum = Integer.parseInt(cellArray[x][y].getNumber());//当前位置的数字
                    int rightNum = Integer.parseInt(cellArray[x1][y].getNumber());//当前位置上边的数字
                    if (rightNum > 0) {//上边位置不是0
                        if (currNum == 0) {//当前位置是0
                            return true;
                        } else if (currNum == rightNum) {//上边不是0,并且当前位置和上边的数字相同,此时需要合并数字
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void drawElements() {
        canvas = holder.lockCanvas();

        canvas.drawRect(0, 0, screenWidth, screenWidth, bgPaint);//绘制背景
        drawBoders(canvas);//绘制边框
        drawNumber(canvas);
        drawBottom(canvas);//绘制底部的背景
        drawScore();//绘制分数

        holder.unlockCanvasAndPost(canvas);
    }

//    private class NewThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//        }
//    }


    //返回分数
    public int getScore() {
        return score;
    }

    private OnGameStateChangeListener onGameStateChangeListener;

    public void setOnGameStateChangeListener(OnGameStateChangeListener onGameStateChangeListener) {
        this.onGameStateChangeListener = onGameStateChangeListener;
    }

    //游戏状态改变
    public interface OnGameStateChangeListener {
        void onCellChange();//数字单元格状态改变,正常情况下,每次滑动生效都会执行该方法.

        void onScoreChanged(int currScore);//游戏分数改变.

        void onGameOver(GameView gameView, int currScore, boolean isSuccess);//游戏结束(可能胜利或者失败).
    }
}
