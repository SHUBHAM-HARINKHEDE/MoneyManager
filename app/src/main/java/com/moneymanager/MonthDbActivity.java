package com.moneymanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MonthDbActivity extends AppCompatActivity {
    DatabaseHelper dh = new DatabaseHelper(this);
    DataCollector dc = new DataCollector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_db);

        String monthName = getIntent().getStringExtra("monthName");

        int monthNum = getIntent().getIntExtra("monthNum",12);
        if(monthNum == 12)
            Toast.makeText(MonthDbActivity.this, "Month num = 12", Toast.LENGTH_SHORT).show();
        else
        {
            dc.setCurMonth(monthNum);
            dc.setMonth(monthNum);
        }

        ListView list = (ListView)findViewById(R.id.listView);
        ArrayList<String> theLIst = new ArrayList<>();

        TextView tv = (TextView) findViewById(R.id.check);
        tv.setText(monthName);
        TextView tv2 = (TextView) findViewById(R.id.mTotal);
        tv2.setText("Rs. "+Double.toString(dh.getMonthlyTotal(dc)));
        Cursor cursor = dh.getMonthWiseData(dc);

        int id = 0;
        if(cursor.getCount() > 0)
        {
            if(cursor.moveToFirst())
            {
                theLIst.add("ID"+"\t\t"+"Date"+"\t\t\t\t\t\t\t\t"+"Amount"+"\t\t\t\t"+"Spent on");
                do
                {
                    id++;
                    theLIst.add(String.valueOf(id)+"\t\t"
                            +cursor.getString(0)+"\t\t\t"
                            +String.valueOf(cursor.getDouble(1))+"\t\t\t"
                            +cursor.getString(2));

                    ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theLIst);
                    list.setAdapter(listAdapter);
                }while(cursor.moveToNext());
            }
            else
            {
                Toast.makeText(this, "Something is wrong or data not available", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
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
