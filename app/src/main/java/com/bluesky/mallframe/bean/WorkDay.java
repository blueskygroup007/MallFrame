package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:一圈中的某天,哪个班组上哪个班，例如：今天丙班上第一个白班，也就是一圈第一个班
 */
public class WorkDay extends BmobObject {
    //序号
    private Integer number;
    //哪个班
    private WorkGroup workgroup;
    //上什么班
    private WorkDayKind workdaykind;
    //扩展参数
    private String flag;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public WorkGroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(WorkGroup workgroup) {
        this.workgroup = workgroup;
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
                ", workgroup=" + workgroup +
                ", workdaykind=" + workdaykind +
                ", flag='" + flag + '\'' +
                '}';
    }
}
