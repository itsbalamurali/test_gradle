package com.girmiti.mobilepos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.logger.Logger;
import com.girmiti.mobilepos.util.CurrencyFormat;

import java.io.ByteArrayOutputStream;

import static com.girmiti.mobilepos.logger.Logger.getNewLogger;

public class SignatureCapture extends Activity {

    LinearLayout mContent;
    Signature mSignature;
    Bitmap mBitmap;
    Button capture;
    byte[] imageData;
    ProgressDialog progress;
    public static final String TAG = "SignatureCapture";
    public static final int HANDLER_DELAY_TIME = 3000;
    public static final int IMAGE_WIDTH = 210;
    public static final int IMAGE_HEIGHT = 100;
    public static final int BITMAP_COMPRESS_SIZE = 90;
    private Logger logger = getNewLogger(SignatureCapture.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signature_capture);

        mContent = (LinearLayout) findViewById(R.id.layout);
        capture = (Button) findViewById(R.id.capture);

        mSignature = new Signature(this, null);
        mContent.addView(mSignature, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String amount = b.getString("txn_amount");
            ((TextView) findViewById(R.id.amount_value)).setText(CurrencyFormat.getFormattedCurrency(amount));
        }

        capture.setEnabled(false);
    }

    public void clear(View view) {
        mSignature.clear();
        capture.setEnabled(false);
    }

    public void capture(View view) {
        mContent.setDrawingCacheEnabled(true);
        imageData = mSignature.save(mContent);

        progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.processing));
        progress.setCancelable(false);
        progress.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                moveToNext();
            }

        }, HANDLER_DELAY_TIME);
    }

    void moveToNext() {
        progress.dismiss();
        Intent intent = new Intent(getApplicationContext(), SlidingMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        finish();
        Toast.makeText(this, getString(R.string.signature_captured_successfully), Toast.LENGTH_SHORT).show();
    }

    class Signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final RectF dirtyRect = new RectF();
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;

        public Signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public byte[] save(View v) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }

            Canvas canv = new Canvas(mBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            v.draw(canv);

            Bitmap result = Bitmap.createScaledBitmap(mBitmap, IMAGE_WIDTH, IMAGE_HEIGHT, false);

            result.compress(Bitmap.CompressFormat.PNG, BITMAP_COMPRESS_SIZE, baos);

            return baos.toByteArray();

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
        float eventX;
        float eventY;
        @Override
        public boolean onTouchEvent(MotionEvent event) {
             eventX = event.getX();
             eventY = event.getY();
            capture.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:
                    actionUpImpl(event);
                    break;

                default:
                    logger.debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void actionUpImpl(MotionEvent event) {
            resetDirtyRect(eventX, eventY);
            int historySize = event.getHistorySize();
            for (int i = 0; i < historySize; i++) {
                float historicalX = event.getHistoricalX(i);
                float historicalY = event.getHistoricalY(i);
                expandDirtyRect(historicalX, historicalY);
                path.lineTo(historicalX, historicalY);
            }
            path.lineTo(eventX, eventY);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
