package com.example.administrator.foodapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @version v1.0
 * @类描述: 封装了ViewHolder
 * @项目名称: e_Clinic
 * @包名: com.aode.e_clinicapp.base.adapter
 * @类名称: BaseViewHolder
 * @创建人: 陈映苗
 * @创建时间: 2016/7/2 10:50
 * @bug [nothing]
 * @Copyright 陈映苗
 * @mail 1320080989@qq.com
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> viewSparseArray;

    protected BaseAdapter.OnItemClickListener mListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        this.viewSparseArray = new SparseArray<View>();
        this.mListener = listener;
        itemView.setOnClickListener(this);
    }

    protected <T extends View> T findView(int id) {

        View view = viewSparseArray.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            viewSparseArray.put(id, view);
        }
        return (T) view;
    }

    public View getView(int id) {
        return findView(id);
    }

    public TextView getTextViewId(int id) {
        return findView(id);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return findView(id);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view, getLayoutPosition());
        }
    }
}
