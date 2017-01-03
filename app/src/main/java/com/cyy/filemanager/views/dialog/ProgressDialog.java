package com.cyy.filemanager.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.cyy.filemanager.R;

/**
 * Created by cyy on 16/12/5.
 *
 */

public class ProgressDialog extends BaseDialog {

    private TextView loadTextView;

    public static ProgressDialog show(Context context){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        return dialog;
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ProgressDialog(Context context) {
        this(context , R.style.CustomDialogBg_ProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///设置dialog的宽度
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.heightPixels / 5;
        setDialogWidth(width);
        setDialogHeight(width);

    }


    @Override
    public int getLayoutView() {
        return 0;
    }

}
