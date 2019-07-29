package com.example.layoutmanagergroup.layoutmanger;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layoutmanagergroup.Config.ItemConfig;

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;


    public CardLayoutManager(RecyclerView mRecyclerView, ItemTouchHelper mItemTouchHelper) {
        this.mRecyclerView = mRecyclerView;
        this.mItemTouchHelper = mItemTouchHelper;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount > ItemConfig.DEFAULT_SHOW_ITEM) {

            for (int position = ItemConfig.DEFAULT_SHOW_ITEM; position >= 0; position--) {
                //寻找view并添加
                final View view = recycler.getViewForPosition(position);
                addView(view);
                //测量view宽高
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view));

                Log.i("YYY", "onLayoutChildren");

                if (position == ItemConfig.DEFAULT_SHOW_ITEM) {
                    float scale = 1 - (position - 1) * ItemConfig.DEFAULT_SCALE;
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    float y = (position - 1) * view.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y;
                    Log.i("LHD", "布局 position = " + position + " y = " + y);
                    view.setTranslationY(y);
                } else if (position > 0) {
                    float scale = 1 - position * ItemConfig.DEFAULT_SCALE;
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    float y = position * view.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y;
                    Log.i("LHD", "布局 position = " + position + " y = " + y);
                    view.setTranslationY(y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        } else {
            for (int position = itemCount - 1; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view));

                if (position > 0) {
                    view.setScaleX(1 - position * ItemConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * ItemConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(childViewHolder);
            }
            return false;
        }
    };

}
