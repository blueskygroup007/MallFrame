package com.bluesky.mallframe.data;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

import static com.bluesky.mallframe.base.AppConstant.REST_DAY;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:倒班方案
 */
public class TurnSolution extends BmobObject implements Serializable, Cloneable {


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

    public TurnSolution() {
        workdaykinds.add(REST_DAY);
    }

    public TurnSolution(TurnSolution solution) {
        this.user = solution.user;
        this.name = solution.name;
        this.company = solution.company;
        this.active = solution.active;
        this.yourgroup = solution.yourgroup;
        this.workdays = solution.workdays;
        this.workgroups = solution.workgroups;
        this.workdaykinds = solution.workdaykinds;
        this.flags = solution.flags;
    }

    public TurnSolution(UpLoadTurnSolution solution) {
        this.user = solution.user;
        this.name = solution.name;
        this.company = solution.company;
        this.active = solution.active;
        this.yourgroup = solution.yourgroup;
        this.workdays = solution.workdays;
        this.workgroups = solution.workgroups;
        this.workdaykinds = solution.workdaykinds;
        this.flags = solution.flags;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnSolution solution = (TurnSolution) o;
        return Objects.equal(user, solution.user) &&
                Objects.equal(name, solution.name) &&
                Objects.equal(company, solution.company) &&
                Objects.equal(active, solution.active) &&
                Objects.equal(yourgroup, solution.yourgroup) &&
                Objects.equal(workdays, solution.workdays) &&
                Objects.equal(workgroups, solution.workgroups) &&
                Objects.equal(workdaykinds, solution.workdaykinds) &&
                Objects.equal(flags, solution.flags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user, name, company, active, yourgroup, workdays, workgroups, workdaykinds, flags);
    }

    @Override
    public TurnSolution clone() {
        //User没有Clone
        TurnSolution turnSolution = null;
        try {
            turnSolution = (TurnSolution) super.clone();
            turnSolution.workdays = new ArrayList<>();
            turnSolution.workdaykinds = new ArrayList<>();
            turnSolution.workgroups = new ArrayList<>();
            for (WorkDay workDay :
                    workdays) {
                turnSolution.workdays.add(workDay.clone());
            }
            for (WorkGroup workGroup :
                    workgroups) {
                turnSolution.workgroups.add(workGroup.clone());
            }
            for (WorkDayKind workDayKind :
                    workdaykinds) {
                turnSolution.workdaykinds.add(workDayKind.clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return turnSolution;
    }

    /**
     * 获取自己所在的班组
     *
     * @return 班组名字
     */
/*    public String getDefaultWorkGroup() {
        for (WorkGroup workGroup :
                workgroups) {
            if (workGroup.getFlag().equals(WorkGroup.FLAG_DEFAULT_WORKGROUP)) {
                return workGroup.getName();
            }
        }
        return "";
    }*/

    /**
     * 获取自己所在的班组
     *
     * @return 班组序号
     */
    public int getDefaultWorkGroup() {
        for (int i = 0; i < workgroups.size(); i++) {
            if (workgroups.get(i).getFlag().equals(WorkGroup.FLAG_DEFAULT_WORKGROUP)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "TurnSolution{" +
                "user=" + user +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", active=" + active +
                ", yourgroup=" + yourgroup +
                ", workdays=" + workdays +
                ", workgroups=" + workgroups +
                ", workdaykinds=" + workdaykinds +
                ", flags='" + flags + '\'' +
                '}';
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
