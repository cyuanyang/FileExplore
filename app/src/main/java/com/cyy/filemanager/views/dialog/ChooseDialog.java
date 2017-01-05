package com.cyy.filemanager.views.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cyy.filemanager.R;

/**
 * Created by study on 17/1/3.
 * 选择对话框
 */

public class ChooseDialog extends BaseDialog implements View.OnClickListener{

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

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        for (int i = 0 ; i < mainLayout.getChildCount() ; i++ ) {
            View v = mainLayout.getChildAt(i);
            if (v instanceof RelativeLayout){
                v.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (Integer.parseInt((String) v.getTag())){
            case 100:
                if (mCallback!=null)mCallback.sortByName();
                break;

            case 101:
                if (mCallback!=null)mCallback.sortByTime();
                break;

            case 102:
                if (mCallback!=null)mCallback.sortByType();
                break;
        }
        dismiss();
    }

    private Callback mCallback;
    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public interface Callback{
        void sortByName();
        void sortByTime();
        void sortByType();
    }
}
