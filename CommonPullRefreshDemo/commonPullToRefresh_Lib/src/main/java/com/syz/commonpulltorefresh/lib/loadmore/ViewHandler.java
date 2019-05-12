package com.syz.commonpulltorefresh.lib.loadmore;

import android.view.View;
import android.view.View.OnClickListener;

import com.syz.commonpulltorefresh.lib.loadmore.ILoadViewMoreFactory.ILoadMoreView;

public interface ViewHandler {

	/**
	 * 
	 * @param view
	 * @param adapter
	 * @param loadMoreView
	 * @param onClickListener
	 * @return 是否有 init ILoadMoreView
	 */
	public boolean handleSetAdapter(View contentView, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);

	public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener);

}
