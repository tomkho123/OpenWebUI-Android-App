package com.openwebui.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * ServoView - Advanced SurfaceView for Servo web engine
 * Provides rendering surface and touch handling
 */
public class ServoView extends SurfaceView implements SurfaceHolder.Callback {

    static {
        try {
            System.loadLibrary("openwebui-servo");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load native library: " + e);
        }
    }

    // Native methods
    private native void nativeInit();
    private native void nativeLoadUrl(String url);
    private native void nativeGoBack();
    private native void nativeReload();
    private native void nativeCleanup();
    private native void nativeSetSurface(long surfacePtr, int width, int height);
    private native boolean nativeHandleTouchEvent(int action, float x, float y);

    private boolean isInitialized = false;
    private int surfaceWidth = 0;
    private int surfaceHeight = 0;
    private Handler mainHandler;

    private OnUrlChangedListener urlChangedListener;

    public interface OnUrlChangedListener {
        void onUrlChanged(String url);
    }

    public ServoView(Context context) {
        super(context);
        getHolder().addCallback(this);

        // Configure SurfaceView for Servo rendering
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setOnUrlChangedListener(OnUrlChangedListener listener) {
        this.urlChangedListener = listener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainHandler.post(() -> {
            android.view.Surface surface = holder.getSurface();
            if (surface != null && surface.isValid()) {
                long surfacePtr = getSurfacePointer(surface);
                if (surfacePtr != 0) {
                    nativeInit();
                    nativeSetSurface(surfacePtr, getWidth(), getHeight());
                    isInitialized = true;
                }
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;

        mainHandler.post(() -> {
            android.view.Surface surface = holder.getSurface();
            if (surface != null && surface.isValid()) {
                long surfacePtr = getSurfacePointer(surface);
                if (surfacePtr != 0) {
                    nativeSetSurface(surfacePtr, width, height);
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mainHandler.post(() -> {
            nativeCleanup();
            isInitialized = false;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInitialized) {
            return false;
        }

        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        boolean handled = nativeHandleTouchEvent(action, x, y);

        return handled || super.onTouchEvent(event);
    }

    /**
     * Load a URL in Servo
     */
    public void loadUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        }

        mainHandler.post(() -> {
            if (!isInitialized) {
                throw new IllegalStateException("ServoView not initialized");
            }
            nativeLoadUrl(url);

            if (urlChangedListener != null) {
                urlChangedListener.onUrlChanged(url);
            }
        });
    }

    /**
     * Navigate back in history
     */
    public void goBack() {
        mainHandler.post(() -> {
            if (!isInitialized) {
                return;
            }
            nativeGoBack();
        });
    }

    /**
     * Reload current page
     */
    public void reload() {
        mainHandler.post(() -> {
            if (!isInitialized) {
                return;
            }
            nativeReload();
        });
    }

    /**
     * Get native surface pointer
     */
    private native long getSurfacePointer(android.view.Surface surface);
}
