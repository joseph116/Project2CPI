package com.example.appname.View.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appname.R;

public class RequestForPermissionActivity extends AppCompatActivity {

    private Button mAllowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_permission);
        mAllowButton = findViewById(R.id.allow_button);
        mAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForPermission();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canAccessExternalSd()) {
            finish();
        }
    }

    //==============================================================================================
    //  FOR PERMISSION
    //==============================================================================================

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {
        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }
        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "1");
        switch (theme) {
            case "1":
                setTheme(R.style.Theme1);
                break;
            case "2":
                setTheme(R.style.Theme2);
                break;
            case "3":
                setTheme(R.style.Theme3);
                break;
            case "4":
                setTheme(R.style.Theme4);
                break;
            case "5":
                setTheme(R.style.Theme5);
            default:
                break;
        }
    }
}
