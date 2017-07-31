package com.app.happyshop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.app.happyshop.R;

import java.util.List;


public abstract class BaseRecyclerViewAdapter<ListType, HolderType extends BaseRecyclerViewAdapter.BaseRecyclerViewHolder> extends RecyclerView.Adapter<HolderType> {

    protected List<ListType> itemList;
    protected Context context;
    protected int lastPosition = -1;

    public BaseRecyclerViewAdapter(List<ListType> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public HolderType onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return getHolderView(itemView);
    }

    protected abstract HolderType getHolderView(View itemView);

    @Override
    public void onBindViewHolder(HolderType holder, int position) {
        if (position > lastPosition) {
           /* Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.scale);
            holder.rowItem.startAnimation(animation);*/
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rowItem;

        public BaseRecyclerViewHolder(View view) {
            super(view);
            rowItem = (LinearLayout) view.findViewById(R.id.rowItem);
        }
    }
}
