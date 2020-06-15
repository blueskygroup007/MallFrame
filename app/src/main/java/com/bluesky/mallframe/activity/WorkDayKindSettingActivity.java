package com.bluesky.mallframe.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.ui.BSNumberPicker;
import com.google.common.base.Strings;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.app.AlertDialog.THEME_TRADITIONAL;
import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_TIME_NO_SECS;

public class WorkDayKindSettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_TERM_DAY";

    //控件
    private BSNumberPicker mNumberPicker;
    private RecyclerView mRecyclerView;
    private TextView mTvMsg;

    //数据
    private TurnSolution mSolution;
    private List<WorkDayKind> mWorkDayKinds;
    private RvTermDaysAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mNumberPicker.setOnNumberChangeListener(new BSNumberPicker.OnNumberChangeListener() {
            @Override
            public void onNumberInc(int number) {
                if (number > mWorkDayKinds.size()) {
                    WorkDayKind workDayKind = new WorkDayKind();
                    mAdapter.getList().add(workDayKind);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNumberDec(int number) {
                if (number < mWorkDayKinds.size()) {
                    mAdapter.getList().remove(mAdapter.getList().size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(FLAG_INTENT_DATA);
        mWorkDayKinds = mSolution.getWorkdaykinds();
        LogUtils.d(mWorkDayKinds);

        mNumberPicker.setNumber(mWorkDayKinds.size());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RvTermDaysAdapter(mWorkDayKinds);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("编辑班次");
        } else {
            LogUtils.e("toolbar没有找到");
        }

        mNumberPicker = findViewById(R.id.np_term_days_count);
        mRecyclerView = findViewById(R.id.rv_term_days_list);
        mTvMsg = findViewById(R.id.tv_term_days_setting_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static final String MSG_BLANK_IS_EMPTY = "存在空项!";
    public static final String MSG_BLANK_IS_REPEAT = "项目重复!";
    public static final String MSG_DUFAULT_IS_UNCHECKED = "默认未设置!";
    public static final String MSG_NO_CHANGE = "无改动";
    public static final String MSG_OK = "修改正确!";

    @Override
    public void onBackPressed() {
        if (mAdapter.isEmpty()) {
            mTvMsg.setText("提示:" + MSG_BLANK_IS_EMPTY);
            return;
        }
        if (mAdapter.isRepeat()) {
            mTvMsg.setText("提示:" + MSG_BLANK_IS_REPEAT);
            return;
        }
        /*没有改动,直接退出*/
        if (!mAdapter.isChanged()) {
            mTvMsg.setText("提示:" + MSG_NO_CHANGE);
            finish();
        } else {
            /*修改正确,弹出是否保存对话框*/
            mTvMsg.setText("提示:" + MSG_OK);
            showNormalDialog();
            return;
        }
    }

    /**
     * 是否保存的对话框
     */
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.ic_save_black_24dp);
        normalDialog.setTitle("保存");
        normalDialog.setMessage("您修改了设置,是否保存?");

        normalDialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        SolutionDataSource mRemote = new SolutionRemoteDataSource();
                        mSolution.setWorkdaykinds(mWorkDayKinds);
                        mRemote.updateSolution(mSolution);
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("不保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_work_day_kind_setting;

    }

    private class RvTermDaysAdapter extends RecyclerView.Adapter<RvTermDaysAdapter.ViewHolder> {
        private List<WorkDayKind> mBackupList = new ArrayList<>();
        private List<WorkDayKind> mList;

        public RvTermDaysAdapter(List<WorkDayKind> list) {
            mBackupList.addAll(list);
            mList = list;
        }

        public List<WorkDayKind> getList() {
            return mList;
        }

        /**
         * 是否有修改
         *
         * @return
         */
        public boolean isChanged() {
            return !CollectionUtils.isEqualCollection(mList, mBackupList);
        }


        public boolean isEmpty() {
            for (WorkDayKind workDayKind : mList) {
                if (Strings.isNullOrEmpty(workDayKind.getName())
                        || Strings.isNullOrEmpty(workDayKind.getStarttime())
                        || Strings.isNullOrEmpty(workDayKind.getEndtime())) {
                    return true;
                }
            }
            return false;
        }

        public boolean isRepeat() {
            Set<String> repeated = new HashSet<>();
            for (WorkDayKind workDayKind : mList
            ) {
                if (!repeated.add(workDayKind.getName())) {
                    return true;
                }
            }
            return false;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_days_item_card_style_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mEtName.removeTextChangedListener((TextWatcher) holder.mEtName.getTag());

        }

        @Override
        public void onBindViewHolder(@NonNull RvTermDaysAdapter.ViewHolder holder, int position) {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mList.get(position).setName(s.toString());
                }
            };

            WorkDayKind workDayKind = mList.get(position);
            holder.mTvNumber.setText(String.valueOf(position + 1));
            holder.mEtName.setText(workDayKind.getName());
            holder.mTvSetupTime.setText(String.format("%s至%s", workDayKind.getStarttime(), workDayKind.getEndtime()));
            /*设置上下班日期的点击事件*/
            holder.mTvSetupTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    chooseTimeDialog2(mList, position);
                    showTwoTimeDialog(position);
                }
            });
            /*班次名称修改的监听事件*/
            holder.mEtName.addTextChangedListener(watcher);
            holder.mEtName.setTag(watcher);
        }

        private void showTwoTimeDialog(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkDayKindSettingActivity.this);
            View view = LayoutInflater.from(WorkDayKindSettingActivity.this).inflate(R.layout.two_datepicker, null);
            TimePicker pickerStart = view.findViewById(R.id.tp_start);
            TimePicker pickerEnd = view.findViewById(R.id.tp_end);
            pickerStart.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
            pickerEnd.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
            pickerStart.setIs24HourView(true);
            pickerEnd.setIs24HourView(true);
            builder.setView(view);
            builder.setNegativeButton(R.string.text_btn_two_picker_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(R.string.text_btn_two_picker_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, pickerStart.getCurrentHour());
                    calendar.set(Calendar.MINUTE, pickerStart.getCurrentMinute());
                    String startTime = FORMAT_ONLY_TIME_NO_SECS.format(calendar.getTime());
                    calendar.set(Calendar.HOUR_OF_DAY, pickerEnd.getCurrentHour());
                    calendar.set(Calendar.MINUTE, pickerEnd.getCurrentMinute());
                    String endTime = FORMAT_ONLY_TIME_NO_SECS.format(calendar.getTime());
                    mList.get(position).setStarttime(startTime);
                    mList.get(position).setEndtime(endTime);
                    mAdapter.notifyDataSetChanged();
                    LogUtils.d(String.format("开始时间=%s----结束时间=%s", startTime, endTime));
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_button_round_blank);
//            changeTimePickerColor();
        }

        /**
         * 指定分割线divider颜色，设定Picker大小
         */
        public void changeTimePickerColor() {
            Resources systemResources = Resources.getSystem();
            int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
            int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");

            NumberPicker hourNumberPicker = findViewById(hourNumberPickerId);
            NumberPicker minuteNumberPicker = findViewById(minuteNumberPickerId);

            setNumberPickerDivider(hourNumberPicker, Color.YELLOW);
            setNumberPickerDivider(minuteNumberPicker, Color.GREEN);
/*            setNumberpickerTextColour(hourNumberPicker, Color.RED);
            setNumberpickerTextColour(minuteNumberPicker, Color.BLUE);*/

//        setPickerSize(hourNumberPicker, 30, this);
        }

        /**
         * 指定分割线颜色
         *
         * @param numberPicker
         * @param color
         */
        private void setNumberPickerDivider(NumberPicker numberPicker, int color) {

            try {
                Field dividerFields = NumberPicker.class.getDeclaredField("mSelectionDivider");

                dividerFields.setAccessible(true);

                dividerFields.set(numberPicker, new ColorDrawable(color));

            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                LogUtils.w("setNumberPickerTxtClr", e);
            }
        }


        /**
         * 另一种指定文字颜色的方法
         *
         * @param number_picker
         * @param color
         */
        private void setNumberpickerTextColour(NumberPicker number_picker, int color) {
            final int count = number_picker.getChildCount();

            for (int i = 0; i < count; i++) {
                View child = number_picker.getChildAt(i);

                try {
                    Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                    wheelpaint_field.setAccessible(true);
                    ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    number_picker.invalidate();
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    LogUtils.i("setNumberPickerTxtClr", "set_numberpicker_text_colour: " + e);
                }
            }
        }


        /**
         * 指定NumberPicker大小
         *
         * @param np
         * @param widthDpValue NumberPicker和NumberPicker的宽度值
         */
        private void setPickerSize(NumberPicker np, int widthDpValue) {
            int widthPxValue = ConvertUtils.dp2px(widthDpValue);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPxValue, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            np.setLayoutParams(params);
        }


        private void chooseTimeDialog(List<WorkDayKind> list, int position) {
            String startTime = list.get(position).getStarttime();
            String endTime = list.get(position).getEndtime();
            Calendar calendar = Calendar.getInstance();
            CostumTimePickerDialog dialog = new CostumTimePickerDialog(
                    WorkDayKindSettingActivity.this,
                    THEME_TRADITIONAL, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Toast.makeText(WorkDayKindSettingActivity.this, String.format("%s点%s分", hourOfDay, minute), Toast.LENGTH_SHORT).show();
                }
            }, 0, 0, true);

            dialog.show();
        }

        private void chooseTimeDialog2(List<WorkDayKind> list, int position) {
            String startTime = list.get(position).getStarttime();
            String endTime = list.get(position).getEndtime();
            Date curDate = stringToDate(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            TimePickerDialog dialog = new TimePickerDialog(WorkDayKindSettingActivity.this, THEME_TRADITIONAL, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(1900, 0, 1, hourOfDay, minute);
                    String modifyStingTimeStart = FORMAT_ONLY_TIME_NO_SECS.format(calendar1.getTime());
                    calendar1.add(Calendar.HOUR_OF_DAY, Integer.parseInt(mList.get(position).getFlag()));
                    String modifyStingTimeEnd = FORMAT_ONLY_TIME_NO_SECS.format(calendar1.getTime());

                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            dialog.show();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            EditText mEtName;
            TextView mTvSetupTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mEtName = itemView.findViewById(R.id.et_name);
                mTvSetupTime = itemView.findViewById(R.id.tv_setup_time);
            }
        }
    }

    private Date stringToDate(String date) {
        if (date == null || date.isEmpty()) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            return calendar.getTime();
        } else {
            try {
                return FORMAT_ONLY_TIME_NO_SECS.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    class CostumTimePickerDialog extends TimePickerDialog {

        public CostumTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
/*            BSNumberPicker numberPicker = new BSNumberPicker(TermDaysSettingActivity.this);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            params.gravity = Gravity.TOP;
            params.leftMargin = 100;
            params.rightMargin = 100;
//            params.topMargin = -100;
            //获取按钮位置
            Button button = getButton(DialogInterface.BUTTON_POSITIVE);
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.height = getResources().getDisplayMetrics().heightPixels + 200;
//            getWindow().getDecorView().getLayoutParams();
            addContentView(numberPicker, params);*/
        }


    }
}
