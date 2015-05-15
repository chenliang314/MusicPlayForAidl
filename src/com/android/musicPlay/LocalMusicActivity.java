package com.android.musicPlay;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.android.musicPlay.adapter.MusicListAdapter;
import com.android.musicPlay.common.Constant;
import com.android.musicPlay.loader.MusicLoader;
import com.android.musicPlay.model.Music;

public class LocalMusicActivity extends BaseActivity implements
        LoaderCallbacks<List<Music>> {

    private ListView mMusicList;
    private View mLoadingWheel;
    private MusicListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(Constant.LOADER_ID_LOCAL, null,
                this);
    }

    @Override
    protected void initCommonView() {
        // TODO Auto-generated method stub
        super.initCommonView();
        mTitle.setText(R.string.local_music_tab_name);
        mSettingBtn.setVisibility(View.GONE);
    }
    
    @Override
    protected void initContentView() {
        View view = View.inflate(this, R.layout.local_music_list, mExpendLayout);

        mMusicList = (ListView) view.findViewById(R.id.music_list);
        mAdapter = new MusicListAdapter(this,mController);
        mMusicList.setAdapter(mAdapter);

        mLoadingWheel = view.findViewById(R.id.loading_wheel);
    }

    @Override
    public Loader<List<Music>> onCreateLoader(int arg0, Bundle arg1) {
        return new MusicLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Music>> arg0, List<Music> arg1) {
        if (mLoadingWheel != null) {
            mLoadingWheel.setVisibility(View.GONE);
        }
        if (mMusicList != null) {
            mMusicList.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.addData(arg1);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Music>> arg0) {
        mAdapter.clearData();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(Constant.LOADER_ID_LOCAL);
    }

}
