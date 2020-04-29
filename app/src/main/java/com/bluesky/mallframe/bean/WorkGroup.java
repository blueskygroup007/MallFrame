package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:班组
 */
public class WorkGroup extends BmobObject {
    //班组名称
    private String name;
    //班组序号
    private Integer number;
    //倒班基准(日期,不含时间)
    private String basedate;
    //扩展参数
    private String flag;

    @Override
    public String toString() {
        return "WorkGroup{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", basedate=" + basedate +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getBasedate() {
        return basedate;
    }

    public void setBasedate(String basedate) {
        this.basedate = basedate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
