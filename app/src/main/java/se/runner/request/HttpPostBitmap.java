package se.runner.request;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import se.runner.R;

/**
 * Created by zieng on 3/31/16.
 */
public class HttpPostBitmap extends AsyncTask<String, Void, String>
{
    final private String TAG = "HttpPostBitmap";
    private StringBuilder strUrl;
    private String path;
    private ContentValues parameters;
    private HttpCallback httpCallback;
    private String filename;

    public HttpPostBitmap(String path,ContentValues parameters,HttpCallback httpCallback)
    {
        strUrl = new StringBuilder(HttpCallback.baseUrl);
        strUrl.append(path);
        this.path = path;
        this.parameters = parameters;
        this.httpCallback = httpCallback;

        filename = (String) parameters.get("iconfile");
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(strUrl.toString());
            Log.v(TAG, strUrl.toString());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(filename, options);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.close();
            outputStream.close();


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
    protected void onPostExecute(String get) {
        if (httpCallback != null)
            httpCallback.onPost(get);
    }
}
