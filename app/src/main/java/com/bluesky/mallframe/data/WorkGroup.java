package com.bluesky.mallframe.data;

import com.google.common.base.Objects;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/4/26
 * Description:班组
 */
public class WorkGroup extends BmobObject implements Cloneable,Iinformation {
    //班组名称
    private String name = "";
    //班组序号
    private Integer number=0;
    //倒班基准(日期,不含时间)
    private String basedate = "";
    //扩展参数
    private String flag = "";

    public static final String FLAG_DEFAULT_WORKGROUP = "DEFAULT";

    /*
     * todo 知识点:深拷贝方法之一:重写clone
     *  具体:实现Cloneable接口,重写clone方法,try catch异常
     *  注意一:如果全部是基本类型,那么只需要调用super的clone方法.
     *  注意二:如果还有对象类型,就必须继续重写该对象的clone.
     *  使用:显式调用WorkGroup.clone(),而不是List.Addall()
     * */
    @Override
    public WorkGroup clone() {
        WorkGroup workGroup = null;
        try {
            workGroup = (WorkGroup) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return workGroup;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkGroup workGroup = (WorkGroup) o;
        return Objects.equal(name, workGroup.name) &&
                Objects.equal(number, workGroup.number) &&
                Objects.equal(basedate, workGroup.basedate) &&
                Objects.equal(flag, workGroup.flag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, number, basedate, flag);
    }

    @Override
    public String getInfoName() {
        return name;
    }

    @Override
    public int getInfoNumber() {
        return number;
    }

    @Override
    public String getInfoDescribe() {
        return basedate;
    }

    //todo 知识点:查看WorkGroup类的Equals()方法,了解Equals和HashCode的区别与不同
    /*
     * 1,hashcode效率高,但不绝对正确.
     * 2,hashcode用于hashmap和hashset,用于这两个类时,必须重写两个方法.
     * 3,先用hashcode比较,相同再用equals比较,效率最佳.也绝对正确.
     *
     * 引申知识点:如何重写equals和hashcode方法.
     * 1,如果不重写hashcode,就会默认继承object的hashcode,
     * */

    /*hashcode例子写法*/
/*    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(number);
        sb.append(name);
        sb.append(basedate);
        sb.append(flag);
        *//*将所有需要参与计算的属性值都合并成一个字符串，然后转换成一个字符数组*//*
        char[] charArr = sb.toString().toCharArray();
        *//*然后遍历这个字符数组进行计算*//*
        int hash = 0;

        for (char c : charArr) {
            hash = hash * 131 + c;
        }
        return hash;
    }*/
}
