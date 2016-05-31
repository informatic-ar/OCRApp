package com.mamawco.apps.ocrapp;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by waleed on 5/29/16.
 */
public class OCREngine {
    private static TessBaseAPI tessBaseAPI  = new TessBaseAPI();
    public static TessBaseAPI getInstance(){
        return tessBaseAPI;
    }

    public static boolean initEngine(String dataPath,String language_code /*, int ocr_engine_mode*/){

         return getInstance().init(dataPath, language_code,TessBaseAPI.OEM_CUBE_ONLY);
    }

}
