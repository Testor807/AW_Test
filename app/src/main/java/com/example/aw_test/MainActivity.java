package com.example.aw_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Button btn, btnLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.txt);
        btn = findViewById(R.id.btn);
        btnLaunch = findViewById(R.id.btn_launch);

        updateUIState(checkAccessibilityPermission());

        btn.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

        btnLaunch.setOnClickListener(view -> launchApp("com.google.android.youtube"));

        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this::updateUIState);
    }

    private void updateUIState(boolean isAccessibilityEnabled) {
        if (isAccessibilityEnabled) {
            text.setText("The Accessibility Service has been started");
            btn.setEnabled(false);
            btnLaunch.setEnabled(true);
        } else {
            text.setText("The Accessibility Service has been closed");
            btn.setEnabled(true);
            btnLaunch.setEnabled(false);
        }
    }

    private void launchApp(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAccessibilityPermission() {
        try {
            return Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED) != 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
