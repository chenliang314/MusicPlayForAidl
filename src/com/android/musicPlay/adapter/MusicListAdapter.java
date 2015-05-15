package com.android.musicPlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.musicPlay.common.MusicPlayController;
import com.android.musicPlay.model.Music;
import com.android.musicPlay.R;

public class MusicListAdapter extends BaseAdapter implements OnClickListener {
    
    private MusicPlayController mController;
    private ArrayList<Music> mData = new ArrayList<Music>();
    private Context mCtxt;

    public MusicListAdapter(Context context,MusicPlayController controller) {
        mCtxt = context;
        mController = controller;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void addData(List<Music> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public Music getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mCtxt, R.layout.music_item, null);
            holder.mDisplayName = (TextView) view.findViewById(R.id.music_name);
            holder.mDisplayName.setOnClickListener(this);
            holder.mMusicIndex = (TextView) view.findViewById(R.id.music_index);
            holder.mCutMusicBtn = (TextView) view.findViewById(R.id.music_cut);
            holder.mCutMusicBtn.setOnClickListener(this);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
       
        holder.mDisplayName.setText(getItem(position).mDisplayName);
        holder.mDisplayName.setTag(getItem(position));
        holder.mMusicIndex.setText(String.valueOf(position + 1));
        return view;
    }

    static class ViewHolder {
        TextView mCutMusicBtn;
        TextView mDisplayName;
        TextView mMusicIndex;
    }

    @Override
    public void onClick(View v) {
      if (v.getId() == R.id.music_cut) {
          Toast.makeText(mCtxt, "剪辑铃声", Toast.LENGTH_LONG).show();
          return;
      }
      if (v.getId() == R.id.music_name) {
          Music music = (Music) v.getTag();
          mController.play(music, mData);
          return;
      }
    }
}
