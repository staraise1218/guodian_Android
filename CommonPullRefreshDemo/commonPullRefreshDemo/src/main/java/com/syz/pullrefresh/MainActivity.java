package com.syz.pullrefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author syz
 * @date 2016-4-24
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goListView(View view) {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    public void goRecyclerView(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    public void goGridView(View view) {
        startActivity(new Intent(this, GridViewAtivity.class));
    }

    public void goSwipeListView(View view) {
        startActivity(new Intent(this, SwipeListViewActivity.class));
    }
}
