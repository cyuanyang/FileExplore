package com.cyy.filemanager.file;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by study on 16/12/24.
 * 文件的排序类
 */

public class SortFile {

    public static final int SORT_BY_NAME = 1; //
    public static final int SORT_BY_TYPE = 2; //

    public static List<FileModel> sort( List<FileModel> fileModels , int type){
        switch (type){
            case SORT_BY_NAME:
                Collections.sort(fileModels, new Comparator<FileModel>() {
                    @Override
                    public int compare(FileModel lhs, FileModel rhs) {
                        if (lhs.isDir && !rhs.isDir){
                            return -10;
                        }else if (!lhs.isDir && rhs.isDir){
                            return 10;
                        }
                        return lhs.file.getName().compareTo(rhs.file.getName());
                    }
                });
                break;
            case SORT_BY_TYPE:
                Collections.sort(fileModels, new Comparator<FileModel>() {
                    @Override
                    public int compare(FileModel lhs, FileModel rhs) {
                        if (lhs.isDir && !rhs.isDir){
                            return 10;
                        }else if (!lhs.isDir && rhs.isDir){
                            return -10;
                        }
                        return lhs.file.getName().compareTo(rhs.file.getName());
                    }
                });

                break;
        }


        return null;
    }
}
