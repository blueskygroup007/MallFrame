package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:工作日类型，日，中，夜，休
 */
public class WorkDayKind extends BmobObject {
    //序号
    private Integer number;
    //名称
    private String name;
    //上班时间
    private String starttime;
    //下班时间
    private String endtime;
}
