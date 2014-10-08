package com.blogspot.vsvydenko.reutersreader.ui;

import com.blogspot.vsvydenko.reutersreader.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by vsvydenko on 07.10.14.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }
}
