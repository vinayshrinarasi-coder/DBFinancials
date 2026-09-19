package com.financials.db.dbfinancials;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewPolicy extends AppCompatActivity {
    public EditText holder, certificateNumber, dateOfDeposit, depositAmount, maturityAmount, dateOfMaturity, interest, nominee, rateOfInterest, bankName, durationInt, remarks;
    Policy p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policy);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String json = b.getString("poly");
            if (json != null) {
                p = new Gson().fromJson(json, Policy.class);
            }
        }
        if (p != null) {
            showViewDialog(p);
        } else {
            finish();
        }
    }

    private void showViewDialog(Policy poly) {
        final Calendar myCalendar = Calendar.getInstance();
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(poly.getDateOfDeposit());
            if (d != null) myCalendar.setTime(d);
        } catch (Exception ignored) {}

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dateOfDeposit.setText(sdf.format(myCalendar.getTime()));
        };

        final Calendar myCalendar2 = Calendar.getInstance();
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(poly.getDateOfMaturity());
            if (d != null) myCalendar2.setTime(d);
        } catch (Exception ignored) {}

        final DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dateOfMaturity.setText(sdf.format(myCalendar2.getTime()));
        };

        dateOfDeposit = findViewById(R.id.dateOfDeposit);
        dateOfDeposit.setOnClickListener(view -> new DatePickerDialog(ViewPolicy.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        dateOfMaturity = findViewById(R.id.dateOfMaturity);
        dateOfMaturity.setOnClickListener(view -> new DatePickerDialog(ViewPolicy.this, date2, myCalendar2
                .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                myCalendar2.get(Calendar.DAY_OF_MONTH)).show());

        holder = findViewById(R.id.holder);
        certificateNumber = findViewById(R.id.certificateNumber);
        depositAmount = findViewById(R.id.depositAmount);
        maturityAmount = findViewById(R.id.maturityAmount);
        interest = findViewById(R.id.interest);
        rateOfInterest = findViewById(R.id.rateOfInterest);
        bankName = findViewById(R.id.bankName);
        durationInt = findViewById(R.id.durationInt);
        remarks = findViewById(R.id.remarks);
        nominee = findViewById(R.id.nominee);

        holder.setText(poly.getHolder());
        certificateNumber.setText(poly.getCertificateNumber());
        dateOfDeposit.setText(poly.getReadableDateOfDeposit());
        depositAmount.setText(String.valueOf(poly.getDepositAmount()));
        maturityAmount.setText(String.valueOf(poly.getMaturityAmount()));
        dateOfMaturity.setText(poly.getReadableDateOfMaturity());
        interest.setText(String.valueOf(poly.getInterest()));
        nominee.setText(poly.getNominee());
        rateOfInterest.setText(String.valueOf(poly.getRateOfInterest()));
        bankName.setText(poly.getBankName());
        durationInt.setText(String.valueOf(poly.getDurationInt()));
        remarks.setText(poly.getRemarks());

        View saveBtn = findViewById(R.id.save);
        if (saveBtn != null) {
            saveBtn.setVisibility(View.INVISIBLE);
        }
    }
}