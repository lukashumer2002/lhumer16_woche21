package com.example.lhumer16_woche21;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "now";
    Button okay;
    EditText a;
    EditText b;
    EditText c;
    List<Entry> list;
    Spinner s1;
    Spinner s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okay = findViewById(R.id.buttonOk);
        a = (EditText) findViewById(R.id.editDate);
        b =(EditText)findViewById(R.id.betrag);
        c = findViewById(R.id.editTextdescription);
        list = new ArrayList<Entry>();
        s1 = findViewById(R.id.spinnerE_A);
        s2 = findViewById(R.id.spinnerLastChosen);
        String[] arr = new String[]{"Einnahmen","Ausgaben"};
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        s1.setAdapter(ad);
        String[] arr2 = new String[]{"Party", "Fußball"};
        ArrayAdapter ad2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arr2);
        s2.setAdapter(ad2);
    }

    public void ok()
    {
       double betrag = Double.valueOf(b.toString());
       String date = a.getText().toString();
       String kategorie = c.getText().toString();
       list.add(new Entry(date,betrag,kategorie));
       writeCsv();
       updateSpinner();
       clear();
    }

    public void clear()
    {
        a.clearAnimation();
        b.clearAnimation();
        c.clearAnimation();
    }

    public void updateSpinner()
    {

    }

    public void loadApplication()
    {

    }

    public List<Entry> writeCsv() {
        list = new ArrayList<Entry>();
        File file = new File("abc.csv");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < list.size(); i++) {
                String x = list.get(i).getDate()+";"+list.get(i).getBetrag()+";"+list.get(i).getKategorie();
                bw.write(x);
            }

            } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return list;
    }

    private InputStream getInputStreamForAsset(String filename) {

        Log.d(TAG, "getInputStreamForAsset: " + filename);
        AssetManager assets = getAssets();
        try {
            return assets.open(filename);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
