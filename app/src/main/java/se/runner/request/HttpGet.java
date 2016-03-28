package se.runner.request;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpGet extends AsyncTask<String, Integer, String> {
    private StringBuilder strUrl;

    private HttpCallback httpCallback;

    public HttpGet(String path, ContentValues parameters, HttpCallback callback) {
        strUrl = new StringBuilder(HttpCallback.baseUrl);
        strUrl.append(path);
        httpCallback = callback;
        int flag = 0;
        for (String key : parameters.keySet()) {
            strUrl.append(flag == 0 ? "?" : "&");
            strUrl.append(key);
            strUrl.append("=");
            strUrl.append(parameters.get(key));
            flag = 1;
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strUrl.toString());
            Log.v("Http Get url", strUrl.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner scanner = new Scanner(in);
            return scanner.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String get) {
        if (httpCallback != null)
            httpCallback.onPost(get);
    }
}
