package com.cyy.filemanager.views.dialog;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.cyy.filemanager.R;


/**
 * Created cyy de on 2016/7/22.
 * 普通提示 提示dialog
 * <p/>
 * <p/>
 * new AlertDialog.Builder(this.getContext())
 * .setTitle("提示")
 * .setMessage("我好开心啊")
 * .setLeftBtn("加油")
 * .setRightBtn("你没")
 * .setHeaderImageReceroucr(R.drawable.bg_dialog_rocket)
 * .build().show();
 * <p/>
 * 不设置其中一个btn只显示其中一个btn
 */
public class AlertDialog extends BaseDialog implements View.OnClickListener {

    private OnRightClickListener rightClickListener;
    protected OnLeftClickListener leftClickListener;
    private OnCancelClickListener onCancelClickListener;

    private TextView titleView;
    private TextView msgView;
    private Button rightBtn ,leftBtn , cancelBtn;

    public interface OnRightClickListener {
        void onRightClick(Dialog dialog, View v);
    }

    public interface OnLeftClickListener {
        void onLeftClick(Dialog dialog, View v);
    }
    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog, View v);
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AlertDialog(Context context) {
        this(context, R.style.CustomDialogBg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
    }

    @Override
    public int getLayoutView() {
        return R.layout.alert_dialog_layout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_left) {
            if (leftClickListener != null) leftClickListener.onLeftClick(this , view);
        } else if (view.getId() == R.id.btn_right) {
            if (rightClickListener != null) rightClickListener.onRightClick(this , view);
        }else if (view.getId() == R.id.btn_cancel){
            if (onCancelClickListener!=null) onCancelClickListener.onCancelClick(this , view);
        }
        dismiss();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void initView() {
        super.initView();
        titleView = (TextView) findViewById(R.id.dialog_title);
        msgView = (TextView) findViewById(R.id.dialog_msg);

        rightBtn = (Button) findViewById(R.id.btn_right);
        leftBtn = (Button) findViewById(R.id.btn_left);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        rightBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public void setMyTitle(String title) {
        if (title!=null)
            titleView.setText(title);
    }

    public void setMyMessage(String message) {
        if (message!=null){
            msgView.setText(message);
        }

    }

    public void setBtnLeft(String left) {
        if (left!=null)
            leftBtn.setText(left);
    }

    public void setBtnRight(String right) {
        if (right!=null){
            rightBtn.setText(right);
        }
    }

    public void setRightClickListener(OnRightClickListener listener){
        this.rightClickListener = listener;
    }

    public void setLeftClickListener(OnLeftClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setOnCancelListener(OnCancelClickListener listener) {
        this.onCancelClickListener = listener;
    }

    /**
     * 建造者
     */
    public static class Builder {
        private String title;
        private String message;
        private String leftBtn;
        private String rightBtn;
        private OnRightClickListener rightClickListener;
        private OnLeftClickListener leftClickListener;
        private OnCancelClickListener onCancelClickListener;

        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String m) {
            message = m;
            return this;
        }

        public Builder setLeftBtn(String left) {
            leftBtn = left;
            return this;
        }

        public Builder setRightBtn(String right) {
            rightBtn = right;
            return this;
        }

        public Builder setRightClickListener(OnRightClickListener listener) {
            rightClickListener = listener;
            return this;
        }

        public Builder setLeftClickListener(OnLeftClickListener listener) {
            leftClickListener = listener;
            return this;
        }

        public Builder setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
            this.onCancelClickListener = onCancelClickListener;
            return this;
        }

        public AlertDialog build() {
            AlertDialog dialog = new AlertDialog(context);
            dialog.setMyTitle(title);
            dialog.setMyMessage(message);
            dialog.setBtnLeft(leftBtn);
            dialog.setBtnRight(rightBtn);
            dialog.setRightClickListener(rightClickListener);
            dialog.setLeftClickListener(leftClickListener);
            dialog.setOnCancelListener(onCancelClickListener);
            return dialog;
        }
    }
}
