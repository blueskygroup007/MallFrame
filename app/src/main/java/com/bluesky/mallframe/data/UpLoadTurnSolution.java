package com.bluesky.mallframe.data;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/7/14
 * Description:
 */
public class UpLoadTurnSolution extends BmobObject {
    //方案归属于哪个用户
    protected User user;
    //方案名称
    protected String name;
    //是否激活
    protected String company;

    /* todo 知识点:Boolean和Integer包装类应该赋初值,否则默认初始值是null,String也该赋初值 */
    protected Boolean active = false;
    //你是哪个班组(在list中的序号,第几个班组)
    protected Integer yourgroup = 0;
    //一圈中，每天倒班方式
    protected List<WorkDay> workdays = new ArrayList<>();
    //一共有几个班，甲乙丙丁
    protected List<WorkGroup> workgroups = new ArrayList<>();

    //一共有几种工作日（日，中，夜，休）
    protected List<WorkDayKind> workdaykinds = new ArrayList<>();
    //扩展参数
    protected String flags = "";
    //唯一比TurnSolution多的成员变量,应该也可以用flags来代替,那样的话,两个类就完全一样了.
    private String createdate = "";

    public UpLoadTurnSolution(TurnSolution solution, String date) {
        setUser(solution.getUser());
        setName(solution.getName());
        setCompany(solution.getCompany());
        setActive(solution.getActive());
        setYourgroup(solution.getYourgroup());
        setWorkdays(solution.getWorkdays());
        setWorkgroups(solution.getWorkgroups());
        setWorkdaykinds(solution.getWorkdaykinds());
        setFlags(solution.getFlags());

        createdate = date;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
