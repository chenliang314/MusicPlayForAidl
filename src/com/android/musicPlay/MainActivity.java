package com.android.musicPlay;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    /**
     * 搜索按钮
     **/
    protected Button mSearchBtn;
    /**
     * 跳转本地歌曲界面
     **/
    protected Button mLocalMusic;
    /**
     * 跳转视频界面
     **/
    protected Button mLocalVideo;

    @Override
    protected void initContentView() {
        View view = View.inflate(this, R.layout.main_activity, mExpendLayout);
        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);

        mLocalMusic = (Button) view.findViewById(R.id.local_music);
        mLocalMusic.setOnClickListener(this);

        mLocalVideo = (Button) view.findViewById(R.id.local_video);
        mLocalVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.search_btn) {

            return;
        }
        if (v.getId() == R.id.local_music) {
            Intent intent = new Intent(this, LocalMusicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        if (v.getId() == R.id.local_video) {
            Toast.makeText(this, "本地视频", Toast.LENGTH_LONG).show();
            return;
        }
    }

}
