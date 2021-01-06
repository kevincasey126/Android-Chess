package com.example.kiss5.a306_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class SinglePlayerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        View.OnTouchListener{

    Model model;
    InteractionModel iModel;
    SingleplayerController singleplayerController;
    SingleplayerChessView view;
    ChessAI ai;
    LinearLayout box;
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        box = new LinearLayout(this);

        singleplayerController = new SingleplayerController();
        model = new Model(0);
        view = new SingleplayerChessView(this);
        iModel = new InteractionModel();
        ai = new ChessAI();

        view.setSingleplayerController(singleplayerController);
        view.setIModel(iModel);
        view.setModel(model);
        singleplayerController.setModel(model);
        singleplayerController.setIModel(iModel);
        singleplayerController.setAI(ai);
        model.addSubscriber(view);
        iModel.addSubscriber(view);
        view.setOnTouchListener(this);

        detector = new GestureDetector(this, this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.addView(view);
        setContentView(box);
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        detector.onTouchEvent(motionEvent);
        return true;    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        singleplayerController.handleDown(motionEvent);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}
