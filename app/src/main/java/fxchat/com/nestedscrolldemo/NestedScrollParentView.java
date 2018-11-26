package fxchat.com.nestedscrolldemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import java.lang.ref.WeakReference;


/**
 * Created by wenjiarong on 2018/11/23 0023.
 */
public class NestedScrollParentView extends ConstraintLayout implements NestedScrollingParent {
    public NestedScrollParentView(Context context) {
        this(context, null);
    }

    public NestedScrollParentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    WeakReference<RecyclerView> weakReferenceRecyclerView;
    Scroller mScroller;
    NestedScrollingParentHelper mParentHelper;

    final int MAXY = 250;
    final int MINY = -250;

    int lastFling = 0;

    private void init() {
        mScroller = new Scroller(this.getContext());
        mParentHelper = new NestedScrollingParentHelper(this);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        Log.e("@@@@wjr", " : onStartNestedScroll");
        return isEnabled() && (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        this.mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes, 0);
        mScroller.forceFinished(true);
        Log.e("@@@@wjr", " : onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        this.mParentHelper.onStopNestedScroll(target);
        Log.e("@@@@wjr", " : onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("@@@@wjr", " : onNestedScroll");

    }


    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if (target instanceof RecyclerView) {
            //能否向下滚动，false表示已经滚动到底部
            if (dy > 0 && this.getScrollY() < 0) {
                int willConsume = Math.min(dy, -1 * this.getScrollY());
                scrollBy(0, willConsume);
                consumed[1] = willConsume;
            } else if (dy < 0 && this.getScrollY() > 0) {
                int willConsume = Math.max(dy, -1 * this.getScrollY());
                scrollBy(0, willConsume);
                consumed[1] = willConsume;
            }
            if (!target.canScrollVertically(1)) {
                //到达底部
                if (dy > 0) {
                    int willConsume = Math.min(dy, Math.max(MAXY - this.getScrollY(), 0));
                    scrollBy(0, willConsume);
                    consumed[1] = willConsume;
                }
            }
            //能否向上滚动，false表示已经滚到到顶部
            if (!target.canScrollVertically(-1)) {
                //到达顶部
                if (dy < 0) {
                    int willConsume = Math.max(dy, Math.min(MINY - this.getScrollY(), 0));
                    scrollBy(0, willConsume);
                    consumed[1] = willConsume;
                }
            }
        }
        Log.e("@@@@wjr", " : onNestedPreScroll");

    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("@@@@wjr", " : onNestedFling");
        return true;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.e("@@@@wjr", " : onNestedPreFling");
        if (target instanceof RecyclerView) {
            weakReferenceRecyclerView = new WeakReference<>((RecyclerView) target);
            Log.e("@@@@@wjr", " : fling " + velocityY + " min :" + Integer.MIN_VALUE + " max :" + Integer.MAX_VALUE + " start：" + getScrollY());
//            mScroller.forceFinished(true);
//            mScroller = new Scroller(getContext());
            mScroller.fling(0, 0, (int) velocityX, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            lastFling = 0;
            invalidate();
            return true;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            RecyclerView recyclerView = weakReferenceRecyclerView.get();
            if (recyclerView == null) {
                this.scrollBy(0, mScroller.getCurrY() - lastFling);
                postInvalidate();
            } else {
                if (!recyclerView.canScrollVertically(1)) {
                    //列表触底
                    if (mScroller.getCurrY() - lastFling > 0) {
                        //还要向下
                        if (getScrollY() >= MAXY) {
                            //如果已经超出最多
                            mScroller.forceFinished(true);
                            return;
                        }
                        int offset = Math.min(MAXY - getScrollY(), mScroller.getCurrY() - lastFling);
                        this.scrollBy(0, offset);
                        Log.e("@@@@@wjr", " this: fling current : " + offset + " 底部 :");
                    } else if (this.getScrollY() > 0) {
                        //向上，parent有一部分偏移，我们要修正它
                        Log.e("@@@@@wjr", " this: fling current : " + (mScroller.getCurrY() - lastFling) + " 底部 :");
                        this.scrollBy(0, mScroller.getCurrY() - lastFling);
                    } else {
                        Log.e("@@@@@wjr", " recycler: fling current : " + (mScroller.getCurrY() - lastFling));
                        recyclerView.scrollBy(0, mScroller.getCurrY() - lastFling);
                    }
                } else if (!recyclerView.canScrollVertically(-1)) {
                    //列表触顶
                    if (mScroller.getCurrY() - lastFling < 0) {
                        //还要向上
                        if (getScrollY() <= MINY) {
                            //如果已经超出最多
                            mScroller.forceFinished(true);
                            return;
                        }
                        int offset = Math.max(MINY - getScrollY(), mScroller.getCurrY() - lastFling);
                        this.scrollBy(0, offset);
                        Log.e("@@@@@wjr", " this: fling current : " + offset + " 顶部 :");
                    } else if (this.getScrollY() < 0) {
                        //向下，parent有一部分偏移，我们要修正它
                        Log.e("@@@@@wjr", " this: fling current : " + (mScroller.getCurrY() - lastFling) + " 顶部 :");
                        this.scrollBy(0, mScroller.getCurrY() - lastFling);
                    } else {
                        Log.e("@@@@@wjr", " recycler: fling current : " + (mScroller.getCurrY() - lastFling));
                        recyclerView.scrollBy(0, mScroller.getCurrY() - lastFling);
                    }
                } else {
                    Log.e("@@@@@wjr", " recycler: fling current : " + (mScroller.getCurrY() - lastFling));
                    recyclerView.scrollBy(0, mScroller.getCurrY() - lastFling);
                }
            }
            postInvalidate();
        }
        lastFling = mScroller.getCurrY();
    }


    @Override
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

}
