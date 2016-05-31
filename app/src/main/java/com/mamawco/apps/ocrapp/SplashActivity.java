package com.mamawco.apps.ocrapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    //Change this path to where you place the tessdata folder in your device
    //will later remove it and bundle all of our data within the app
    private final String TESSDATA_PATH = "/storage/emulated/0/Download" ;
    private final String LANGUAGE = "ara";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new LoadTask().execute(TESSDATA_PATH,LANGUAGE);
    }
    class LoadTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            return OCREngine.initEngine(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success){
                Toast.makeText(SplashActivity.this,"Success",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }else {
                Toast.makeText(SplashActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                System.exit(-1);
            }
        }
    }
}