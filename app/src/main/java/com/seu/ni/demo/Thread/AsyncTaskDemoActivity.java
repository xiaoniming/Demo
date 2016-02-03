/**
 * Created by ni on 2015/12/22.
 *
 * @author ni
 * <h1>@version 1.0</h1>
 */
package com.seu.ni.demo.Thread;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.seu.ni.demo.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author ni
 * @since level15
 */
public class AsyncTaskDemoActivity extends AppCompatActivity {
    final static String TAG = "------>";
    final String IMAGE_URL = "http://www.bz55.com/uploads/allimg/141014/138-141014103S8.jpg";
    final String FILE_NAME = "temp_file";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);
        mImageView = (ImageView) findViewById(R.id.thread_asynctask_image);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(IMAGE_URL);
    }

    void L(String pString) {
        Log.i(TAG, pString);
    }

    void L(int pInt) {
        Log.i(TAG, "" + pInt);
    }

    /**
     *
     */
    class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        ProgressDialog mProgressDialog;


        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(AsyncTaskDemoActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle("正在下载");
            mProgressDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {


            try {
                InputStream _InputStream = null;
                OutputStream _OutputStream = null;
                HttpURLConnection _HttpURLConnection = null;

                try {
                    _HttpURLConnection = httpConnect(params[0]);
                    _InputStream = _HttpURLConnection.getInputStream();
                    _OutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);

                    int totalLength = _HttpURLConnection.getContentLength();
                    L(totalLength);
                    int seg = 0;
                    int current = 0;
                    byte[] bytes = new byte[1024 * 5];
                    while ((seg = _InputStream.read(bytes)) != -1) {
                        _OutputStream.write(bytes, 0, seg);
                        current += seg;
                        int progress = (int) (((float) current / (float) totalLength) * 100f);
                        publishProgress(progress);
                        SystemClock.sleep(10);
                    }

                } finally {
                    if (_HttpURLConnection != null) _HttpURLConnection.disconnect();
                    if (_InputStream != null) _InputStream.close();
                    if (_OutputStream != null) _OutputStream.close();
                }
                return BitmapFactory.decodeFile(getFileStreamPath(FILE_NAME).getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap pBitMap) {
            if (pBitMap != null) {
                mProgressDialog.cancel();
                mImageView.setImageBitmap(pBitMap);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Bitmap aBitMap) {
            super.onCancelled(aBitMap);
        }

        private HttpURLConnection httpConnect(String pUrl) {
            URL _Url;
            HttpURLConnection _HttpURLConnection = null;

            try {
                _Url = new URL(pUrl);
                _HttpURLConnection = (HttpURLConnection) _Url.openConnection();
                _HttpURLConnection.setRequestMethod("GET");
                _HttpURLConnection.setConnectTimeout(8000);
                return _HttpURLConnection;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }


}
