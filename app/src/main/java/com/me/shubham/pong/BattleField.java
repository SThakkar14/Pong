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
        Paint paint;

        int squarePositionLeft;
        int squarePositionTop;
        int squareDimension;

        int squareXVelocity;
        int squareYVelocity;

        int screenWidth;
        int screenHeight;

        int paddleLeft;
        int paddleTop;
        int paddleWidth;
        int paddleHeight;

        boolean touchStarted;

        int prevTouchX;
        int prevTouchY;

        public myView(Context context) {
            super(context);

            paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));

            squareXVelocity = 5;
            squareYVelocity = 5;

            paddleLeft = 0;
            paddleTop = 0;
            paddleWidth = 50;
            paddleHeight = 400;

            touchStarted = false;
        }

        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
            screenHeight = h;
            screenWidth = w;

            squarePositionLeft = w / 2 - 10;
            squarePositionTop = h / 2 - 10;
            squareDimension = 20;
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (squarePositionLeft < 0 || squarePositionLeft + squareDimension > screenWidth || overlaps())
                squareXVelocity *= -1;
            if (squarePositionTop < 0 || squarePositionTop + squareDimension > screenHeight)
                squareYVelocity *= -1;

            canvas.drawRect(squarePositionLeft, squarePositionTop, squarePositionLeft + squareDimension, squarePositionTop + squareDimension, paint);

            squarePositionLeft += squareXVelocity;
            squarePositionTop += squareYVelocity;

            canvas.drawRect(paddleLeft, paddleTop, paddleLeft + paddleWidth, paddleTop + paddleHeight, paint);

            invalidate();
        }

        public boolean onTouchEvent(@NonNull MotionEvent e) {
            int touchX = (int) e.getRawX();
            int touchY = (int) e.getRawY();

            int action = e.getAction();
            if (action == MotionEvent.ACTION_DOWN && isContained(touchX, touchY)) {
                touchStarted = true;
                prevTouchX = touchX;
                prevTouchY = touchY;
            } else if (action == MotionEvent.ACTION_UP)
                touchStarted = false;
            else if (action == MotionEvent.ACTION_MOVE && touchStarted) {
                int possibleTop = paddleTop + touchY - prevTouchY;
                if (possibleTop >= 0 && possibleTop + paddleHeight <= screenHeight)
                    paddleTop = possibleTop;
                prevTouchY = touchY;
            }
            return true;
        }

        private boolean isContained(int touchX, int touchY) {
            return touchX > paddleLeft && touchX < paddleLeft + paddleWidth
                    && touchY > paddleTop && touchY < paddleTop + paddleHeight;
        }

        private boolean overlaps() {
            return squarePositionLeft <= paddleLeft + paddleWidth
                    && squarePositionTop + squareDimension >= paddleTop
                    && squarePositionTop <= paddleTop + paddleHeight;
        }

    }

}
