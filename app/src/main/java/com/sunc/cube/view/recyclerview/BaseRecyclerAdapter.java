package com.sunc.cube.view.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public abstract class BaseRecyclerAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerArrayAdapter<M, VH> {
    protected EndlessScrollListener.IMore mMore;

    public BaseRecyclerAdapter(Activity activity, List<M> data, EndlessScrollListener.IMore more) {
        super(activity, data);
        mMore = more;
    }

    protected final void updateStatus(RecyclerView.ViewHolder viewHolder) {
        ((MoreHolder) viewHolder).updateStatus(mMore);
    }
}
