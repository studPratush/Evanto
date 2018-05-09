package com.example.root.evanto;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GeneratorActivity extends AppCompatActivity {
    Button saveqr;
    ImageView image;
    String text2Qr;
    String splitString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        Intent ii = getIntent();
        text2Qr = ii.getStringExtra(Description.senduid);
        splitString = text2Qr.substring(29);
        image = (ImageView) findViewById(R.id.image);
        saveqr = (Button)findViewById(R.id.saveqr);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
        saveqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(GeneratorActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path+"/Evanto/Participated Events");
                dir.mkdirs();
                File file = new File(dir,""+Description.useEvent+".png");
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view,"Image Saved",Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}

