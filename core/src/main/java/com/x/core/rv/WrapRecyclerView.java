package com.x.core.rv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.x.core.R;


/**
 * Created by liguo on 2017/6/16.
 */

public class WrapRecyclerView extends RecyclerView implements WrapConstants {

    private static final int DEF_LAYOUT_MANAGER_TYPE = LAYOUT_MANAGER_TYPE_LINEAR;
    private static final int DEF_GRID_SPAN_COUNT = 3;
    private static final int DEF_LAYOUT_MANAGER_ORIENTATION = OrientationHelper.VERTICAL;

    private WrapRecyclerViewAdapter wrapAdapter = null;
    private int curLayoutManagerType = DEF_LAYOUT_MANAGER_TYPE;
    private int ori = WrapConstants.ORI_VERTICAL;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private Drawable mVerticalDivider;
    private Drawable mHorizontalDivider;
    private int mRippleResource = 0;
    private int mVerticalDividerHeight;
    private int mHorizontalDividerHeight;
    //    private Drawable mDefAllDivider;
//    private int mDefAllDividerHeight;
    private DividerDefault mDividerDefault;
    private boolean isDefaultItemDecoration = true;

    private void init(Context ctx, @Nullable AttributeSet attrs) {
        TypedArray ta = ctx.obtainStyledAttributes(attrs, R.styleable.WrapRecyclerView);
        int manager_type = ta.getInt(R.styleable.WrapRecyclerView_wrv_layoutManager, DEF_LAYOUT_MANAGER_TYPE);
        int ori = ta.getInt(R.styleable.WrapRecyclerView_wrv_layoutManagerOrientation, DEF_LAYOUT_MANAGER_ORIENTATION);
        this.ori = ori;
        boolean is_reverse_layout = ta.getBoolean(R.styleable.WrapRecyclerView_wrv_isReverseLayout, false);

        switch (manager_type) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                setLayoutManager(new LinearLayoutManager(ctx, ori, is_reverse_layout));
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                int grid_count = ta.getInt(R.styleable.WrapRecyclerView_wrv_spanCount, DEF_GRID_SPAN_COUNT);
                setLayoutManager(new GridLayoutManager(ctx, grid_count, ori, is_reverse_layout));
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                int staggered_grid_count = ta.getInt(R.styleable.WrapRecyclerView_wrv_spanCount, DEF_GRID_SPAN_COUNT);
                setLayoutManager(new StaggeredGridLayoutManager(staggered_grid_count, ori));
                break;
        }

//        mDefAllDivider = ta.getDrawable(R.styleable.WrapRecyclerView_wrv_divider);
//        mDefAllDividerHeight = (int)ta.getDimension(R.styleable.WrapRecyclerView_wrv_dividerHeight, -1);
        mVerticalDivider = ta.getDrawable(R.styleable.WrapRecyclerView_wrv_dividerVertical);
        mHorizontalDivider = ta.getDrawable(R.styleable.WrapRecyclerView_wrv_dividerHorizontal);
        mVerticalDividerHeight = (int) ta.getDimension(R.styleable.WrapRecyclerView_wrv_dividerVerticalHeight, -1);
        mHorizontalDividerHeight = (int) ta.getDimension(R.styleable.WrapRecyclerView_wrv_dividerHorizontalHeight, -1);

        ta.recycle();

        wrapAdapter = new WrapRecyclerViewAdapter(this);

        mDividerDefault = new DividerDefault(this, mVerticalDivider, mHorizontalDivider, mVerticalDividerHeight, mHorizontalDividerHeight);
        super.addItemDecoration(mDividerDefault);
    }

    public int getRippleResource() {
        return mRippleResource;
    }

    public void setRippleResource(int rippleResource) {
        this.mRippleResource = rippleResource;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (null == layout)
            return;
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager glm = ((GridLayoutManager) layout);
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (wrapAdapter.isHeaderView(position) || wrapAdapter.isFooterView(position)) {
                        return glm.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });

            curLayoutManagerType = LAYOUT_MANAGER_TYPE_GRID;
        } else if (layout instanceof StaggeredGridLayoutManager) {
            curLayoutManagerType = LAYOUT_MANAGER_TYPE_STAGGERED_GRID;
        } else if (layout instanceof LinearLayoutManager) {
            curLayoutManagerType = LAYOUT_MANAGER_TYPE_LINEAR;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        wrapAdapter.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new ReqAdapterDataObserver(wrapAdapter));
        super.setAdapter(wrapAdapter);
    }

    private void notifyDataChanged() {
        if (this.getAdapter() != null) {
            this.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        super.addItemDecoration(decor);
        this.isDefaultItemDecoration = false;
    }

    public final int getCurLayoutManagerType() {
        return this.curLayoutManagerType;
    }

    public final int getOri() {
        return this.ori;
    }

    public final void addHeaderView(View view) {
        wrapAdapter.addHeaderView(view);
        notifyDataChanged();
    }

    public final void addHeaderView(int pos, View view) {
        wrapAdapter.addHeaderView(pos, view);
        notifyDataChanged();
    }

    public final void removeHeaderView(View view) {
        wrapAdapter.removeHeaderView(view);
        notifyDataChanged();
    }

    public final void removeAllHeaderView() {
        wrapAdapter.removeAllHeaderView();
        notifyDataChanged();
    }

    public final void addFooterView(View view) {
        wrapAdapter.addFooterView(view);
        notifyDataChanged();
    }

    public final void removeFooterView(View view) {
        wrapAdapter.removeFooterView(view);
        notifyDataChanged();
    }

    public final int getHeaderViewCount() {
        return wrapAdapter.getHeaderViewCount();
    }

    public final int getFooterViewCount() {
        return wrapAdapter.getFooterViewCount();
    }

    public void setDividerVertical(Drawable dividerVertical) {
        if (!isDefaultItemDecoration || mVerticalDividerHeight <= 0) return;

        if (this.mVerticalDivider != dividerVertical) {
            this.mVerticalDivider = dividerVertical;
        }

        if (null != mDividerDefault) {
            mDividerDefault.setVerticalDividerDrawable(mVerticalDivider);

            if (null != wrapAdapter) {
                wrapAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerHorizontal(Drawable dividerHorizontal) {
        if (!isDefaultItemDecoration || mHorizontalDividerHeight <= 0) return;

        if (this.mHorizontalDivider != dividerHorizontal) {
            this.mHorizontalDivider = dividerHorizontal;
        }

        if (null != mDividerDefault) {
            mDividerDefault.setHorizontalDividerDrawable(mHorizontalDivider);

            if (null != wrapAdapter) {
                wrapAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean canHandLongClick = false;

    public final void setItemViewHandLongClick(boolean bool) {
        this.canHandLongClick = bool;
    }

    public final boolean getItemViewHandLongClick() {
        return this.canHandLongClick;
    }

    public final void setOnItemClickListener(OnRecycleItemClickListener listener) {
        wrapAdapter.setOnItemClickListener(listener);
    }

    public final void setOnItemLongClickListener(OnRecycleItemLongClickListener listener) {
        wrapAdapter.setOnItemLongClickListener(listener);
    }

    public final void moveToPosition(int index, int dy) {
        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            llm.scrollToPositionWithOffset(index, dy);
//            llm.setStackFromEnd(true);    // GridLayoutManager not supports

        }
    }

    private static class ReqAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private WrapRecyclerViewAdapter wa;

        public ReqAdapterDataObserver(WrapRecyclerViewAdapter adapter) {
            this.wa = adapter;
        }

        @Override
        public void onChanged() {
            wa.notifyDataSetChanged();
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            wa.notifyItemInserted(wa.getHeaderViewCount() + positionStart);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            wa.notifyItemRemoved(wa.getHeaderViewCount() + positionStart);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            wa.notifyItemRangeChanged(wa.getHeaderViewCount() + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            wa.notifyItemMoved(wa.getHeaderViewCount() + fromPosition, wa.getHeaderViewCount() + toPosition);
        }
    }

    public interface OnRecycleItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnRecycleItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    //==========================================LoadMore==========================================
    private ILoadMoreView mLoadMoreView;
    private OnLoadMoreListener onLoadMoreListener;
    private WrapRecyclerViewOnScrollListener onLoadMoreScroller;

    public interface OnLoadMoreListener {
        boolean onLoadMore();
    }

    public interface ILoadMoreView {
        boolean isLoading();

        void setLoading(boolean loading);

        View getView();
    }

    public void setOnLoadMoreView(ILoadMoreView loadMoreView) {
        if (mLoadMoreView != null || loadMoreView == null) {
            return;
        }
        this.mLoadMoreView = loadMoreView;
        initLoadMoreView();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void initLoadMoreView() {
        if (null == onLoadMoreScroller) {
            onLoadMoreScroller = new WrapRecyclerViewOnScrollListener(this.getLayoutManager()) {
                @Override
                public void onScrolledToTop() {

                }

                @Override
                public void onScrolledToBottom() {
                    if (mLoadMoreView.isLoading()) {
                        return;
                    }
                    if (null != onLoadMoreListener) {
                        if (onLoadMoreListener.onLoadMore()) {
                            mLoadMoreView.setLoading(true);
                            wrapAdapter.addFooterView(mLoadMoreView.getView());
                            wrapAdapter.notifyDataSetChanged();
                            //moveToPosition(pos, 0);
                            int pos = wrapAdapter.getHeaderViewCount() + wrapAdapter.getAdapterCount() + wrapAdapter.getFooterViewCount();
                            smoothScrollToPosition(pos - 1);
                        }
                    }
                }
            };
        }
        this.addOnScrollListener(onLoadMoreScroller);
        mLoadMoreView.getView().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
    }

    public void refreshLoadMoreComplete() {
        mLoadMoreView.setLoading(false);
        wrapAdapter.removeFooterView(mLoadMoreView.getView());
        wrapAdapter.notifyDataSetChanged();
    }
}
