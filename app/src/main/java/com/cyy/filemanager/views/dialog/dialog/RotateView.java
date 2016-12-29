//package com.cyy.filemanager.views.dialog.dialog;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//
//import com.entstudy.moreteacherpad.R;
//
///**
// * Created by cyy on 16/12/5.
// * 旋转的菊花图片
// */
//
//public class RotateView extends ImageView {
//
//    private int parter = 12 ;// 菊花被分成了多少份
//
//    private float preAngle = 0 ;
//    private boolean attcahed = false;
//    private int duration=200;
//
//    public RotateView(Context context) {
//        super(context);
//        init(null);
//    }
//
//    public RotateView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//
//    public RotateView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(attrs);
//    }
//
//    private void init(AttributeSet attrs){
//        if (attrs!=null){
//            TypedArray a = this.getContext().obtainStyledAttributes(attrs , R.styleable.RotateView);
//            duration = a.getInt(R.styleable.RotateView_duration , duration);
//            a.recycle();
//        }
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        attcahed = false;
//        this.clearAnimation();
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        attcahed = true;
//        start();
//    }
//
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (visibility==VISIBLE){
//            attcahed = true;
//            start();
//        }else {
//            preAngle = 0;
//            attcahed = false;
//            clearAnimation();
//        }
//    }
//
//    private void start(){
//        this.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                float toDegrees = 360/parter + preAngle;
//                RotateAnimation animation = new RotateAnimation(preAngle , toDegrees , Animation.RELATIVE_TO_SELF ,  0.5f , Animation.RELATIVE_TO_SELF ,0.5f);
//                animation.setDuration(1);
//                animation.setFillAfter(true);
//                RotateView.this.startAnimation(animation);
//                preAngle = toDegrees%360 == 0 ? 0f : toDegrees;
//                if (attcahed){
//                    RotateView.this.postDelayed(this , duration);
//                }
//            }
//        },0);
//    }
//
//}
