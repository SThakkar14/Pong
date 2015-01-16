package com.me.shubham.pong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class BattleField extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new myView(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battle_field, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class myView extends View {
        Paint paint = new Paint();
        int ballPositionX;
        int ballPositionY;
        int ballRadius;

        int ballXVelocity;
        int ballYVelocity;

        int screenWidth;
        int screenHeight;

        int rectPositionX;
        int rectPositionY;
        int rectWidth;
        int rectHeight;

        boolean touchStarted;

        public myView(Context context) {
            super(context);

            ballRadius = 20;
            paint.setColor(Color.parseColor("#CD5C5C"));

            ballXVelocity = 5;
            ballYVelocity = 5;

            rectPositionX = 0;
            rectPositionY = 0;
            rectWidth = 100;
            rectHeight = 400;

            touchStarted = false;
        }

        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
            screenHeight = h;
            screenWidth = w;

            ballPositionX = w / 2;
            ballPositionY = h / 2;
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (ballPositionX - ballRadius < 0 || ballPositionX + ballRadius > screenWidth)
                ballXVelocity *= -1;
            if (ballPositionY - ballRadius < 0 || ballPositionY + ballRadius > screenHeight)
                ballYVelocity *= -1;

            canvas.drawCircle(ballPositionX, ballPositionY, ballRadius, paint);
            ballPositionX += ballXVelocity;
            ballPositionY += ballYVelocity;

            canvas.drawRect(rectPositionX, rectPositionY, rectPositionX + rectWidth, rectPositionY + rectHeight, paint);

            invalidate();
        }

        public boolean onTouchEvent(@NonNull MotionEvent e) {

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    touchStarted = true;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    touchStarted = false;
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (touchStarted) {
                        rectPositionY = (int) e.getRawY();
                    }
                    break;
                }

            }

            return true;
        }

    }

}
