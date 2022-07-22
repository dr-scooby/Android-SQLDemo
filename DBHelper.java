package com.jah.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "customer_table";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_AGE = "customer_age";
    public static final String ACTIVE_CUSTOMER = "active_customer";
    public static final String COLUMN_ID = "id";

    public DBHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    // this is called first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "create table " + CUSTOMER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME + " text, " + CUSTOMER_AGE + " int, " + ACTIVE_CUSTOMER + " bool )";

        db.execSQL(createTable);

    }

    // this is called if database version number changes
    // prevents app from crashing on database changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // ADD THE CUSTOMER
    public boolean addCustomer(CustomerModel customer){

        SQLiteDatabase db = this.getWritableDatabase(); // instantiate the SQL lite database, writeable
        ContentValues cv = new ContentValues(); // like a hash map, pair of values
        cv.put(CUSTOMER_NAME, customer.getName());
        cv.put(CUSTOMER_AGE, customer.getAge());
        cv.put(ACTIVE_CUSTOMER, customer.isIsactive());
        long result = db.insert(CUSTOMER_TABLE, null , cv);
        if(result == -1){ // failed -1
            return false;
        }else{
            return true;
        }
    }

    // delete a Customer
    public boolean deleteCustomer(CustomerModel cm){
        boolean success = false;

        SQLiteDatabase db =this.getWritableDatabase();
        String sql = "delete from " + CUSTOMER_TABLE + " where " + COLUMN_ID + " = "+ cm.getId();
        Cursor cursor = db.rawQuery(sql, null); // run the query, get a cursor
        if(cursor.moveToFirst()){ // if cursor can move to first row, success, deleted
            success = true;
        }else{
            success = false; // ID not found, not deleted
        }

        return success;
    }

    // get all customer listing
    public List<CustomerModel> getAll(){
        List<CustomerModel> listing = new ArrayList<>();

        String query = "select * from " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase(); // get a readable
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
                do{
                    int custid = cursor.getInt(0);
                    String custname = cursor.getString(1);
                    int custage = cursor.getInt(2);
                    boolean custact = cursor.getInt(3) == 1 ? true: false;
                    CustomerModel cm = new CustomerModel(custid, custname, custage, custact);
                    listing.add(cm);
                }while(cursor.moveToNext());
        }else{
            // do nothing
        }

        return listing;
    }
}
