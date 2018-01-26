package com.atgc.cotton.activity.goodsDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.ImagePagerActivity;
import com.atgc.cotton.activity.LoginActivity;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.activity.shoppingCar.ShoppingCarActivity;
import com.atgc.cotton.adapter.GoodsClassifyAdapter;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.GoodsDetailEntity;
import com.atgc.cotton.entity.GoodsEvaluateEntity;
import com.atgc.cotton.entity.OrderGoods;
import com.atgc.cotton.entity.OrderGoodsListEntity;
import com.atgc.cotton.entity.PhotoInfo;
import com.atgc.cotton.entity.VendGoodsAttrEntity;
import com.atgc.cotton.presenter.GoodsDetailPresenter;
import com.atgc.cotton.presenter.view.IGoodsDetailView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.UIUtils;
import com.atgc.cotton.widget.MultiImageView;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情页
 * Created by liw on 2017/7/5.
 */

public class GoodsDetailActivity extends MvpActivity<GoodsDetailPresenter> implements IGoodsDetailView {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.rl_classify)
    RelativeLayout rlClassify;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.vp_img)
    ViewPager vpImg;
    @BindView(R.id.ll_oval)
    LinearLayout llOval;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.multiImagView)
    MultiImageView multiImagView;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R.id.tv_more)
    TextView tvMore;

    private Dialog dialog;
    private ArrayList<String> imgList;
    private ArrayList<ImageView> views;
    private List<ImageView> point_views;
    private Double goodsPrice;
    private int goodsNumber;
    private GoodsClassifyAdapter adapter;
    private String goodsName;
    private String userName;
    private List<VendGoodsAttrEntity> goodsAttr;
    private int goodsId;
    private DbUtils mDbUtils;
    private String userId;
    private List<VendGoodsAttrEntity> newAttr;
    private List<OrderGoods> all;
    private String goodId = "75";
    private String buyNum;
    private TextView tv_num;

    protected void initUI() {

    }

    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            goodId = bundle.getString("goodId", "");
            Log.i("info", "===============goodId:" + goodId);
            if (!TextUtils.isEmpty(goodId)) {
                mPresenter.getGoodsDetail(Integer.parseInt(goodId));
                mDbUtils = DbUtils.create(this);
            }
        }
//          先用测试数据
//        mPresenter.getGoodsDetail(Integer.parseInt(goodId));
//        mDbUtils = DbUtils.create(this);
        mPresenter.searchEvaluate(App.getInstance().getToken(), Integer.parseInt(goodId), 1, 10);
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


    @OnClick({R.id.rl_classify, R.id.img_more, R.id.img_back, R.id.tv_evaluate, R.id.tv_more})
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

            case R.id.tv_evaluate:
                Intent intent = new Intent(GoodsDetailActivity.this, GoodsEvaluateActivity.class);
                intent.putExtra("goodId", goodId);
                startActivity(intent);
                break;
            case R.id.tv_more:
                Intent intent1 = new Intent(GoodsDetailActivity.this, GoodsEvaluateActivity.class);
                intent1.putExtra("goodId", goodId);
                startActivity(intent1);
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
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_price.setText("¥ " + goodsPrice);
        tv_num.setText("库存:  " + goodsNumber);

        LRecyclerView rv_content = (LRecyclerView) view.findViewById(R.id.rv_content);
        rv_content.setPullRefreshEnabled(false);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoodsClassifyAdapter(this);
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_content.setAdapter(lRecyclerViewAdapter);

        adapter.setDatas(newAttr);
        adapter.notifyDataSetChanged();
        //底部布局
        View footView = LayoutInflater.from(context).inflate(R.layout.foot_view_type, null);
        lRecyclerViewAdapter.addFooterView(footView);

        //购买数量
        final EditText et_repertory = (EditText) footView.findViewById(R.id.et_repertory);
        et_repertory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    if (!s.equals("")) {
                        if (Integer.parseInt(s + "") == 0) {
                            et_repertory.setText("0");
                        }
                        if (Integer.parseInt(s + "") > goodsNumber) {
                            et_repertory.setText(goodsNumber);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //减购买数量
        footView.findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_repertory.getText().toString();
                if (!TextUtils.isEmpty(num)) {
                    if (Integer.parseInt(num) > 0) {
                        num = Integer.parseInt(num) - 1 + "";
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
                if (!TextUtils.isEmpty(num)) {
                    if (Integer.parseInt(num) < goodsNumber) {
                        num = Integer.parseInt(num) + 1 + "";
                        et_repertory.setText(num);
                    }

                }

            }
        });

        //加入购物车
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.getInstance().isLogin()) {
                    openActivity(LoginActivity.class);
                }

                if (("" + goodsPrice).equals("0.0")) {
                    showToast("商品价格为零");
                    return;
                }

                String xNum = et_repertory.getText().toString();
                if (goodsNumber < Integer.parseInt(xNum)) {
                    showToast("库存不足");
                    return;
                }
                Map<String, Set<Integer>> map2 = adapter.getMap2();   //是否选中
                Map<String, String> map = adapter.getMap();     //选中的分类类别 和，分类名
                for (Map.Entry<String, Set<Integer>> entry : map2.entrySet()) {
                    String key = entry.getKey();
                    Set<Integer> value = entry.getValue();
                    if (value.isEmpty()) { //选中状态为空
                        showToast("请选择: " + key);
                        return;
                    }
                }
                String type = "";      //选择的类别
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();    // 分类类别
                    String value = entry.getValue(); //分类名
                    L.i(key + value);
                    type = type + key + ":" + value + "|"; //暂时用空格分开
                }

                if (!TextUtils.isEmpty(type)) {
                    type = type.substring(0, type.length() - 1);
                }
                //购买数量
                buyNum = et_repertory.getText().toString();
                String imgUrl = imgList.get(0);

                //加入购物车的商品
                OrderGoods entity = new OrderGoods();
                entity.setBuyNum(Integer.parseInt(buyNum));
                entity.setGoodsName(goodsName);
                entity.setGoodsPrice(goodsPrice);
                entity.setImgUrl(imgUrl);
                entity.setTitle(userName);
                entity.setType(type);
                entity.setGoodsId(goodsId);
                entity.setUserId(userId);

                //加入购物车，即存入数据库
                try {
                    //查询数据库，如果看是插入还是更新.  通过商品id和商品分类
                    List<OrderGoods> list = mDbUtils.findAll(OrderGoods.class);

                    List<OrderGoods> entityList = new ArrayList<OrderGoods>();

                    if (list == null || list.size() == 0) {
                        //数据库没商品
                        entityList.add(entity);
                        saveAll(entityList);
                    } else {
                        //数据库有商品
                        if (list.size() > 0) {
                            //先清空数据库
                            mDbUtils.deleteAll(list);
                            boolean isUpdata = false;
                            int updataNum = 0;
                            int updataPos = 0;

                            for (int i = 0; i < list.size(); i++) {

                                int id = list.get(i).getGoodsId();
                                String type1 = list.get(i).getType();
                                int num = list.get(i).getBuyNum();
                                //id相同，type相同就更新，否则插入
                                if (id == goodsId && type1.equals(type)) {
                                    isUpdata = true;
                                    updataNum = num;
                                    updataPos = i;
                                }
                            }
                            if (isUpdata) {
                                list.get(updataPos).setBuyNum(updataNum + Integer.parseInt(buyNum));
                                saveAll(list);
                            } else {
                                list.add(entity);
                                saveAll(list);
                            }
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买
                if (!App.getInstance().isLogin()) {
                    openActivity(LoginActivity.class);
                }
                if (goodsNumber < 1) {
                    showToast("库存不足");
                    return;
                }
                dialog.dismiss();
                Map<String, Set<Integer>> map2 = adapter.getMap2();   //是否选中
                Map<String, String> map = adapter.getMap();     //选中的分类类别 和，分类名
                for (Map.Entry<String, Set<Integer>> entry : map2.entrySet()) {
                    String key = entry.getKey();
                    Set<Integer> value = entry.getValue();
                    if (value.isEmpty()) { //选中状态为空
                        showToast("请选择: " + key);
                        return;
                    }
                }
                String type = "";      //选择的类别
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();    // 分类类别
                    String value = entry.getValue(); //分类名
                    L.i(key + value);
                    type = type + key + ": " + value + "  ";
                }
                //购买数量
                buyNum = et_repertory.getText().toString();
                String imgUrl = imgList.get(0);
                OrderGoods entity = new OrderGoods();
                entity.setBuyNum(Integer.parseInt(buyNum));
                entity.setGoodsName(goodsName);
                entity.setGoodsPrice(goodsPrice);
                entity.setImgUrl(imgUrl);
                entity.setTitle(userName);
                entity.setType(type);
                entity.setGoodsId(goodsId);
                entity.setUserId(userId);

                List<OrderGoods> datas = new ArrayList<>();

                //加上店铺名
                OrderGoods orderGoodsEntity = new OrderGoods();
                orderGoodsEntity.setHead(1);    //用head来做布局不同的显示
                orderGoodsEntity.setTitle(userName); //显示店铺名布局
                orderGoodsEntity.setGoodsId(goodsId); //用id来做选中状态
                orderGoodsEntity.setUserId(userId);
                datas.add(orderGoodsEntity);

                //加上商品
                datas.add(entity);
                OrderGoodsListEntity entity1 = new OrderGoodsListEntity();
                entity1.setData(datas);
                String goodsJson = JSON.toJSONString(entity1);
                L.i(goodsJson);
                //订单页
                Intent intent = new Intent(context, WriteOrderActivity.class);

                intent.putExtra("goodsJson", goodsJson);


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

    private void saveAll(List<OrderGoods> list) {
        try {
            L.i(list.toString());
            mDbUtils.saveAll(list);
//            mDbUtils.deleteAll(OrderGoods.class);      //调试用
            showToast("已加入购物车");
            goodsNumber = goodsNumber - Integer.parseInt(buyNum);
            tv_num.setText("库存:  " + goodsNumber);
            goShoppingCar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void goShoppingCar() {
        Intent intent = new Intent(context, ShoppingCarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_no);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String url = (String) view.getTag();
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            int position = 0;
            for (int i = 0; i < imgList.size(); i++) {
                if (imgList.get(i).equals(url)) {
                    position = i;
                }
            }
            ImagePagerActivity.startImagePagerActivity(context, imgList, position, imageSize, false);
        }
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
        }

        for (int i = 0; i < goodsGallery.size(); i++) {
            final String imgUrl = goodsGallery.get(i).getImgUrl();
//            imgList.add(imgUrl);
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewPager.LayoutParams());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    int position = 0;
                    for (int i = 0; i < imgList.size(); i++) {
                        if (imgList.get(i).equals(imgUrl)) {
                            position = i;
                        }
                    }
                    ImagePagerActivity.startImagePagerActivity(context, imgList, position, imageSize, false);
                }
            });
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
        goodsId = bean.getGoodsId();
        userId = bean.getUserId();

        //用户名--即店铺名
        userName = bean.getUserName();

        newAttr = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        //给 goodsAttr做处理
        if (goodsAttr != null && goodsAttr.size() != 0) {
            for (int i = 0; i < goodsAttr.size(); i++) {
                String attrName = goodsAttr.get(i).getAttrName();
                if (!keys.contains(attrName)) {
                    keys.add(attrName);
                    newAttr.add(goodsAttr.get(i));
                } else {
                    String attrValue1 = goodsAttr.get(i).getAttrValue();
                    for (int j = 0; j < newAttr.size(); j++) {
                        if (newAttr.get(j).getAttrName().equals(attrName)) {
                            String attrValue = newAttr.get(j).getAttrValue();
                            attrValue = attrValue + "@#" + attrValue1;
                            newAttr.get(j).setAttrValue(attrValue);
                        }
                    }
                }
            }
        }
        bean.setGoodsAttr(newAttr);
        //还有查询数据库让库存显示正确
        try {
            all = mDbUtils.findAll(OrderGoods.class);
            if (all != null) {
                for (int i = 0; i < all.size(); i++) {
                    if ((all.get(i).getGoodsId() + "").equals(goodId)) {
                        int buyNum = all.get(i).getBuyNum();
                        goodsNumber = goodsNumber - buyNum;
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

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

    @Override
    public void getEvaluteSuccess(String s) {
        BaseResult result = JSON.parseObject(s, BaseResult.class);
        if (result.getCode() == 1) {
//            showToast("没有更多数据");
            return;
        }
        Log.i("info", "====================s:" + s);
        GoodsEvaluateEntity entity = JSON.parseObject(s, GoodsEvaluateEntity.class);
        if (entity != null) {
            int code = entity.getCode();
            if (code == 0) {
                List<GoodsEvaluateEntity.DataBean> datas = entity.getData();
                Log.i("info", "====================datas:" + datas);
                if (datas != null && datas.size() != 0) {
                    GoodsEvaluateEntity.DataBean dataBean = datas.get(0);
                    llEvaluate.setVisibility(View.VISIBLE);
                    if (dataBean != null) {
                        Glide.with(context).load(dataBean.getAvatar()).into(imgHead);
                        tvTitle.setText(dataBean.getContent());
                        tvContent.setText(dataBean.getContent());
                        List<String> pics = dataBean.getPics();
                        List<PhotoInfo> photos = new ArrayList<>();
                        for (String s1 : pics) {
                            PhotoInfo photoInfo = new PhotoInfo();
                            photoInfo.file_thumb = s1;
                            photoInfo.type = 0;
                            photos.add(photoInfo);
                        }
                        multiImagView.setSceenW(UIUtils.getScreenWidth(context));
                        multiImagView.setList(photos);
                    }
                }
            }
        } else {
            showToast("服务器异常");
        }
    }

    @Override
    public void onError(String s) {
        Log.i("info", "================msg:" + s);
        showToast(s);
    }
}
