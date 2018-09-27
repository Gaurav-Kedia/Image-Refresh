package com.gaurav.imagerefresh;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String PATH = "https://www.google.co.in/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png";
    private ProgressDialog progressDialog;
    private ImageView imageview;
    private String Res;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = (ImageView) findViewById(R.id.resultImageView);
        btn = (Button) findViewById(R.id.refreshButton);
    }
    public void onRefreshImage(View v){
        String TAG = "Refresh";
        progressDialog = ProgressDialog.show(this, "", "Downloading Image from " + PATH);
        DownloadImageTask task = new DownloadImageTask();
        task.execute();
        progressDialog.hide();
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            return downloadImage(PATH);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            btn.setText(Res);
                imageview.setImageBitmap(bitmap);
                //imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
    public Bitmap downloadImage(String path){
        final String TAG = "Download Task";
        Bitmap bitmap = null;
        InputStream inStream;
        try{
            URL url = new URL(path);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setReadTimeout(10000);
            urlConn.setConnectTimeout(15000);
            //urlConn.setDoInput(true);
            urlConn.connect();
            Res = String.valueOf(urlConn.getResponseCode());
            inStream = urlConn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inStream);

        }catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "URL error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Download failed" + e.getMessage());
        }
        return bitmap;
    }
}
