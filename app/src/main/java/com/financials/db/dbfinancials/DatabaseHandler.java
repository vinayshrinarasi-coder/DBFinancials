package com.financials.db.dbfinancials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBFinancials";
    public static final String TABLE_CONTACTS = "Policy";
    private static final String id = "id";
    private static final String certificateNumber = "certificateNumber";
    private static final String holder = "holder";
    private static final String bankName = "bankName" ;
    private static final String remarks = "remarks";
    private static final String depositAmount = "depositAmount";
    private static final String maturityAmount = "maturityAmount";
    private static final String interest = "interest";
    private static final String rateOfInterest = "rateOfInterest";
    private static final String durationInt = "durationInt";
    private static final String dateOfDeposit = "dateOfDeposit";
    private static final String dateOfMaturity = "dateOfMaturity";
    private static final String nominee = "nominee";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + "id" + " INTEGER PRIMARY KEY autoincrement,"
                + "certificateNumber" + " TEXT,"
                + "holder" + " TEXT,"
                + "bankName" + " TEXT,"
                + "remarks" + " TEXT,"
                + "depositAmount" + " DOUBLE,"
                + "maturityAmount" + " DOUBLE,"
                + "interest" + " DOUBLE,"
                + "rateOfInterest" + " DOUBLE,"
                + "durationInt" + " DOUBLE,"
                + "dateOfDeposit" + " DATE,"
                + "dateOfMaturity" + " DATE,"
                + "nominee" + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    void addPolicy(Policy policy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(holder, policy.getHolder());
        values.put(certificateNumber, policy.getCertificateNumber());
        values.put(dateOfDeposit, policy.getDateOfDeposit());
        values.put(depositAmount, policy.getDepositAmount());
        values.put(maturityAmount, policy.getMaturityAmount());
        values.put(dateOfMaturity, policy.getDateOfMaturity());
        values.put(interest, policy.getInterest());
        values.put(rateOfInterest, policy.getRateOfInterest());
        values.put(nominee, policy.getNominee());
        values.put(bankName, policy.getBankName());
        values.put(durationInt, policy.getDurationInt());
        values.put(remarks, policy.getRemarks());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // code to get the single policy
    Policy getPolicyByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {holder, certificateNumber,dateOfDeposit,depositAmount,maturityAmount,dateOfMaturity,interest,nominee,rateOfInterest,bankName,durationInt,remarks,this.id},  "id =?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Policy policy = new Policy(cursor.getString(cursor.getColumnIndex("holder")),
                cursor.getString(cursor.getColumnIndex("certificateNumber")),
                cursor.getString(cursor.getColumnIndex("dateOfDeposit")),
                cursor.getDouble(cursor.getColumnIndex("depositAmount")),
                cursor.getDouble(cursor.getColumnIndex("maturityAmount")),
                cursor.getString(cursor.getColumnIndex("dateOfMaturity")),
                cursor.getDouble(cursor.getColumnIndex("interest")),
                cursor.getString(cursor.getColumnIndex("nominee")),
                cursor.getDouble(cursor.getColumnIndex("rateOfInterest")),
                cursor.getString(cursor.getColumnIndex("bankName")),
                cursor.getDouble(cursor.getColumnIndex("durationInt")),
                cursor.getString(cursor.getColumnIndex("remarks")),
                cursor.getInt(cursor.getColumnIndex("id")));
        // return policy
        return policy;
    }

    // code to get all policys in a list view
    public List<Policy> getAllPolicies() {
        List<Policy> policyList = new ArrayList<Policy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Policy policy = new Policy(cursor.getString(cursor.getColumnIndex("holder")),
                        cursor.getString(cursor.getColumnIndex("certificateNumber")),
                        cursor.getString(cursor.getColumnIndex("dateOfDeposit")),
                        cursor.getDouble(cursor.getColumnIndex("depositAmount")),
                        cursor.getDouble(cursor.getColumnIndex("maturityAmount")),
                        cursor.getString(cursor.getColumnIndex("dateOfMaturity")),
                        cursor.getDouble(cursor.getColumnIndex("interest")),
                        cursor.getString(cursor.getColumnIndex("nominee")),
                        cursor.getDouble(cursor.getColumnIndex("rateOfInterest")),
                        cursor.getString(cursor.getColumnIndex("bankName")),
                        cursor.getDouble(cursor.getColumnIndex("durationInt")),
                        cursor.getString(cursor.getColumnIndex("remarks")),
                        cursor.getInt(cursor.getColumnIndex("id")));
                policyList.add(policy);
            } while (cursor.moveToNext());
        }



        // return policy list
        return policyList;
    }

    public List<Policy> getPoliciesSQL(String sql) {
        List<Policy> policyList = new ArrayList<Policy>();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Policy policy = new Policy(cursor.getString(cursor.getColumnIndex("holder")),
                        cursor.getString(cursor.getColumnIndex("certificateNumber")),
                        cursor.getString(cursor.getColumnIndex("dateOfDeposit")),
                        cursor.getDouble(cursor.getColumnIndex("depositAmount")),
                        cursor.getDouble(cursor.getColumnIndex("maturityAmount")),
                        cursor.getString(cursor.getColumnIndex("dateOfMaturity")),
                        cursor.getDouble(cursor.getColumnIndex("interest")),
                        cursor.getString(cursor.getColumnIndex("nominee")),
                        cursor.getDouble(cursor.getColumnIndex("rateOfInterest")),
                        cursor.getString(cursor.getColumnIndex("bankName")),
                        cursor.getDouble(cursor.getColumnIndex("durationInt")),
                        cursor.getString(cursor.getColumnIndex("remarks")),
                        cursor.getInt(cursor.getColumnIndex("id")));
                policyList.add(policy);
            } while (cursor.moveToNext());
        }

        // return policy list
        return policyList;
    }

    // code to update the single policy
    public int updatePolicy(Policy policy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(holder, policy.getHolder());
        values.put(certificateNumber, policy.getCertificateNumber());
        values.put(dateOfDeposit, policy.getDateOfDeposit());
        values.put(depositAmount, policy.getDepositAmount());
        values.put(maturityAmount, policy.getMaturityAmount());
        values.put(dateOfMaturity, policy.getDateOfMaturity());
        values.put(interest, policy.getInterest());
        values.put(rateOfInterest, policy.getRateOfInterest());
        values.put(nominee, policy.getNominee());
        values.put(bankName, policy.getBankName());
        values.put(durationInt, policy.getDurationInt());
        values.put(remarks, policy.getRemarks());

        // updating row
        return db.update(TABLE_CONTACTS, values, id + " = ?",
                new String[] { String.valueOf(policy.getId()) });
    }

    // Deleting single policy
    public void deletePolicy(Policy policy) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, id + " = ?",
                new String[] { String.valueOf(policy.getId()) });
        db.close();
    }

    // Getting policys Count
    public int getPoliciesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }

        // return count
        return count;
    }

    public List<Policy> getBackup(String sql) {
        List<Policy> policyList = new ArrayList<Policy>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Policy policy = new Policy(cursor.getString(cursor.getColumnIndex("holder")),
                        cursor.getString(cursor.getColumnIndex("certificateNumber")),
                        cursor.getString(cursor.getColumnIndex("dateOfDeposit")),
                        cursor.getDouble(cursor.getColumnIndex("depositAmount")),
                        cursor.getDouble(cursor.getColumnIndex("maturityAmount")),
                        cursor.getString(cursor.getColumnIndex("dateOfMaturity")),
                        cursor.getDouble(cursor.getColumnIndex("interest")),
                        cursor.getString(cursor.getColumnIndex("nominee")),
                        cursor.getDouble(cursor.getColumnIndex("rateOfInterest")),
                        cursor.getString(cursor.getColumnIndex("bankName")),
                        cursor.getDouble(cursor.getColumnIndex("durationInt")),
                        cursor.getString(cursor.getColumnIndex("remarks")),
                        cursor.getInt(cursor.getColumnIndex("id")));
                policyList.add(policy);
            } while (cursor.moveToNext());
        }

        // return policy list
        return policyList;
    }

}
