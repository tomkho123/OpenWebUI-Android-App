package com.openwebui.app;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;

/**
 * ServoView - Custom SurfaceView that renders Servo web engine
 * Replaces Android WebView for better performance
 */
public class ServoView extends SurfaceView implements SurfaceHolder.Callback {

    static {
        System.loadLibrary("openwebui-servo");
    }

    // Native methods implemented in Rust
    private native void nativeInit();
    private native void nativeLoadUrl(String url);
    private native void nativeGoBack();
    private native void nativeReload();
    private native void nativeCleanup();
    private native void onSurfaceChanged(int width, int height);

    private boolean isInitialized = false;
    private int surfaceWidth = 0;
    private int surfaceHeight = 0;

    public ServoView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        nativeInit();
        isInitialized = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
        if (isInitialized) {
            onSurfaceChanged(width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        nativeCleanup();
        isInitialized = false;
    }

    /**
     * Load a URL in Servo
     * @param url The URL to load (must include protocol, e.g., http://)
     */
    public void loadUrl(String url) {
        if (!isInitialized) {
            throw new IllegalStateException("ServoView not initialized");
        }
        nativeLoadUrl(url);
    }

    /**
     * Navigate back in history
     */
    public void goBack() {
        if (!isInitialized) {
            return;
        }
        nativeGoBack();
    }

    /**
     * Reload current page
     */
    public void reload() {
        if (!isInitialized) {
            return;
        }
        nativeReload();
    }

    public int getSurfaceWidth() {
        return surfaceWidth;
    }

    public int getSurfaceHeight() {
        return surfaceHeight;
    }
}
