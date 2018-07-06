/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.girmiti.mobilepos.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.qrcode.camera.CameraManager;
import com.girmiti.mobilepos.qrcode.config.ZXingLibConfig;
import com.girmiti.mobilepos.util.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import java.io.IOException;
import java.util.ArrayList;

import static com.girmiti.mobilepos.R.id.img_left_toolbar_arrow;

/**
 * The barcode reader activity itself. This is loosely based on the
 * CameraPreview example included in the Android SDK.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * @author modified by Jim.H
 */
public final class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String  TAG  = CaptureActivity.class.getSimpleName();

    private static final long      INTENT_RESULT_DURATION = 1500L;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean                hasSurface;
    private ArrayList<BarcodeFormat> decodeFormats;
    private String                 characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private ZXingLibConfig config;
    private Button cancelbtn;

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        ImageView leftArrow;
        TextView toolBar;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.zxinglib_capture);
        toolBar = (TextView) findViewById(R.id.tvToolbar_left);
        toolBar.setText(R.string.qr_code_title);

        leftArrow = (ImageView) findViewById(img_left_toolbar_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            config = (ZXingLibConfig) intent.getSerializableExtra(ZXingLibConfig.INTENT_KEY);
        }
        if (config == null) {
            config = new ZXingLibConfig();
        }

        CameraManager.init(getApplication(), config);
        viewfinderView = (ViewfinderView) findViewById(R.id.zxinglib_viewfinder_view);
        cancelbtn = (Button) findViewById(R.id.cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handler = null;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this, config);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetStatusView();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.zxinglib_preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        Intent intent = getIntent();
        decodeFormats = null;
        characterSet = null;
        if (intent != null) {
            characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
            // Scan the formats the intent requested, and return the result to the calling activity.
            decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
            if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
                int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
                int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
                if (width > 0 && height > 0) {
                    CameraManager.get().setManualFramingRect(width, height);
                }
            }
        }

        beepManager.updatePrefs();

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Handle these events so they don't launch the Camera app
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //No Operation is performing during surface change
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param barcode A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();

        beepManager.playBeepSoundAndVibrate();
        drawResultPoints(barcode, rawResult);

        viewfinderView.drawResultBitmap(barcode);

        String resultContent = rawResult.toString();
        if (config.copyToClipboard) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(resultContent);
        }

        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.RESULT, resultContent);
        intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
        }
        Message message = Message.obtain(handler, R.id.zxinglib_return_scan_result);
        message.obj = intent;
        handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of
     * the barcode.
     *
     * @param barcode A bitmap of the captured image.
     * @param rawResult The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.zxinglib_result_image_border));
            paint.setStrokeWidth(Constants.STROKE_WIDTH_THREE);
            paint.setStyle(Paint.Style.STROKE);
            Rect border = new Rect( Constants.TWO, Constants.TWO, barcode.getWidth() - Constants.TWO, barcode.getHeight() - Constants.TWO);
            canvas.drawRect(border, paint);

            paint.setColor(getResources().getColor(R.color.zxinglib_result_points));
            if (points.length == Constants.TWO) {
                paint.setStrokeWidth(Constants.STROKE_WIDTH_FOUR);
                drawLine(canvas, paint, points[0], points[1]);
            } else if (points.length == Constants.FOUR
                    && (rawResult.getBarcodeFormat().equals(BarcodeFormat.UPC_A) || rawResult
                            .getBarcodeFormat().equals(BarcodeFormat.EAN_13))) {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[Constants.ONE]);
                drawLine(canvas, paint, points[Constants.TWO], points[Constants.THREE]);
            } else {
                paint.setStrokeWidth(Constants.STROKE_WIDTH_TEN);
                for (ResultPoint point : points) {
                    canvas.drawPoint(point.getX(), point.getY(), paint);
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b) {
        canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializating camera", e);
        }
    }

    private void resetStatusView() {
        cancelbtn.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }
}
