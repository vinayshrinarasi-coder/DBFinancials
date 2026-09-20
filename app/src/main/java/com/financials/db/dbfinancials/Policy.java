package com.financials.db.dbfinancials;


import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Policy {
    String holder,certificateNumber,nominee,bankName,durationString,remarks,dateOfDeposit,dateOfMaturity,category;
    double rateOfInterest,durationInt,depositAmount,maturityAmount,interest;
    int id;

    public Policy() {}

    public Policy(String holder,String certificateNumber,String dateOfDeposit,double depositAmount, double maturityAmount,String dateOfMaturity,double interest,String nominee,double rateOfInterest,String bankName,double durationInt,String remarks,int id, String category) {
        this.holder = holder;
        this.certificateNumber = certificateNumber;
        this.nominee = nominee;
        this.bankName = bankName;
        this.remarks = remarks;
        this.depositAmount = depositAmount;
        this.maturityAmount = maturityAmount;
        this.interest = interest;
        this.rateOfInterest = rateOfInterest;
        this.durationInt = durationInt;
        this.dateOfDeposit = dateOfDeposit;
        this.dateOfMaturity = dateOfMaturity;
        this.id = id;
        this.category = category;
    }

    public Policy(String holder,String certificateNumber,String dateOfDeposit,double depositAmount, double maturityAmount,String dateOfMaturity,double interest,String nominee,double rateOfInterest,String bankName,double durationInt,String remarks, String category) {
        this.holder = holder;
        this.certificateNumber = certificateNumber;
        this.nominee = nominee;
        this.bankName = bankName;
        this.remarks = remarks;
        this.depositAmount = depositAmount;
        this.maturityAmount = maturityAmount;
        this.interest = interest;
        this.rateOfInterest = rateOfInterest;
        this.durationInt = durationInt;
        this.dateOfDeposit = dateOfDeposit;
        this.dateOfMaturity = dateOfMaturity;
        this.category = category;
    }



    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public double getDurationInt() {
        return durationInt;
    }

    public void setDurationInt(double durationInt) {
        this.durationInt = durationInt;
    }

    public String getDateOfDeposit() {
        return dateOfDeposit;
    }

    public String getReadableDateOfDeposit() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfDeposit);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(date1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateOfDeposit;
    }

    public void setDateOfDeposit(String dateOfDeposit) {
        this.dateOfDeposit = dateOfDeposit;
    }

    public String getDateOfMaturity() {
        return dateOfMaturity;
    }

    public String getReadableDateOfMaturity() {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfMaturity);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(date1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateOfMaturity;
    }

    public void setDateOfMaturity(String dateOfMaturity) {
        this.dateOfMaturity = dateOfMaturity;
    }

    public static String convertToDbFormat(String date) {
        try {
            Date d = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            return new SimpleDateFormat("yyyy-MM-dd").format(d);
        } catch (Exception e) {
            return date;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
