package com.openwebui.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private Button btnSettings;
    private String serverUrl;
    private static final String PREFS_NAME = "OpenWebUIPrefs";
    private static final String KEY_SERVER_URL = "server_url";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup UI
        setupUI();

        // Load saved URL or show settings dialog
        serverUrl = getSavedUrl();
        if (serverUrl == null || serverUrl.isEmpty()) {
            showSettingsDialog();
        } else {
            loadServerUrl(serverUrl);
        }
    }

    private void setupUI() {
        // Create layout programmatically
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        // Create header
        android.widget.LinearLayout header = new android.widget.LinearLayout(this);
        header.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        header.setPadding(12, 12, 12, 12);
        header.setBackgroundColor(0xFF1976D2);

        android.widget.TextView title = new android.widget.TextView(this);
        title.setText("Open WebUI");
        title.setTextSize(20);
        title.setTextColor(0xFFFFFFFF);
        title.setPadding(0, 0, 16, 0);
        title.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
            0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        btnSettings = new Button(this);
        btnSettings.setText("⚙️");
        btnSettings.setTextSize(18);
        btnSettings.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
        btnSettings.setBackgroundColor(0x00000000);
        btnSettings.setOnClickListener(v -> showSettingsDialog());

        header.addView(title);
        header.addView(btnSettings);
        layout.addView(header);

        // Create WebView
        webView = new WebView(this);
        webView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            0, 1f));
        layout.addView(webView);

        // Create ProgressBar
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        android.widget.LinearLayout.LayoutParams progressParams =
            new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        progressParams.gravity = android.view.Gravity.CENTER;
        progressBar.setLayoutParams(progressParams);
        layout.addView(progressBar);

        setContentView(layout);

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

        // Configure WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                progressBar.setVisibility(View.GONE);
                showError("Connection failed: " + description);
            }
        });

        // Enable file upload
        webView.setWebChromeClient(new android.webkit.WebChromeClient());
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
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(serverUrl);
    }

    private void showSettingsDialog() {
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
        });

        dialog.show();
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
