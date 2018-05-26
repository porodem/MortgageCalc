package com.porodem.mortgagecalc;

public class Calculations {

    public static Calculations sCalculations;

    private int mCapital, mAgency, mFirstPay, mOwnerRights, mEstimate, mInsurance, mOther;

    public static Calculations get() {
        if (sCalculations == null) {
            sCalculations = new Calculations();
        }
        return sCalculations;
    }

    private Calculations() {
        mOwnerRights = 2000;
        mEstimate = 3000;
        mInsurance = 12500;
        mOther = 6000;
    }

    public int getCapital() {
        return mCapital;
    }

    public void setCapital(int capital) {
        mCapital = capital;
    }

    public int getAgency() {
        return mAgency;
    }

    public void setAgency(int agency) {
        mAgency = agency;
    }

    public int getFirstPay() {
        return mFirstPay;
    }

    public void setFirstPay(int firstPay) {
        mFirstPay = firstPay;
    }

    public int getOwnerRights() {
        return mOwnerRights;
    }

    public void setOwnerRights(int ownerRights) {
        mOwnerRights = ownerRights;
    }

    public int getEstimate() {
        return mEstimate;
    }

    public void setEstimate(int estimate) {
        mEstimate = estimate;
    }

    public int getInsurance() {
        return mInsurance;
    }

    public void setInsurance(int insurance) {
        mInsurance = insurance;
    }

    public int getOther() {
        return mOther;
    }

    public void setOther(int other) {
        mOther = other;
    }

    public int getTotal() {
        return  mCapital - (mAgency + mFirstPay + mOwnerRights + mEstimate + mInsurance + mOther);
    }

    public int getExtraPayments() {
        return mOwnerRights + mEstimate + mInsurance + mOther;
    }
}
