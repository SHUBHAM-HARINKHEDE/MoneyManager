package com.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class showDb extends AppCompatActivity {

    DatabaseHelper dh = new DatabaseHelper(this);
    DataCollector dc = new DataCollector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        String date = getIntent().getStringExtra("date");
        dc.setDate(date);
        dc.setCurDate(date);

        ListView list = (ListView)findViewById(R.id.listView);
        ArrayList<String> theLIst = new ArrayList<>();

        TextView tv = (TextView) findViewById(R.id.check);
        tv.setText(dc.getDate());
        TextView tv1 = (TextView) findViewById(R.id.dTotal);
        tv1.setText("Rs. "+Double.toString(dh.getDailyTotal(dc)));
        Cursor cursor = dh.getDateWiseData(dc);

        int id = 0;
        if(cursor.getCount() > 0)
        {
           if(cursor.moveToFirst())
           {
               theLIst.add("ID"+"\t\t\t\t\t\t\t\t"+"Amount"+"\t\t\t\t\t\t\t\t"+"Spent on");
               do
               {
                   id++;
                   theLIst.add(String.valueOf(id)+"\t\t\t\t\t\t\t\t"+String.valueOf(cursor.getDouble(0))+"\t\t\t\t\t\t\t\t"+cursor.getString(1));

                   ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theLIst);
                   list.setAdapter(listAdapter);
               }while(cursor.moveToNext());
           }
            else
           {
               Toast.makeText(showDb.this, "Something is wrong or data not available", Toast.LENGTH_SHORT).show();
           }
        }
        else
        {
            Toast.makeText(showDb.this, "No data found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}