package com.openwebui.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String serverUrl;
    private GestureDetectorCompat gestureDetector;
    private static final String PREFS_NAME = "OpenWebUIPrefs";
    private static final String KEY_SERVER_URL = "server_url";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Enable fullscreen
            enableFullscreen();

            // Setup fullscreen UI
            setupFullscreenUI();

            // Setup gesture detector
            setupGestures();

            // Load saved URL or show settings dialog
            serverUrl = getSavedUrl();
            if (serverUrl == null || serverUrl.isEmpty()) {
                // Delay showing dialog to avoid crash during initialization
                new android.os.Handler().postDelayed(() -> {
                    showSettingsDialog();
                }, 500);
            } else {
                loadServerUrl(serverUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error initializing app: " + e.getMessage());
        }
    }

    private void enableFullscreen() {
        // Hide status bar and navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setupFullscreenUI() {
        // Create WebView for fullscreen
        webView = new WebView(this);
        webView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT));

        // NO padding - let content use full screen
        // We'll handle notification bar conflicts differently
        webView.setPadding(0, 0, 0, 0);

        setContentView(webView);

        // Configure WebView
        configureWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        WebSettings settings = webView.getSettings();

        // Enable JavaScript
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        // Enable responsive design
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        // Enable file upload
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Improve performance
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // Reduce font size by 5% (0.95 = 95% of original size)
        settings.setTextZoom(95);

        // Configure WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Re-apply fullscreen after page load
                enableFullscreen();

                // Inject CSS for better keyboard handling with delay to ensure page is ready
                new android.os.Handler().postDelayed(() -> {
                    injectKeyboardOptimizationCSS();
                }, 500);

                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showError("Connection failed: " + description);
            }
        });

        // Enable file upload
        webView.setWebChromeClient(new android.webkit.WebChromeClient());
    }

    private void setupGestures() {
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Only trigger if touch is well below status bar area
                if (e.getY() > 150) { // Increased to 150px for safety
                    showSettingsDialog();
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // Only trigger if touch is well below status bar area
                if (e.getY() > 150) { // Increased to 150px for safety
                    showSettingsDialog();
                }
            }
        });

        webView.setOnTouchListener((v, event) -> {
            float y = event.getY();

            // Completely ignore all touches in top 150px for system gestures
            if (y <= 150) {
                return false; // Let system handle notification bar pulls
            }

            gestureDetector.onTouchEvent(event);
            return false;
        });
    }

    private void loadServerUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            showError("Please enter a server URL");
            showSettingsDialog();
            return;
        }

        // Validate URL
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        serverUrl = url;
        webView.loadUrl(serverUrl);

        // Inject CSS after a short delay to ensure page starts loading
        new android.os.Handler().postDelayed(() -> {
            injectKeyboardOptimizationCSS();
        }, 1000);
    }

    private void showSettingsDialog() {
        try {
            runOnUiThread(() -> {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();

                    View dialogView = inflater.inflate(R.layout.dialog_settings, null);
                    builder.setView(dialogView);

                    EditText etServerUrl = dialogView.findViewById(R.id.etServerUrl);
                    CheckBox cbSaveUrl = dialogView.findViewById(R.id.cbSaveUrl);
                    Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                    Button btnConnect = dialogView.findViewById(R.id.btnConnect);

                    // Set current URL
                    if (serverUrl != null) {
                        etServerUrl.setText(serverUrl);
                    }

                    AlertDialog dialog = builder.create();

                    btnCancel.setOnClickListener(v -> {
                        if (serverUrl == null || serverUrl.isEmpty()) {
                            finish(); // Exit if no URL set
                        } else {
                            dialog.dismiss();
                            enableFullscreen(); // Re-enable fullscreen
                        }
                    });

                    btnConnect.setOnClickListener(v -> {
                        String inputUrl = etServerUrl.getText().toString().trim();

                        if (inputUrl.isEmpty()) {
                            showError("Please enter a server URL");
                            return;
                        }

                        // Save URL if checked
                        if (cbSaveUrl.isChecked()) {
                            saveUrl(inputUrl);
                        } else {
                            clearSavedUrl();
                        }

                        serverUrl = inputUrl;
                        dialog.dismiss();
                        loadServerUrl(serverUrl);
                        enableFullscreen(); // Re-enable fullscreen
                    });

                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error showing settings: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error: " + e.getMessage());
        }
    }

    private void saveUrl(String url) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_SERVER_URL, url);
        editor.apply();
    }

    private String getSavedUrl() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SERVER_URL, null);
    }

    private void clearSavedUrl() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_SERVER_URL);
        editor.apply();
    }

    private void showError(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    private void injectKeyboardOptimizationCSS() {
        // Inject comprehensive CSS and JavaScript for keyboard handling
        String js = "javascript:(function() {" +
            "// Set proper viewport" +
            "var viewport = document.querySelector('meta[name=viewport]');" +
            "if (viewport) {" +
            "  viewport.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0');" +
            "} else {" +
            "  var meta = document.createElement('meta');" +
            "  meta.name = 'viewport';" +
            "  meta.content = 'width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0';" +
            "  document.head.appendChild(meta);" +
            "}" +

            // Add comprehensive CSS
            "var style = document.createElement('style');" +
            "style.id = 'keyboard-optimization';" +
            "style.innerHTML = " +
            "'html, body {" +
            "  height: 100% !important;" +
            "  overflow: auto !important;" +
            "  position: relative;" +
            "}' +" +
            "'input, textarea, select {" +
            "  font-size: 16px !important;" + // Prevent zoom on focus
            "  max-width: 100% !important;" +
            "}' +" +
            "'@media (max-height: 600px) {" +
            "  html {" +
            "    zoom: 0.75;" + // Zoom out when keyboard opens
            "  }" +
            "}' +" +
            "'@media (min-height: 601px) {" +
            "  html {" +
            "    zoom: 1;" + // Reset zoom when keyboard closes
            "  }" +
            "'}" +
            "'body.keyboard-open {" +
            "  zoom: 0.75;" +
            "'}';" +
            "document.head.appendChild(style);" +

            // Add JavaScript to detect keyboard and apply classes
            "var originalHeight = window.innerHeight;" +
            "window.addEventListener('resize', function() {" +
            "  var currentHeight = window.innerHeight;" +
            "  var heightDiff = originalHeight - currentHeight;" +

            "  if (heightDiff > 200) {" + // Keyboard opened
            "    document.body.classList.add('keyboard-open');" +
            "    document.documentElement.style.zoom = '0.75';" +
            "  } else {" + // Keyboard closed
            "    document.body.classList.remove('keyboard-open');" +
            "    document.documentElement.style.zoom = '1';" +
            "  }" +
            "}, false);" +

            // Force initial check
            "setTimeout(function() {" +
            "  if (window.innerHeight < 600) {" +
            "    document.documentElement.style.zoom = '0.75';" +
            "  }" +
            "}, 1000);" +

            "console.log('Keyboard optimization CSS injected');" +
            "})()";
        webView.evaluateJavascript(js, null);
    }

    @Override
    public void onBackPressed() {
        // Show settings dialog on back press (to exit or change server)
        showSettingsDialog();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            enableFullscreen();
        }
    }
}