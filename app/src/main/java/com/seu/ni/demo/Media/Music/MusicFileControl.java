package com.seu.ni.demo.Media.Music;

import android.os.Environment;

import java.io.File;

/**
 * Created by ni on 2015/11/19.
 */
public class MusicFileControl {
    File[] musicFiles;
    int currentPosition = 0;

    public MusicFileControl() {
        musicFiles = getMusicFiles();
    }

    /**
     * 获取系统 Music 文件夹下音乐文件
     *
     * @return files
     */
    public File[] getMusicFiles() {

        //获取系统音乐文件目录
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        String musicFolderPath = rootPath + "/Music";
        File file = new File(musicFolderPath);
        //判断文件夹是否存在
        if (file.exists()) {
            musicFiles = file.listFiles();
            //音乐文件存在
            if (musicFiles.length != 0) {
                //设置存在 FLAG
//                FLAG_MusicExits = true;
//                //设置第一首音乐地址
//                currentMusicPosition = 0;
                return musicFiles;
            } else {
//                FLAG_MusicExits = false;
//                Toast.makeText(this, "目录下无音乐文件!", Toast.LENGTH_SHORT).show();
            }
        } else {
//            Toast.makeText(this, "音乐文件夹未找到!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    /**
     * 根据position找音乐
     *
     * @param position 在Files[] 中的position
     * @return musicFIle
     */
    public File fileAt(int position) {

        if (position < 0) {
            currentPosition = musicFiles.length - 1;
            return musicFiles[currentPosition];
        }
        if (position < musicFiles.length) {
            currentPosition = position;
            return musicFiles[position];
        } else {
            currentPosition = 0;
            return musicFiles[0];
        }
    }

    public File getPreviousFile() {
        return fileAt(--currentPosition);
    }

    public File getNextFile() {
        return fileAt(++currentPosition);
    }

    public File getCurrentFile() {
        return fileAt(currentPosition);
    }

    public File getRandomFile() {

        return fileAt((int)( Math.random() * musicFiles.length));
    }

    public String getSongName() {
        //获取歌曲名字
        String songname = getCurrentFile().getName();
        //去掉后四位
        //TODO 有待提升
        songname = songname.substring(0, songname.length() - 4);
        return songname;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
    }

}
