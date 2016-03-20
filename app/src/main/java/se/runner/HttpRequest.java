package se.runner;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpRequest extends AsyncTask<String, Integer, String> {
    private static String baseUrl = "http://10.214.0.195:9000";
    private StringBuilder strUrl;

    public interface HttpCallback {
        void onPost(String get);
    }

    private HttpCallback httpCallback;

    public HttpRequest(String path, ContentValues parameters, HttpCallback callback) {
        strUrl = new StringBuilder(baseUrl);
        strUrl.append(path);
        httpCallback = callback;
        int flag = 0;
        for (String key : parameters.keySet()) {
            strUrl.append(flag == 0 ? "?" : "&&");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strUrl.toString());
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
