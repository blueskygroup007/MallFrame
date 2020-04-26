package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:班组
 */
class WorkGroup extends BmobObject {
    //班组名称
    private String name;
    //班组序号
    private Integer number;
    //倒班基准
    private BmobDate basedate;
}
