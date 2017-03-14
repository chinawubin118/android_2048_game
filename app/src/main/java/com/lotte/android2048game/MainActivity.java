package com.lotte.android2048game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameView.OnGameStateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameView gameView = new GameView(this);
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
    public void onGameWin(final GameView gameView, int currScore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setTitle("提示").setMessage("恭喜,游戏胜利!是否开始新的一局?")
                .setPositiveButton("再玩一把", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.initGame();
                    }
                }).setNegativeButton("不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).create();
        alertDialog.show();
    }
}
