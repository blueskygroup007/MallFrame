package com.bluesky.mallframe.data.source;

import com.bluesky.mallframe.data.TurnSolution;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/5/3
 * Description:
 */
public interface SolutionDataSource {
    /**
     * 获取所有solution的回调
     */
    interface LoadSolutionsCallback {
        void onSolutionsLoaded(List<TurnSolution> solutions);

        void onDataNotAvailable();
    }

    /**
     * 获取某个solution的回调
     */
    interface GetSolutionCallback {
        void onSolutionLoaded(TurnSolution solution);

        void onDataNotAvailable();
    }

    /**
     * 得到所有solution
     */
    void loadSolutions(LoadSolutionsCallback callback);

    /**
     * 得到某个solution
     */
    void getSolution(GetSolutionCallback callback);

    /**
     * 保存一个solution
     */
    void addSolution(TurnSolution solution);

    /**
     * 更新一个solution
     */
    void updateSolution(TurnSolution solution);

    /**
     * 激活某个solution,用id
     */
    void activeSolution(String id);

    /**
     * 废弃某个solution
     */
    void disableSolution(String id);

    /**
     * 删除所有solution
     */
    void deleteAllSolutions(List<BmobObject> solutions);

    /**
     * 删除一个solution
     */
    void deleteSolution(String id);

    /**
     * 刷新
     */
    void refreshSolutions();
}
