package se.runner.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Sea on 3/15/2016.
 */
public class Greeting extends AsyncTask<String, Integer, String> {
    private Context context;
    String strUrl = "http://10.214.0.195:9000/greeting";

    public Greeting(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner scanner = new Scanner(in);
            return scanner.nextLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String get) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(get);
        builder.create().show();
    }
}
