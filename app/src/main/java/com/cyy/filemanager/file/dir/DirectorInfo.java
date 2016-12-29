package com.cyy.filemanager.file.dir;

import java.util.List;

/**
 * Created by study on 16/12/28.
 * 目录信息
 */

public class DirectorInfo<T> {

    public String dir; ///当前目录
    public List<T> files;///当前目录的所有文件

    private State state;

    public DirectorInfo(String dir , List<T> files){
        this.dir = dir;
        this.files = files;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return this.state;
    }

    public static class State{
        public int position;
    }

}
