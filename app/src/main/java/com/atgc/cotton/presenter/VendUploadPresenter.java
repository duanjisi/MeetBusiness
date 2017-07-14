package com.atgc.cotton.presenter;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.GoodsDetailEntity;
import com.atgc.cotton.entity.VendGoodsEntity;
import com.atgc.cotton.presenter.view.IVendUploadView;
import com.atgc.cotton.retrofit.MyObserver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GMARUnity on 2017/7/10.
 */
public class VendUploadPresenter extends BasePresenter<IVendUploadView> {
    public VendUploadPresenter(IVendUploadView mvpView) {
        super(mvpView);
    }

    /**
     * 上传商品
     *
     * @param map
     */
    public void uploadGoods(Map<String, Object> map) {
        Map<String, RequestBody> params = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof String) {
                RequestBody body = RequestBody.create(MediaType.parse("text/plain"), (String) o);
                params.put(entry.getKey(), body);
            } else if (o instanceof File) {
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), (File) o);
                params.put(entry.getKey() + "\"; filename=\"" + ((File) o).getName() + "", body);
            }
        }
        mvpView.showLoading();
        addSubscription(api.uploadMyVendGoods(App.getInstance().getToken(), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult entity = BaseResult.parse(s);
                        if (entity != null) {
                            if (entity.getCode() == 0 && entity.getStatus() == 200) {
                                mvpView.upLoadSccess();
                            } else {
                                mvpView.onError(entity.getMessage());
                            }
                        } else {
                            mvpView.onError(s);
                        }
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.onError(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }

    /**
     * 获取商品详情
     *
     * @param id
     */
    public void getGoodsDetail(String id) {
        mvpView.showLoading();
        addSubscription(api.getGoodsDetail(Integer.parseInt(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        GoodsDetailEntity entity = JSON.parseObject(s, GoodsDetailEntity.class);
                        if (entity != null) {
                            if (entity.getCode() == 0 && entity.getStatus() == 200) {
                                GoodsDetailEntity.DataBean dataBean = entity.getData();
                                if (dataBean != null) {
                                    mvpView.getGoodsDetail(dataBean);
                                }
                            } else {
                                mvpView.onError(entity.getMessage());
                            }
                        } else {
                            mvpView.onError(s);
                        }
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.onError(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }

    /**
     * 修改商品
     *
     * @param map
     */
    public void changeMyGoods(Map<String, Object> map, int id) {
        Map<String, RequestBody> params = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof String) {
                RequestBody body = RequestBody.create(MediaType.parse("text/plain"), (String) o);
                params.put(entry.getKey(), body);
            } else if (o instanceof File) {
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), (File) o);
                params.put(entry.getKey() + "\"; filename=\"" + ((File) o).getName() + "", body);
            }
        }
        mvpView.showLoading();
        addSubscription(api.changeMyGoods(App.getInstance().getToken(), id, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult entity = BaseResult.parse(s);
                        if (entity != null) {
                            if (entity.getCode() == 0 && entity.getStatus() == 200) {
                                mvpView.changeGoodsSuccess();
                            } else {
                                mvpView.onError(entity.getMessage());
                            }
                        } else {
                            mvpView.onError(s);
                        }
                    }

                    @Override
                    public void onError_(String msg) {
                        mvpView.hideLoading();
                        mvpView.onError(msg);
                    }

                    @Override
                    public void onCompleted_() {

                    }
                }));
    }
}
