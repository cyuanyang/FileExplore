package com.cyy.filemanager.views.dialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by cyy on 16/12/5.
 *
 */

public abstract class BaseDialog extends Dialog{

    protected FrameLayout dialogMainLayout;

    public BaseDialog(Context context) {
        super(context);
        initView();
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化View
     */
    protected void initView(){
        dialogMainLayout = new FrameLayout(this.getContext());
        setContentView(dialogMainLayout);
        LayoutInflater.from(getContext() ).inflate(getLayoutView() , dialogMainLayout , true);
    }

    ///设置dialog的宽度
    public void setDialogWidth(int width){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = width;
    }
    ///设置dialog的宽度
    public void setDialogHeight(int height){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = height;
    }

    abstract public int getLayoutView();

}
