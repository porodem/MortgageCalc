package com.porodem.mortgagecalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class IntroFragment extends Fragment {

    private Button mStartButton;
    private EditText mEndPriceInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro, container, false);

        mEndPriceInput = v.findViewById(R.id.etEndFlatPrice);
        mEndPriceInput.addTextChangedListener(new NumberTextWatcher(mEndPriceInput));

        mStartButton = v.findViewById(R.id.btnStart);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempStr = mEndPriceInput.getText().toString();
                int flatPrice = 1500000; //default Flat Price
                if (!tempStr.equals("")) {
                    flatPrice = Integer.valueOf(tempStr.replaceAll("\\D+", ""));
                }
                Intent i = MainActivity.newIntent(getActivity(), flatPrice);
                startActivity(i);
            }
        });

        return v;
    }
}
