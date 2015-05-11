package net.suweya.autohidetoolbarhelper;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;

/**
 * Created by suweya on 2015/5/11.
 */
public class AutoHideToolbarHelper {

    private static final int HEADER_HIDE_ANIM_DURATION = 300;

    private int mActionBarAutoHideSensivity;
    private int mActionBarAutoHideMinY;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;

    private int mActionbarSize;

    private View view;

    public AutoHideToolbarHelper(Context context, View toolbar) {

        mActionBarAutoHideSensivity = context.getResources()
                .getDimensionPixelOffset(R.dimen.action_bar_auto_hide_sensivity);
        mActionBarAutoHideMinY = context.getResources()
                .getDimensionPixelOffset(R.dimen.action_bar_auto_hide_min_y);
        this.view = toolbar;

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            mActionbarSize = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
    }

    public void registerOnScrollListener(AbsListView view) {

        view.setClipToPadding(false);//clipToPadding
        view.setPadding(0, mActionbarSize, 0, 0);
        view.setOnScrollListener(new AbsListView.OnScrollListener() {

            final static int ITEMS_THRESHOLD = 3;
            int lastFvi = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                        lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                );
                lastFvi = firstVisibleItem;
            }
        });
    }

    private void onMainContentScrolled(int currentY, int deltaY) {

        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
        autoShowOrHideActionBar(shouldShow);
    }

    private void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    /**
     * Hide or show ToolBar
     *
     * @param shown
     */
    public void onActionBarAutoShowOrHide(boolean shown) {

        if (shown) {
            view.animate()
                    .translationY(0)
                    .alpha(1)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        } else {
            view.animate()
                    .translationY(-view.getBottom())
                    .alpha(0)
                    .setDuration(HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        }
    }
}
