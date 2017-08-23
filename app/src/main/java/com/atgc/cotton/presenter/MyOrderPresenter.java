package com.atgc.cotton.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.entity.AlipayOrder;
import com.atgc.cotton.entity.BaseResult;
import com.atgc.cotton.entity.MyOrderEntity;
import com.atgc.cotton.entity.OrderEvaluateEntity;
import com.atgc.cotton.entity.PayWx;
import com.atgc.cotton.entity.WxOrder;
import com.atgc.cotton.presenter.view.IMyOrderView;
import com.atgc.cotton.retrofit.MyObserver;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GMARUnity on 2017/7/14.
 */
public class MyOrderPresenter extends BasePresenter<IMyOrderView> {
    public MyOrderPresenter(IMyOrderView mvpView) {
        super(mvpView);
    }

    /**
     * 获取我买到的订单
     */
    public void getMyBuyOrder(final int status, int page, int size) {
        mvpView.showLoading();
        Log.i("token:", " " + App.getInstance().getToken() + "   " + App.getInstance().getUid());
        addSubscription(api.getMyBuyOrder(App.getInstance().getToken(), status, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        Log.i("order", "" + status + " " + s);
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                MyOrderEntity data = JSON.parseObject(s, MyOrderEntity.class);
                                if (data != null && data.getData() != null) {
                                    mvpView.getMyOrderSuccess(true, data.getData());
                                }
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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
     * 获取我买到的待评价订单
     */
    public void getMyBuyEvaluateOrder(int page, int size) {
        mvpView.showLoading();
        addSubscription(api.getMyBuyEvaluateOrder(App.getInstance().getToken(), page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                OrderEvaluateEntity data = JSON.parseObject(s, OrderEvaluateEntity.class);
                                if (data != null && data.getData() != null)
                                    mvpView.getMyEvaluateOrderSuccess(data.getData());
                            } else {
                                mvpView.getMyEvaluateOrderSuccess(null);
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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
     * 获取我的订单列表(我卖出的)
     */
    public void getMySellOrder(int status, int page, int size) {
        mvpView.showLoading();
        addSubscription(api.getMySellOrder(App.getInstance().getToken(), status, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                MyOrderEntity data = JSON.parseObject(s, MyOrderEntity.class);
                                if (data != null && data.getData() != null) {
                                    mvpView.getMyOrderSuccess(false, data.getData());
                                }
                            } else {
                                mvpView.getMyOrderSuccess(false, null);
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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
     * 删除订单
     */
    public void deleteMyBuyOrder(int id) {
        mvpView.showLoading();
        addSubscription(api.deleteMyBuyOrder(App.getInstance().getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //买家操作订单 只操作自己的订单，操作包括：取消订单 确认收货
    //操作类型 cancel:取消订单 confirm:确认收货
//    public void operateMyOrder(Map<String,String> map){
//        mvpView.showLoading();
//        addSubscription(api.operateMyOrder(App.getInstance().getToken(), map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObserver<String>() {
//                    @Override
//                    public void onNext_(String s) {
//                        mvpView.hideLoading();
//                        BaseResult result = BaseResult.parse(s);
//                        if (result != null) {
//                            if (result.getCode() == 0 && result.getStatus() == 200) {
//                                mvpView.deleteOrderSuccess();
//                            } else {
//                                mvpView.onError(result.getMessage());
//                            }
//                        } else {
//                            mvpView.onError("服务器异常，请稍后重试");
//                        }
//                    }
//
//                    @Override
//                    public void onError_(String msg) {
//                        mvpView.hideLoading();
//                        mvpView.onError(msg);
//                    }
//
//                    @Override
//                    public void onCompleted_() {
//
//                    }
//                }));
//    }

    //操作类型 cancel:取消订单
    public void cancelOrder(int orderid) {
        mvpView.showLoading();
        addSubscription(api.cancelOrder(App.getInstance().getToken(), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //confirm:确认收货
    public void confirmOrder(int orderid) {
        mvpView.showLoading();
        addSubscription(api.confirmOrder(App.getInstance().getToken(), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //cancelRefund:买家取消退款申请
    public void cancelRefund(int orderid) {
        mvpView.showLoading();
        addSubscription(api.cancelRefund(App.getInstance().getToken(), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //refund:买退款申请
    public void refund(int orderid) {
        mvpView.showLoading();
        addSubscription(api.refund(App.getInstance().getToken(), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //agreeRefund:卖家同意退款
    public void agreeRefund(int orderid) {
        mvpView.showLoading();
        addSubscription(api.agreeRefund(App.getInstance().getToken(), orderid, "android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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

    //disAgreeRefund:卖家拒绝退款
    public void disAgreeRefund(int orderid) {
        mvpView.showLoading();
        addSubscription(api.disAgreeRefund(App.getInstance().getToken(), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        BaseResult result = BaseResult.parse(s);
                        if (result != null) {
                            if (result.getCode() == 0 && result.getStatus() == 200) {
                                mvpView.deleteOrderSuccess();
                            } else {
                                mvpView.onError(result.getMessage());
                            }
                        } else {
                            mvpView.onError("服务器异常，请稍后重试");
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
     * 支付宝付款
     */
    public void aliPay(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        mvpView.showLoading();
        addSubscription(api.alipay(App.getInstance().getToken(), id, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        AlipayOrder alipayOrder = JSON.parseObject(s, AlipayOrder.class);
                        if (alipayOrder != null) {
                            int code = alipayOrder.getCode();
                            if (code == 0) {
                                mvpView.alipaySuccess(alipayOrder);

                            } else {
                                mvpView.onError(alipayOrder.getMessage());
                            }

                        } else {
                            mvpView.onError(alipayOrder.getMessage());
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
     * 微信付款
     */
    public void wxPay(int id) {
        mvpView.showLoading();
        addSubscription(api.wxpay(App.getInstance().getToken(), id, "android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onNext_(String s) {
                        mvpView.hideLoading();
                        WxOrder wxOrder = JSON.parseObject(s, WxOrder.class);
                        if (wxOrder != null) {
                            int code = wxOrder.getCode();
                            if (code == 0) {
                                PayWx data = wxOrder.getData();
                                if (data != null) {
                                    mvpView.wxpaySuccess(data);
                                }
                            } else {
                                mvpView.onError(wxOrder.getMessage());
                            }
                        } else {
                            mvpView.onError(wxOrder.getMessage());
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
