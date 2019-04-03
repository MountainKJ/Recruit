package com.x.core.widget.wrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

public abstract class BaseRecycleViewAdapter<VH extends BaseRecycleViewHolder> extends RecyclerView.Adapter<VH> {
    @SuppressWarnings("unused")
    private static final String TAG = BaseRecycleViewAdapter.class.getSimpleName();
    private Context ctx;
    protected LayoutInflater inflater;
    private ArrayList<AdapterItem> mDataList = new ArrayList<>();

    /** key=clsName , value=layoutID列表 */
    private HashMap<String, Set<Integer>> mLayoutIdSetMap = new LinkedHashMap<>();

    /** key=clsName+"_"+layoutID , value=viewType */
    private HashMap<String, Integer> mCls_LayoutId_Map = new HashMap<>();

    /** key=viewType , value=layoutID */
    private SparseArray<Integer> mViewType_LayoutID_Array = new SparseArray<>();

    public BaseRecycleViewAdapter(Context ctx) {
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        onInitViewType();
    }

    /** (1)初始化数据类型对应的layoutID */
    protected abstract void onInitViewType();

    /** (2)可以根据不同的ViewType创建不同的ViewHolder */
    protected abstract VH onCreateViewHolder(View view,Context ctx,int viewType);

    /** (3)销毁时调用 */
    protected abstract void onDestroy();

    /** (4)当同一种数据类型对应了多种显示样式的时候，此方法就必须要重写 */
    protected int choseLayoutIdFromList(Object data,Set<Integer> set,int pos){
        if(set.size() == 1) {
            Iterator<Integer> iter = set.iterator();
            return iter.next();
        }else if(set.size() > 1){
            throw new RuntimeException("must Override choseLayoutIdFromList method");
        }else{
            throw new RuntimeException("set.size() < 0");
        }
    }

    /** (5)根据索引获取对应的LayoutID */
    protected final int getLayoutIdByPosition(int position) {
        AdapterItem item = getItem(position);
        Object data = item.getData();
        final String className = data.getClass().getName();
        String parentClassName = data.getClass().getSuperclass().getName();
        String clsArr[] = new String[]{className,parentClassName};
        int selectedLayoutId = 0;
        for (int i = 0; i< clsArr.length;i++){
            String clsName = clsArr[i];
            Set<Integer> set = null;
            if(mLayoutIdSetMap.containsKey(clsName)){
                set =  mLayoutIdSetMap.get(className);
            }
            if(set != null && set.size() > 0){
                if(set.size() > 1){
                    selectedLayoutId = choseLayoutIdFromList(data,set,position);
                }else{
                    Iterator<Integer> iter = set.iterator();
                    selectedLayoutId = iter.next();
                }
                break;
            }
        }
        if(selectedLayoutId == 0){
            throw new RuntimeException("Not found suitable layout ID");
        }
        return selectedLayoutId;
    }

    /** (5)根据viewType获取对应的LayoutID */
    protected final int getLayoutIdByViewType(int viewType) {
        return mViewType_LayoutID_Array.get(viewType);
    }

    /** (6)根据positoin获得数据实体，然后根据数据实体获取对应的viewType，viewType的取值跟addViewType()的顺序有关  */
    @Override
    public final int getItemViewType(int position) {
        AdapterItem item = getItem(position);
        int layoutId = getLayoutIdByPosition(position);
        String indexKey = getIndexKey(item.getData().getClass(),layoutId);
        return mCls_LayoutId_Map.get(indexKey);
    }

    /** (7) */
    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = inflater.inflate(getLayoutIdByViewType(viewType),parent,false);
            VH vh = onCreateViewHolder(view,getCtx(),viewType);
            vh.initViews();
            return vh;
        } catch (Exception e) {
            throw e;
        }
    }

    /** (8) */
    @Override
    public final void onBindViewHolder(VH holder, int position) {
        holder.setPosIndex(position);
        holder.setItem(getItem(position));
        onBindViewHolderCallback(holder,position);
    }

    protected void onBindViewHolderCallback(VH holder, int position){

    }

    //=============================================================================================
    public void destroy(){
        mDataList.clear();
        mLayoutIdSetMap.clear();
        mCls_LayoutId_Map.clear();
        mViewType_LayoutID_Array.clear();
        onDestroy();
    }

    private String getIndexKey(Class<?> c,int layoutId){
        String key = c.getName();
        return key+"_"+layoutId;
    }

    public final void addViewType(Class<?> c, int layoutId) {
        String key = c.getName();
        Set<Integer> set = null;
        if(mLayoutIdSetMap.containsKey(key)){
            set = mLayoutIdSetMap.get(key);
        }
        if(set == null){
            set = new TreeSet<>();
            mLayoutIdSetMap.put(key, set);
        }
        if(set.size() > 0 && set.contains(layoutId)){
            return;
        }
        set.add(layoutId);
        String indexKey = getIndexKey(c,layoutId);
        if(!mCls_LayoutId_Map.containsKey(indexKey)) {
            int viewType = mCls_LayoutId_Map.size();
            mCls_LayoutId_Map.put(indexKey, viewType);
            mViewType_LayoutID_Array.put(viewType,layoutId);
        }
    }

    @Override
    public final int getItemCount() {
        return mDataList.size();
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    public final void clearItems(){
        mDataList.clear();
    }

    private AdapterItem convertDataToAdapterItem(Object data, Object state){
        if (data == null)
            return null;
        return new AdapterItem(data, state);
    }

    public final AdapterItem addItem(Object data, Object state) {
        return addItem(mDataList.size(),data,state);
    }

    public final AdapterItem addItem(int index, Object data, Object state) {
        AdapterItem adapterItem = convertDataToAdapterItem(data, state);
        if(adapterItem != null)
            mDataList.add(index,adapterItem);
        return adapterItem;
    }

    public final AdapterItem addItem(AdapterItem ai) {
        mDataList.add(ai);
        return ai;
    }

    public final AdapterItem addItem(Object data, Object state, NewOnAdapterItemStateChangeListener listener) {
        AdapterItem item = addItem(data, state);
        if (item != null)
            item.setOnAdapterItemStateChangeListener(listener);
        return item;
    }

    public final AdapterItem getItem(int location){
        return mDataList.get(location);
    }

    public final void delItem(AdapterItem item) {
        if (item == null || mDataList == null || mDataList.size() == 0)
            return;
        mDataList.remove(item);
    }

    public final AdapterItem delItem(int position) {
        if(mDataList == null)
            return null;
        if (position >= mDataList.size())
            return null;
        return mDataList.remove(position);
    }

    public final ArrayList<AdapterItem> getItems() {
        ArrayList<AdapterItem> items = new ArrayList<>();
        items.addAll(mDataList);
        return items;
    }

    protected final Context getCtx() {
        return ctx;
    }
}
