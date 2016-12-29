package com.cyy.filemanager.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by study on 16/12/28.
 *
 */

public class Delete {

    public interface Callback{
        void success();
    }
    private Callback mCallback;
    private List<FileModel> temp ;
    public Delete(List<FileModel> fileModels, Callback callback){
        mCallback = callback;
        initTempDatas(fileModels);
    }

    private void initTempDatas(List<FileModel> fileModels){
        if (fileModels==null || fileModels.size()<=0){
            return;
        }
        if (temp == null)temp = new ArrayList<>(5);
        for (FileModel fileMode : fileModels) {
            if (fileMode.select){
                temp.add(fileMode);
            }
        }

    }

    /// delete
    public void delete(){
        if (temp.size()>0){
            FileModel model = temp.remove(0);
            doDel(model.isDir , model.file.getAbsolutePath());
            delete();
        }else {
            temp.clear();
            temp = null;

            if (mCallback!=null){
                mCallback.success();
            }
        }
    }

    private void doDel(boolean isDir , String path){
        try {
            String cmd;
            if (isDir){
                cmd = "rm -rf " + path;
            }else {
                cmd = "rm -f " + path;
            }
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
