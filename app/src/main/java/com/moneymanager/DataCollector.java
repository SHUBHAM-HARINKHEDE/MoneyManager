package com.moneymanager;


import java.lang.ref.PhantomReference;

public class DataCollector {

    private String _reason;
    private double _amount;
    private String _date;
    private int _month;
    private String _dailyTotal;
    private String _monthTotal;
    private  String _curDate;
    private int _curMonthNum;
    private String _monthName;

    /*public DataCollector(int id, String reason, double amount, String date) {
        this._id = id;
        this._reason = reason;
        this._amount = amount;
        this._date = date;
    }

    public DataCollector(String reason, double amount, String date) {
        this._reason = reason;
        this._amount = amount;
        this._date = date;
    }*/



    public void setMonthName(String monthName) { this._monthName = monthName; }
    public String getMonthName() { return this._monthName; }

    public void setMonth(int month){
        this._month = month;
    }
    public int getMonth(){
        return this._month;
    }

    public void setCurMonth(int curMonthNum){ this._curMonthNum = curMonthNum; }
    public int getCurMonth(){ return this._curMonthNum; }

    public void setCurDate(String curDate){ this._curDate = curDate; }
    public String getCurDate(){ return this._curDate; }

    public void setReason(String reason) {
        this._reason = reason;
    }
    public String getReason() {
        return this._reason;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    public double getAmount() {
        return this._amount;
    }

    public void setDate(String date){
        this._date = date;
    }
    public String getDate(){
        return this._date;
    }

    public void setMonthTotal(String monthTotal){ this._monthTotal = monthTotal; }
    public String getMonthTotal(){ return this._monthTotal; }

    public void setDailyTotal(String dailyTotal){ this._dailyTotal = dailyTotal; }
    public String getDailyTotal(){ return this._dailyTotal; }

}
