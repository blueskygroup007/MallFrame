package com.bluesky.mallframe.data.source.local;


import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.source.SolutionDataSource;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @author BlueSky
 * @date 2020/5/3
 * Description:
 */
public class SolutionLocalDataSource implements SolutionDataSource {
    @Override
    public void loadSolutions(LoadSolutionsCallback callback) {

    }

    @Override
    public void getSolution(GetSolutionCallback callback) {

    }

    @Override
    public void addSolution(TurnSolution solution) {

    }

    @Override
    public void updateSolution(TurnSolution solution) {

    }

    @Override
    public void activeSolution(String id) {

    }

    @Override
    public void disableSolution(String id) {

    }

    @Override
    public void deleteAllSolutions(List<BmobObject> solutions) {

    }


    @Override
    public void deleteSolution(String id) {

    }

    @Override
    public void refreshSolutions() {

    }
}
