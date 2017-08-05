package com.moneymanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;


public class HomeActivity extends AppCompatActivity{

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView, monthTotal, dailyTotal, curMonth;
    private int year, month, day, selectedYear, selectedMonth, selectedDay;
    private String[] monthName = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    private EditText amnt, reasn;
    private String date, curDate;
    //boolean dateFlag = false;
    DatabaseHelper dh = new DatabaseHelper(this);
    DataCollector dc = new DataCollector();

    FloatingActionButton fab, fab1, fab2, fab3; //fab4;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    TextView textFab1, textFab2, textFab3;
    boolean isOpen = false;

    boolean stmtFlag = false;
    //for datepicker
    boolean isDismiss = false;

    ListView listVew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        monthList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        //fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        textFab1 = (TextView) findViewById(R.id.textFab1);
        textFab2 = (TextView) findViewById(R.id.textFab2);
        //textFab3 = (TextView) findViewById(R.id.textFab3);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                monthList();
                showDialogListView();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {
                animateFab();
                stmtFlag = true;
                showDialog(999);


            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                showTotal();
                //Toast.makeText(HomeActivity.this, "This ia Fab3", Toast.LENGTH_SHORT).show();
            }
        });

        /*fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                viewAll();
                //Toast.makeText(HomeActivity.this, "This ia Fab3", Toast.LENGTH_SHORT).show();
            }
        });*/


        //Date picker and calender
        dateView = (TextView) findViewById(R.id.dateView);
        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        */
        calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        showCurDate();

        //Display daily and month total
        //displayTotal();

        //Database condition
        if(dh.checkDb())
        {
            Toast toast = Toast.makeText(HomeActivity.this,"Data is available", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(HomeActivity.this,"Db is empty", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Fab button animation
    private void animateFab()
    {
        if(isOpen)
        {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            //fab4.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            //fab4.setClickable(false);
            textFab1.setVisibility(View.INVISIBLE);
            textFab2.setVisibility(View.INVISIBLE);
            //textFab3.setVisibility(View.INVISIBLE);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            //fab4.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            //fab4.setClickable(true);
            textFab1.setVisibility(View.VISIBLE);
            textFab2.setVisibility(View.VISIBLE);
            //textFab3.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }


    //Date picker and calender
    @SuppressWarnings("deprecation")
    public void setDate(View view)

    {
        //dateFlag = true;
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, selectedYear, selectedMonth, selectedDay);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        isDismiss = true;
                        showCurDate();
                        dialog.dismiss();
                    }
                }
            });
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2, arg3);
                }
            };

    private void showDate(int s_year, int s_month, int s_day) {
       if(s_year > year || s_month > month || s_day > day )
       {
           Toast.makeText(HomeActivity.this, "You can not enter data for future dates!", Toast.LENGTH_SHORT).show();
            showCurDate();
       }
        else
       {
           if(stmtFlag)
           {
               if(isDismiss)
               {
                   isDismiss = false;
                   stmtFlag = false;
                   showCurDate();
               }
               else
               {
                   stmtFlag = false;
                   //dateView.setText(new StringBuilder().append(day).append("/").append(monthName[month]).append("/").append(year));
                   date = (new StringBuilder().append(s_day).append("-").append(monthName[s_month]).append("-").append(s_year)).toString();
                   Intent intent = new Intent(HomeActivity.this, showDb.class);
                   intent.putExtra("date", date);
                   startActivity(intent);
                   finish();
               }


           }
           else
           {
               if (isDismiss)
               {
                   isDismiss = false;
                   showCurDate();
               }
               else
               {
                   date = (new StringBuilder().append(s_day).append("-").append(monthName[s_month]).append("-").append(s_year)).toString();
                   dateView.setText(date);
                   dc.setDate(date);
                   dc.setMonth(month);
               }
           }
       }
    }

    private void showCurDate() {
        //dateFlag = false;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        curDate = (new StringBuilder().append(day).append("-").append(monthName[month]).append("-").append(year)).toString();
        dateView.setText(curDate);
        dc.setCurDate(curDate);
        dc.setDate(curDate);
        dc.setCurMonth(month);
        dc.setMonth(month);
    }
    //End of DatePicker and calender

    //Month listview dialog box
    public void monthList()
    {
        listVew = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_itam, R.id.textItem, monthName);
        listVew.setAdapter(adapter);
        listVew.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ViewGroup vg = (ViewGroup) view;
                TextView tv = (TextView)vg.findViewById(R.id.textItem);
                //Toast.makeText(HomeActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MonthDbActivity.class);
                intent.putExtra("monthNum", selectMonthNum(tv.getText().toString()));
                intent.putExtra("monthName",tv.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    public void showDialogListView()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Select Month");
        builder.setPositiveButton("Cancel",null);
        builder.setView(listVew);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int selectMonthNum(String month)
    {
        switch(month)
        {
            case "Jan":
                return 0;

            case "Feb":
                return 1;

            case "Mar":
                return 2;

            case "Apr":
                return 3;

            case "May":
                return 4;

            case "June":
                return 5;

            case "July":
                return  6;

            case "Aug":
                return 7;

            case "Sep":
                return 8;

            case "Oct":
                return 9;

            case "Nov":
                return 10;

            case "Dec":
                return 11;

            default:
                return 12;
        }

    }
    //End of listview dialog


    //set data after clicking submit buttonb
    public void setData(View v)
    {
        //Edit box of amount and reason
        amnt = (EditText)findViewById(R.id.enterAmnt);
        reasn = (EditText)findViewById(R.id.enterReasn);
        String temp_amnt_rs = amnt.getText().toString();
        String reasn_to = reasn.getText().toString();

        /*if(dateFlag)
            dc.setDate(date);
        else
            dc.setDate(curDate);*/

        if(!(temp_amnt_rs.isEmpty() || temp_amnt_rs.equals("") || reasn_to.isEmpty() || reasn_to.equals("")))
        {
            double amnt_rs = Double.parseDouble(temp_amnt_rs);
            dc.setAmount(amnt_rs);
            dc.setReason(reasn_to);
            Boolean flag = dh.insertData(dc);
            //displayTotal();

            amnt.setText("");
            reasn.setText("");
            showCurDate();

            if(flag)
            {
                Toast toast = Toast.makeText(HomeActivity.this,"Successfully Recorded!", Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(HomeActivity.this,"Failed!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(HomeActivity.this,"No data entered!", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    //Show daily and monthly total
    public void showTotal()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_total, null);
        TextView dailyTotal = (TextView) view.findViewById(R.id.dailyTotal);
        TextView monthlyTotal = (TextView) view.findViewById(R.id.monthlyTotal);
        String total = "Rs. "+Double.toString(dh.getDailyTotal(dc));
        dailyTotal.setText(total);
        total = "Rs. "+Double.toString(dh.getMonthlyTotal(dc));
        monthlyTotal.setText(total);
        builder.setView(view);
        builder.setPositiveButton("Ok",null);
        AlertDialog dialog = builder.create();
        dialog.show();
        total = null;
    }



    public void viewAll()
    {
        Cursor res = dh.getAllData();
        if(res.getCount() == 0)
            showMessage("Error", "Data not found");
        else
        {
            StringBuffer buffer = new StringBuffer();

            while(res.moveToNext())
            {
                buffer.append("ID :"+res.getString(0)+"\n");
                buffer.append("Date :"+res.getString(1)+"\n");
                buffer.append("Amount :"+res.getString(2)+"\n");
                buffer.append("Reason :"+res.getString(3)+"\n");
                buffer.append("Month Num :"+res.getString(4)+"\n\n");
            }
            showMessage("Data: ", buffer.toString());
        }
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton("Close",null);
        builder.setMessage(message);
        builder.show();
    }


    //Back button functionality

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit Money Manager?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

}
