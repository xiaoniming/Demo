package com.seu.ni.demo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by ni on 2016/1/15.
 */
public class CameraUtils {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0x10;

    /**
     * A safe way to get an instance of the Camera object
     * @return
     */
    public static Camera getCameraInstance(int i) {
        Camera c = null;
        try {
            c = Camera.open(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


    /**
     * check For Camera Permissions
     * @param context
     * @return
     */
    public static boolean checkForCameraPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager
                .PERMISSION_GRANTED;
    }


    /**
     * ask for camera permissions
     * @param activity
     */
    public static void askForCameraPermissions(Activity activity) {
        if (!checkForCameraPermissions(activity.getApplicationContext())) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(activity.getApplicationContext(), "We need your permission!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }

        }

    }
    /***
     * Check if this device has a camera     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


}
