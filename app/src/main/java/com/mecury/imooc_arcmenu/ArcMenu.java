package com.mecury.imooc_arcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by 海飞 on 2016/7/7.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final int POST_LEFT_TOP = 0;
    private static final int POST_LEFT_BOTTOM = 1;
    private static final int POST_RIGHT_TOP = 2;
    private static final int POST_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.LEFT_BOTTOM;
    private int mRadius;
    /*
     * 菜单的状态
     */
    private Status mCurrentStatus = Status.CLOSE;

    //菜单的主按钮
    private View mCButton;

    private OnMenuItemClickListener mMenuItemClickListener;

    //判断主按钮的状态
    public enum Status{ CLOSE, OPEN }

    /*
     * 菜单的位置
     */
    public enum Position{ LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM}

    //点击的回调接口
    public interface OnMenuItemClickListener{
        void onClick(View view, int position);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener){
        this.mMenuItemClickListener = mMenuItemClickListener;
    }
    //不确定：构造器的作用是获得xml文件设置的自定义的属性
    public ArcMenu(Context context) {
        this(context,null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100
                , getResources().getDisplayMetrics());

        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu
                , defStyleAttr, 0);

        int pos = a.getInt(R.styleable.ArcMenu_position,POST_RIGHT_BOTTOM);

        switch (pos){
            case POST_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POST_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POST_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POST_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }

        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));

        Log.e("TAG", "position = " + mPosition + ",radiusn = " + mRadius);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();

        for (int i = 0;i < count; i++){

            //测量Child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed){

            layoutCButton();

            int count = getChildCount();

            for (int i=0;i<count-1;i++){
                View child = getChildAt(i + 1);

                child.setVisibility(View.GONE);

                //根据半径计算x，y轴距离
                int cl = (int) (mRadius * Math.sin(Math.PI/2/(count-2)*i));
                int ct = (int) (mRadius * Math.cos(Math.PI/2/(count-2)*i));

                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();


                if (mPosition == Position.LEFT_BOTTOM){
                    ct = getMeasuredHeight() - cHeight - ct;
                }else if (mPosition == Position.RIGHT_TOP){
                    cl = getMeasuredWidth() - cWidth - cl;
                }else if (mPosition == Position.RIGHT_BOTTOM){
                    ct = getMeasuredHeight() - cHeight - ct;
                    cl = getMeasuredWidth() - cWidth - cl;
                }

                child.layout(cl, ct, cl+cWidth, ct+cHeight);
            }
        }
    }

    private void layoutCButton(){

        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);

        int l = 0;
        int t = 0;

        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        switch (mPosition){
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }

        mCButton.layout(l, t, l+width, t+height)    ;
    }


    @Override
    public void onClick(View v) {

        rotateCButton(mCButton, 0f, 180f, 300);

        toggleMenu(300);

        Log.e("TAG", mCurrentStatus+"");
    }

    /*
     *切换菜单
     */
    public  void toggleMenu(int duration) {

        int count = getChildCount();

        for (int i = 0; i < count-1; i++){

            final View childView = getChildAt(i+1);

            //显示图标
            childView.setVisibility(View.VISIBLE);

            //end(0, 0)
            int cl = (int) (mRadius * Math.sin(Math.PI/2/(count-2)*i));
            int ct = (int) (mRadius * Math.cos(Math.PI/2/(count-2)*i));

            int xFlag = 1;
            int yFlag = 2;

            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM){
                xFlag = -1;
            }

            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP){
                yFlag = -1;
            }

            AnimationSet animSet = new AnimationSet(true);
            Animation tranAnim = null;

            //to open,以结束的位置为坐标原点
            if (mCurrentStatus == Status.CLOSE){

                tranAnim = new TranslateAnimation(xFlag*cl,0,yFlag*ct,0);
                childView.setClickable(true);
                childView.setFocusable(true);
            }else{

                tranAnim = new TranslateAnimation(0,xFlag*cl,0,yFlag*ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }

            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset((i*100)/count);

            //监听动画结束
            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE){
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0,720,Animation.RELATIVE_TO_SELF,0.5f
                    , Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);

            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(tranAnim);
            childView.startAnimation(animSet);

            //为小按钮的点击添加动画及监听事件
            final int pos = i+1; //当前item
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuItemClickListener != null){
                        mMenuItemClickListener.onClick(childView, pos);
                    }
                    menuItemAnim(pos - 1);
                    changeStatus();
                }
            });
        }

        changeStatus();
    }

    //添加menuItem的点击动画
    private void menuItemAnim(int position) {

        for (int i = 0; i < getChildCount()-1; i++){

            View childView = getChildAt(i+1);
            if (i == position){

                childView.startAnimation(scaleBigAnim(300));
            }else{

                childView.startAnimation(scaleASmallAnim(300));
            }
        }
    }

    //被点击的次级按钮，会变大并且淡出
    private Animation scaleBigAnim(int duration) {

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    //当其中一个次级按钮被点击时，其他按钮缩小并且逐渐淡出
    private Animation scaleASmallAnim(int duration) {

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    //切换菜单状态
    private void changeStatus() {

        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    public  boolean isOpen(){
        return mCurrentStatus == Status.OPEN;
    }

    //旋转动画
     private void rotateCButton(View v, float start, float end, int duration){
         RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f
                 , Animation.RELATIVE_TO_SELF, 0.5f);
         anim.setDuration(duration);
         anim.setFillAfter(true);
         v.startAnimation(anim);
     }
}
