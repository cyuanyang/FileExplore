package com.cyy.filemanager.views.dialog;

import android.content.Context;
import android.widget.ListView;

import com.cyy.filemanager.R;

/**
 * Created by study on 17/1/3.
 * 选择对话框
 */

public class ChooseDialog extends BaseDialog {

    private final static int TYPE_MULTIPLE_CHOOSE = 1 ; //多选
    private final static int TYPE_ONE_CHOOSE = 0 ; //单选

    private int style = TYPE_ONE_CHOOSE;

    private ListView mListView;

    public ChooseDialog(Context context) {
        this(context, R.style.CustomDialogBg);

    }

    public ChooseDialog(Context context , int styleId){
        super(context, styleId);
    }

    @Override
    public int getLayoutView() {
        return R.layout.layout_dialog_choose;
    }

    @Override
    protected void initView() {
        super.initView();

    }
}
