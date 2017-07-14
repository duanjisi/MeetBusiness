package com.atgc.cotton.activity.goodsDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.adapter.GoodsClassifyAdapter;
import com.atgc.cotton.entity.GoodsDetailEntity;
import com.atgc.cotton.entity.VendGoodsAttrEntity;
import com.atgc.cotton.presenter.GoodsDetailPresenter;
import com.atgc.cotton.presenter.view.IGoodsDetailView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.UIUtils;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情页
 * Created by liw on 2017/7/5.
 */

public class GoodsDetailActivity extends MvpActivity<GoodsDetailPresenter> implements IGoodsDetailView {


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
    @Bind(R.id.vp_img)
    ViewPager vpImg;
    @Bind(R.id.ll_oval)
    LinearLayout llOval;
    @Bind(R.id.line)
    View line;

    private Dialog dialog;
    private ArrayList<String> imgList;
    private ArrayList<ImageView> views;
    private List<ImageView> point_views;
    private List<VendGoodsAttrEntity> goodsAttr;
    private int goodsPrice;
    private int goodsNumber;
    private GoodsClassifyAdapter adapter;
    private String goodsName;

    protected void initUI() {
//        int screenWidth = UIUtils.getScreenWidth(this);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vpImg.getLayoutParams();
//        layoutParams.height = (int) (screenWidth*0.7);
//        vpImg.setLayoutParams(layoutParams);


    }

    protected void initData() {
        //TODO 先用测试数据
        mPresenter.getGoodsDetail(56);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initUI();
        initData();
    }


    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this);
    }


    @OnClick({R.id.rl_classify, R.id.img_more, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.rl_classify:   //选择分类
                if (dialog == null) {

                    showGoodsDialog();
                } else {
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

    /**
     * 选择分类dialog
     */
    private void showGoodsDialog() {

        dialog = new Dialog(this, R.style.Dialog_full);
        View view = View.inflate(this, R.layout.dialog_goods, null);
        dialog.setContentView(view);

        ImageView img_head = (ImageView) view.findViewById(R.id.img_head);
        Glide.with(context).load(imgList.get(0)).into(img_head);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_price.setText("¥ " + goodsPrice);
        tv_num.setText("库存:  " + goodsNumber);

        LRecyclerView rv_content = (LRecyclerView) view.findViewById(R.id.rv_content);
        rv_content.setPullRefreshEnabled(false);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoodsClassifyAdapter(this);
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_content.setAdapter(lRecyclerViewAdapter);

        adapter.setDatas(goodsAttr);
        adapter.notifyDataSetChanged();
        //底部布局
        View footView = LayoutInflater.from(context).inflate(R.layout.foot_view_type, null);
        lRecyclerViewAdapter.addFooterView(footView);

        //购买数量
        final EditText et_repertory = (EditText) footView.findViewById(R.id.et_repertory);


        //减购买数量
        footView.findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_repertory.getText().toString();
                if (!TextUtils.isEmpty(num)){
                    if(Integer.parseInt(num)>0){
                        num = Integer.parseInt(num)-1+"";
                        et_repertory.setText(num);
                    }


                }
            }
        });
        //加购买数量
        footView.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_repertory.getText().toString();
                if (!TextUtils.isEmpty(num)){
                    num = Integer.parseInt(num)+1+"";
                    et_repertory.setText(num);

                }

            }
        });

        //加入购物车
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast("加入购物车");
                Map<String, Set<Integer>> map2 = adapter.getMap2();   //是否选中
                Map<String, String> map = adapter.getMap();     //选中的分类类别 和，分类名
                for (Map.Entry<String, Set<Integer>> entry : map2.entrySet()) {
                    String key = entry.getKey();
                    Set<Integer> value = entry.getValue();

                    if(value.isEmpty()){ //选中状态为空
                        showToast("请选择: "+key);
                        return;
                    }
                }

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();    // 分类类别
                    String value = entry.getValue(); //分类名
                    L.i(key+value);

                }
                //购买数量
                String buyNum = et_repertory.getText().toString();


            }
        });

        view.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买
                dialog.dismiss();
                String type="";      //选择的类别
                Map<String, Set<Integer>> map2 = adapter.getMap2();   //是否选中
                Map<String, String> map = adapter.getMap();     //选中的分类类别 和，分类名
                for (Map.Entry<String, Set<Integer>> entry : map2.entrySet()) {
                    String key = entry.getKey();
                    Set<Integer> value = entry.getValue();

                    if(value.isEmpty()){ //选中状态为空
                        showToast("请选择: "+key);
                        return;
                    }
                }

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();    // 分类类别
                    String value = entry.getValue(); //分类名
                    L.i(key+value);
                    type = type+ key+": "+value+"  ";
                }
                //购买数量
                String buyNum = et_repertory.getText().toString();
                Intent intent = new Intent(context, WriteOrderActivity.class);

                intent.putExtra("buyNum",buyNum);
                intent.putExtra("type",type);
                intent.putExtra("goodsPrice",goodsPrice);
                String imgUrl = imgList.get(0);
                intent.putExtra("imgUrl",imgUrl);
                intent.putExtra("goodsName",goodsName);


                startActivity(intent);


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

        //不弹出软键盘
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 查询商品详情成功
     *
     * @param bean
     */
    @Override
    public void getGoodsSuccess(GoodsDetailEntity.DataBean bean) {

        imgList = new ArrayList<>();      //商品图链接
        views = new ArrayList<>();   //商品图
        point_views = new ArrayList<>();    //小圆点

        List<GoodsDetailEntity.DataBean.GoodsGalleryBean> goodsGallery = bean.getGoodsGallery();
        for (int i = 0; i < goodsGallery.size(); i++) {
            String imgUrl = goodsGallery.get(i).getImgUrl();
            imgList.add(imgUrl);
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);

            ImageView view1 = new ImageView(this);

            if (i == 0) {
                view1.setImageResource(R.drawable.oval_choose);
            } else {
                view1.setImageResource(R.drawable.oval_default);
            }
            llOval.addView(view1);
            point_views.add(view1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view1.getLayoutParams();
            params.leftMargin = UIUtils.dip2px(context, 10);
            view1.setLayoutParams(params);
        }

        //商品名
        goodsName = bean.getGoodsName();

        //商品价格
        goodsPrice = bean.getGoodsPrice();
        goodsAttr = bean.getGoodsAttr();
        goodsNumber = bean.getGoodsNumber();


        tvPrice.setText("¥ " + goodsPrice);

        tvName.setText(goodsName);

        vpImg.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ImageView view = views.get(position);
                Glide.with(context).load(imgList.get(position)).into(view);
                container.addView(view);
                return view;

            }
        });
        vpImg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                for (int i = 0; i < point_views.size(); i++) {
                    if (i == position) {
                        point_views.get(i).setImageResource(R.drawable.oval_choose);
                    } else {
                        point_views.get(i).setImageResource(R.drawable.oval_default);
                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}