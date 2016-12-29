package com.cyy.filemanager.views;

import android.content.Context;
import android.util.AttributeSet;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by study on 16/12/27.
 *
 */

public class MyFloatingActionsMenu extends FloatingActionsMenu  {
    public MyFloatingActionsMenu(Context context) {
        super(context);
        initView();
    }

    public MyFloatingActionsMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyFloatingActionsMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
    }

    public void dissmiss(){
        this.setVisibility(GONE);
    }

    public void show(){
        this.setVisibility(VISIBLE);
    }
}
