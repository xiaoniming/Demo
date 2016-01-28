package com.seu.ni.demo.Media.Camera;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.seu.ni.demo.R;
import com.seu.ni.demo.Utils.CameraUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CustomCameraActivity extends AppCompatActivity {
    private static final String TAG = "-----CustomCamera----->";
    private Camera mCamera;
    private CameraPreview mPreview;
    private Button mCapture;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                File file = MainCameraActivity.createOutputMediaFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    Matrix mat = new Matrix();
                    mat.postRotate(90);
                    Bitmap save = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    save.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mCamera.startPreview();
        }
    };


    public static <T> void L(T para) {
        Log.i(TAG, "" + para);
    }

    public <T> void T(T para) {
        Toast.makeText(CustomCameraActivity.this, para + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);


        mCapture = (Button) findViewById(R.id.bt_camera_custom_capture);
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }

    @Override
    protected void onResume() {
        L("onresume");
        super.onResume();
        if (CameraUtils.checkForCameraPermissions(this)) {
            beginPreview();
        } else {
            CameraUtils.askForCameraPermissions(CustomCameraActivity.this);
        }
    }


    private void beginPreview() {

        initCamera();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //clear all views before adding, or it will cause ERROR when camera is released while SurfaceView is still
        // trying to create
        preview.removeAllViews();
        preview.addView(mPreview);
    }


    /**
     * results for asking camera permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CameraUtils.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted!
                    beginPreview();
                } else {
                    T("we have not get access to your camera!");
                    // permission denied! Disable the functionality that depends on this permission.
                }
                return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }


    @Override
    protected void onPause() {
        L("onpause");
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    @Override
    protected void onDestroy() {
        L("ondestroy");
        super.onDestroy();

    }


    private void initCamera() {
        mCamera = CameraUtils.getCameraInstance(0);
        if (mCamera == null) {
            return;
        }
        Camera.Parameters params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


}
