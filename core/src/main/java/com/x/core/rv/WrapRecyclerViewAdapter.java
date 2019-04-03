package com.x.core.rv;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.x.core.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by root on 16-1-28.
 */
public final class WrapRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {
    private final String TAG = "WrapRecyclerViewAdapter";

    /**
     * ChildAdapter 的ViewType取值必须小于65536
     */
    private final List<View> headerViewList;
    private final List<View> footerViewList;
    private RecyclerView.Adapter adapter;
    private final RecyclerView recyclerView;
    private WrapRecyclerView.OnRecycleItemClickListener itemClickListener;
    private WrapRecyclerView.OnRecycleItemLongClickListener itemLongClickListener;

    /** key：buildKey(View) value：viewType(int类型) */
    private final Map<String,Integer> viewTypeMap = new HashMap<>();
    /** key：viewType(int类型) value：View(headerView) */
    private final SparseArray<View> viewTypeArray = new SparseArray<>();
    private final AtomicInteger viewTypeCreator = new AtomicInteger(0);

    public WrapRecyclerViewAdapter(RecyclerView recyclerView) {
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    private boolean isFooterViewByViewType(int viewType) {
        return viewTypeArray.indexOfKey(viewType) >= 0;
    }

    private boolean isHeaderViewByViewType(int viewType) {
        return viewTypeArray.indexOfKey(viewType) >= 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderViewByViewType(viewType) || isFooterViewByViewType(viewType)) {
            View vv = viewTypeArray.get(viewType);
            return new HeaderOrFooterViewHolder(vv);
        }
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && recyclerView instanceof WrapRecyclerView) {
            final int rippleRes = ((WrapRecyclerView) recyclerView).getRippleResource();
            if (rippleRes != 0) {
                holder.itemView.setBackgroundResource(rippleRes);
            }
        }
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(this);
        }
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position) || isFooterView(position)) {
            return;
        }
        adapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            View v = getHeaderViewByPosition(position);
            return getViewTypeByView(v);
        }
        if (isFooterView(position)) {
            View v = getFooterViewByPosition(position);
            return getViewTypeByView(v);
        }
        return adapter.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getHeaderViewCount() + getFooterViewCount() + getAdapterCount();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(v, recyclerView.getChildAdapterPosition(v) - getHeaderViewCount());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (itemLongClickListener == null) {
            return false;
        }
        return itemLongClickListener.onItemLongClick(v, recyclerView.getChildAdapterPosition(v) - getHeaderViewCount());
    }

    public final void addHeaderView(View view) {
        addHeaderView(getHeaderViewCount(),view);
    }
    public final void addHeaderView(int pos,View view) {
        addToViewMap(view);
        headerViewList.add(pos,view);
    }

    public final void removeHeaderView(View view) {
        if (headerViewList.contains(view)) {
            removeToViewMap(view);
            headerViewList.remove(view);
        }
    }

    public final void removeAllHeaderView() {
        if (headerViewList.size() == 0) {
            return;
        }
        List<View> del = new ArrayList<>();
        del.addAll(headerViewList);
        for (View view1 : del) {
            removeHeaderView(view1);
        }
    }

    public final View getHeaderViewByPosition(int position){
        if(isHeaderView(position)) {
            return headerViewList.get(position);
        }else{
            return null;
        }
    }
    public final int getHeaderViewCount() {
        return headerViewList.size();
    }

    public final int indexOfHeaderViewList(View view) {
        if(headerViewList.contains(view)){
            return headerViewList.indexOf(view);
        }
        return -1;
    }


    public final void addFooterView(View view) {
        addToViewMap(view);
        footerViewList.add(view);
    }

    public final void removeFooterView(View view) {
        if (footerViewList.contains(view)) {
            removeToViewMap(view);
            footerViewList.remove(view);
        }
    }

    public final View getFooterViewByPosition(int position){
        if(isFooterView(position)) {
            int reallyIndex = getIndexOfFooterViewList(position);
            return footerViewList.get(reallyIndex);
        }else{
            return null;
        }
    }

    public final int getFooterViewCount() {
        return footerViewList.size();
    }

    private int getIndexOfFooterViewList(int pos){
        return pos - (getHeaderViewCount() + getAdapterCount());
    }

    public final int getViewTypeByView(View view){
        String key = buildKey(view);
        if(!viewTypeMap.containsKey(key)){
            throw new RuntimeException("viewTypeMap not contains key : [" + key+"]");
        }
        return viewTypeMap.get(key);
    }

    public final boolean isHeaderView(int position) {
        int c = getHeaderViewCount();
        return c > 0 && position >= 0 && position < c;
    }

    public final boolean isFooterView(int position) {
        return getFooterViewCount() > 0 && position >= (getHeaderViewCount() + getAdapterCount());
    }

    public final int getAdapterCount() {
        return adapter.getItemCount();
    }

    private static class HeaderOrFooterViewHolder extends RecyclerView.ViewHolder {
        public HeaderOrFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(WrapRecyclerView.OnRecycleItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(WrapRecyclerView.OnRecycleItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public final void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.setHasStableIds(adapter.hasStableIds());
    }

    private final String buildKey(View view){
        return StringUtil.replaceTrim_R_N(System.identityHashCode(view)+"");
    }

    private void addToViewMap(View view){
        String key = buildKey(view);
        if(viewTypeMap.containsKey(key)){
            return;
        }
        int vt = viewTypeCreator.decrementAndGet();
        viewTypeMap.put(key,vt);
        viewTypeArray.put(vt,view);
    }

    private void removeToViewMap(View view){
        String k = buildKey(view);
        int vt = viewTypeMap.get(k);
        viewTypeMap.remove(k);
        viewTypeArray.remove(vt);
    }
}
