    package com.moneymanager;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.nfc.tech.NfcA;
    import android.widget.Toast;


    public class DatabaseHelper extends SQLiteOpenHelper {

        protected static final int DATABASE_VERSION = 1;
        protected static final String DATABASE_NAME = "moneyManagerDb.db";
        //public static final String[] TABLE_MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        protected static final String TABLE_DAILY_RECORD = "daily_record";
        protected static final String TABLE_MONTH_RECORD = "month_record";
        protected static final String COL_ID = "id";
        protected static final String COL_DATE = "date";
        protected static final String COL_AMOUNT = "amount";
        protected static final String COL_REASON = "reason";
        protected static final String COL_MN = "month_num";
        protected static final String COL_MONTH = "month_name";
        protected static final String COL_TOTAL = "total_spendings";
        protected SQLiteDatabase db;
        DataCollector dc = new DataCollector();

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //for (int i = 0; i < 12; i++) {
                String CREATE_DAILY_RECORD_TABLE = "CREATE TABLE " + TABLE_DAILY_RECORD + "(id INT PRIMARY KEY, date TEXT, amount REAL, reason TEXT, month_num LONG);";
                db.execSQL(CREATE_DAILY_RECORD_TABLE);
                String CREATE_MONTH_RECORD_TABLE = "CREATE TABLE " + TABLE_MONTH_RECORD + "(id INT PRIMARY KEY, month_num INT, month_name TEXT, total_spendings DOUBLE);";
                db.execSQL(CREATE_MONTH_RECORD_TABLE);
            //}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //for (int i = 0; i < 12; i++)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_RECORD + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTH_RECORD + ";");
            onCreate(db);
        }



        public Boolean insertData(DataCollector dc) {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            String sql = "SELECT * FROM "+TABLE_DAILY_RECORD+";";
            Cursor cursor = db.rawQuery(sql, null);
            int count = cursor.getCount();

            values.put(COL_ID, count);
            values.put(COL_DATE, dc.getDate());
            values.put(COL_AMOUNT, dc.getAmount());
            values.put(COL_REASON, dc.getReason());
            values.put(COL_MN, dc.getMonth());

            long result = db.insert(TABLE_DAILY_RECORD,null,values);
            db.close();
            if(result == -1)
                return false;
            else
                return true;
        }

        public double getDailyTotal(DataCollector dc)
        {
            db = this.getWritableDatabase();
            String sql = "SELECT sum(amount) FROM "+TABLE_DAILY_RECORD+" WHERE date = '"+dc.getCurDate()+"';";
            Cursor res = db.rawQuery(sql, null);
            if(res.moveToFirst())
                return res.getDouble(0);
            else
                return 0.110;
        }

        public double getMonthlyTotal(DataCollector dc)
        {
            db = this.getWritableDatabase();
            String sql = "SELECT sum(amount) FROM "+TABLE_DAILY_RECORD+" WHERE month_num = "+dc.getCurMonth()+";";
            Cursor res = db.rawQuery(sql,null);
            if(res.moveToFirst())
                return res.getDouble(0);
            else
                return 0.110;
        }

        public boolean checkDb()
        {
            db = this.getReadableDatabase();
            String sql = "SELECT * FROM "+TABLE_DAILY_RECORD+";";
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.getCount()<=1){
                return false;
            }
            return true;
        }


        public Cursor getAllData()
        {
            db = this.getWritableDatabase();
            String sql = "SELECT * FROM "+TABLE_DAILY_RECORD+";";
            Cursor res = db.rawQuery(sql, null);
            return res;

        }

        public Cursor getDateWiseData(DataCollector dc)
        {
            db = this.getWritableDatabase();
            String sql = "SELECT amount, reason FROM "+TABLE_DAILY_RECORD+" WHERE date = '"+dc.getDate()+"';";
            Cursor res = db.rawQuery(sql, null);
            return res;

        }

        public Cursor getMonthWiseData(DataCollector dc)
        {
            db = this.getWritableDatabase();
            String sql = "SELECT date, amount, reason FROM "+TABLE_DAILY_RECORD+" WHERE month_num = "+dc.getMonth()+";";
            Cursor res = db.rawQuery(sql, null);
            return res;

        }

    }
