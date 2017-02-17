package com.natarajan.easycommunication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsTask extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String LOG_TAG = "MainAudioRecord";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int SELECT_PICTURE = 1002;
    String LeftString;
    String OrientFile;
    String RightString;
    ImageView alpaView;
    Bitmap bitmap;
    ValueEnum enumval;
    private Uri fileUri;
    Intent intent = new Intent();
    LinearLayout layout;
    MediaPlayer mPlayer = null;
    Map<String, Integer> map = new HashMap();
    File mediaFile;
    int roateImage = 0;


    private enum ValueEnum {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        L,
        M,
        N,
        O,
        P,
        Q,
        R,
        S,
        T,
        U,
        V,
        W,
        X,
        Y,
        Z
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tast_settings);
        Toast.makeText(this, "Please swipe left or right for other actions", 0).show();
        this.map.put("A", Integer.valueOf(R.drawable.eating));
        this.map.put("B", Integer.valueOf(R.drawable.drink));
        this.map.put("C", Integer.valueOf(R.drawable.sleep));
        this.map.put("D", Integer.valueOf(R.drawable.rest));
        this.map.put("E", Integer.valueOf(R.drawable.yes));
        this.map.put("F", Integer.valueOf(R.drawable.no));
        this.layout = (LinearLayout) findViewById(R.id.layoutmain);
        this.alpaView = (ImageView) findViewById(R.id.articles);
        DisplayImage(this.alpaView.getTag().toString());
        if (savedInstanceState != null) {
            this.OrientFile = savedInstanceState.getString("picture");
            this.alpaView = (ImageView) findViewById(R.id.articles);
            if (savedInstanceState.containsKey("cameraImageUri")) {
                this.fileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
            }
            DisplayImageOnRestoration(this.OrientFile);
        }
        this.layout.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                SettingsTask.this.DisplayImageRight((String) SettingsTask.this.alpaView.getTag());
            }

            public void onSwipeLeft() {
                SettingsTask.this.DisplayImageLeft((String) SettingsTask.this.alpaView.getTag());
            }

            public void onSwipeBottom() {
            }
        });
    }

    @SuppressLint({"NewApi"})
    public void DisplayImageOnRestoration(String Image) {
        this.mediaFile = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic").getPath() + File.separator + Image + ".jpg");
        this.alpaView.setImageResource(((Integer) this.map.get(Image)).intValue());
        if (this.mediaFile.exists()) {
            this.bitmap = decodeFile(this.mediaFile);
            int rotateImage = getCameraPhotoOrientation(this, Uri.parse(this.mediaFile.getAbsolutePath()), this.mediaFile.getAbsolutePath());
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation((float) rotateImage);
            }
            this.alpaView.setImageBitmap(this.bitmap);
        } else {
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation(0.0f);
            }
            this.alpaView.setImageResource(((Integer) this.map.get("art" + Image)).intValue());
        }
        this.alpaView.setTag(Image);
    }

    @SuppressLint({"NewApi"})
    public void DisplayImageRight(String Image) {
        if (Image.equalsIgnoreCase("A")) {
            this.RightString = "F";
        } else {
            this.RightString = getDecrementedString(Image);
        }
        try {
            this.mediaFile = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic").getPath() + File.separator + this.RightString + ".jpg");
            this.bitmap = decodeFile(this.mediaFile);
        } catch (Exception e) {
        }
        this.alpaView.setImageResource(((Integer) this.map.get(this.RightString)).intValue());
        if (this.mediaFile.exists()) {
            int rotateImage = getCameraPhotoOrientation(this, Uri.parse(this.mediaFile.getAbsolutePath()), this.mediaFile.getAbsolutePath());
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation((float) rotateImage);
            }
            this.alpaView.setImageBitmap(this.bitmap);
            Log.i("Right Display", "file Found using " + this.mediaFile);
        } else {
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation(0.0f);
            }
            this.alpaView.setImageResource(((Integer) this.map.get(this.RightString)).intValue());
            Log.i("Left Display", "No file Found using default" + this.mediaFile);
        }
        this.alpaView.setTag(this.RightString);
    }

    @SuppressLint({"NewApi"})
    public void DisplayImageLeft(String Image) {
        if (Image.equalsIgnoreCase("F")) {
            this.LeftString = "A";
        } else {
            this.LeftString = getIncrementedString(Image);
        }
        try {
            this.mediaFile = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic").getPath() + File.separator + this.LeftString + ".jpg");
            this.bitmap = decodeFile(this.mediaFile);
            Log.i("mediaFile is", "file" + this.mediaFile);
        } catch (Exception e) {
        }
        this.alpaView.setImageResource(((Integer) this.map.get(this.LeftString)).intValue());
        if (this.mediaFile.exists()) {
            int rotateImage = getCameraPhotoOrientation(this, Uri.parse(this.mediaFile.getAbsolutePath()), this.mediaFile.getAbsolutePath());
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation((float) rotateImage);
            }
            this.alpaView.setImageBitmap(this.bitmap);
            Log.i("Left Display", "file Found using " + this.mediaFile);
        } else {
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation(0.0f);
            }
            this.alpaView.setImageResource(((Integer) this.map.get(this.LeftString)).intValue());
            Log.i("Left Display", "No file Found using default" + this.mediaFile);
        }
        this.alpaView.setTag(this.LeftString);
    }

    public static String getIncrementedString(String str) {
        StringBuilder sb = new StringBuilder();
        char[] toCharArray = str.toCharArray();
        int length = toCharArray.length;
        for (int i = 0; i < length; i += MEDIA_TYPE_IMAGE) {
            sb.append((char) (toCharArray[i] + MEDIA_TYPE_IMAGE));
        }
        return sb.toString();
    }

    public static String getDecrementedString(String str) {
        StringBuilder sb = new StringBuilder();
        char[] toCharArray = str.toCharArray();
        int length = toCharArray.length;
        for (int i = 0; i < length; i += MEDIA_TYPE_IMAGE) {
            sb.append((char) (toCharArray[i] - 1));
        }
        return sb.toString();
    }

    public void onClickAlpa(View view) {
    }

    @SuppressLint({"NewApi"})
    public void DisplayImage(String Image) {
        this.mediaFile = getMediaFilePath(Image);
        this.alpaView.setImageResource(((Integer) this.map.get(Image)).intValue());
        if (this.mediaFile.exists()) {
            this.bitmap = decodeFile(this.mediaFile);
            int rotateImage = getCameraPhotoOrientation(this, Uri.parse(this.mediaFile.getAbsolutePath()), this.mediaFile.getAbsolutePath());
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation((float) rotateImage);
            }
            this.alpaView.setImageBitmap(this.bitmap);
        } else {
            if (VERSION.SDK_INT > 11) {
                this.alpaView.setRotation(0.0f);
            }
            this.alpaView.setImageResource(((Integer) this.map.get(Image)).intValue());
        }
        this.alpaView.setTag(Image);
    }

    private File getMediaFilePath(String Image) {
        try {
            this.mediaFile = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic").getPath() + File.separator + Image + ".jpg");
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        }
        return this.mediaFile;
    }

    private Bitmap decodeFile(File f) {
        Options o = new Options();
        o.inPurgeable = true;
        o.inJustDecodeBounds = true;
        o.inInputShareable = true;
        o.inScaled = false;
        o.inPreferredConfig = Config.ARGB_8888;
        try {
            FileInputStream fis = new FileInputStream(f);
            Log.i("f  is", "file " + f);
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, o);
            fis.close();
            int scale = MEDIA_TYPE_IMAGE;
            int height = o.outHeight;
            int width = o.outWidth;
            if (height > 320 || width > 400) {
                int halfHeight = height / MEDIA_TYPE_VIDEO;
                int halfWidth = width / MEDIA_TYPE_VIDEO;
                scale = 4;
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            Bitmap b = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, o2);
            o2.inPurgeable = true;
            o2.inInputShareable = true;
            fis.close();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public void onClickCapBtn(View view) {
        Toast.makeText(this, "you clicked on Capture button", 0).show();
        this.alpaView = (ImageView) findViewById(R.id.articles);
        String imageName = (String) this.alpaView.getTag();
        Log.i("fileUri in Intent is", BuildConfig.FLAVOR + this.fileUri);
        Builder getImageFrom = new Builder(this);
        getImageFrom.setTitle("Select:");
        CharSequence[] opsChars = new CharSequence[MEDIA_TYPE_VIDEO];
        opsChars[0] = getResources().getString(R.string.takepic);
        opsChars[MEDIA_TYPE_IMAGE] = getResources().getString(R.string.opengallery);
        getImageFrom.setItems(opsChars, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                        SettingsTask.this.intent.putExtra("OriUri", SettingsTask.this.fileUri);
                        SettingsTask.this.startActivityForResult(cameraIntent, SettingsTask.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    } catch (Exception e) {
                        if (VERSION.SDK_INT >= 23) {
                            Toast.makeText(SettingsTask.this, "Running Android M and Above? Please confirm you have provided app CAMERA permission", 0).show();
                        }
                    }
                } else if (which == SettingsTask.MEDIA_TYPE_IMAGE) {
                    try {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra("filename", (String) SettingsTask.this.alpaView.getTag());
                        intent.setAction("android.intent.action.GET_CONTENT");
                        SettingsTask.this.startActivityForResult(Intent.createChooser(intent, SettingsTask.this.getResources().getString(R.string.pickgallery)), SettingsTask.SELECT_PICTURE);
                    } catch (Exception e2) {
                        if (VERSION.SDK_INT >= 23) {
                            Toast.makeText(SettingsTask.this, "Running Android M and Above? Please confirm you have provided app READ STORAGE permission", 0).show();
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        getImageFrom.show();
    }




    private Uri getOutputMediaFileUri(int type) {
        Log.i("Uri", "value is " + Uri.fromFile(getOutputMediaFile(type)).toString());
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        this.alpaView = (ImageView) findViewById(R.id.articles);
        String imageName = (String) this.alpaView.getTag();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic");
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            if (mediaStorageDir.exists()) {
                this.mediaFile = new File(mediaStorageDir + File.separator + imageName + ".jpg");
                Log.i("check", "o/p file path is" + this.mediaFile);
            }
            return this.mediaFile;
        }
        Log.d("ActMastPic", "failed to create directory");
        Toast.makeText(this, "could not create directory", Toast.LENGTH_LONG).show();
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            int orientation = new ExifInterface(new File(imagePath).getAbsolutePath()).getAttributeInt("Orientation", MEDIA_TYPE_IMAGE);
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

    @SuppressLint({"NewApi"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor myCursor = null;
        try {
            String outpath;
            String str;
            int rotateImage;
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SELECT_PICTURE && resultCode == -1) {
                Uri selectedImageUri = data.getData();
                this.alpaView = (ImageView) findViewById(R.id.articles);
                String imageName = (String) this.alpaView.getTag();
                Log.i("filename in on act is", imageName);
                this.mediaFile = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ActMastPic").getPath() + File.separator + imageName + ".jpg");
                Log.i("selectedImageUri is ", BuildConfig.FLAVOR + selectedImageUri);
                outpath = getPath(this, selectedImageUri);
                str = getOutputMediaFile(MEDIA_TYPE_IMAGE).toString();
                MoveFile(outpath, str, MEDIA_TYPE_IMAGE);
                Log.i("string value is", str);
                Log.i("outpath value is", outpath);
                this.bitmap = decodeFile(this.mediaFile);
                if (this.mediaFile.exists()) {
                    rotateImage = getCameraPhotoOrientation(this, Uri.parse(this.mediaFile.getAbsolutePath()), this.mediaFile.getAbsolutePath());
                    if (VERSION.SDK_INT > 11) {
                        this.alpaView.setRotation((float) rotateImage);
                    }
                    this.alpaView.setImageBitmap(this.bitmap);
                }
            }
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == -1 && VERSION.SDK_INT >= 19) {
                Bundle extras = data.getExtras();
                Log.i("fileUri in onAct is", BuildConfig.FLAVOR + ((Uri) extras.get("OriUri")));
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imageUri = getImageUri(this, imageBitmap);
                Log.i("imageUri is", BuildConfig.FLAVOR + imageUri);
                outpath = getPath(this, imageUri);
                Log.i("New OutPath is", BuildConfig.FLAVOR + outpath);
                System.out.println(imageBitmap);
                this.alpaView.setImageBitmap(imageBitmap);
                str = getOutputMediaFile(MEDIA_TYPE_IMAGE).toString();
                Log.i("Str is", BuildConfig.FLAVOR + str);
                Log.i("newpath is", BuildConfig.FLAVOR + outpath);
                MoveFile(outpath, str, MEDIA_TYPE_VIDEO);
            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == -1 && VERSION.SDK_INT < 19) {
                String[] projection = new String[]{"_id", "image_id", "kind", "_data"};
                myCursor = getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection, "kind=1", null, "_id DESC");
                String thumbnailPath = BuildConfig.FLAVOR;
                myCursor.moveToFirst();
                long imageId = myCursor.getLong(myCursor.getColumnIndexOrThrow("image_id"));
                long thumbnailImageId = myCursor.getLong(myCursor.getColumnIndexOrThrow("_id"));
                thumbnailPath = myCursor.getString(myCursor.getColumnIndexOrThrow("_data"));
                myCursor.close();
                String[] largeFileProjection = new String[MEDIA_TYPE_VIDEO];
                largeFileProjection[0] = "_id";
                largeFileProjection[MEDIA_TYPE_IMAGE] = "_data";
                myCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, largeFileProjection, null, null, "_id DESC");
                String largeImagePath = BuildConfig.FLAVOR;
                myCursor.moveToFirst();
                largeImagePath = myCursor.getString(myCursor.getColumnIndexOrThrow("_data"));
                myCursor.close();
                Uri uriLargeImage = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
                Log.i("value of uriLargeImage", "value is" + uriLargeImage.toString());
                try {
                    rotateImage = getCameraPhotoOrientation(this, uriLargeImage, largeImagePath);
                    MoveFile(largeImagePath, this.mediaFile.toString(), MEDIA_TYPE_VIDEO);
                    this.bitmap = decodeFile(this.mediaFile);
                    this.alpaView = (ImageView) findViewById(R.id.articles);
                    if (VERSION.SDK_INT > 11) {
                        this.alpaView.setRotation((float) rotateImage);
                    }
                    this.alpaView.setImageBitmap(this.bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app STORAGE permission", 0).show();
            }
        } catch (Throwable th) {
            myCursor.close();
        }
    }

    public void MoveFile(String largeImagePath, String F2, int i) {
        try {
            File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "ActMastPic");
            Log.i("im movefile cam path", BuildConfig.FLAVOR + largeImagePath);
            Log.i("im movefile NEW path is", BuildConfig.FLAVOR + F2);
            if (sd.canWrite()) {
                File source = new File(largeImagePath);
                File destination = new File(F2);
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(destination);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.i("im movefile source is", BuildConfig.FLAVOR + source);
                if (i == MEDIA_TYPE_VIDEO) {
                    source.delete();
                }
            }
        } catch (Exception e) {
            Log.i("movefile", "failed");
            e.printStackTrace();
        }
    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, 0);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @TargetApi(19)
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat;
        if (VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String[] split;
            if (isExternalStorageDocument(uri)) {
                split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[MEDIA_TYPE_IMAGE];
                }
                return null;
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (!isMediaDocument(uri)) {
                return null;
            } else {
                split = DocumentsContract.getDocumentId(uri).split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[MEDIA_TYPE_IMAGE];
                selectionArgs[0] = split[MEDIA_TYPE_IMAGE];
                return getDataColumn(context, contentUri, "_id=?", selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return null;
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[MEDIA_TYPE_IMAGE];
        projection[0] = "_data";
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void onClickRecBtn(View view) {
        Toast.makeText(this, "you clicked on Record button", 0).show();
        this.alpaView = (ImageView) findViewById(R.id.articles);
        String imageName = (String) this.alpaView.getTag();
        try {
            Intent i = new Intent("android.intent.action.AUDIORECORDKIDS");
            i.putExtra("picture", imageName);
            startActivity(i);
        } catch (Exception e) {
            if (VERSION.SDK_INT >= 23) {
                Toast.makeText(this, "Running Android M and Above? Please confirm you have provided app AUDIO permission", 0).show();
            }
        }
    }
}
