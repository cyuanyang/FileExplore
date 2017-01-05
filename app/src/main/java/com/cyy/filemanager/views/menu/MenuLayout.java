package com.cyy.filemanager.views.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyy.filemanager.R;
import com.cyy.filemanager.views.dialog.ChooseDialog;

import org.w3c.dom.Text;

/**
 * Created by study on 17/1/3.
 * menu
 */

public class MenuLayout extends LinearLayout implements View.OnClickListener {

    public interface OnMenuCallback{
        void onMenuChangeHideFileState(boolean isShow);
    }

    private OnMenuCallback menuCallback;
    private ChooseDialog.Callback sortCallback;
    private LinearLayout hideFileLayout;
    private TextView hideFileTextView;
    private Boolean isShowHideFile = false;

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

        hideFileLayout = (LinearLayout) linearLayout.findViewById(R.id.hideFileLayout);
        hideFileTextView = (TextView) linearLayout.findViewById(R.id.hideFileTextView);
    }

    @Override
    public void onClick(View v) {
        switch (Integer.parseInt((String) v.getTag())){
            case 100:
                showSortDialog(sortCallback);
                break;
            case 101:
                this.isShowHideFile = !this.isShowHideFile;
                isShowHideFile();
                if (menuCallback!=null)menuCallback.onMenuChangeHideFileState(this.isShowHideFile);
                break;
        }
    }

    private void isShowHideFile(){
        Drawable drawable ;
        String text;
        if (this.isShowHideFile){
            drawable = this.getContext().getResources().getDrawable(R.drawable.ic_eye_hide);
            text = "不显示隐藏文件";
        }else {
            drawable = this.getContext().getResources().getDrawable(R.drawable.ic_eye);
            text = "显示隐藏文件";
        }
        hideFileTextView.setText(text);
        hideFileTextView.setCompoundDrawablesWithIntrinsicBounds( drawable, null , null , null);
    }

    ///设置是否显示隐藏文件
    public void setShowHideFile(Boolean showHideFile) {
        isShowHideFile = showHideFile;
        isShowHideFile();
    }

    public void setMenuSortCallback(ChooseDialog.Callback callback){
        sortCallback = callback;
    }

    /**
     * 显示排序对话框
     */
    private void showSortDialog(ChooseDialog.Callback callback){
        ChooseDialog dialog = new ChooseDialog(this.getContext());
        dialog.setmCallback(callback);
        dialog.show();
    }

    public void setMenuCallback(OnMenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }
}
