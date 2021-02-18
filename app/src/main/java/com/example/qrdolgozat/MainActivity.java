package com.example.qrdolgozat;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button scanbtn,kiirbtn;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();

            }
        });
        kiirbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kiir();
            }
        });

    }


    public void Kiir(){
        Date datum = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formazotDAtum= dateFormat.format(datum);
        String sorr = String.format("%s \n",formazotDAtum,textView.toString());
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
           try {

               File file = new File(Environment.getExternalStorageDirectory(),"scannedCodes.csv");
               BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
               bw.append(sorr);
               bw.append(System.lineSeparator());
               bw.close();
           }catch (IOException e)
           {
               Log.d("kiiras", e.getMessage());
           }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            Toast.makeText(this, "Kiláptél a Scanböl", Toast.LENGTH_SHORT).show();
        }
        else{
            textView.setText("QR code Éréke:"+result.getContents());
            Uri uri = Uri.parse(result.getContents());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        scanbtn = (Button) findViewById(R.id.scanbtn);
        kiirbtn = (Button)findViewById(R.id.kiirbtn);
        textView = (TextView) findViewById(R.id.textid);

    }
}