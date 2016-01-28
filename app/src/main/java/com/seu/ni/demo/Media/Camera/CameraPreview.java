package com.seu.ni.demo.Media.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.seu.ni.demo.Utils.CameraUtils;

import java.util.List;

/**
 * Created by ni on 2016/1/15.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "-----CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Size mPreviewSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged");
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null) {
            //preview surface does not exits
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or reformatting changes here
        //start preview with new settings
        try {
            Camera.Size size = getOptimalPreviewSize(width, height);
            if (size == null) {
                Log.d(TAG, "no supported preview size");
                return;
            }
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.getParameters().setPreviewSize(size.width, size.height);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getOptimalPreviewSize(int width, int height) {
        //if screen orientation is portrait, we need to exchange w & h
        if (width < height) {
            int t = width;
            width = height;
            height = t;
        }
        List<Camera.Size> supportSizes = mCamera.getParameters().getSupportedPreviewSizes();
        mPreviewSize = null;
        float ratio = height / (float) width;
        float diffr = Float.MAX_VALUE;
        int diffw = Integer.MAX_VALUE;

        for (Camera.Size size : supportSizes) {
            float r = size.height / (float) size.width;
            int w = size.width;
            // find the closest width
            if (Math.abs(w - width) < diffw) {
                mPreviewSize = size;
                diffw = Math.abs(w - width);
            } else if (Math.abs(w - width) == diffw) {
                // width exactly the same, find the closest ratio
                if (Math.abs(ratio - r) < diffr) {
                    mPreviewSize = size;
                    diffr = Math.abs(ratio - r);
                }
            }
        }
        return mPreviewSize;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed");
        // Take care of releasing the Camera preview in your activity.
        mCamera.stopPreview();

    }


}
