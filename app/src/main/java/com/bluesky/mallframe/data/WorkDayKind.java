package com.bluesky.mallframe.data;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:工作日类型，日，中，夜，休
 */
public class WorkDayKind extends BmobObject implements Cloneable, Iinformation {
    //序号
    private Integer number;
    //名称
    private String name;
    //上班时间(不含秒的日期)
    private String starttime;
    //下班时间(不含秒的日期)
    private String endtime;
    //扩展参数
    private String flag;

    @Override
    protected WorkDayKind clone() {
        WorkDayKind workDayKind = null;
        try {
            workDayKind = (WorkDayKind) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return workDayKind;
    }

    @Override
    public String toString() {
        return "WorkDayKind{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String getInfoName() {
        return name;
    }

    @Override
    public int getInfoNumber() {
        return number;
    }

    @Override
    public String getInfoDescribe() {
        return String.format("%s到%s", getStarttime(), getEndtime());
    }
}
