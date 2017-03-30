package com.framgia.mobileprototype;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 27/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype
 */
public abstract class PermissionActivity extends AppCompatActivity implements PermissionCallback {
    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isCorrectApiLevel()) checkClassPermissions();
        else onPermissionGranted(getClassPermissions());
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permissions);
            } else {
                onPermissionDenied(permissions);
            }
        }
    }

    private void checkClassPermissions() {
        String[] classPermissions = getClassPermissions();
        if (classPermissions != null) {
            askForPermissions(classPermissions);
        }
    }

    private void askForPermissions(String[] permissions) {
        List<String> deniedPermissions = getDeniedPermissions(permissions);
        if (!deniedPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                deniedPermissions.toArray(new String[deniedPermissions.size()]),
                MY_PERMISSIONS_REQUEST);
        }
    }

    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> result = new ArrayList<>();
        for (String permission : permissions) {
            if (!isGrantedPermission(permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    private boolean isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED;
    }

    private boolean isCorrectApiLevel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private String[] getClassPermissions() {
        RegisterPermission registerPermission =
            this.getClass().getAnnotation(RegisterPermission.class);
        if (registerPermission != null) {
            return registerPermission.permissions();
        }
        return null;
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        Toast.makeText(this, R.string.msg_grant_permission, Toast.LENGTH_LONG).show();
        finish();
    }
}
