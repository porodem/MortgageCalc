package com.porodem.mortgagecalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {

    public static final String EXTRA_OTHER_PAYMENTS = "com.porodem.mortgagecalc.settings.payments";

    private int mRights, mEstimate, mInsurance, mOtherExtraPay;
    private Calculations mCalculations;
    private EditText mEditTextRights, mEditTextEstimate, mEditTextInsurance, mEditTextOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCalculations = Calculations.get();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        mEditTextRights = v.findViewById(R.id.etOwnersRight);
        mEditTextEstimate = v.findViewById(R.id.etEstimateFlat);
        mEditTextInsurance = v.findViewById(R.id.etInsurance);
        mEditTextOther = v.findViewById(R.id.etOtherPayments);

        mEditTextRights.setText(String.valueOf(mCalculations.getOwnerRights()));
        mEditTextEstimate.setText(String.valueOf(mCalculations.getEstimate()));
        mEditTextInsurance.setText(String.valueOf(mCalculations.getInsurance()));
        mEditTextOther.setText(String.valueOf(mCalculations.getOther()));

        Button button = v.findViewById(R.id.btnConfirmOtherPays);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextRights.getText().length()==0) {
                  mRights = 0;
                } else {
                    mRights = Integer.valueOf(mEditTextRights.getText().toString());
                }
                if (mEditTextEstimate.getText().length()==0) {
                    mEstimate = 0;
                } else {
                    mEstimate = Integer.valueOf(mEditTextEstimate.getText().toString());
                }
                if (mEditTextInsurance.getText().length()==0) {
                    mInsurance = 0;
                } else {
                    mInsurance = Integer.valueOf(mEditTextInsurance.getText().toString());
                }
                if (mEditTextOther.getText().length()==0) {
                    mOtherExtraPay = 0;
                } else {
                    mOtherExtraPay = Integer.valueOf(mEditTextOther.getText().toString());
                }

                mCalculations.setOwnerRights(mRights);
                mCalculations.setEstimate(mEstimate);
                mCalculations.setInsurance(mInsurance);
                mCalculations.setOther(mOtherExtraPay);
                int payments = mRights + mEstimate + mInsurance + mOtherExtraPay;
                sendResult(Activity.RESULT_OK, payments);
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void sendResult(int resultCode, int pay) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_OTHER_PAYMENTS, pay);
        getActivity().setResult(resultCode, intent);
    }
}
