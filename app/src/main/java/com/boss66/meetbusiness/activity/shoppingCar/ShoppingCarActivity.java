package com.boss66.meetbusiness.activity.shoppingCar;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.boss66.meetbusiness.R;
import com.boss66.meetbusiness.activity.base.BaseActivity;
import com.boss66.meetbusiness.adapter.MenuViewTypeAdapter;
import com.boss66.meetbusiness.entity.ViewTypeBean;
import com.boss66.meetbusiness.listener.OnItemClickListener;
import com.boss66.meetbusiness.widget.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liw on 2017/6/19.
 */

public class ShoppingCarActivity extends BaseActivity implements View.OnClickListener {

    private List<ViewTypeBean> mViewTypeBeanList;
    private MenuViewTypeAdapter menuAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_car);
        initUI();
    }

    private void initUI() {

        // 这里只是模拟数据，模拟Item的ViewType，根据ViewType决定显示什么菜单，到时候你可以根据你的数据来决定ViewType。
        mViewTypeBeanList = new ArrayList<>();
        for (int i = 0, j = 0; i < 30; i++, j++) {
            ViewTypeBean viewTypeBean = new ViewTypeBean();
            if (j == 0) {
                viewTypeBean.setViewType(MenuViewTypeAdapter.VIEW_TYPE_MENU_NONE);
                viewTypeBean.setContent("我没有菜单");
            } else if (j == 1) {
                viewTypeBean.setViewType(MenuViewTypeAdapter.VIEW_TYPE_MENU_SINGLE);
                viewTypeBean.setContent("我有1个菜单");
                j=-1;
            }
            mViewTypeBeanList.add(viewTypeBean);
        }
        SwipeMenuRecyclerView menuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.addItemDecoration(new ListViewDecoration());

        menuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        menuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        menuAdapter = new MenuViewTypeAdapter(mViewTypeBeanList);
        menuAdapter.setOnItemClickListener(onItemClickListener);

        menuRecyclerView.setAdapter(menuAdapter);

        findViewById(R.id.img_back).setOnClickListener(this);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_out);
    }
    /**
     * 菜单创建器。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            if (viewType == MenuViewTypeAdapter.VIEW_TYPE_MENU_NONE) {// 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
                // Do nothing.
            } else if (viewType == MenuViewTypeAdapter.VIEW_TYPE_MENU_SINGLE) {// 需要添加单个菜单的Item。
                SwipeMenuItem wechatItem = new SwipeMenuItem(ShoppingCarActivity.this)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.drawable.ic_launcher)
                        .setText("删除")
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(wechatItem);

            }
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(ShoppingCarActivity.this, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(ShoppingCarActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                mViewTypeBeanList.remove(adapterPosition);
                menuAdapter.notifyItemRemoved(adapterPosition);

                mViewTypeBeanList.remove(adapterPosition-1);
                menuAdapter.notifyItemRemoved(adapterPosition-1);

            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(ShoppingCarActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
