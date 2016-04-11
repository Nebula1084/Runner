package se.runner.request;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import se.runner.ui.MainActivity;

public class HttpPost extends AsyncTask<String, Integer, String>
{
    private transient StringBuilder strUrl;
    private HttpCallback httpCallback;
    private ContentValues parameters;
    private String TAG="HttpPost";

    // for string
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
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setUseCaches(false);
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

//            Writer writer = new OutputStreamWriter(urlConnection.getOutputStream());
//            int flag = 0;
//            for (String key : parameters.keySet()) {
//                writer.write(flag == 0 ? "" : "&");
//                writer.write(key);
////                writer.write(parameters.getAsByte(key));
//                writer.write("=");
//                writer.write((String)parameters.get(key));
//                flag = 1;
//            }
//
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            Scanner scanner = new Scanner(in);
//            return scanner.nextLine();

            int flag = 0;
            StringBuilder postData = new StringBuilder();
            for (String key: parameters.keySet() )
            {
                if(flag != 0)
                    postData.append('&');
                postData.append(URLEncoder.encode(key,"UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode((String) parameters.get(key),"UTF-8") );
                flag = 1;
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            int responsCode = conn.getResponseCode();
            Log.e(TAG,"response code="+responsCode);

//            String result;
//            if (responsCode == HttpURLConnection.HTTP_OK)
//            {
//                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                StringBuilder sb = new StringBuilder();
//                for (int c; (c = in.read()) >= 0;)
//                    sb.append((char)c);
//                Log.e("Response",sb.toString());
//                result = sb.toString();
//            }
//            else
//                result = "HTTP_NOT_OK";
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            Log.e("Response",sb.toString());

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String get)
    {
        if (httpCallback != null)
            httpCallback.onPost(get);
    }

}
