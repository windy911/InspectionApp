package com.pm.cameraui.widget;

public interface TouchHandlerListener {
    //当游标的位置没有越界的时候，这时候不需要滚动HorizontalScrollView，调用该方法是为了通知APP更新焦点。
    public void updateCursorPos(int cursorPos);

    //游标越界的时候，调用该方法，让HorizontalScrollView可以左右滚动
    public void doScrollX(int dx);

    //HorizontalScrollView一个速度，让它可以滚动起来。
    public void doFling(int speedX);

    // 触摸抬起，只有点击动作的时候，调用该方法，通知APP有个点击动作。
    public void doTouchupNoMove();

    //触摸抬起，如果中间有MOVE的动作，通知App MOVE动作结束
    public void doTouchUp();

    //向下滑动
    public void slideDown();

    //向上滑动
    public void slideUp();
}