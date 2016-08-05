package com.soloask.android.account.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.soloask.android.R;

import java.util.List;

/**
 * Created by Lebron on 2016/6/29.
 */
public class PriceAdapter extends BaseQuickAdapter<String> {

    public PriceAdapter(List<String> data) {
        super(R.layout.item_price_view, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String price) {
        baseViewHolder.setText(R.id.tv_price, price);
    }

}
