package se.runner.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.runner.R;

public class QRCodeActivity extends AppCompatActivity {
    final private String TAG="QRActivity";

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    private String qrContent ="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if( intent != null )
        {
            qrContent = intent.getExtras().getString("tid");
            Log.e(TAG,"get qr input="+ qrContent);
        }
        else
        {
            Log.e(TAG,"intent is null");
        }

        fill_qr_img();
    }

    public void fill_qr_img()
    {
        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = (width < height)? width:height;
        smallerDimension *= 0.75;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try
        {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE,smallerDimension,smallerDimension);
            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(matrixWidth,matrixHeight, Bitmap.Config.RGB_565);
            for(int i=0;i<matrixWidth;i++)
                for(int j=0;j<matrixHeight;j++)
                    bmp.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
            ( (ImageView) findViewById(R.id.qr_imageView) ).setImageBitmap(bmp);
        }
        catch (WriterException we)
        {
            we.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}