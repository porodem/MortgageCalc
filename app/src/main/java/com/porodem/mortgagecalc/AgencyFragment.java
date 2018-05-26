package com.porodem.mortgagecalc;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Date;
import java.util.zip.Inflater;

public class AgencyFragment extends DialogFragment {

    public static final String EXTRA_AG_PAY = "com.porodem.mortgagecalc.extra.pay";
    public static final String EXTRA_AG_PERCENT = "com.porodem.mortgagecalc.extra.percent";

    private static final String ARG_AG_PAY = "agencyBasicPay";
    private static final String ARG_AG_PERC = "agencyPercent";

    private EditText mEditBasicPay;
    private EditText mEditPercent;

    public static AgencyFragment newInstance(int basicPay, int percent) {
        Bundle args = new Bundle();
        args.putInt(ARG_AG_PAY, basicPay);
        args.putInt(ARG_AG_PERC, percent);
        AgencyFragment fragment = new AgencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int basicPay = getArguments().getInt(ARG_AG_PAY, 10000);
        int percent = getArguments().getInt(ARG_AG_PERC, 1);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_agency, null);

        mEditBasicPay = v.findViewById(R.id.etAgencyDialogBasicPay);
        mEditPercent = v.findViewById(R.id.etAgencyDialogPercent);
        mEditBasicPay.setText(String.valueOf(basicPay));
        mEditPercent.setText(String.valueOf(percent));

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(v)
                .setTitle(R.string.menu_agency)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int pay, per;
                        if (mEditBasicPay.getText().length()==0) {
                            pay = 0;
                        } else {
                            pay = Integer.valueOf(mEditBasicPay.getText().toString());
                        }
                        if (mEditPercent.getText().length()==0) {
                            per = 0;
                        } else {
                            per = Integer.valueOf(mEditPercent.getText().toString());
                        }
                        sendResult(Activity.RESULT_OK, pay, per);
                    }
                })
                .create();
        return dialog;
    }

    private void sendResult(int resultCode, int basic, int percent) {
        if (getTargetFragment()==null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_AG_PAY, basic);
        intent.putExtra(EXTRA_AG_PERCENT, percent);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
