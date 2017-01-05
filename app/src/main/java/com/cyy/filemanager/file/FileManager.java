package com.cyy.filemanager.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cyy.filemanager.file.dir.DirectorInfo;
import com.cyy.filemanager.file.dir.DirectoryStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by study on 16/12/20.
 *
 */

public class FileManager {

    public static final File ROOT_DIR = Environment.getExternalStorageDirectory();

    private Context context;

    private File currentDir; ///当前所在目录

    private Copy copy;
    private DirectoryStack<DirectorInfo<FileModel>> stack = new DirectoryStack<DirectorInfo<FileModel>>(); //目录栈
    private boolean hideFileIsShow = false; //隐藏文件是否显示
    private int sortType  = SortFile.SORT_BY_NAME;//文件排序类型

    public FileManager(Context c){
        this.context = c;
        currentDir = ROOT_DIR;
    }

    ///刷新当前文件夹信息
    public List<FileModel> refreshCurrentDirectoryInfo(){
        File[] files = currentDir.listFiles();
        List<FileModel> result = new ArrayList<>(20);
        if (files!=null){
            for (File file : files) {
                FileModel mo = new FileModel();
                if (!hideFileIsShow){
                    if (file.getName().startsWith(".")){
                        continue;
                    }
                }
                mo.name = file.getName();
                mo.isDir = file.isDirectory();
                mo.file = file;
                result.add(mo);
            }
        }

        this.sortFileModel(result , sortType);

        return result;
    }
    ///当push到下一个文件夹的时候记住当前的状态
    ///在pushDir()之前调用
    public void saveState(DirectorInfo.State state){
        stack.getFirst().setState(state);
    }
    ///返回当前文件夹在链表中的保存状态
    public DirectorInfo.State getCurrentState(){
        return stack.getFirst().getState();
    }

    ///进入某一个文件夹
    public List<FileModel> pushDir(File dir){

        currentDir = dir;

        File[] files = currentDir.listFiles();
        List<FileModel> result = new ArrayList<>(20);
        if (files!=null){
            for (File file : files) {
                FileModel mo = new FileModel();
                if (!hideFileIsShow){
                    if (file.getName().startsWith(".")){
                        continue;
                    }
                }
                mo.name = file.getName();
                mo.isDir = file.isDirectory();
                mo.file = file;
                result.add(mo);
            }
        }

        this.sortFileModel(result , sortType);

        stack.push(new DirectorInfo<FileModel>(currentDir.getAbsolutePath() , result));
        return result;
    }
    ///返某一个到文件夹
    public List<FileModel> popDir(){
        currentDir = currentDir.getParentFile();
        if (currentDir!=null){
            stack.pop();
            DirectorInfo<FileModel> directorInfo = stack.getFirst();
            return directorInfo.files;
        }
        return Collections.emptyList();
    }

    public void cancleCopy(){
        if (copy!=null){
            copy.release();
            copy = null;
        }
    }

    public void initCopy(List<FileModel> fileModels , Copy.CopyCallback copyCallback , boolean isPaste){
        copy = new Copy(isPaste);
        copy.initCopy(fileModels);
        copy.setCallback(copyCallback);
    }

    public void paste(File desDir){
        if (copy == null){
            Log.e("FileManager" ,"先调用initCopy()");
            return;
        }
        copy.paste(desDir);

    }

    public void deleteFile(List<FileModel> fileModels , Delete.Callback callback){
        new Delete(fileModels , callback).delete();
    }

    ///对文件进行排序
    public List<FileModel> sortFileModel(List<FileModel> fileModels , int sortType){
        this.sortType = sortType;
        return SortFile.sort(fileModels , sortType);
    }

    ///返回当前文件排序类型
    public int getSortType(){
        return this.sortType;
    }

    ///是否显示隐藏文件
    public void isShowHideFile(boolean isShow){
        this.hideFileIsShow = isShow;
    }

    ///返回当前的所在的文件夹
    public File getCurrentDirFile(){
        return currentDir;
    }

    public void openFile(File file){
        new FileType().openFile(file , context);
    }
}
