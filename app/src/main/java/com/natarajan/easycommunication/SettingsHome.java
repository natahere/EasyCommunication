package com.natarajan.easycommunication;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsHome extends AppCompatActivity {
    Button b1;
    Button b2;
    Intent intent;
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_SETTINGS = 123;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_settings);
        this.b1 = (Button) findViewById(R.id.mediaBtn);
        this.b2 = (Button) findViewById(R.id.capbtn);

        if (Build.VERSION.SDK_INT >= 23) {
            getPermissions();
        }

    }

    public void audioPicAct(View view) {
        switch (Integer.parseInt(view.getTag().toString())) {
            case SettingsTask.MEDIA_TYPE_IMAGE /*1*/:
                this.intent = new Intent("android.intent.action.SETTINGSTASK");
                break;
            case SettingsTask.MEDIA_TYPE_VIDEO /*2*/:
                this.intent = new Intent("android.intent.action.SMSSETTING");
                break;
        }
        startActivity(this.intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_settings) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent("android.intent.action.HELPACT"));
        return true;
    }


    //added permissions

    @TargetApi(23)
    public void getPermissions() {
        List<String> permissionsNeeded = new ArrayList();
        final List<String> permissionsList = new ArrayList();
        if (!addPermission(permissionsList, "android.permission.RECORD_AUDIO")) {
            permissionsNeeded.add("AUDIO");
        }
        if (!addPermission(permissionsList, "android.permission.CAMERA")) {
            permissionsNeeded.add("Camera access");
        }
        if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE")) {
            permissionsNeeded.add("Read Storage access");
        }
        if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            permissionsNeeded.add("Write Storage Access");
        }
        if (!addPermission(permissionsList, "android.permission.SEND_SMS")) {
            permissionsNeeded.add("Send SMS access");
        }
        if (!addPermission(permissionsList, "android.permission.CALL_PHONE")) {
            permissionsNeeded.add("Call Phone access");
        }
        if (permissionsList.size() <= 0) {
            return;
        }
        if (permissionsNeeded.size() > 0) {
            String message = "You need to grant access to " + ((String) permissionsNeeded.get(0));
            for (int i = 0; i < permissionsNeeded.size(); i++) {
                message = message + ", " + ((String) permissionsNeeded.get(i));
            }
            showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SettingsHome.this.requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
                }
            });
            return;
        }
        requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS_SETTINGS:
                Map<String, Integer> perms = new HashMap();
                perms.put("android.permission.RECORD_AUDIO", Integer.valueOf(0));
                perms.put("android.permission.CAMERA", Integer.valueOf(0));
                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.SEND_SMS", Integer.valueOf(0));
                perms.put("android.permission.CALL_PHONE", Integer.valueOf(0));
                for (int i = 1; i < permissions.length; i++) {
                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                }
                if (((Integer) perms.get("android.permission.RECORD_AUDIO")).intValue() == 0 && ((Integer) perms.get("android.permission.CAMERA")).intValue() == 0 && ((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0 && ((Integer) perms.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0 && ((Integer) perms.get("android.permission.SEND_SMS")).intValue() == 0 && ((Integer) perms.get("android.permission.CALL_PHONE")).intValue() == 0) {
                    Toast.makeText(this, "Thanks for giving access to all permissions, app will function as expected", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(this, "Looks like you denied some of the permissions, app might not work as expected", Toast.LENGTH_LONG).show();
                    return;
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

}
