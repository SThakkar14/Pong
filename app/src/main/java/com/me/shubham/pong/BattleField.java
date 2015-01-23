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

    private static class Paddle {

        float paddleLeft;
        float paddleTop;
        float paddleWidth;
        float paddleHeight;

        boolean touchStarted;

        float x;
        float y;

        private Paddle(float paddleLeft, float paddleTop, float paddleWidth, float paddleHeight) {
            this.paddleLeft = paddleLeft;
            this.paddleTop = paddleTop;
            this.paddleLeft = paddleLeft;
            this.paddleWidth = paddleWidth;
            this.paddleHeight = paddleHeight;

            touchStarted = false;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

    private static class Square {
        float squareXVelocity;
        float squareYVelocity;

        float squarePositionLeft;
        float squarePositionTop;
        float squareDimension;

        float x;
        float y;

        private Square(float squarePositionLeft, float squarePositionTop, float squareDimension) {
            squareXVelocity = 5;
            squareYVelocity = 5;

            this.squarePositionLeft = squarePositionLeft;
            this.squarePositionTop = squarePositionTop;
            this.squareDimension = squareDimension;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

    class myView extends View {
        Paint paint;
        Square square;
        Paddle paddleLeft;
        Paddle paddleRight;

        int screenWidth;
        int screenHeight;

        public myView(Context context) {
            super(context);

            paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));

            paddleLeft = new Paddle(0, 0, 50, 400);
            paddleRight = new Paddle(0, 0, 50, 400);

        }

        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
            screenHeight = h;
            screenWidth = w;

            square = new Square(w / 2 - 10, h / 2 - 10, 20);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            drawComponents(canvas);

            canvas.drawRect(paddleLeft, paddleTop, paddleLeft + paddleWidth, paddleTop + paddleHeight, paint);
            canvas.drawRect(squarePositionLeft, squarePositionTop, squarePositionLeft + squareDimension, squarePositionTop + squareDimension, paint);


            if (squarePositionLeft < 0 || squarePositionLeft + squareDimension > screenWidth || overlaps())
                squareXVelocity *= -1;
            if (squarePositionTop < 0 || squarePositionTop + squareDimension > screenHeight)
                squareYVelocity *= -1;


            squarePositionLeft += squareXVelocity;
            squarePositionTop += squareYVelocity;


            invalidate();
        }

        public void drawComponents(Canvas canvas) {
            canvas.drawRect(paddleLeft.paddleLeft, paddleLeft.paddleTop, paddleLeft.paddleLeft + paddleLeft.paddleWidth, );
        }

        public boolean onTouchEvent(@NonNull MotionEvent e) {

            int touchX = (int) e.getRawX();
            int touchY = (int) e.getRawY();

            int action = e.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {

                if (!paddleOneTouched && isContained(touchX, touchY, 1)) {
                    paddleOneTouched = true;

                } else if (!paddleTwoTouched && isContained(touchX, touchY, 2)) {
                    paddleTwoTouched = true;
                }


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

        private boolean isContained(int touchX, int touchY, int paddleIndex) {
            if (paddleIndex == 1)
                return touchX > paddleOneLeft && touchX < paddleOneLeft + paddleOneWidth
                        && touchY > paddleOneTop && touchY < paddleOneTop + paddleOneHeight;
            else
                return touchX > paddleTwoLeft && touchX < paddleTwoLeft + paddleTwoWidth
                        && touchY > paddleTwoTop && touchY < paddleTwoTop + paddleTwoHeight;
        }

        private boolean overlaps(int paddleIndex) {
            return squarePositionLeft <= paddleLeft + paddleWidth
                    && squarePositionTop + squareDimension >= paddleTop
                    && squarePositionTop <= paddleTop + paddleHeight;
        }

    }

}
