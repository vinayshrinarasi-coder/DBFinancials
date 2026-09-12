package com.financials.db.dbfinancials;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditPolicy extends AppCompatActivity {
    public EditText holder, certificateNumber, dateOfDeposit, depositAmount, maturityAmount, dateOfMaturity, interest, nominee, rateOfInterest, bankName, durationInt, remarks;
    Policy p;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_policy);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String json = b.getString("poly");
            if (json != null) {
                p = new Gson().fromJson(json, Policy.class);
            }
        }
        save = findViewById(R.id.save);
        if (p != null) {
            showViewDialog(p);
        } else {
            Toast.makeText(this, "Error: Policy data missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showViewDialog(Policy poly) {
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dateOfDeposit.setText(sdf.format(myCalendar.getTime()));

            try {
                String sDate1 = dateOfMaturity.getText().toString();
                Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(sDate1);
                if (date1 != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    double yearsInBetween = cal.get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR);
                    double monthsDiff = cal.get(Calendar.MONTH) - myCalendar.get(Calendar.MONTH);
                    double ageInMonths = yearsInBetween * 12.00 + monthsDiff;
                    durationInt.setText(String.valueOf(ageInMonths / 12.00));
                }
            } catch (Exception ignored) {}
        };

        final Calendar myCalendar2 = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dateOfMaturity.setText(sdf.format(myCalendar2.getTime()));
            try {
                String sDate1 = dateOfDeposit.getText().toString();
                Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(sDate1);
                if (date1 != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    double yearsInBetween = myCalendar2.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
                    double monthsDiff = myCalendar2.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
                    double ageInMonths = yearsInBetween * 12.00 + monthsDiff;
                    durationInt.setText(String.valueOf(ageInMonths / 12.00));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        dateOfDeposit = findViewById(R.id.dateOfDeposit);
        dateOfDeposit.setOnClickListener(view -> new DatePickerDialog(EditPolicy.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        dateOfMaturity = findViewById(R.id.dateOfMaturity);
        dateOfMaturity.setOnClickListener(view -> new DatePickerDialog(EditPolicy.this, date2, myCalendar2
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
        dateOfDeposit.setText(poly.getDateOfDeposit());
        depositAmount.setText(String.valueOf(poly.getDepositAmount()));
        maturityAmount.setText(String.valueOf(poly.getMaturityAmount()));
        dateOfMaturity.setText(poly.getDateOfMaturity());
        interest.setText(String.valueOf(poly.getInterest()));
        nominee.setText(poly.getNominee());
        rateOfInterest.setText(String.valueOf(poly.getRateOfInterest()));
        bankName.setText(poly.getBankName());
        durationInt.setText(String.valueOf(poly.getDurationInt()));
        remarks.setText(poly.getRemarks());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInterest();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        maturityAmount.addTextChangedListener(watcher);
        depositAmount.addTextChangedListener(watcher);

        save.setOnClickListener(view -> {
            if (save.getText().toString().trim().equalsIgnoreCase("Update Policy")) {
                final String holderStr = holder.getText().toString().trim();
                final String certificateNumberStr = certificateNumber.getText().toString().trim();
                final String dateOfDepositStr = dateOfDeposit.getText().toString().trim();
                final String depositAmountStr = depositAmount.getText().toString().trim();
                final String maturityAmountStr = maturityAmount.getText().toString().trim();
                final String dateOfMaturityStr = dateOfMaturity.getText().toString().trim();
                final String interestStr = interest.getText().toString().trim();
                final String nomineeStr = nominee.getText().toString().trim();
                final String rateOfInterestStr = rateOfInterest.getText().toString().trim();
                final String bankNameStr = bankName.getText().toString().trim();
                final String durationIntStr = durationInt.getText().toString().trim();
                final String remarksStr = remarks.getText().toString().trim();

                if (holderStr.isEmpty()) msg("Please enter holder name..!!");
                else if (certificateNumberStr.isEmpty()) msg("Please enter certificate number..!!");
                else if (dateOfDepositStr.isEmpty()) msg("Please enter date of deposit..!!");
                else if (depositAmountStr.isEmpty()) msg("Please enter deposit amount..!!");
                else if (maturityAmountStr.isEmpty()) msg("Please enter maturity amount..!!");
                else if (dateOfMaturityStr.isEmpty()) msg("Please enter date of maturity..!!");
                else if (interestStr.isEmpty()) msg("Please enter interest..!!");
                else if (nomineeStr.isEmpty()) msg("Please enter nominee..!!");
                else if (rateOfInterestStr.isEmpty()) msg("Please enter rate of interest..!!");
                else if (bankNameStr.isEmpty()) msg("Please enter bank name..!!");
                else if (durationIntStr.isEmpty()) msg("Please enter duration..!!");
                else if (validateNumber(depositAmountStr, "Please enter valid deposit amount..!!") &&
                        validateNumber(maturityAmountStr, "Please enter valid maturity amount..!!") &&
                        validateDouble(rateOfInterestStr, "Please enter valid rate of interest..!!")) {
                    save.setText("Please Wait..");
                    MediaPlayer playr = MediaPlayer.create(EditPolicy.this, R.raw.coin);
                    if (playr != null) {
                        playr.setOnCompletionListener(mp -> {
                            DatabaseHandler db = new DatabaseHandler(EditPolicy.this);
                            db.deletePolicy(p);
                            db.addPolicy(new Policy(holderStr, certificateNumberStr, dateOfDepositStr, Integer.parseInt(depositAmountStr), Integer.parseInt(maturityAmountStr), dateOfMaturityStr, Integer.parseInt(interestStr), nomineeStr, Double.parseDouble(rateOfInterestStr), bankNameStr, Double.parseDouble(durationIntStr), remarksStr));
                            Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Display.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        });
                        playr.start();
                    } else {
                        DatabaseHandler db = new DatabaseHandler(EditPolicy.this);
                        db.deletePolicy(p);
                        db.addPolicy(new Policy(holderStr, certificateNumberStr, dateOfDepositStr, Integer.parseInt(depositAmountStr), Integer.parseInt(maturityAmountStr), dateOfMaturityStr, Integer.parseInt(interestStr), nomineeStr, Double.parseDouble(rateOfInterestStr), bankNameStr, Double.parseDouble(durationIntStr), remarksStr));
                        Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Display.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
            }
        });
    }

    private void updateInterest() {
        String daStr = depositAmount.getText().toString().trim();
        String maStr = maturityAmount.getText().toString().trim();
        if (!daStr.isEmpty() && !maStr.isEmpty()) {
            try {
                int da = Integer.parseInt(daStr);
                int ma = Integer.parseInt(maStr);
                interest.setText(String.valueOf(ma - da));
            } catch (NumberFormatException e) {
                interest.setText("0");
            }
        } else {
            interest.setText("0");
        }
    }

    private boolean validateNumber(String num, String msg) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception e) {
            msg(msg);
            return false;
        }
    }

    private boolean validateDouble(String num, String msg) {
        try {
            Double.parseDouble(num);
            return true;
        } catch (Exception e) {
            msg(msg);
            return false;
        }
    }

    private void msg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}