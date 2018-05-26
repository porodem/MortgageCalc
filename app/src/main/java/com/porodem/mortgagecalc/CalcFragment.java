package com.porodem.mortgagecalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CalcFragment extends Fragment {

    public static final String TAG = "CalcFragment";
    public static final String ARG_END_PRICE = "com.porodem.mortgagecalc.arg.end.price";

    private static final String DIALOG_AGENCY = "DialogAgency";
    private static final String DIALOG_DISP_CALC = "DialogDisplayCalc";
    private static final int REQUEST_AGENCY = 0;
    private static final int REQUEST_PAYMENTS = 1;

    private EditText flatPriceField, etStartPercent, etHaveCash;
    private TextView tvStartPay, tvMonthly, tvCreditSum,
                    tvAgencyTax, tvLastCash;
    private Button mButton;
    private Switch mSwitchAgencyTax;
    private ImageView mImageAgencySettings;

    private Spinner mPercentSpinner, mYearsSpinner;
    Double[] percents = new Double[] {8.8, 8.9, 9.0, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10.0};
    Integer[] years = new Integer[] {5,7,10,12,15,20,25};

    ArrayAdapter<Double> adapter;
    ArrayAdapter<Integer> adapterYears;

    int[] flatPrice = new int[9];
    private int mEndPrice;

    private int months;
    private int mHaveMoney = 370000;
    private int mFlatPrice = 2000000;
    private int mOtherPayments;
    private int mCreditSum, mFirstPay, mFirstPayPercent;
    private double percent = 9.1;
    int agenstvoCommision; //2nd var for temporary saving value when disable it by switch
    int mBasicAgencyPay = 34000;
    int mAgencyPercent = 1;
    boolean haveRieltor;
    int lastMoney;

    public Calculations mCalculations;

    public static CalcFragment newInstance(int endPrice) {
        Bundle args = new Bundle();
        args.putInt(ARG_END_PRICE, endPrice);
        CalcFragment fragment = new CalcFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCalculations = Calculations.get();
        mOtherPayments = mCalculations.getExtraPayments();

        Bundle args = getArguments();
        mEndPrice = args.getInt(ARG_END_PRICE);

        for (int i = flatPrice.length - 2; i>= 0; i--) {
            flatPrice[i] = mEndPrice - 50000*(flatPrice.length - (i+1));
        }
        flatPrice[8] = mEndPrice;
        mFlatPrice = mEndPrice;
        adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, percents);
        adapterYears = new ArrayAdapter<Integer>(getActivity(), R.layout.custom_spinner, years);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        SeekBar seekFlatPrice = view.findViewById(R.id.seekFlatPrice);

        flatPriceField = view.findViewById(R.id.etFlatPrice);
        flatPriceField.setText(formatNumber(mFlatPrice));
        flatPriceField.addTextChangedListener(new NumberTextWatcher(flatPriceField));

        etStartPercent = view.findViewById(R.id.etStartPercent);
        etStartPercent.setText(String.valueOf(15));
        etStartPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()!= 0) {
                    calculate();
                    updateUI();
                }

            }
        });

        etHaveCash = view.findViewById(R.id.etHaveCash);
        etHaveCash.setText(String.valueOf(mHaveMoney));
        etHaveCash.addTextChangedListener(new NumberTextWatcher(etHaveCash));

        mPercentSpinner = view.findViewById(R.id.etPercent);
        mPercentSpinner.setAdapter(adapter);
        mPercentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = Double.valueOf(mPercentSpinner.getSelectedItem().toString());
                tvMonthly.setText(String.valueOf(monthlyPay()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mYearsSpinner = view.findViewById(R.id.etYears);
        mYearsSpinner.setAdapter(adapterYears);
        mYearsSpinner.setSelection(2);
        mYearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                months = 12 * Integer.valueOf(mYearsSpinner.getSelectedItem().toString());
                tvMonthly.setText(String.valueOf(monthlyPay()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvStartPay = view.findViewById(R.id.tvStartPay);
        tvStartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.start_pay_info, Toast.LENGTH_LONG).show();
            }
        });
        tvMonthly = view.findViewById(R.id.tvMonthly);
        tvCreditSum = view.findViewById(R.id.tvCreditSum);
        tvAgencyTax = view.findViewById(R.id.tvAgencyTax);

        tvLastCash = view.findViewById(R.id.tvLastCash);
        tvLastCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DisplayCalcFragment dialog = DisplayCalcFragment.newInstance();
                dialog.show(fm, DIALOG_DISP_CALC);
            }
        });

        mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculate();
                updateUI();
            }
        });

        seekFlatPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int p, boolean b) {
                progress = flatPrice[p];
                mFlatPrice = progress;
                flatPriceField.setText(String.valueOf(formatNumber(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calculate();
                updateUI();
            }
        });

        mImageAgencySettings = view.findViewById(R.id.imageAgencySettings);
        mImageAgencySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                AgencyFragment dialog = AgencyFragment.newInstance(mBasicAgencyPay, mAgencyPercent);
                dialog.setTargetFragment(CalcFragment.this, REQUEST_AGENCY);
                dialog.show(fm, DIALOG_AGENCY);
            }
        });

        mSwitchAgencyTax = view.findViewById(R.id.switchAgencyTax);
        mSwitchAgencyTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    haveRieltor = false;
                    calculate();
                    updateUI();
                } else {
                    haveRieltor = true;
                    calculate();
                    updateUI();
                }
            }
        });

        calculate();
        updateUI();
        return view;
    }

    private String  formatNumber(int sourceNumber) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(sourceNumber);
    }

    private void updateUI() {
        tvStartPay.setText(formatNumber(mFirstPay));
        tvCreditSum.setText(formatNumber(mCreditSum));
        tvAgencyTax.setText(String.valueOf(agenstvoCommision));
        tvMonthly.setText(monthlyPay());

        tvLastCash.setText(formatNumber(lastMoney));
        if (lastMoney<0) {
            tvLastCash.setTextColor(getResources().getColor(R.color.redPrice));
        } else  {
            tvLastCash.setTextColor(getResources().getColor(R.color.greenLast));
        }
    }

    private void calculate() {
        updateNumbers();
        //take months
        months = 12 * Integer.valueOf(mYearsSpinner.getSelectedItem().toString());
        //take first-start payment percent
        if (etStartPercent.getText().toString().matches("\\d+(?:\\.\\d+)?")) {
            mFirstPayPercent = Integer.valueOf(etStartPercent.getText().toString());
        } else Toast.makeText(getActivity(), "input correct value!", Toast.LENGTH_SHORT).show();

        //calculate first payment amount and display it
        mFirstPay = mFlatPrice/100 * mFirstPayPercent;

        mCreditSum = mFlatPrice - mFirstPay;

        mFirstPayPercent = Integer.valueOf(etStartPercent.getText().toString());
        if (haveRieltor) {
            tvAgencyTax.setTextColor(getResources().getColor(R.color.redPrice));
            agenstvoCommision = mBasicAgencyPay + (mFlatPrice / 100 * mAgencyPercent);
            mCalculations.setAgency(agenstvoCommision);
        } else {
            agenstvoCommision = 0;
            tvAgencyTax.setTextColor(getResources().getColor(R.color.colorGray));
        }

        mCalculations.setFirstPay(mFirstPay);
        mCalculations.setAgency(agenstvoCommision);
        //mOtherPayments insurance, documents, etc.
        lastMoney = mHaveMoney - (agenstvoCommision + mFirstPay + mOtherPayments);
    }

    private String monthlyPay() {
        //formula from http://www.reghelp.ru/kak_rasschitat_plateji_po_kreditu.shtml
        double z = percent / 1200;
        double a = z * Math.pow(1+z,months) / (Math.pow(1+z,months)-1);
        double pay = mCreditSum * a;
        int ix = (int)Math.round(pay);
        String mPayx = formatNumber(ix);

        // formula (bad variant) from //http://www.platesh.ru/annuitetnie-plateshi/
        /*double p =  percent / 1200;
        double b = mFlatPrice * (p+(p/((Math.pow(1+p,months))-1)));
        int i = (int)Math.round(b);
        String mPay = formatNumber(i);*/

        return mPayx;
    }

    private void updateNumbers() {
        String tempString = flatPriceField.getText().toString();
        mFlatPrice = Integer.valueOf(tempString.replaceAll("\\D+",""));
        //take how much money you have
        tempString = etHaveCash.getText().toString();
        mHaveMoney = Integer.valueOf(tempString.replaceAll("\\D+",""));
        mCalculations.setCapital(mHaveMoney);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.extra_pay:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, REQUEST_PAYMENTS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        /*int i = data.getIntExtra(SettingsFragment.EXTRA_OTHER_PAYMENTS, 0);
        Log.d(TAG, "result OK " + i + " request code: " + requestCode);*/
        if (requestCode == REQUEST_AGENCY) {
            mBasicAgencyPay = data.getIntExtra(AgencyFragment.EXTRA_AG_PAY, 1);
            mAgencyPercent = data.getIntExtra(AgencyFragment.EXTRA_AG_PERCENT, 1);
            calculate();
            updateUI();
            return;
        }
        if (requestCode == REQUEST_PAYMENTS) {
            mOtherPayments = data.getIntExtra(SettingsFragment.EXTRA_OTHER_PAYMENTS, 0);
            calculate();
            updateUI();
        }
    }


}
