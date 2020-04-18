package com.bluesky.mallframe.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author BlueSky
 * @date 2020/4/13
 * Description:
 */
public class User extends BmobUser {
    //昵称
    private String nickname;
    //注册时间
    private String registerdate;
    //签名
    private String desc;
    //年龄
    private Integer age;
    //性别
    private Boolean gender;
    //头像
    private BmobFile avatar;

    public User() {
        this.nickname = "未命名";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        this.registerdate = simpleDateFormat.format(date);
        this.desc = "这个人很懒!什么也没留下!";
        this.age = 0;
        this.gender = true;
        this.avatar = null;
    }

    public User(String nickname, String registerdate, String desc, Integer age, Boolean gender, BmobFile avatar) {
        this.nickname = nickname;
        this.registerdate = registerdate;
        this.desc = desc;
        this.age = age;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                ", nickname='" + nickname + '\'' +
                ", registerdate=" + registerdate +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", avatar=" + avatar +
                '}';
    }
}
