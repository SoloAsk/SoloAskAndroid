package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.activity.LoginActivity;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.model.Movie;
import com.soloask.android.util.Constant;
import com.soloask.android.util.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by Lebron on 2016/6/21.
 */
public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<Movie> list;

    public HotAdapter(Context context, List datas) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        list = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_hot_view, parent, false), context, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).listenersView.setText(String.format(context.getResources().getString(R.string.format_listerers), list.get(position).getVoteCount()));
            ((ItemViewHolder) holder).questionView.setText(list.get(position).getOverview());
            //((ItemViewHolder) holder).priceView.setText(list.get(position).getVoteAverage()+"");
            ((ItemViewHolder) holder).respondentView.setText(list.get(position).getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constant.TYPE_HEADER;
        } else if (position + 1 == getItemCount()) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout container, voiceLayout;
        TextView questionView;
        TextView respondentView;
        TextView priceView;
        TextView listenersView;
        TextView timeLengthView;

        public ItemViewHolder(View itemView, final Context context, int viewType) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_respondent);
            questionView = (TextView) itemView.findViewById(R.id.tv_question);
            respondentView = (TextView) itemView.findViewById(R.id.tv_respondent_describe);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_amount);
            priceView = (TextView) itemView.findViewById(R.id.tv_voice_price);
            timeLengthView = (TextView) itemView.findViewById(R.id.tv_time_length);
            container = (RelativeLayout) itemView.findViewById(R.id.rl_hot_item);
            voiceLayout = (RelativeLayout) itemView.findViewById(R.id.rl_voice_container);

            timeLengthView.setVisibility(View.GONE);
            priceView.setText(String.format(context.getResources().getString(R.string.format_price), "$0.99"));
            if (viewType == Constant.TYPE_HEADER) {
                voiceLayout.setBackgroundResource(R.drawable.selector_voice_bg_free);
                priceView.setText(R.string.listen_for_free);
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPreferencesHelper.getPreferenceBoolean(context, Constant.KEY_IS_LOGINED, false)) {
                        Intent intent = new Intent(context, QuestionDetailActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        ((Activity) context).startActivityForResult(intent, Constant.CODE_REQUEST);
                    }
                }
            });
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
