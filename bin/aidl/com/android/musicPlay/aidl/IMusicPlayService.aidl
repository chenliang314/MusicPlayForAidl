package com.android.musicPlay.aidl;

import com.android.musicPlay.model.Music;
import com.android.musicPlay.aidl.IMusicPlayClient;

interface IMusicPlayService{

   /** 将客户端的aidl传递给service */
   void setMusicPlayClient(IMusicPlayClient client);
   
   /** 并设置播放模式：循环，单曲循环，随机循环 */
   void setMode(int mode);
   
   /** 播放歌曲  */
   void play(inout Music item,inout List<Music> data);
   
   /**跳转到指定位置开始播放   */
   void seekto(int progress);
   
   /** 暂停歌曲 播放 */
   void pause();
      
   /** 停止歌曲 播放 */
   void stop();
   
   /** 播放下一首 */
   void next();
}