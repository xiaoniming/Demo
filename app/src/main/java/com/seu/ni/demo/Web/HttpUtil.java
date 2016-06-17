package com.seu.ni.demo.Web;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by ni on 2015/12/5.
 */
public class HttpUtil {
    public static void SendHttpRequest(final String address, final HttpCallBackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection mConn = null;
                try {
                    URL url = new URL(address);
                    mConn = (HttpURLConnection) url.openConnection();
                    mConn.setRequestMethod("GET");
                    mConn.setConnectTimeout(8000);
                    mConn.setReadTimeout(8000);
                    mConn.setDoInput(true);
                    mConn.setDoOutput(true);
                    if (mConn.getResponseCode() == 200) {
                    }

                    InputStream in = mConn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onErr(e);
                } finally {
                    if (mConn != null) mConn.disconnect();
                }
            }
        }).start();

    }

    public static void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.i("myMessage", "id: " + id + "; name: " + name + "; version: " + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void parseJSONWithGSON(String data) {
        Gson gson = new Gson();
        List<APP> appList = gson.fromJson(data, new TypeToken<List<APP>>() {
        }.getType());
        for (APP app : appList) {
            Log.i("myMessage", "id: " + app.id + "; name: " + app.name + "; version: " + app.version);
        }
    }

    class APP {
        float version;
        String name;
        int id;
    }


}
