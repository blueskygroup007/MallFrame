package com.bluesky.mallframe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
    private int mNumber = 1;

    //    private ConstraintLayout mLayoutRoot;
    private Button mBtnDec;
    private Button mBtnInc;
    private TextView mTvNumber;
    private OnNumberChangeListener mListener = null;

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
        mNumber = a.getInteger(R.styleable.BSNumberPicker_current_number, 1);
        a.recycle();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_bs_number_picker, this, true);
        mBtnDec = findViewById(R.id.btn_dec);
        mBtnInc = findViewById(R.id.btn_inc);
        mTvNumber = findViewById(R.id.tv_number);
//        mLayoutRoot = findViewById(R.id.layout_root);

//        mLayoutRoot.setBackgroundColor(Color.WHITE);
        mTvNumber.setTextColor(mTextColor);
        mTvNumber.setTextSize(mTextSize);
        mTvNumber.setText(String.valueOf(mNumber));
    }

    public void setNumber(int number) {
        mTvNumber.setText(String.valueOf(number));
    }

    public int getNumber() {
        return Integer.parseInt(mTvNumber.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        /*如果没有设置监听器,直接返回,必须有*/
        if (mListener == null) {
            return;
        }
        int number = getNumber();
        switch (v.getId()) {
            case R.id.btn_dec:
                mTvNumber.setText(String.valueOf(--number));
                mListener.onNumberDec(number);
                break;
            case R.id.btn_inc:
                mTvNumber.setText(String.valueOf(++number));
                mListener.onNumberInc(number);
                break;
            default:
        }
    }

    public void setOnNumberChangeListener(OnNumberChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnNumberChangeListener {
        void onNumberInc(int number);

        void onNumberDec(int number);
    }

}
