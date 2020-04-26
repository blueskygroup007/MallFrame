package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:某个班今天是第一个班，例如：今天丙班上第一个白班，也就是一圈第一个班
 */
public class WorkDay extends BmobObject {
    //序号
    private Integer number;
    //哪个班
    private WorkGroup workgroup;
    //上什么班
    private WorkDayKind workdaykind;
}
