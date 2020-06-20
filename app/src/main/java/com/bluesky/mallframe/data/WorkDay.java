package com.bluesky.mallframe.data;

import androidx.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.Locale;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:一圈中的某天,哪个班组上哪个班，例如：今天丙班上第一个白班，也就是一圈第一个班
 */
public class WorkDay extends BmobObject implements Cloneable, Iinformation {
    //序号
    private Integer number;
    //上什么班
    private WorkDayKind workdaykind;
    //扩展参数
    private String flag;

    @Override
    public WorkDay clone() {
        WorkDay workDay = null;
        try {
            workDay = (WorkDay) super.clone();
            workDay.setWorkdaykind(workdaykind.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return workDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDay workDay = (WorkDay) o;
        return Objects.equal(number, workDay.number) &&
                Objects.equal(workdaykind, workDay.workdaykind) &&
                Objects.equal(flag, workDay.flag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number, workdaykind, flag);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


    public WorkDayKind getWorkdaykind() {
        return workdaykind;
    }

    public void setWorkdaykind(WorkDayKind workdaykind) {
        this.workdaykind = workdaykind;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "WorkDay{" +
                "number=" + number +
                ", workdaykind=" + workdaykind +
                ", flag='" + flag + '\'' +
                '}';
    }

    @Override
    public String getInfoName() {
        return String.format(Locale.CHINA, "第%d天-%s", getNumber(), workdaykind.getInfoName());

    }

    @Override
    public int getInfoNumber() {
        return number;
    }

    @Override
    public String getInfoDescribe() {
        return workdaykind.getInfoDescribe();
    }
}
