package com.natarajan.easycommunication;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMSSetting extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    CheckBox call;
    int callPerm;
    EditText name;
    EditText phone;
    SharedPreferences preferences;
    CheckBox sms;
    int smsPerm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_setting);
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        this.name = (EditText) findViewById(R.id.input_name);
        this.name.setText(getValueFromSP("name"));
        this.phone = (EditText) findViewById(R.id.input_phone);
        this.phone.setText(getValueFromSP("phone"));
        this.sms = (CheckBox) findViewById(R.id.checkBox);
        this.sms.setChecked(getFromSP("sms"));
        this.call = (CheckBox) findViewById(R.id.checkBox2);
        this.call.setChecked(getFromSP("call"));
    }

    public void onClickSave(View view) {
        if (VERSION.SDK_INT >= 23) {
            getPermissions();
        }
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        Editor editor = this.preferences.edit();
        editor.putString("name", this.name.getText().toString());
        editor.putString("phone", this.phone.getText().toString());
        editor.putBoolean("sms", this.sms.isChecked());
        editor.putBoolean("call", this.call.isChecked());
        editor.commit();
        Log.i("values", "name is" + this.name.getText().toString() + "phone is" + this.phone.getText().toString() + "sms is" + this.sms.isChecked() + "call is" + this.call.isChecked());
        Toast.makeText(this, "Details Saved", Toast.LENGTH_LONG).show();
    }

    private boolean getFromSP(String key) {
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        return this.preferences.getBoolean(key, false);
    }

    private String getValueFromSP(String key) {
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        return this.preferences.getString(key, "blank");
    }

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
            showMessageOKCancel(message, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SMSSetting.this.requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
                }
            });
            return;
        }
        requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 124);
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != 0) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, OnClickListener okListener) {
        new Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
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
                    Toast.makeText(this, "Thanks for giving access to all permissions, app will function as expected", 0).show();
                    return;
                } else {
                    Toast.makeText(this, "Some Permission is Denied, app might not work as expected", 0).show();
                    return;
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }
}
