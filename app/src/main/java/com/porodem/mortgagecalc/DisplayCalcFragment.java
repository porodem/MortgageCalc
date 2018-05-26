package com.porodem.mortgagecalc;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayCalcFragment extends DialogFragment {

    private TextView mCapital, mAgency, mFirstPay,
            mOwnerRights, mEstimate, mInsurance, mOther, mTotal;
    Calculations mCalculations;


    public static DisplayCalcFragment newInstance( ) {
        return new DisplayCalcFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalculations = Calculations.get();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_display_calc, null);
        mCapital = v.findViewById(R.id.tvDisplayCapital);
        mAgency = v.findViewById(R.id.tvDispalyAgencyTax);
        mFirstPay = v.findViewById(R.id.tvDiplayStartPay);
        mOwnerRights = v.findViewById(R.id.tvDisplayOwnerPay);
        mEstimate = v.findViewById(R.id.tvDisplayEstimate);
        mInsurance = v.findViewById(R.id.tvDisplayInsurance);
        mOther = v.findViewById(R.id.tvDisplayExtraPay);
        mTotal = v.findViewById(R.id.tvDiplayTotal);

        mCapital.setText(String.valueOf(mCalculations.getCapital()));
        mAgency.setText(String.valueOf(mCalculations.getAgency()));
        mFirstPay.setText(String.valueOf(mCalculations.getFirstPay()));
        mOwnerRights.setText(String.valueOf(mCalculations.getOwnerRights()));
        mEstimate.setText(String.valueOf(mCalculations.getEstimate()));
        mInsurance.setText(String.valueOf(mCalculations.getInsurance()));
        mOther.setText(String.valueOf(mCalculations.getOther()));
        int total = mCalculations.getTotal();
        if (total<0) {
            mTotal.setTextColor(getResources().getColor(R.color.redPrice));
        } else {
            mTotal.setTextColor(getResources().getColor(R.color.greenLast));
        }
        mTotal.setText(String.valueOf(total));

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(v)
                .setTitle(R.string.total)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        return dialog;
    }
}
