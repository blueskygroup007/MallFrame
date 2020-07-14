package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.UpLoadTurnSolution;
import com.bluesky.mallframe.data.source.remote.UpLoadSolutionRemoteDataSource;
import com.google.common.base.Strings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.bluesky.mallframe.activity.EditActivity.FLAG_INTENT_DATA;
import static com.bluesky.mallframe.activity.EditActivity.FLAG_INTENT_TYPE;

/**
 * @author BlueSky
 * @date 2020/7/14
 * Description:
 */
public class UpLoadActivity extends BaseActivity {

    private RecyclerView mRvListView;
    private UpLoadAdapter mAdapter;
    private UpLoadSolutionRemoteDataSource mSource;
    private List<UpLoadTurnSolution> mSolutions;

    @Override
    protected void initEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvListView.setLayoutManager(layoutManager);
        mAdapter = new UpLoadAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UpLoadTurnSolution upLoadTurnSolution = mSolutions.get(position);
                Intent intent = new Intent();
                intent.putExtra(FLAG_INTENT_DATA, upLoadTurnSolution);
                intent.putExtra(FLAG_INTENT_TYPE, EditActivity.TYPE_DISPLAY);
                intent.setClass(UpLoadActivity.this, EditActivity.class);
                startActivityForResult(intent, EditActivity.REQUESTCODE);
            }
        });
        mRvListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mSource = new UpLoadSolutionRemoteDataSource();
        mSource.loadSolutions(new UpLoadSolutionRemoteDataSource.LoadSolutionsCallback() {
            @Override
            public void onSolutionsLoaded(List<UpLoadTurnSolution> solutions) {
                LogUtils.d("共享的倒班表=" + solutions.toString());
                mSolutions = solutions;
                mAdapter.setData(mSolutions);
            }

            @Override
            public void onDataNotAvailable() {
                Toast.makeText(UpLoadActivity.this, "没有共享的倒班表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initView() {
        mRvListView = findViewById(R.id.rv_upload_list);
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现
        toolbar.setTitle("我的共享");
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        } else {
            LogUtils.d("toolbar not found!");
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_upload;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_upload_cancel:
                Toast.makeText(UpLoadActivity.this, "取消共享", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_item_upload_download:
                Toast.makeText(UpLoadActivity.this, "下载方案", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class UpLoadAdapter extends RecyclerView.Adapter<UpLoadAdapter.ViewHolder> {
        private List<UpLoadTurnSolution> mListData = new ArrayList<>();
        private int mPosition;

        private OnItemClickListener mListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public void setData(List<UpLoadTurnSolution> listData) {
            mListData = listData;
            notifyDataSetChanged();
        }

        public List<UpLoadTurnSolution> getData() {
            return mListData;
        }

        @NonNull
        @Override
        public UpLoadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(UpLoadActivity.this).inflate(R.layout.item_solution, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mCvRoot.setOnClickListener(null);
        }

        @Override
        public void onBindViewHolder(@NonNull UpLoadAdapter.ViewHolder holder, int position) {
            TurnSolution solution = mListData.get(position);
            holder.mTvNumber.setText(String.format(Locale.CHINA, "%d#", position + 1));
            holder.mTvName.setText(solution.getName());
            holder.mTvInfo.setText(String.format(Locale.CHINA, "天数:%d  班组:%d  %s", solution.getWorkdays().size(), solution.getWorkgroups().size(), solution.getDefaultWorkGroup()));
            String company = Strings.isNullOrEmpty(solution.getCompany()) ? "无" : solution.getCompany();
            holder.mTvCompany.setText(String.format(Locale.CHINA, "公司:%s", company));

            holder.mCvRoot.setOnClickListener(new OnHolderClickListener(holder, position));
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class OnHolderClickListener implements View.OnClickListener {

            private UpLoadAdapter.ViewHolder mViewHolder;
            private int mPosition;

            public OnHolderClickListener(UpLoadAdapter.ViewHolder holder, int position) {
                mViewHolder = holder;
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                mListener.onItemClick(mPosition);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView mTvNumber;
            TextView mTvName;
            TextView mTvInfo;
            TextView mTvCompany;
            CheckBox mCbDefault;
            CardView mCvRoot;
//            ImageView mIvDefault;
//            ImageView mIvUpload;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mTvName = itemView.findViewById(R.id.tv_name);
                mTvInfo = itemView.findViewById(R.id.tv_info);
                mTvCompany = itemView.findViewById(R.id.tv_company);
                mCbDefault = itemView.findViewById(R.id.cb_default);
                mCvRoot = itemView.findViewById(R.id.card_root);

//                mIvDefault = itemView.findViewById(R.id.iv_default);
//                mIvUpload = itemView.findViewById(R.id.iv_upload);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                UpLoadTurnSolution solution = mListData.get(getLayoutPosition());
                getMenuInflater().inflate(R.menu.menu_context_upload, menu);
                menu.setHeaderTitle(solution.getName());
                setCurPosition(getLayoutPosition());
            }
        }

        public void setCurPosition(int layoutPosition) {
            mPosition = layoutPosition;
        }

        public int getCurPosition() {
            return mPosition;
        }
    }
}
