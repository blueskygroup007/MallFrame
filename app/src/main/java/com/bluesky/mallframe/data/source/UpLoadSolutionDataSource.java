package com.bluesky.mallframe.data.source;

import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.UpLoadTurnSolution;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/7/14
 * Description:
 */
public interface UpLoadSolutionDataSource {

    /**
     * 获取所有solution的回调
     */
    interface LoadSolutionsCallback {
        void onSolutionsLoaded(List<UpLoadTurnSolution> solutions);

        void onDataNotAvailable();
    }

    /**
     * 获取某个solution的回调
     */
    interface GetSolutionCallback {
        void onSolutionLoaded(UpLoadTurnSolution solution);

        void onDataNotAvailable();
    }

    /**
     * 得到所有solution
     */
    void loadSolutions(UpLoadSolutionDataSource.LoadSolutionsCallback callback);

    /**
     * 得到某个solution
     */
    void getSolution(UpLoadSolutionDataSource.GetSolutionCallback callback);

    /**
     * 保存一个solution
     */
    void addSolution(UpLoadTurnSolution solution);

    /**
     * 更新一个solution
     */
    void updateSolution(UpLoadTurnSolution solution);



    /**
     * 删除所有solution
     */
    void deleteAllSolutions(List<BmobObject> solutions);

    /**
     * 删除一个solution
     */
    void deleteSolution(UpLoadTurnSolution solution);

}
