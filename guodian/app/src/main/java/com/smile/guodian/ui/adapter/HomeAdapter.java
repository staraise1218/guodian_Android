package com.smile.guodian.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smile.guodian.R;
import com.smile.guodian.base.Type;
import com.smile.guodian.model.entity.CategoryList;
import com.smile.guodian.model.entity.GuessGoods;
import com.smile.guodian.model.entity.HomeBase;
import com.smile.guodian.model.entity.HomeTop;
import com.smile.guodian.model.entity.Hot;
import com.smile.guodian.utils.GlideUtil;
import com.smile.guodian.widget.CirclePageIndicator;
import com.smile.guodian.widget.FooterLoading;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.common.view.HorizontalListView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<HomeBase> list;
    private List<HomeTop.Carousel> loopList;
    private List<HomeBase> headlineList;
    private final SpanSizeLookup spanSizeLookup = new SpanSizeLookup();
    private LayoutInflater inflater;
    private List<Hot> hotList;
    private List<Hot> customer;
    private List<CategoryList> categoryLists;

    public void setCategoryLists(List<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }

    public void setCustomer(List<Hot> customer) {
        this.customer = customer;
    }

    public void setGuessGoods(List<GuessGoods> guessGoods) {
        this.guessGoods = guessGoods;
    }

    private List<GuessGoods> guessGoods;

    public void setRegabout(List<Hot> regabout) {
        this.regabout = regabout;
    }

    private List<Hot> regabout;

    public void setHotList(List<Hot> hotList) {
        this.hotList = hotList;
    }

    public HomeAdapter(Context context, Activity activity, List<HomeBase> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setLoopList(List<HomeTop.Carousel> loopList) {
        this.loopList = loopList;
    }

    public void setHeadlineList(List<HomeBase> headlineList) {
        this.headlineList = headlineList;
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return spanSizeLookup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HomeBase.TYPE_CAROUSEL:
                //Banner
                view = inflater.inflate(R.layout.item_home_type_carousel, parent, false);
                return new CarouselHolder(view);
            case HomeBase.TYPE_CATEGORY:
                view = inflater.inflate(R.layout.fragment_navigation_home_category, parent, false);
                return new CategoryHolder(view);
            case HomeBase.TYPE_HEADLINE:
                view = inflater.inflate(R.layout.item_home_type_headline, parent, false);
                return new HeadlineHolder(view);
            case HomeBase.TYPE_HEADER:
                view = inflater.inflate(R.layout.item_home_type_header, parent, false);
                return new HeaderHolder(view);
            case HomeBase.TYPE_DIVIDER:
                view = inflater.inflate(R.layout.item_home_type_divider, parent, false);
                return new PlaceHolder(view);
            case HomeBase.TYPE_LIVE:
                view = inflater.inflate(R.layout.item_home_type_live, parent, false);
                return new LiveHolder(view);
            case HomeBase.TYPE_HOT:
                //猜你喜欢
                view = inflater.inflate(R.layout.item_home_type_hot, parent, false);
                return new CommonHolder(view);
            case HomeBase.TYPE_RECOMMEND:
                view = inflater.inflate(R.layout.item_home_type_recommend, parent, false);
                return new RecommendHolder(view);
            case HomeBase.TYPE_PLACE:
                view = inflater.inflate(R.layout.item_place, parent, false);
                return new PlaceHolder(view);
            default:
                view = inflater.inflate(R.layout.item_footer_loading, parent, false);
                return new FooterHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        HomeBase bean = list.get(position);
        if (viewHolder instanceof CarouselHolder) {
            CarouselHolder holder = (CarouselHolder) viewHolder;
            holder.viewPager.setAdapter(new ImageHomeAdapter(context, activity, loopList));
            holder.indicator.setViewPager(holder.viewPager);
//            holder.viewPager.setInterval(4000);
            holder.viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
            holder.viewPager.startAutoScroll();
        } else if (viewHolder instanceof HeadlineHolder) {
            //热门
            HeadlineHolder holder = (HeadlineHolder) viewHolder;
            HeadlineShowAdapter headlineShowAdapter = new HeadlineShowAdapter(context, activity);
            headlineShowAdapter.setHots(customer);
            holder.horizontalListView.setAdapter(headlineShowAdapter);
        } else if (viewHolder instanceof HeaderHolder) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            HomeHeaderAdapter headerAdapter = new HomeHeaderAdapter(context);
            headerAdapter.setGuessGoods(guessGoods);
            holder.gridView.setAdapter(headerAdapter);
            holder.tvTitle.setText(bean.getName());
        } else if (viewHolder instanceof LiveHolder) {
            LiveHolder holder = (LiveHolder) viewHolder;
//            GlideUtil.load(activity, bean.getUrl(), holder.ivHome);
//            holder.tvTitle.setText(bean.getName());
            HomeLiveAdapter homeLiveAdapter = new HomeLiveAdapter(context);
            homeLiveAdapter.setBannars(regabout);
            holder.listView.setAdapter(homeLiveAdapter);

        } else if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            HotShowAdapter headlineShowAdapter = new HotShowAdapter(context, activity);
            headlineShowAdapter.setHots(hotList);
            holder.gridView.setAdapter(headlineShowAdapter);
//            holder.gridView.setAdapter(new HomeHeaderAdapter(context) );
//            GlideUtil.load(activity, bean.getUrl(), holder.ivHome);
//            holder.tvTitle.setText(bean.getName());
//            holder.tvPrice.setText(String.format("%s%s", bean.getCurrency(), bean.getPrice()));
        } else if (viewHolder instanceof RecommendHolder) {
            RecommendHolder holder = (RecommendHolder) viewHolder;
            GlideUtil.load(activity, bean.getUrl(), holder.ivHome);
            holder.tvTitle.setText(bean.getName());
            holder.tvPrice.setText(String.format("%s%s", bean.getCurrency(), bean.getPrice()));
            holder.tvCount.setText(String.format("%d人付款", new Random().nextInt(10000)));

        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder holder = (FooterHolder) viewHolder;
            holder.footerLoading.onLoad(Type.TYPE_FOOTER_FULL == list.get(position).getType());
        } else if (viewHolder instanceof CategoryHolder) {
//            CategoryHolder categoryHolder = (CategoryHolder) viewHolder;
//            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryLists, context);
//            categoryListAdapter.setCategoryLists(categoryLists);
//            categoryHolder.gridView.setAdapter(categoryListAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CarouselHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_pager)
        AutoScrollViewPager viewPager;
        @BindView(R.id.indicator)
        CirclePageIndicator indicator;


        CarouselHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadlineHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_type_headline)
        HorizontalListView horizontalListView;

        HeadlineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class CommonHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.iv_home)
//        ImageView ivHome;
//        @BindView(R.id.tv_title)
//        TextView tvTitle;
//        @BindView(R.id.tv_price)
//        TextView tvPrice;
        @BindView(R.id.item_home_hot_content)
        GridView gridView;


        CommonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class RecommendHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home)
        ImageView ivHome;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_count)
        TextView tvCount;

        RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getLayoutPosition());
                }
            });
        }

    }

    class LiveHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.iv_home)
//        ImageView ivHome;
//        @BindView(R.id.tv_title)
//        TextView tvTitle;
        @BindView(R.id.home_type_live_content)
        ListView listView;

        LiveHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.item_header_show_content)
        GridView gridView;

        HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class PlaceHolder extends RecyclerView.ViewHolder {
        PlaceHolder(View itemView) {
            super(itemView);
        }
    }


    private class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int position) {
            return list.get(position).getSpanCount();
        }
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.catergory)
        GridView gridView;

        CategoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.footer)
        FooterLoading footerLoading;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}