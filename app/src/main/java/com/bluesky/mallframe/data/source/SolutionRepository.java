package com.bluesky.mallframe.data.source;

import com.bluesky.mallframe.data.source.local.SolutionLocalDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;

/**
 * @author BlueSky
 * @date 2020/5/3
 * Description://todo (不需要）三级缓存的task仓库实现,持有"本地local","远程remote"的实例各一个,
 */
public class SolutionRepository /*implements SolutionDataSource*/ {
    public final SolutionLocalDataSource mLocalDataSource;
    public final SolutionRemoteDataSource mRemoteDataSource;

    public SolutionRepository(SolutionLocalDataSource localDataSource, SolutionRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }
}
