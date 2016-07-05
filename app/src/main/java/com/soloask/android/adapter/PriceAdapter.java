package com.soloask.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.util.Constant;

import java.util.List;

/**
 * Created by Lebron on 2016/6/29.
 */
public class PriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List mDatas;
    private TextView mTempView;
    private String mChoosedPrice;

    public PriceAdapter(Context context, List list) {
        mContext = context;
        mDatas = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_price_view, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).priceView.setText(mDatas.get(position).toString());
        ((ItemViewHolder) holder).priceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTempView != null) {
                    mTempView.setBackgroundResource(R.drawable.bg_btn_price_normal);
                    mTempView.setTextColor(mContext.getResources().getColor(R.color.color_common_red));
                }
                ((ItemViewHolder) holder).priceView.setBackgroundResource(R.drawable.bg_btn_price_choosed);
                ((ItemViewHolder) holder).priceView.setTextColor(mContext.getResources().getColor(android.R.color.white));
                mChoosedPrice = mDatas.get(position).toString();
                mTempView = ((ItemViewHolder) holder).priceView;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constant.TYPE_HEADER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public String getChoosedPrice() {
        return mChoosedPrice;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView priceView;

        public ItemViewHolder(View itemView, int viewType) {
            super(itemView);
            priceView = (TextView) itemView.findViewById(R.id.tv_price);
            if (viewType == Constant.TYPE_HEADER) {
                //priceView.setBackgroundResource(R.drawable.bg_btn_price_choosed);
                //mTempView = priceView;
            }
        }
    }
}
