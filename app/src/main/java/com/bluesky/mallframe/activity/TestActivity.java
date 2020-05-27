package com.bluesky.mallframe.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bluesky.mallframe.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTvDate = findViewById(R.id.tv_base_date);
        mTvDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_base_date:
                //Todo 弹出一个日期选择对话框
                showChooseDateDialog();
                break;

            default:

        }
    }

    private void showChooseDateDialog() {
        DatePickerDialog dialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dialog = new DatePickerDialog(this, DatePickerDialog.THEME_TRADITIONAL);
        } else {
            dialog = new DatePickerDialog(this, DatePickerDialog.THEME_TRADITIONAL, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                }
            }, 2000, 4, 1);
        }
        dialog.show();
    }


}
