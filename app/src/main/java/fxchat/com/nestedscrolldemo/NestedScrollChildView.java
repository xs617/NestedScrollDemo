package fxchat.com.nestedscrolldemo;

import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;

/**
 * Created by wenjiarong on 2018/11/23 0023.
 */
public class NestedScrollChildView implements NestedScrollingChild {

    @Override
    public void setNestedScrollingEnabled(boolean b) {

    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return false;
    }

    @Override
    public boolean startNestedScroll(int i) {
        return false;
    }

    @Override
    public void stopNestedScroll() {

    }

    @Override
    public boolean hasNestedScrollingParent() {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int i, int i1, int i2, int i3, @Nullable int[] ints) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int i, int i1, @Nullable int[] ints, @Nullable int[] ints1) {
        return false;
    }

    @Override
    public boolean dispatchNestedFling(float v, float v1, boolean b) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreFling(float v, float v1) {
        return false;
    }
}
