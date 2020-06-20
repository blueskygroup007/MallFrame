package com.bluesky.mallframe.data;

import com.google.common.base.Objects;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:工作日类型，日，中，夜，休
 */
public class WorkDayKind extends BmobObject implements Cloneable, Iinformation, Serializable {
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
    public WorkDayKind clone() {
        WorkDayKind workDayKind = null;
        try {
            workDayKind = (WorkDayKind) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return workDayKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDayKind that = (WorkDayKind) o;
        return Objects.equal(number, that.number) &&
                Objects.equal(name, that.name) &&
                Objects.equal(starttime, that.starttime) &&
                Objects.equal(endtime, that.endtime) &&
                Objects.equal(flag, that.flag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number, name, starttime, endtime, flag);
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
