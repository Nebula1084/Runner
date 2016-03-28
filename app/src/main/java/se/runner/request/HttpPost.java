package se.runner.request;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpPost extends AsyncTask<String, Integer, String> {
    private StringBuilder strUrl;
    private HttpCallback httpCallback;
    private ContentValues parameters;

    public HttpPost(String path, ContentValues parameters, HttpCallback callback) {
        strUrl = new StringBuilder(HttpCallback.baseUrl);
        strUrl.append(path);
        httpCallback = callback;
        this.parameters = parameters;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strUrl.toString());
            Log.v("Http POST url", strUrl.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());
            int flag = 0;
            for (String key : parameters.keySet()) {
                writer.write(flag == 0 ? "" : "&");
                writer.write(key);
                writer.write(parameters.getAsByte(key));
                flag = 1;
            }
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
