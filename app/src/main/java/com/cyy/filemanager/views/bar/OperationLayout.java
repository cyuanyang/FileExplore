package com.cyy.filemanager.views.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cyy.filemanager.R;

/**
 * Created by study on 16/12/26.
 *
 *
 */

public class OperationLayout extends LinearLayout {

    public OperationLayout(Context context) {
        super(context);
        initView();
    }

    public OperationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OperationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_operation , this);

    }
}
