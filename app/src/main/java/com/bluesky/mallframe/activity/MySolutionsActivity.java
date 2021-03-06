package com.bluesky.mallframe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.App;
import com.bluesky.mallframe.base.AppConstant;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.UpLoadTurnSolution;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.data.source.remote.UpLoadSolutionRemoteDataSource;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;


public class MySolutionsActivity extends BaseActivity {
    private SolutionRemoteDataSource mSource;
    private List<TurnSolution> mSolutions;
    private SolutionAdapter mAdapter;
    private RecyclerView mRvSolution;
//    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_solutions_set_default:
                Toast.makeText(this, "默认", Toast.LENGTH_SHORT).show();
                setSolutionActive(mSolutions, mAdapter.getCurPostion());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_item_solutions_delete:
                //todo 删除solution,要弹出对话框,对话框要改成能设定title的
                App.showDialog(this, AppConstant.DELETE_DIALOG_RES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MySolutionsActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        mSource.deleteSolution(mSolutions.get(mAdapter.getCurPostion()).getObjectId());
                        mSolutions.remove(mAdapter.getCurPostion());
                        mAdapter.notifyDataSetChanged();
                    }
                }, null);
                break;
            case R.id.menu_item_solutions_edit:
                Intent intent = new Intent();
                intent.putExtra(EditActivity.FLAG_INTENT_DATA, mSolutions.get(mAdapter.getCurPostion()));
                intent.setClass(MySolutionsActivity.this, EditActivity.class);
                startActivityForResult(intent, EditActivity.REQUESTCODE);
                break;
            case R.id.menu_item_solutions_upload:
                //todo upload方案的处理
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                Date date = calendar.getTime();
                String onlyDate = FORMAT_ONLY_DATE.format(date);

                //todo bug:这样生成的solution的user对象,在表格中,不是指针.
//                UpLoadTurnSolution upLoadTurnSolution = new UpLoadTurnSolution(mSolutions.get(mAdapter.getCurPostion()), onlyDate);
                TurnSolution solution = mSolutions.get(mAdapter.getCurPostion());
                UpLoadTurnSolution upLoadTurnSolution = new UpLoadTurnSolution(solution, onlyDate);
                /*upLoadTurnSolution.setUser(solution.getUser());
                upLoadTurnSolution.setName(solution.getName());
                upLoadTurnSolution.setCompany(solution.getCompany());
                upLoadTurnSolution.setActive(solution.getActive());
                upLoadTurnSolution.setYourgroup(solution.getYourgroup());
                upLoadTurnSolution.setWorkdays(solution.getWorkdays());
                upLoadTurnSolution.setWorkgroups(solution.getWorkgroups());
                upLoadTurnSolution.setWorkdaykinds(solution.getWorkdaykinds());
                upLoadTurnSolution.setFlags(solution.getFlags());*/

//                upLoadTurnSolution.setUser(BmobUser.getCurrentUser(User.class));
                LogUtils.d("currentUser=  " + BmobUser.getCurrentUser(User.class).toString());
                LogUtils.d("getUser=  " + upLoadTurnSolution.getUser().toString());
                new UpLoadSolutionRemoteDataSource().addSolution(upLoadTurnSolution);

                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EditActivity.REQUESTCODE:
                    //如果返回的solution被设定为默认,则要取消其他的默认,因为默认只能有一个
                    //不管resultcode是ok还是cancel,solution都可能已经改变,因为有三个编辑子页面
                    if (data != null) {
                        TurnSolution solution = (TurnSolution) data.getSerializableExtra(EditActivity.FLAG_INTENT_DATA);
                        mSolutions.set(mAdapter.getCurPostion(), solution);
                        if (solution.getActive()) {
                            setSolutionActive(mSolutions, mAdapter.getCurPostion());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置默认,并取消其他solution的默认
     *
     * @param position
     */
    private void setSolutionActive(List<TurnSolution> list, int position) {
        //两个方案,目前用方案1
        //1.每个solution中都有activie标识
        //2.User中有默认solution字段

        //遍历设置每个solution的active
        for (int i = 0; i < mSolutions.size(); i++) {
            list.get(i).setActive(i == position);
            //更新后台每条solution
            SolutionRemoteDataSource remote = new SolutionRemoteDataSource();
            remote.updateSolution(list.get(i));
        }
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSolution.setLayoutManager(layoutManager);
        mAdapter = new SolutionAdapter();
        //todo 单击监听的响应事件,应该设定为修改默认,原app是查看
/*        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(EditActivity.DATA_SOLUTION, mSolutions.get(position));
                intent.setClass(MySolutionsActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });*/


        /*
         * todo 知识点:RecyclerView右键菜单的实现之三:我的方式
         *      1,ViewHolder实现OnCreateContextMenuListener接口,
         *       并在实现方法中inflate菜单布局(可将item的信息放入title),
         *       并记录当前position(ViewHolder.getLayoutPosition()方法也可获得)
         *      2,给adapter增加setPosition和getPosition方法,以便于在处理菜单事件时,获取item数据
         *      3,让Activity重写onContextItemSelected,处理菜单事件
         * */

        mRvSolution.setAdapter(mAdapter);
        /* todo 知识点:RecyclerView右键菜单的实现之一:复杂方式
         *
         *  步骤:1,item根布局添加longClickable="true"
         *       2,registerForContextMenu,注册给activity
         *       3,重载onCreateContextMenu和onContextItemSelected方法
         * */

        /* 方式二:
         * 步骤:1,在ViewHolder中为每个itemView的根布局设置setOnLongClickListener监听，
         *      2,然后在长按监听回调中设置当前的position，
         *      3,为每个itemView设置setOnCreateContextMenuListener监听，通过上面记录的position来执行相应的动作。
         * */
    }

/*  //在ViewHolder中设置了该监听,所以activity中已不需要.只需要复写onContextItemSelected
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_solutions, menu);
    }*/

    @Override
    protected void initData() {

        mSource = new SolutionRemoteDataSource();
        mSource.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
            @Override
            public void onSolutionsLoaded(List<TurnSolution> solutions) {
                LogUtils.d("获取的倒班表=" + solutions.toString());
                mSolutions = solutions;
                mAdapter.setData(mSolutions);
            }

            @Override
            public void onDataNotAvailable() {
                Toast.makeText(MySolutionsActivity.this, "没有可用的倒班表", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现
        toolbar.setTitle("我的倒班");
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

//        toolbar.inflateMenu(R.menu.menu_activity_mysolution);

        //查找控件
        mRvSolution = findViewById(R.id.rv_solutions);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_my_solutions;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_mysolution, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_add_new:
                Toast.makeText(this, "点击了新建", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add_download:
                Toast.makeText(this, "点击了下载", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder> {
        private List<TurnSolution> mListData = new ArrayList<>();
        private int mPosition;

        public void setData(List<TurnSolution> listData) {
            mListData = listData;
            notifyDataSetChanged();
        }

        public void setCurPosition(int position) {
            mPosition = position;
        }

        public int getCurPostion() {
            return mPosition;
        }

        @NonNull
        @Override
        public SolutionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MySolutionsActivity.this).inflate(R.layout.item_solution, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
//            holder.mCbDefault.setOnCheckedChangeListener(null);
        }

        @Override
        public void onBindViewHolder(@NonNull SolutionAdapter.ViewHolder holder, int position) {
            TurnSolution solution = mListData.get(position);
            holder.mTvNumber.setText(String.format(Locale.CHINA, "%d#", position + 1));
            holder.mTvName.setText(solution.getName());
            holder.mTvInfo.setText(String.format(Locale.CHINA, "天数:%d  班组:%d  %s", solution.getWorkdays().size(), solution.getWorkgroups().size(), solution.getDefaultWorkGroup()));
            String company = Strings.isNullOrEmpty(solution.getCompany()) ? "无" : solution.getCompany();
            holder.mTvCompany.setText(String.format(Locale.CHINA, "公司:%s", company));
            holder.mIvDefault.setVisibility(solution.getActive() ? View.VISIBLE : View.INVISIBLE);
//            holder.mIvUpload.setVisibility();
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView mTvNumber;
            TextView mTvName;
            TextView mTvInfo;
            TextView mTvCompany;
            CheckBox mCbDefault;
            CardView mCvRoot;
            ImageView mIvDefault;
            ImageView mIvUpload;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mTvName = itemView.findViewById(R.id.tv_name);
                mTvInfo = itemView.findViewById(R.id.tv_info);
                mTvCompany = itemView.findViewById(R.id.tv_company);
                mCbDefault = itemView.findViewById(R.id.cb_default);
                mCvRoot = itemView.findViewById(R.id.card_root);
                mIvDefault = itemView.findViewById(R.id.iv_default);
                mIvUpload = itemView.findViewById(R.id.iv_upload);
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                TurnSolution solution = mListData.get(getLayoutPosition());
                getMenuInflater().inflate(R.menu.menu_solutions, menu);
                menu.setHeaderTitle(solution.getName());
                setCurPosition(getLayoutPosition());
            }


        }
    }
}
