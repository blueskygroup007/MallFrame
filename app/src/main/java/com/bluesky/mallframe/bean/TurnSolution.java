package com.bluesky.mallframe.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:倒班方案
 */
public class TurnSolution extends BmobObject {
    //方案名称
    private String name;
    //是否激活
    private Boolean active;
    //你是哪个班组
    private WorkGroup yourgroup;
    //一圈中，每天倒班方式
    private List<WorkDay> workdays;
    //一共有几个班，甲乙丙丁
    private List<WorkGroup> workgroups;

    //一共有几种工作日（日，中，夜，休）
    private List<WorkDayKind> workdaykinds;
    //扩展参数
    private String flags;
}
