package com.bluesky.mallframe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MultiAutoCompleteTextView;

import com.bluesky.mallframe.R;

public class Test2Activity extends AppCompatActivity {

    private MultiAutoCompleteTextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        mTextView = findViewById(R.id.mactv_test);
    }
}
