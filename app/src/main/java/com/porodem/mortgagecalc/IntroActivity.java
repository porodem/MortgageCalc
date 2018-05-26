package com.porodem.mortgagecalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class IntroActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new IntroFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
