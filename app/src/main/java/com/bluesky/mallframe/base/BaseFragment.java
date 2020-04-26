package com.bluesky.mallframe.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author BlueSky
 * @date 2020/3/30
 * Description:
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(), container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initView(view, savedInstanceState);
        initData();
        initEvent();
    }

    protected abstract void initEvent();

    protected abstract void initData();

//    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initView(View view);

    protected abstract int setLayout();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
