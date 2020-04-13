package com.bluesky.mallframe.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author BlueSky
 * @date 2020/4/13
 * Description:
 */
public class User extends BmobUser {
    //用户名
    private String username;
    //昵称
    private String nickname;
    //注册时间
    private BmobDate registerdate;
    //签名
    private String desc;
    //年龄
    private Integer age;
    //性别
    private Boolean gender;
    //头像
    private BmobFile avatar;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BmobDate getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(BmobDate registerdate) {
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
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", registerdate=" + registerdate +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", avatar=" + avatar +
                '}';
    }
}
