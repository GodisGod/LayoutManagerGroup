package com.example.layoutmanagergroup.base;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layoutmanagergroup.Config.ItemConfig;
import com.example.layoutmanagergroup.layoutmanger.CardLayoutManager;

import java.util.List;

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */


public class ItemTouchHelperCallback<T> extends ItemTouchHelper.Callback {

    private final RecyclerView.Adapter adapter;
    private List<T> dataList;
    private OnSlideListener<T> mListener;

    public ItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> dataList) {
        this.adapter = checkIsNull(adapter);
        this.dataList = checkIsNull(dataList);
    }

    public ItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> dataList, OnSlideListener<T> listener) {
        this.adapter = checkIsNull(adapter);
        this.dataList = checkIsNull(dataList);
        this.mListener = listener;
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    public void setOnSlideListener(OnSlideListener<T> mListener) {
        this.mListener = mListener;
    }

    //声明不同状态下可以移动的方向（idle, swiping, dragging）
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int slideFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof CardLayoutManager) {
            //拖动只支持上下
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //滑动同时支持上下左右
            slideFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return makeMovementFlags(dragFlags, slideFlags);
    }

    //拖动的项目从旧位置移动到新位置时调用
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    //滑动到消失后的调用
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        viewHolder.itemView.setOnTouchListener(null);

        Log.i("LHD", "direction = " + direction + "  ItemTouchHelper.UP = " + ItemTouchHelper.UP + "  ItemTouchHelper.DOWN = " + ItemTouchHelper.DOWN);
        if (direction == ItemTouchHelper.UP || direction == ItemTouchHelper.DOWN) {

//            mListener.onSlided(viewHolder, remove, direction == ItemTouchHelper.UP ? ItemConfig.SLIDED_LEFT : ItemConfig.SLIDED_RIGHT);
            adapter.notifyDataSetChanged();
            return;
        }

        int layoutPosition = viewHolder.getLayoutPosition();
        T remove = dataList.remove(layoutPosition);
        adapter.notifyDataSetChanged();
        if (mListener != null) {
            Log.i("LHD", "ItemTouchHelper.LEFT = " + ItemTouchHelper.LEFT + "  ItemTouchHelper.RIGHT = " + ItemTouchHelper.RIGHT);
            mListener.onSlided(viewHolder, remove, direction == ItemTouchHelper.LEFT ? ItemConfig.SLIDED_LEFT : ItemConfig.SLIDED_RIGHT);
        }

        if (adapter.getItemCount() == 0) {
            if (mListener != null) {
                mListener.onClear();
            }
        }

    }


    //返回值决定是否有滑动操作
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //返回值决定是否有拖动操作
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    //自定义拖动与滑动交互
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float ratioX = dX / getThresholdX(recyclerView, viewHolder);
            float ratioY = dY / getThresholdY(recyclerView, viewHolder);

//            Log.i("LHD", "ItemTouchHelper.ACTION_STATE_SWIPE dx = " + dX + " dy = " + dY + "  ratioX = " + ratioX);
//            Log.i("LHD", "ItemTouchHelper.ACTION_STATE_SWIPE dx = " + dX + " dy = " + dY + "  ratioY = " + ratioY);

            float ratio;

            int direction;
            //谁大按谁排
            if (Math.abs(dX) > Math.abs(dY)) {
                ratio = ratioX;
                if (dX > 0) {
                    direction = ItemConfig.SLIDING_RIGHT;
                } else {
                    direction = ItemConfig.SLIDING_LEFT;
                }
            } else {
                ratio = ratioY;
                if (dY > 0) {
                    direction = ItemConfig.SLIDING_DOWN;
                } else {
                    direction = ItemConfig.SLIDING_UP;
                }
            }

            if (ratioX > 1) {
                ratioX = 1;
            } else if (ratioX < -1) {
                ratioX = -1;
            }
            itemView.setRotation(ratioX * ItemConfig.DEFAULT_ROTATE_DEGREE);

            if (ratio > 1) {
                ratio = 1;
            } else if (ratio < -1) {
                ratio = -1;
            }

            int childCount = recyclerView.getChildCount();
            if (childCount > ItemConfig.DEFAULT_SHOW_ITEM) {
                for (int position = 1; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    float scale = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE;

                    Log.i("YYY", "onChildDraw");
                    //注意这里,由于在CardLayoutManager中addView的时候是倒序添加的
                    //假设真实的数据源是A,B,C,D,E，那么在CardLayoutManager中的添加顺序是E,D,C,B,A
                    //所以我们在这里使用recyclerView.getChildAt(position);获取的顺序其实是倒序的
                    //即position = 0 view = E
                    //即position = 1 view = D
                    //即position = 2 view = C
                    //即position = 3 view = B
                    //即position = 4 view = A
                    //要好好理解这一点
                    View view = recyclerView.getChildAt(position);
                    view.setScaleX(scale);
                    view.setScaleY(scale);

                    float y = (index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y;
                    Log.i("DDD", "position111 = " + position + " y = " + y + "  scale = " + scale);
                    view.setTranslationY(y);

//                    //排列方向
//                    int sort = ItemConfig.SORT_DOWN;
//                    switch (sort) {
//                        case ItemConfig.SORT_UP:
//                            view.setTranslationY(-(index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y);
//                            break;
//                        case ItemConfig.SORT_RIGHT:
//                            view.setTranslationX((index - Math.abs(ratio)) * itemView.getMeasuredWidth() / ItemConfig.DEFAULT_TRANSLATE_X);
//                            break;
//                        case ItemConfig.SORT_LEFT:
//                            view.setTranslationX(-(index - Math.abs(ratio)) * itemView.getMeasuredWidth() / ItemConfig.DEFAULT_TRANSLATE_X);
//                            break;
//                        case ItemConfig.SORT_DOWN:
//                        default:
//                            view.setTranslationY((index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y);
//                            break;
//                    }

                }
            } else {
                for (int position = 0; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    float scale = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE;
                    view.setScaleX(scale);
                    view.setScaleY(scale);

                    float y = (index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y;
                    Log.i("DDD", "position222 = " + position + " y = " + y + "  scale = " + scale);
                    view.setTranslationY(y);
                }
            }
            if (mListener != null) {
                if (ratio != 0) {
                    mListener.onSliding(viewHolder, ratioX, ratioX < 0 ? ItemConfig.SLIDING_LEFT : ItemConfig.SLIDING_RIGHT);
                } else {
                    mListener.onSliding(viewHolder, ratioX, ItemConfig.SLIDING_NONE);
                }
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            Log.i("LHD", "ItemTouchHelper.ACTION_STATE_DRAG");
        }
    }

    //调用时与元素的用户交互已经结束，也就是它也完成了它的动画时候
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
    }

    //是否可以把拖动的ViewHolder拖动到目标ViewHolder之上
    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        return super.canDropOver(recyclerView, current, target);
    }

    //获取拖动
    @Override
    public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder selected, @NonNull List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        return super.chooseDropTarget(selected, dropTargets, curX, curY);
    }

    //设置手指离开后ViewHolder的动画时间
    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    //返回值作为滑动的流程程度，越小越难滑动，越大越好滑动
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 1f;
    }

    //返回值滑动消失的距离，滑动小于这个值不消失，大于消失
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 10f;
    }

    private float getThresholdX(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    private float getThresholdY(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return recyclerView.getHeight() * 0.4f;
    }

}
