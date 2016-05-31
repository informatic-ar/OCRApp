package com.mamawco.apps.ocrapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @ColorInt private final int SUCCESS_COLOR   =  0xFF45863C;
    private final int CAPTURE_REQUEST_CODE = 100;
    ProgressDialog progressDialog ;
    EditText resultText;
    TextView resultStatus ;
    Button picImage ;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resultText = (EditText) findViewById(R.id.text);
        resultStatus = (TextView) findViewById(R.id.result_status);
        picImage = (Button)findViewById(R.id.pic_image);
        preview = (ImageView)findViewById(R.id.image_preview);
        progressDialog = new ProgressDialog(this);

        picImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CAPTURE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAPTURE_REQUEST_CODE & data!=null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                preview.setImageBitmap(bitmap);
                extractText(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    private void extractText(Bitmap bitmap){
        new Task().execute(bitmap);
    }



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


    class Task extends AsyncTask<Bitmap,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.waitmsg));
            progressDialog.setCancelable(false);//TODO: will allow cancelling the process later
            progressDialog.show();
            resultStatus.setText(R.string.processing);
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            TessBaseAPI tessBaseAPI = OCREngine.getInstance();
            tessBaseAPI.setImage(ReadFile.readBitmap(params[0]));
            String result = tessBaseAPI.getUTF8Text();

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            resultText.setText(s);
            if (s.length()>0){
                resultStatus.setTextColor(SUCCESS_COLOR);
                resultStatus.setText(R.string.success_ocr);
            }
            else{
                resultStatus.setTextColor(Color.RED);
                resultStatus.setText(R.string.error_ocr);
            }
        }
    }
}
