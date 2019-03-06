package com.example.lhumer16_woche21;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    ListView listView;
    String[] arr2;
    ArrayAdapter ad2;
    List<String> lastChosen;
    TextView cash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cash = findViewById(R.id.textViewCash);
        cash.setText("Cash: 0");
        okay = findViewById(R.id.buttonOk);
        lastChosen = new ArrayList<>();
        lastChosen.add("");lastChosen.add("Fußball");lastChosen.add("Party");
        listView = findViewById(R.id.ListView);
        a = (EditText) findViewById(R.id.editDate);
        b =(EditText)findViewById(R.id.betrag);
        c = findViewById(R.id.editTextdescription);
        list = new ArrayList<Entry>();
        s1 = findViewById(R.id.spinnerE_A);
        s2 = findViewById(R.id.spinnerLastChosen);
        String[] arr = new String[]{"Einnahmen","Ausgaben"};
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        s1.setAdapter(ad);
        arr2 = new String[]{" "};
        ad2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arr2);
        s2.setAdapter(ad2);
        setView();
    }

    public void ok(View view) {

        double betrag = Double.valueOf(b.getText().toString());
        String dateString = a.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        try {
            date = simpleDateFormat.parse(dateString);

            String kategorie;
            String kattype = s2.getSelectedItem().toString();



            if (kattype.length() > 1) {
                c.setText(kattype);
            }
            kategorie = c.getText().toString();

            String type = s1.getSelectedItem().toString();
            lastChosen.add(kategorie);
            list.add(new Entry(dateString, betrag, kategorie, type));
            String irg = "Cash: "+ calc();
            cash.setText(irg);


            setView();

            try {
                writeCsv1();
            } catch (IOException e) {
                Toast.makeText(this, "writeCSV1 caused an error", Toast.LENGTH_LONG).show();
            }
            clear();
            setSpinner2();


        } catch (ParseException e)
        {
            Toast.makeText(this, "Datumformat: dd.MM.yyyy", Toast.LENGTH_LONG).show();
            clear();

        }

    }

    public void clear()
    {
        a.setText("");
        b.setText("");
        c.setText("");
        s2.setAdapter(null);
    }

    public void setView()
    {
        String[] array123 = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String x = list.get(i).getDate().toString() + ";" + list.get(i).getBetrag() + ";" + list.get(i).getKategorie() + ";" + list.get(i).getType();
            array123[i] = x;
        }
        ArrayAdapter view1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array123);
        listView.setAdapter(view1);
    }

    public void setSpinner2()
    {
        String[] array1234=new String[lastChosen.size()];
        for (int i = 0; i < lastChosen.size(); i++) {
            array1234[i] = lastChosen.get(i);
        }
        ad2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array1234);
        s2.setAdapter(ad2);
    }

    public void loadApplication()
    {
        String filename = "MyCsv3.csv";
        list.clear();
        try{
            FileInputStream fis = openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            while (true)
            {
                String line = br.readLine();
                if(line==null)
                {
                    break;
                }
                String[] x=line.split(";");
                list.add(new Entry(x[0],Double.valueOf(x[1]),x[2],x[3]));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void writeCsv1() throws IOException {

    String filename = "MyCsv3.csv";
    try
    {
        FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));

        out.println(list.get(list.size()-1).getStringCSV());


        out.flush();
        out.close();


    }catch (Exception e)
    {
        e.printStackTrace();
    }
    }
        public double calc()
        {
            double cashvalue1 = 0;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getType().equals("Einnahmen"))
                {
                    cashvalue1 +=list.get(i).getBetrag();
                }

                if(list.get(i).getType().contains("Ausgaben"))
                {
                    cashvalue1 -=list.get(i).getBetrag();
                }
            }
            return  cashvalue1;
        }
    }

