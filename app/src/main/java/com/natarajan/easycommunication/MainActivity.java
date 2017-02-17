package com.natarajan.easycommunication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainAudioRecordAct";
    String action;
    File audioFile1;
    File audioFile2;
    File audioFile3;
    File audioFile4;
    File audioFile5;
    File audioFile6;
    Bitmap b1;
    Bitmap b2;
    Bitmap b3;
    Bitmap b4;
    Bitmap b5;
    Bitmap b6;
    boolean call;
    ImageView imagev1;
    ImageView imagev2;
    ImageView imagev3;
    ImageView imagev4;
    ImageView imagev5;
    ImageView imagev6;
    String mFileName1;
    String mFileName2;
    String mFileName3;
    String mFileName4;
    String mFileName5;
    String mFileName6;
    MediaPlayer mPlayer = null;
    File mediaFile1;
    File mediaFile2;
    File mediaFile3;
    File mediaFile4;
    File mediaFile5;
    File mediaFile6;
    File mediaStorageDirAud;
    File mediaStorageDirPic;
    String name;
    String phone;
    SharedPreferences preferences;
    boolean sms;
    int storPerm;
    TextToSpeech t2sobj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        try {
            this.mediaStorageDirPic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic");
            this.mediaStorageDirAud = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "ActMastAudio");
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Pic not displayed ? Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        }
        setContentView(R.layout.activity_main);
        this.imagev1 = (ImageView) findViewById(R.id.imageview1);
        this.imagev2 = (ImageView) findViewById(R.id.imageview2);
        this.imagev3 = (ImageView) findViewById(R.id.imageview3);
        this.imagev4 = (ImageView) findViewById(R.id.imageview4);
        this.imagev5 = (ImageView) findViewById(R.id.imageview5);
        this.imagev6 = (ImageView) findViewById(R.id.imageview6);
        this.t2sobj = new TextToSpeech(getApplicationContext(), new OnInitListener() {
            public void onInit(int status) {
                if (status != -1) {
                    MainActivity.this.t2sobj.setLanguage(Locale.US);
                }
            }
        });
        this.mFileName1 = this.mediaStorageDirAud.getPath() + File.separator + "A.3gp";
        this.mFileName2 = this.mediaStorageDirAud.getPath() + File.separator + "B.3gp";
        this.mFileName3 = this.mediaStorageDirAud.getPath() + File.separator + "C.3gp";
        this.mFileName4 = this.mediaStorageDirAud.getPath() + File.separator + "D.3gp";
        this.mFileName5 = this.mediaStorageDirAud.getPath() + File.separator + "E.3gp";
        this.mFileName6 = this.mediaStorageDirAud.getPath() + File.separator + "F.3gp";
        this.audioFile1 = new File(this.mediaStorageDirAud.getPath() + File.separator + "A.3gp");
        this.audioFile2 = new File(this.mediaStorageDirAud.getPath() + File.separator + "B.3gp");
        this.audioFile3 = new File(this.mediaStorageDirAud.getPath() + File.separator + "C.3gp");
        this.audioFile4 = new File(this.mediaStorageDirAud.getPath() + File.separator + "D.3gp");
        this.audioFile5 = new File(this.mediaStorageDirAud.getPath() + File.separator + "E.3gp");
        this.audioFile6 = new File(this.mediaStorageDirAud.getPath() + File.separator + "F.3gp");
        this.mediaFile1 = new File(this.mediaStorageDirPic.getPath() + File.separator + "A.jpg");
        this.mediaFile2 = new File(this.mediaStorageDirPic.getPath() + File.separator + "B.jpg");
        this.mediaFile3 = new File(this.mediaStorageDirPic.getPath() + File.separator + "C.jpg");
        this.mediaFile4 = new File(this.mediaStorageDirPic.getPath() + File.separator + "D.jpg");
        this.mediaFile5 = new File(this.mediaStorageDirPic.getPath() + File.separator + "E.jpg");
        this.mediaFile6 = new File(this.mediaStorageDirPic.getPath() + File.separator + "F.jpg");
        try {
            this.b1 = BitmapFactory.decodeFile(this.mediaFile1.toString());
            this.b2 = BitmapFactory.decodeFile(this.mediaFile2.toString());
            this.b3 = BitmapFactory.decodeFile(this.mediaFile3.toString());
            this.b4 = BitmapFactory.decodeFile(this.mediaFile4.toString());
            this.b5 = BitmapFactory.decodeFile(this.mediaFile5.toString());
            this.b6 = BitmapFactory.decodeFile(this.mediaFile6.toString());
        } catch (Exception e2) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Pic not displayed ? Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        }
        setImageView();
    }

    public void setImageView() {
        try {
            int rotateImage;
            if (this.mediaFile1.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile1.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev1.setRotation((float) rotateImage);
                }
                this.imagev1.setImageBitmap(this.b1);
            } else {
                this.imagev1.setBackgroundResource(R.drawable.eating);
            }
            if (this.mediaFile2.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile2.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev2.setRotation((float) rotateImage);
                }
                this.imagev2.setImageBitmap(this.b2);
            } else {
                this.imagev2.setBackgroundResource(R.drawable.drink);
            }
            if (this.mediaFile3.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile3.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev3.setRotation((float) rotateImage);
                }
                this.imagev3.setImageBitmap(this.b3);
            } else {
                this.imagev3.setBackgroundResource(R.drawable.sleep);
            }
            if (this.mediaFile4.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile4.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev4.setRotation((float) rotateImage);
                }
                this.imagev4.setImageBitmap(this.b4);
            } else {
                this.imagev4.setBackgroundResource(R.drawable.rest);
            }
            if (this.mediaFile5.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile5.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev5.setRotation((float) rotateImage);
                }
                this.imagev5.setImageBitmap(this.b5);
            } else {
                this.imagev5.setBackgroundResource(R.drawable.yes);
            }
            if (this.mediaFile6.exists()) {
                rotateImage = getCameraPhotoOrientation(this.mediaFile6.getAbsolutePath());
                if (VERSION.SDK_INT > 11) {
                    this.imagev6.setRotation((float) rotateImage);
                }
                this.imagev6.setImageBitmap(this.b6);
                return;
            }
            this.imagev6.setBackgroundResource(R.drawable.no);
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Pic not displayed ? Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        }
    }

    public int getCameraPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            int orientation = new ExifInterface(new File(imagePath).getAbsolutePath()).getAttributeInt("Orientation", 1);
            switch (orientation) {
                case R.styleable.View_paddingEnd /*3*/:
                    rotate = 180;
                    break;
                case R.styleable.Toolbar_contentInsetEnd /*6*/:
                    rotate = 90;
                    break;
                case R.styleable.Toolbar_contentInsetRight /*8*/:
                    rotate = 270;
                    break;
            }
            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public void onClickView1(View view) {
        this.action = "I am hungry";
        if (this.audioFile1.exists()) {
            onClickAlpa(this.mFileName1, this.imagev1);
        } else {
            this.t2sobj.speak("I am hungry", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickView2(View view) {
        this.action = "I am feeling Thirsty";
        if (this.audioFile2.exists()) {
            onClickAlpa(this.mFileName2, this.imagev2);
        } else {
            this.t2sobj.speak("I am feeling thirsty", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickView3(View view) {
        this.action = "I am feeling sleepy";
        if (this.audioFile3.exists()) {
            onClickAlpa(this.mFileName3, this.imagev3);
        } else {
            this.t2sobj.speak("I am feeling sleepy", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickView4(View view) {
        this.action = "I want to use the washroom";
        if (this.audioFile4.exists()) {
            onClickAlpa(this.mFileName4, this.imagev1);
        } else {
            this.t2sobj.speak("I want to use the washroom", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickView5(View view) {
        this.action = "Yes, Please";
        if (this.audioFile5.exists()) {
            onClickAlpa(this.mFileName5, this.imagev5);
        } else {
            this.t2sobj.speak("Yes, please", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickView6(View view) {
        this.action = "No, Thanks";
        if (this.audioFile6.exists()) {
            onClickAlpa(this.mFileName6, this.imagev6);
        } else {
            this.t2sobj.speak("No, thanks", 0, null);
        }
        SystemClock.sleep(3000);
        onSMSandCall(this.action);
    }

    public void onClickAlpa(String s, final ImageView iv) {
        this.mPlayer = new MediaPlayer();
        try {
            this.mPlayer.setDataSource(s);
            this.mPlayer.prepare();
            this.mPlayer.start();
            if (this.mPlayer.isPlaying()) {
                iv.setClickable(false);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app AUDIO permission", 0).show();
            }
        }
        this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mPlayer) {
                iv.setClickable(true);
            }
        });
    }

    public void onResume() {
        super.onResume();
        Log.i("Resume", "Am in Resume");
        this.t2sobj = new TextToSpeech(getApplicationContext(), new OnInitListener() {
            public void onInit(int status) {
                if (status != -1) {
                    MainActivity.this.t2sobj.setLanguage(Locale.US);
                }
            }
        });
    }

    public void onDestroy() {
        if (this.t2sobj != null) {
            this.t2sobj.stop();
            this.t2sobj.shutdown();
        }
        super.onDestroy();
    }

    public void onStop() {
        if (this.t2sobj != null) {
            this.t2sobj.stop();
            this.t2sobj.shutdown();
        }
        super.onStop();
    }

    public void speakText(String str) {
        this.t2sobj.speak(str, 0, null);
    }

    public void onSMSandCall(String action) {
        this.preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.PREFS_NAME), 0);
        this.name = this.preferences.getString("name", "no1 name");
        this.phone = this.preferences.getString("phone", "no1 phone");
        this.sms = this.preferences.getBoolean("sms", false);
        this.call = this.preferences.getBoolean("call", false);
        Log.i("values in MainActivity", "name is" + this.name + "phone is" + this.phone + "sms is" + this.sms + "call is" + this.call);
        if (this.sms) {
            try {
                SmsManager.getDefault().sendTextMessage(this.phone, null, this.name + "," + action, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent", 1).show();
            } catch (Exception ex) {
                if (VERSION.SDK_INT >= 23) {
                    Toast.makeText(this, "Please confirm you have provided app SMS permission", 0).show();
                }
                Toast.makeText(getApplicationContext(), ex.getMessage().toString(), 1).show();
                ex.printStackTrace();
            }
        }
        if (this.call) {
            try {
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + this.phone)));
            } catch (Exception e) {
                if (VERSION.SDK_INT >= 23) {
                    Toast.makeText(this, "Please confirm you have provided app CALL permission", 0).show();
                }
                Log.i("permission", "no1 call permission");
            }
        }
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
}
