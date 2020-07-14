package com.bluesky.mallframe.data;

import java.io.Serializable;

/**
 * @author BlueSky
 * @date 2020/7/14
 * Description:
 */
public class UpLoadTurnSolution extends TurnSolution implements Serializable {

    //唯一比TurnSolution多的成员变量,应该也可以用flags来代替,那样的话,两个类就完全一样了.
    private String createdate = "";

    public UpLoadTurnSolution(TurnSolution solution,String date) {
        super(solution);
        createdate = date;
    }


    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
