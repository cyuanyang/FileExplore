package com.cyy.filemanager.views.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cyy.filemanager.R;
import com.cyy.filemanager.views.dialog.ChooseDialog;

/**
 * Created by study on 17/1/3.
 * menu
 */

public class MenuLayout extends LinearLayout implements View.OnClickListener {

    public interface OnMenuCallback{
        void onSortAction();
    }

    private OnMenuCallback menuCallback;
    public MenuLayout(Context context) {
        super(context);
        initView();
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setBackgroundColor(getResources().getColor(R.color.mainColor));
        setOrientation(LinearLayout.VERTICAL);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.layout_menu , this);

        for (int i = 0 ; i<linearLayout.getChildCount() ; i++){
            View view = linearLayout.getChildAt(i);
            if (view instanceof LinearLayout){
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (Integer.parseInt((String) v.getTag())){
            case 100:
                if (menuCallback!=null)menuCallback.onSortAction();
                showSortDialog();
                break;
        }
    }

    /**
     * 显示排序对话框
     */
    private void showSortDialog(){
        ChooseDialog dialog = new ChooseDialog(this.getContext());
        dialog.show();
    }

    public void setMenuCallback(OnMenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }
}
