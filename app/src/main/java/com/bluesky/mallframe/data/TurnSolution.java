package com.bluesky.mallframe.data;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:倒班方案
 */
public class TurnSolution extends BmobObject {


    //方案归属于哪个用户
    private User user;
    //方案名称
    private String name;
    //是否激活
    private Boolean active;
    //你是哪个班组(在list中的序号,第几个班组)
    private Integer yourgroup;
    //一圈中，每天倒班方式
    private List<WorkDay> workdays;
    //一共有几个班，甲乙丙丁
    private List<WorkGroup> workgroups;

    //一共有几种工作日（日，中，夜，休）
    private List<WorkDayKind> workdaykinds;
    //扩展参数
    private String flags;

    @Override
    public String toString() {
        return "TurnSolution{" +
                "user=" + user +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", yourgroup=" + yourgroup +
                ", workdays=" + workdays +
                ", workgroups=" + workgroups +
                ", workdaykinds=" + workdaykinds +
                ", flags='" + flags + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getYourgroup() {
        return yourgroup;
    }

    public void setYourgroup(Integer yourgroup) {
        this.yourgroup = yourgroup;
    }

    public List<WorkDay> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(List<WorkDay> workdays) {
        this.workdays = workdays;
    }

    public List<WorkGroup> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<WorkGroup> workgroups) {
        this.workgroups = workgroups;
    }

    public List<WorkDayKind> getWorkdaykinds() {
        return workdaykinds;
    }

    public void setWorkdaykinds(List<WorkDayKind> workdaykinds) {
        this.workdaykinds = workdaykinds;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }
}
