package com.anugrahrochmat.chuck.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FactDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "fact.db";
    private static final int DATABASE_VERSION = 1;

    public FactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FACT_TABLE = "CREATE TABLE " + FactContract.FactEntry.TABLE_NAME + " (" +
                FactContract.FactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FactContract.FactEntry.COLUMN_FACT_ID + " TEXT NOT NULL, " +
                FactContract.FactEntry.COLUMN_FACT_URL + " TEXT NOT NULL, " +
                FactContract.FactEntry.COLUMN_FACT_VALUE + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_FACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FactContract.FactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
