package com.lotte.android2048game.model;

import android.graphics.Point;

/**
 * Created by wubin on 2017/3/13.
 */

public class Cell {
    //单元格里面的数字
    private String number;
    //单元格的坐标点
    private Point point;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
