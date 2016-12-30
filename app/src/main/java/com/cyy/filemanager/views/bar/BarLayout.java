package com.cyy.filemanager.views.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyy.filemanager.R;

/**
 * Created by study on 16/12/26.
 *
 */

public class BarLayout extends RelativeLayout implements View.OnClickListener {

    public interface MenuListener{
        void copyAction();
        void cutAction();
        void moreAction();
        void deleteAction();
        void menuAction();
    }

    private ImageView mMenuView;
    private TextView titleView;
    private OperationLayout operationLayout;//操作布局

    private MenuListener menuListener;///监听菜单的点击事件

    public BarLayout(Context context) {
        super(context);
        initView();
    }

    public BarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expanded_menu){
            if (menuListener!=null)menuListener.menuAction();
        }else {
            try {
                int tag = v.getTag()==null ? 0 : Integer.parseInt(v.getTag().toString());
                switch (tag){
                    case 1: ///复制
                        if (menuListener!=null)menuListener.copyAction();
                        break;
                    case 2: //cut
                        if (menuListener!=null)menuListener.cutAction();
                        break;
                    case 3:
                        if (menuListener!=null)menuListener.deleteAction();
                        break;
                    case 4: //del
                        if (menuListener!=null)menuListener.moreAction();
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void initView(){
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_bar , this);
        operationLayout = (OperationLayout) findViewById(R.id.operationLayout);
        titleView = (TextView) findViewById(R.id.bar_title);
        mMenuView = (ImageView) findViewById(R.id.expanded_menu);
        operationLayout.setVisibility(GONE);

        for (int i = 0 ; i< operationLayout.getChildCount() ; i++){
            operationLayout.getChildAt(i).setOnClickListener(this);
        }

        mMenuView.setOnClickListener(this);
    }

    public void setMenuListenert(MenuListener l){
        menuListener = l;
    }

    public void isOperate(boolean operation){
        if (operation){
            operationLayout.setVisibility(VISIBLE);
        }else {
            operationLayout.setVisibility(INVISIBLE);
        }
    }

    public void setText(String title){
        titleView.setText(title);
    }


}
