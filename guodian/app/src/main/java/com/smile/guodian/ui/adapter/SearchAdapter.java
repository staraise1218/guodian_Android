package com.smile.guodian.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.model.entity.SearchResultEntity;
import com.smile.guodian.ui.activity.SearchActivity;
import com.smile.guodian.ui.activity.WebActivity;
import com.smile.guodian.ui.adapter.search.SearchHotAdapter;
import com.smile.guodian.widget.FlowLayout;
import com.smile.guodian.widget.HomeGridView;
import com.smile.guodian.widget.SideBar;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public void setType(List<Integer> type) {
        this.type = type;
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    SearchActivity searchActivity;
    private List<Integer> type;
    private SearchResultEntity resultEntity;
    private LayoutInflater inflater;
    private Context context;
    private List<String> hot;
    private List<GuessGoods> guessGoods;
    private String[] list;

    public void setList(String[] list) {
        this.list = list;
    }

    public void setGuessGoods(List<GuessGoods> guessGoods) {
        this.guessGoods = guessGoods;
    }

    public void setHot(List<String> hot) {
        this.hot = hot;
    }

    public void setResultEntity(SearchResultEntity resultEntities) {
        this.resultEntity = resultEntities;
    }

    public SearchAdapter(List<Integer> type, Context context, SearchActivity searchActivity) {
        this.type = type;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.searchActivity = searchActivity;
    }


    @Override
    public int getItemViewType(int position) {
        return type.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        switch (i) {
            case 1:
                view = inflater.inflate(R.layout.search_empty, null);
                return new EmptyViewHolder(view);
            case 2:
                view = inflater.inflate(R.layout.search_hot, null);
                return new HotViewHolder(view);
            case 3:
                view = inflater.inflate(R.layout.search_like, null);
                return new SearchLikeViewHolder(view);
            case 4:
                view = inflater.inflate(R.layout.search_tips, null);
                return new TipsViewHolder(view);
            case 5:
                view = inflater.inflate(R.layout.search_gridview, null);
                return new ResultViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof ResultViewHolder) {
            ResultViewHolder recyclerViewHolder = (ResultViewHolder) viewHolder;
            recyclerViewHolder.total.setText("共有" + resultEntity.getTotal_num() + "款产品");
            ProductAdapter adapter = new ProductAdapter(resultEntity.getList(), context, "");
            recyclerViewHolder.content.setAdapter(adapter);
        }

        if (viewHolder instanceof HotViewHolder) {
            HotViewHolder hotViewHolder = (HotViewHolder) viewHolder;
            SearchHotAdapter hotAdapter = new SearchHotAdapter(hot, context);
            hotViewHolder.gridView.setAdapter(hotAdapter);

        }

        if (viewHolder instanceof SearchLikeViewHolder) {
            SearchLikeViewHolder searchLikeViewHolder = (SearchLikeViewHolder) viewHolder;
            HomeHeaderAdapter headerAdapter = new HomeHeaderAdapter(context);
            headerAdapter.setGuessGoods(guessGoods);
            searchLikeViewHolder.gridView.setAdapter(headerAdapter);
            searchLikeViewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("goodsId", guessGoods.get(position).getGoods_id());
                    context.startActivity(intent);
                }
            });
        }

        if (viewHolder instanceof TipsViewHolder) {
            TipsViewHolder tipsViewHolder = (TipsViewHolder) viewHolder;
            initFlowlayout(tipsViewHolder.flowLayout);
            tipsViewHolder.imageView.setOnClickListener(onClickListener);
        }

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public void initFlowlayout(FlowLayout flowLayoutMoreView) {
        //初始化多选
        flowLayoutMoreView.removeAllViews();
        for (int i = 0; i < list.length; i++) {
            final TextView child = new TextView(context
            );
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            if (i == list.length - 1) {
                params.setMargins(dip2px(context, 5), dip2px(context, 10), dip2px(context, 5), 0);
            } else {
                params.setMargins(dip2px(context, 5), dip2px(context, 10), dip2px(context, 5), 0);
            }
            child.setLayoutParams(params);
            child.setTag(false);
            child.setBackgroundColor(Color.parseColor("#c3c3c3"));
            child.setText(list[i]);
//            child.setTextColor(Color.BLACK);
            child.setTextSize(px2sp(context, 30));
            child.setPadding(dip2px(context, 10), dip2px(context, 10), dip2px(context, 10), dip2px(context, 10));
            child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    searchActivity.search(child.getText().toString());

                    if ((Boolean) child.getTag()) {
//                        child.setTag(false);
//                        child.setBackgroundResource(R.drawable.normal);
                    } else {
//                        child.setTag(true);
//                        child.setBackgroundResource(R.drawable.press);
                    }
                }
            });
            flowLayoutMoreView.addView(child);
            flowLayoutMoreView.requestLayout();
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class SearchLikeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_like_content)
        HomeGridView gridView;

        public SearchLikeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TipsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.flowLayoutMoreView)
        FlowLayout flowLayout;
        @BindView(R.id.del_history)
        ImageView imageView;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class HotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_hot_content)
        GridView gridView;


        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_gridview)
        public HomeGridView content;
        @BindView(R.id.search_total)
        TextView total;

        ResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println(type.size() + "---");
        return type.size();
    }
}
