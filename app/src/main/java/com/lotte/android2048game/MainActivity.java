package com.lotte.android2048game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameView.OnGameStateChangeListener {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView);

        gameView.setOnGameStateChangeListener(this);//注册游戏状态改变监听
    }

    @Override
    public void onCellChange() {

    }

    @Override
    public void onScoreChanged(int currScore) {

    }

    @Override
    public void onGameOver(final GameView gameView, int currScore, boolean isSuccess) {
        String msg = "";
        if (isSuccess) {
            msg = "恭喜,游戏胜利!您的分数:" + currScore + "!";
        } else {
            msg = "失望,游戏失败!您的分数:" + currScore + "!";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setTitle("提示").setMessage(msg + "是否开始新的一局?")
                .setPositiveButton("再玩一把", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.initGame();
                        gameView.drawElements();
                    }
                }).setNegativeButton("不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setTitle("提示").setMessage("当前分数为: " + gameView.getScore() + " ,是否退出游戏?")
                .setPositiveButton("点错了", null)
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        alertDialog.show();
    }
}
