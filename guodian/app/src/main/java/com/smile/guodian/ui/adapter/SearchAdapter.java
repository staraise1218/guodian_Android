package com.smile.guodian.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.model.entity.SearchResultEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public void setType(List<Integer> type) {
        this.type = type;
    }

    private List<Integer> type;
    private SearchResultEntity resultEntity;
    private LayoutInflater inflater;
    private Context context;

    public void setResultEntity(SearchResultEntity resultEntities) {
        this.resultEntity = resultEntities;
    }

    public SearchAdapter(List<Integer> type, Context context) {
        this.type = type;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        System.out.println(type.get(position));
        return type.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        switch (i) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
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
            recyclerViewHolder.total.setText("共有"+resultEntity.getTotal_num()+"款产品");
            ProductAdapter adapter = new ProductAdapter(resultEntity.getList(),context,"");
            recyclerViewHolder.content.setAdapter(adapter);
        }

    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_gridview)
        public GridView content;
        @BindView(R.id.search_total)
        TextView total;

        ResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println(type.size()+"---");
        return type.size();
    }
}
