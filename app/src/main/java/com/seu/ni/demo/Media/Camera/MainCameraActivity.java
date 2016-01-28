package com.seu.ni.demo.Media.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.seu.ni.demo.R;
import com.seu.ni.demo.Utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "-----Camera----->";
    private static final String ACTION_CROPE = "com.android.camera.action.CROP";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String DIRNAME = "myPhoto";
    private static final int REQUEST_CUSTOM_CAPTURE = 0x1110;
    private static final int REQUEST_CAPTURE = 0x1111;
    private static final int REQUEST_CROPE = 0x1112;
    private static final int REQUEST_PICK = 0x1113;
    private Button mCustomCapture, mCapture, mPick, mCrop;
    private ImageView mImage;
    private Uri mImageUri;

    public static <T> void L(T para) {
        Log.i(TAG, "" + para);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera_custom:
                Intent customIntent = new Intent(MainCameraActivity.this, CustomCameraActivity.class);
                startActivityForResult(customIntent, REQUEST_CUSTOM_CAPTURE);
                break;

            case R.id.bt_camera_capture:
                //start camera
                mImageUri = FileUtils.getUri(createOutputMediaFile());
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//MediaStore.ACTION_VIDEO_CAPTURE
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(captureIntent, REQUEST_CAPTURE);
                break;

            case R.id.bt_camera_gallery:
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("image/*");
                startActivityForResult(pickIntent, REQUEST_PICK);
                break;

            case R.id.bt_camera_crop:

                //get real path of picked image from gallery
                String realPath = FileUtils.getPath(this, mImageUri);
                File sourceFile = new File(realPath);
                File destFile = createOutputMediaFile();
                Uri cropUri = FileUtils.getUri(destFile);

                try {
                    //copy original file to our folder
                    FileUtils.copyFile(sourceFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                //begin to crop
                Intent cropIntent = new Intent(ACTION_CROPE);
                cropIntent.setDataAndType(mImageUri, "image/*");
                cropIntent.putExtra("scale", true);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(cropIntent, REQUEST_CROPE);

                break;
            default:
                return;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CUSTOM_CAPTURE:

                 L(resultCode);

                break;
            case REQUEST_CAPTURE:
                if (resultCode == RESULT_OK) {
                    displayImage(mImage, mImageUri);
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled
                } else {
                    //failed to capture
                }
                break;


            case REQUEST_PICK:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mImageUri = data.getData();
                    displayImage(mImage, mImageUri);
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled
                } else {
                    //failed to get content
                }
                break;
            default:
                return;


            case REQUEST_CROPE:
                if (resultCode == RESULT_OK) {
                    mImageUri = data.getData();
                    displayImage(mImage, mImageUri);
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled
                } else {
                    //failed to crop
                }
                break;

        }
    }

    private void initView() {
        mCustomCapture = (Button) findViewById(R.id.bt_camera_custom);
        mCapture = (Button) findViewById(R.id.bt_camera_capture);
        mPick = (Button) findViewById(R.id.bt_camera_gallery);
        mCrop = (Button) findViewById(R.id.bt_camera_crop);
        mImage = (ImageView) findViewById(R.id.iv_camera_show);

        mCustomCapture.setOnClickListener(this);
        mCapture.setOnClickListener(this);
        mPick.setOnClickListener(this);
        mCrop.setOnClickListener(this);

    }

    private void displayImage(ImageView pImageView, Uri pImageUri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pImageUri));
            pImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    /**
     * @return output File
     */
    public static  File createOutputMediaFile() {
        File mediaStorageDirectory;
        String timeStamp;
        File mediaFile;

        //create media directory
        mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES), DIRNAME);
        if (!mediaStorageDirectory.exists()) {
            if (!mediaStorageDirectory.mkdirs()) {
                L("Failed to create directory");
                return null;
            }
        }
        // Name media file
        timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        mediaFile = new File(mediaStorageDirectory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

}
