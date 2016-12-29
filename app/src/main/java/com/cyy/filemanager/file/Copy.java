package com.cyy.filemanager.file;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by study on 16/12/27.
 *
 */

public class Copy {

    public interface CopyCallback{
        void duplicate(Copy copy , File desDir , FileModel sourceFile); //复制的时候 文件又重复
        void complete(); //完全完成复制
        void dirPasteError(Copy copy , File desDir);
    }

    private List<FileModel> copyFileTemp = null; ///复制剪切是缓存

    private CopyCallback callback;///文件操作的回调

    private boolean isCopy;//true 代表表示粘贴 false剪切

    public Copy(boolean isCopy){
        this.isCopy = isCopy;
    }

    public void setCallback(CopyCallback callback) {
        this.callback = callback;
    }

    /**
     * 初始化复制的内容
     * @param fileModels 这里面需要有选择的数据
     */
    public void initCopy(List<FileModel> fileModels ){
        if (copyFileTemp ==null)copyFileTemp = new ArrayList<>(5);
        for (FileModel fileModel : fileModels) {
            if (fileModel.select){
                copyFileTemp.add(fileModel);
            }
        }
    }

//    /**
//     * 移动文件到指定目录
//     * @param desDir
//     */
//    public void moveFile(File desDir){
//        if (desDir == null){
//            return;
//        }
//        if (copyFileTemp == null){
//            return;
//        }
//        mDesDir = desDir;
//        Iterator<FileModel> iterator = copyFileTemp.iterator();
//        if (iterator.hasNext()){
//            FileModel fileModel = iterator.next();
//            copyFileTemp.remove(fileModel);
//            if (fileModel.isDir){
//                if (isCanPaste(desDir , fileModel.file)){
//                    String cmd = "mv " + fileModel.file.getAbsolutePath() + " " + desDir.getAbsolutePath()+ fileModel.file.getName();
//                    try {
//                        Process process = Runtime.getRuntime().exec(cmd);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    if (callback==null)callback.dirPasteError(this , desDir);
//                }
//            }else {
//                //重新创建新的文件名字
//                String[] nameSuffix = new FileType().getFileName(fileModel.file);
//                String newFilePath = desDir.getAbsolutePath()+File.separator+nameSuffix[0]+nameSuffix[1];
//                while (new File(newFilePath).exists()){
//                    nameSuffix = new FileType().getFileName(new File(newFilePath));
//                    newFilePath = desDir.getAbsolutePath()+File.separator+createNewFileName(nameSuffix[0])+nameSuffix[1];
//                }
//                try {
//                    String cmd = "mv " + fileModel.file.getAbsolutePath() + " " + newFilePath;
//                    Process process = Runtime.getRuntime().exec(cmd);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                moveFile(desDir);
//            }
//
//        }else {
//            copyFileTemp = null;
//            mDesDir = null;
//            if (callback!=null){
//                callback.complete();
//            }
//        }
//
//    }

    /**
     * 该文件是否可以复制 一般因为不可以将自己复制到自己的子目录
     * @param desDir
     * @param source
     */
    private boolean isCanPaste(File desDir , File source){
        File parentFile = desDir;

        while (parentFile!=null){
            if (TextUtils.equals(parentFile.getAbsolutePath() , source.getAbsolutePath())){
                return false;
            }
            parentFile = parentFile.getParentFile();
        }
        return true;
    }

    private File mDesDir;
    /**
     * 调用前先调用 initCopy();
     */
    public void paste(File desDir){
        if (desDir==null){
            return;
        }
        if (copyFileTemp==null){
            return;
        }
        mDesDir = desDir;
        Iterator<FileModel> iterator = copyFileTemp.iterator();
        if (iterator.hasNext()){
            FileModel fileModel = iterator.next();
            copyFileTemp.remove(fileModel);

            String fileName = fileModel.file.getName();
            String newFilePath = mDesDir.getAbsolutePath()+File.separator+fileName;
            if (fileModel.isDir){
                //判断文件是否嵌套复制
                if (isCanPaste(desDir , fileModel.file)){
                    if (new File(newFilePath).exists()){
                        ///目标目录中有两个相同的名字的文件 ///需要询问用户如何操作
                        Log.e("Copy" ,"复制的文件夹已经存在");
                        if (callback!=null)callback.duplicate(this , mDesDir , fileModel );
                    }else {
                        //没有直接复制
                        if (isCopy){
                            copyDirectiory(fileModel.file.getAbsolutePath() , newFilePath);
                        }else {
                            moveFile(fileModel.file.getAbsolutePath() , newFilePath);
                        }
                        paste(mDesDir);
                    }
                }else{
                    if (callback!=null)callback.dirPasteError(this , desDir);
                }
            }else {
                if (new File(newFilePath).exists()){
                    ///存在文件的名字一样的
                    if (callback!=null)callback.duplicate(this , mDesDir ,fileModel);
                }else {
                    if (isCopy){
                        copyFile(fileModel.file , new File(newFilePath));
                    }else {
                        moveFile(fileModel.file.getAbsolutePath() , newFilePath);
                    }
                    //继续下一个复制
                    paste(mDesDir);
                }
            }
        }else {
            mDesDir = null;
            copyFileTemp = null;
            if (callback!=null){
                ///全部完成
                callback.complete();
            }
        }
    }

    /**
     *  创建新文件的名字
     * @param oldFileName lod文件的名字
     * @return 一个全新的文件名字 加了个后缀_1 .... _2 _3
     */
    private String createNewFileName(String oldFileName){
        if (oldFileName.lastIndexOf("_")!=-1){
            String indexStr = oldFileName.substring( oldFileName.lastIndexOf("_")+1 ,oldFileName.length());
            try {
                int i = Integer.parseInt(indexStr)+1;
                oldFileName = oldFileName.substring(0 , oldFileName.lastIndexOf("_")+1)+i;
            }catch (NumberFormatException e){
                oldFileName += "_1";
            }
        }else {
            oldFileName += "_1";
        }

        return oldFileName;
    }

    /**
     * 粘贴文件目录的时候 保持两个同时存在
     * @param desDir "目标文件"
     * @param fileModel 原文件
     */
    public void keepSameDesDir(File desDir , FileModel fileModel){
        String fileName = fileModel.file.getName();
        if (fileModel.isDir){ //未文件夹
            /// 新的文件路径
            String newFilePath = desDir.getAbsolutePath()+File.separator+createNewFileName(fileName);
            while (new File(newFilePath).exists()){
                fileName = createNewFileName(fileName);
                newFilePath = desDir.getAbsolutePath()+File.separator+createNewFileName(fileName);
            }
            if (isCopy){
                copyDirectiory(fileModel.file.getAbsolutePath() , newFilePath);
            }else {
                moveFile(fileModel.file.getAbsolutePath()  , newFilePath);
            }

        }else {
            String[] nameSuffix = new FileType().getFileName(fileModel.file);
            String newFilePath = desDir.getAbsolutePath()+File.separator+createNewFileName(nameSuffix[0])+nameSuffix[1];
            while (new File(newFilePath).exists()){
                nameSuffix = new FileType().getFileName(new File(newFilePath));
                newFilePath = desDir.getAbsolutePath()+File.separator+createNewFileName(nameSuffix[0])+nameSuffix[1];
            }
            if (isCopy){
                copyFile(fileModel.file , new File(newFilePath));
            }else {
                moveFile(fileModel.file.getAbsolutePath() , newFilePath);
            }

        }

        ///继续复制
        paste(desDir);
    }

    /**
     * 复制是会覆盖原有的
     * @param desDir  目标文件
     * @param fileModel 原文件
     */
    public void coverCopy(File desDir , FileModel fileModel){
        ///新文件的名字
        String newFilePath = desDir.getAbsolutePath()+File.separator+fileModel.file.getName();
        ///路径是否一样的
        if (TextUtils.equals(newFilePath , fileModel.file.getAbsolutePath())){
            return;
        }
        if (fileModel.isDir){
            if (isCopy){
                copyDirectiory(fileModel.file.getAbsolutePath() , newFilePath);
            }else {
                moveFile(fileModel.file.getAbsolutePath() , newFilePath);
            }

        }else {
            if (isCopy){
                copyFile(fileModel.file , new File(newFilePath));
            }else {
                moveFile(fileModel.file.getAbsolutePath() , newFilePath);
            }
        }
    }

    ///移动文件
    private void moveFile(String sourcePath , String desPath){
        try {
            String cmd = "mv " + sourcePath + " " + desPath;
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            InputStream is = process.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile,File targetFile) {

        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff= null;

        try {
            // 新建文件输入流并对它进行缓冲
            input = new FileInputStream(sourceFile);
            inBuff=new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(targetFile);
            outBuff=new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len =inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                if (inBuff!=null)inBuff.close();
                if (outBuff!=null)outBuff.close();
                if (output!=null)output.close();
                if (input!=null)input.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile=file[i];
                // 目标文件
                File targetFile=new
                        File(new File(targetDir).getAbsolutePath()
                        +File.separator+file[i].getName());
                copyFile(sourceFile,targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1=sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2=targetDir + "/"+ file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 取消缓存
     */
    public void release(){
        if (copyFileTemp==null||copyFileTemp.size()==0){
            copyFileTemp = null;
            return;
        }
        copyFileTemp.clear();
        callback = null;
    }

}
