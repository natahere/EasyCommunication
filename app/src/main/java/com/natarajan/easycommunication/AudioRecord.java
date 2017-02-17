package com.natarajan.easycommunication;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class AudioRecord extends AppCompatActivity implements OnInfoListener {
    private static final String LOG_TAG = "AudioRecordTest";
    static final int MY_PERMISSIONS_REQUEST_AUDIO = 101;
    CountDownTimer Count = null;
    String mFileName;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    Button pb;
    Button st;
    View view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_activity);
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "ActMastAudio");
            if (!(mediaStorageDir.exists() || mediaStorageDir.mkdirs())) {
                Log.d("ActMastAudio", "failed to create directory");
            }
            if (mediaStorageDir.exists()) {
                this.mFileName = mediaStorageDir.getPath() + File.separator + getIntent().getExtras().getString("picture") + ".3gp";
            }
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        }
    }

    public void startRecording(View view) {
        try {
            this.mRecorder = new MediaRecorder();
            this.mRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(1);
            this.mRecorder.setOutputFile(this.mFileName);
            this.mRecorder.setAudioEncoder(1);
            this.mRecorder.setMaxDuration(30000);
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app AUDIO permission", 0).show();
            }
        }
        try {
            this.mRecorder.prepare();
        } catch (IOException e2) {
            Log.e(LOG_TAG, "prepare() failed");
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app AUDIO permission", 0).show();
            }
            finish();
        }
        this.mRecorder.setOnInfoListener(this);
        try {
            this.mRecorder.start();
            Toast.makeText(this, "Recording has begun", 0).show();
            this.Count = new CountDownTimer(30000, 1000) {
                TextView mTextField = ((TextView) AudioRecord.this.findViewById(R.id.textView1));

                public void onTick(long millisUntilFinished) {
                    this.mTextField.setText("seconds remaining: " + (millisUntilFinished / 1000));
                }

                public void onFinish() {
                    this.mTextField.setText("done!");
                }
            }.start();
        } catch (Exception e3) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app AUDIO permission", 0).show();
            }
        }
        ((Button) findViewById(R.id.StartRecBtn)).setVisibility(4);
        ((Button) findViewById(R.id.StopRecBtn)).setVisibility(0);
    }

    public void stopRecording(View view) {
        this.mRecorder.stop();
        this.mRecorder.release();
        this.mRecorder = null;
        this.Count.cancel();
        ((TextView) findViewById(R.id.textView1)).setText("done!");
        Toast.makeText(this, "Recording has been stopped, Audio has been saved", 0).show();
        ((Button) findViewById(R.id.StartRecBtn)).setVisibility(0);
        ((Button) findViewById(R.id.StopRecBtn)).setVisibility(4);
    }

    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == 800) {
            Toast.makeText(this, "Duration of 30 seconds reached - Recording will end now and saved automtically", 1).show();
            stopRecording(this.view);
        }
    }

    private boolean ifMediaFileExists(View view) {
        if (new File(this.mFileName).exists()) {
            return true;
        }
        return false;
    }

    public void onClickPB(View view) {
        if (ifMediaFileExists(view)) {
            this.mPlayer = new MediaPlayer();
            try {
                this.mPlayer.setDataSource(this.mFileName);
                this.mPlayer.prepare();
                this.mPlayer.start();
                this.pb = (Button) findViewById(R.id.pbbtn);
                this.pb.setVisibility(4);
                this.st = (Button) findViewById(R.id.stopbtn);
                this.st.setVisibility(0);
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        } else {
            Toast.makeText(this, "Please record something to check playback", 1).show();
        }
        if (this.mPlayer != null) {
            this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    AudioRecord.this.mPlayer.release();
                    AudioRecord.this.mPlayer = null;
                    AudioRecord.this.pb = (Button) AudioRecord.this.findViewById(R.id.pbbtn);
                    AudioRecord.this.pb.setVisibility(0);
                    AudioRecord.this.st = (Button) AudioRecord.this.findViewById(R.id.stopbtn);
                    AudioRecord.this.st.setVisibility(4);
                }
            });
        }
    }

    public void onClickStop(View view) {
        this.mPlayer.release();
        this.mPlayer = null;
        this.pb = (Button) findViewById(R.id.pbbtn);
        this.pb.setVisibility(4);
        this.st = (Button) findViewById(R.id.stopbtn);
        this.st.setVisibility(0);
        Toast.makeText(this, "Playback stopped", 0).show();
    }
}
