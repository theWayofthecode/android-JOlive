package com.jolive.ui;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ActionSwipeListener implements View.OnTouchListener {
static final String TAG = "SwipeListener";
static final int MIN_DISTANCE = 50;
static final float DIST_ERR = MIN_DISTANCE / 5;

private PopupWindow pWindow;
private String sel;
private JOPanel jop;
private float downX, downY, upX, upY;

public ActionSwipeListener(PopupWindow pWindow, String sel, JOPanel jop){
    this.pWindow = pWindow;
    this.sel = sel;
    this.jop = jop;
}

public boolean onTouch(View v, MotionEvent event) {
    switch(event.getAction()){
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            return true;
        }

        case MotionEvent.ACTION_UP: {
            upX = event.getX();
            upY = event.getY();

            float DX = upX - downX;
            float DY = upY - downY;

            
            if(Math.abs(DX) < MIN_DISTANCE) {
            	Log.i(TAG, "Swipe distance too short");
            	return false;
            }
            
            //right branch
            if (DX > DIST_ERR) {
            	if (DY > DIST_ERR) {
            		rightDownAction();
            	} else if (Math.abs(DY) < DIST_ERR) {
            		rightHorizAction();
            	} else if (DY < -DIST_ERR) {
            		rightUpAction();
            	}
            //left branch
            } else if (DX < -DIST_ERR) {
            	if (DY > DIST_ERR) {
            		leftDownAction();
            	} else if (Math.abs(DY) < DIST_ERR) {
            		leftHorizAction();
            	} else if (DY < -DIST_ERR) {
            		leftUpAction();
            	}
            } else {
            	Log.i(TAG, "DX ~= DIST_ERR");
            	return false;
            }
            pWindow.dismiss();
            return true;
        }
    }
    return false;
}

private void rightUpAction() {
	Log.i(TAG, jop.getName() + ": rightUp");
}

private void rightHorizAction() {
	Log.i(TAG, jop.getName() + ": rightHoriz");
	jop.ctl("exec Open " + sel);
}

private void rightDownAction() {
	Log.i(TAG, jop.getName() + ": rightDown");
}

private void leftUpAction() {
	Log.i(TAG, jop.getName() + ": leftUp");
	TextView t = (TextView)jop.getComponent();
	jop.setData(t.getText().toString());
}

private void leftHorizAction() {
	Log.i(TAG, jop.getName() + ": leftHoriz");
	jop.ctl("exec Close");
}
private void leftDownAction() {
	Log.i(TAG, jop.getName() + ": leftDown");
	jop.ctl("exec " + sel);
}
}
