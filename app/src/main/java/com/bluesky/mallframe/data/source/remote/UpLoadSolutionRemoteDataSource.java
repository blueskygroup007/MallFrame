package com.bluesky.mallframe.data.source.remote;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.UpLoadTurnSolution;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.data.source.UpLoadSolutionDataSource;

import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author BlueSky
 * @date 2020/7/14
 * Description:
 */
public class UpLoadSolutionRemoteDataSource implements UpLoadSolutionDataSource {


    @Override
    public void loadSolutions(LoadSolutionsCallback callback) {
        BmobQuery<UpLoadTurnSolution> query = new BmobQuery<>();
        User user = BmobUser.getCurrentUser(User.class);
        query.addWhereEqualTo("user", user.getObjectId());
//      希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
//      query.include("user,post.author");
        query.findObjects(new FindListener<UpLoadTurnSolution>() {

            @Override
            public void done(List<UpLoadTurnSolution> object, BmobException e) {
                if (e == null) {
                    callback.onSolutionsLoaded(object);
                } else {
                    LogUtils.d("获取所有方案的错误信息:" + e.toString());

                    callback.onDataNotAvailable();
                }
            }
        });
    }

    @Override
    public void getSolution(GetSolutionCallback callback) {

    }

    @Override
    public void addSolution(UpLoadTurnSolution solution) {
        solution.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    try {
                        throw new Exception("上传UpLoadSolution失败.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void updateSolution(UpLoadTurnSolution solution) {
        solution.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    try {
                        throw new Exception("保存Solution失败.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void deleteAllSolutions(List<BmobObject> solutions) {
        new BmobBatch().deleteBatch(solutions).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < results.size(); i++) {
                        BatchResult result = results.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            LogUtils.d("第" + i + "个数据批量删除成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            LogUtils.d("第" + i + "个数据批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode());

                        }
                    }
                } else {
                    LogUtils.d("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void deleteSolution(UpLoadTurnSolution solution) {
//        solution.setObjectId(id);
        solution.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.d("删除成功:" + solution.getUpdatedAt());
                } else {
                    LogUtils.d("删除失败：" + e.getMessage());
                }
            }
        });


    }

    @Override
    public void deleteSolution(String id) {
        UpLoadTurnSolution solution = new UpLoadTurnSolution(new TurnSolution(), "");
        solution.setObjectId(id);
        solution.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.d("删除成功:" + solution.getUpdatedAt());
                } else {
                    LogUtils.d("删除失败：" + e.getMessage());
                }
            }
        });
    }
}
