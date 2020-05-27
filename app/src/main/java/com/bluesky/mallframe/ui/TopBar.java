package com.bluesky.mallframe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluesky.mallframe.R;

/**
 * @author BlueSky
 * @date 2020/5/13
 * Description:
 */
public class TopBar extends RelativeLayout {

    private Button mLeftButton, mRightButton;
    private TextView mTvTitle;

    String titleText;
    float titleTextSize;
    int titleTextColor;

    Drawable leftBtnBackground;
    String leftBtnText;
    Drawable rightBtnBackground;
    String rightBtnText;

    private LayoutParams leftParams, rightParams, titleParams;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        titleText = ta.getString(R.styleable.TopBar_titleText);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 12);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, 0);

        leftBtnBackground = ta.getDrawable(R.styleable.TopBar_leftBtnBackground);
        leftBtnText = ta.getString(R.styleable.TopBar_leftBtnText);

        rightBtnBackground = ta.getDrawable(R.styleable.TopBar_rightBtnBackground);
        rightBtnText = ta.getString(R.styleable.TopBar_rightBtnText);
        ta.recycle();

        mLeftButton = new Button(context);
        mRightButton = new Button(context);
        mTvTitle = new TextView(context);

        mLeftButton.setText(leftBtnText);
        mLeftButton.setBackground(leftBtnBackground);

        mRightButton.setText(rightBtnText);
        mRightButton.setBackground(rightBtnBackground);

        mTvTitle.setText(titleText);
        mTvTitle.setTextSize(titleTextSize);
        mTvTitle.setTextColor(titleTextColor);
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setBackgroundColor(Color.WHITE);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.addRule(ALIGN_PARENT_LEFT);
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.addRule(ALIGN_PARENT_RIGHT);
        addView(mLeftButton, leftParams);
        addView(mRightButton, rightParams);
        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mTvTitle, titleParams);

    }
}
