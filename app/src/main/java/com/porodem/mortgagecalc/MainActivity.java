package com.porodem.mortgagecalc;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    public static final String EXTRA_END_PRICE = "com.porodem.mortgagecalc.extra.end.price";

    public static Intent newIntent(Context packageContext, int endPrice) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_END_PRICE, endPrice);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int price = getIntent().getIntExtra(EXTRA_END_PRICE, 0);
        Fragment fragment = CalcFragment.newInstance(price);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
