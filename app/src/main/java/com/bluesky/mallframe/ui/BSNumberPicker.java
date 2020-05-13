package com.bluesky.mallframe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bluesky.mallframe.R;

/**
 * @author BlueSky
 * @date 2020/5/12
 * Description:
 */
public class BSNumberPicker extends LinearLayout implements View.OnClickListener {

    private int mTextColor = Color.BLACK;
    private float mTextSize = 12;
    private String mNumber = "0";

    private LinearLayout mLayoutRoot;
    private Button mBtnDec;
    private Button mBtnInc;
    private TextView mTvNumber;

    public BSNumberPicker(Context context) {
        super(context);
        initView(context);
        initEvent(context);
    }

    public BSNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypeValue(context, attrs);
        initView(context);
        initEvent(context);

    }

    public BSNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BSNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initEvent(Context context) {
        mBtnDec.setOnClickListener(this);
        mBtnInc.setOnClickListener(this);
    }

    private void initTypeValue(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BSNumberPicker);
        mTextColor = a.getColor(R.styleable.BSNumberPicker_text_color, Color.BLACK);
        mTextSize = a.getDimension(R.styleable.BSNumberPicker_text_size, 12);
        mNumber = a.getString(R.styleable.BSNumberPicker_current_number);
        a.recycle();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.number_picker_bs, this, true);
        mBtnDec = findViewById(R.id.btn_dec);
        mBtnInc = findViewById(R.id.btn_inc);
        mTvNumber = findViewById(R.id.tv_number);
        mLayoutRoot = findViewById(R.id.ll_root);

//        mLayoutRoot.setBackgroundColor(Color.WHITE);
        mTvNumber.setTextColor(mTextColor);
        mTvNumber.setTextSize(mTextSize);
        mTvNumber.setText(mNumber);
    }

    public void setDecListener(OnClickListener listener) {
        mBtnDec.setOnClickListener(listener);
    }

    public void setIncListener(OnClickListener listener) {
        mBtnInc.setOnClickListener(listener);
    }

    public void setNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            mTvNumber.setText(number);
        }
    }

    public String getNumber() {
        return mTvNumber.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        int number;
        String strNumber;
        number = Integer.parseInt(mTvNumber.getText().toString());

        switch (v.getId()) {
            case R.id.btn_dec:
                strNumber = String.valueOf(--number);
                mTvNumber.setText(strNumber);
                break;
            case R.id.btn_inc:
                strNumber = String.valueOf(++number);
                mTvNumber.setText(strNumber);

                break;
            default:
        }
    }

}
