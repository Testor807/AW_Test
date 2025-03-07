package com.example.aw_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WindowActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isFloatWindowOpen = false;
    private Intent floatServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        findViewById(R.id.btn_on).setOnClickListener(this);
        findViewById(R.id.btn_off).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_on) {
            floatServiceIntent = new Intent(this, FloatService.class);
            floatServiceIntent.putExtra(FloatService.OPERATION, FloatService.OPERATION_SHOW);
            if (!Settings.canDrawOverlays(this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
            } else {
                startService(floatServiceIntent);
                Toast.makeText(this, "悬浮框已开启~", Toast.LENGTH_SHORT).show();
                isFloatWindowOpen = true;
            }
        }else if( v.getId() == R.id.btn_off){
            stopFloatWindowService();
            Toast.makeText(this, "悬浮框已关闭~", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopFloatWindowService() {
        if (isFloatWindowOpen) {
            stopService(floatServiceIntent);
            isFloatWindowOpen = false;
        }
    }

    @Override
    protected void onDestroy() {
        stopFloatWindowService();
        super.onDestroy();
    }
}