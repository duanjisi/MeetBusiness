package com.atgc.cotton.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.presenter.GoodsDetailPresenter;
import com.atgc.cotton.presenter.view.IGoodsDetailView;
import com.atgc.cotton.presenter.view.INormalView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品详情页
 * Created by liw on 2017/7/5.
 */

public class GoodsDetailActivity extends MvpActivity<GoodsDetailPresenter> implements IGoodsDetailView {


    @Bind(R.id.img_content)
    ImageView imgContent;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.rl_classify)
    RelativeLayout rlClassify;
    @Bind(R.id.img_more)
    ImageView imgMore;
    @Bind(R.id.img_back)
    ImageView imgBack;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPresenter.loadDataByRetrofitRxjava("101310222");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this);
    }



    @OnClick({R.id.img_content, R.id.rl_classify,R.id.img_more, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_content:
                break;
            case R.id.rl_classify:   //选择分类
                showToast("选择分类");
                if(dialog==null){

                    showGoodsDialog();
                }else {
                    dialog.show();
                }
                break;
            case R.id.img_more:
                showToast("more");


                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void showGoodsDialog() {

        dialog = new Dialog(this, R.style.Dialog_full);
        View view = View.inflate(this,R.layout.dialog_goods,null);
        dialog.setContentView(view);
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("加入购物车");

            }
        });
        view.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买
                openActivity(WriteOrderActivity.class);

            }
        });

        Window dialogWindow = dialog.getWindow();

        dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


}
