package com.atgc.cotton.activity.goodsDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.App;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.base.BaseActivity;
import com.atgc.cotton.activity.base.MvpActivity;
import com.atgc.cotton.entity.LocalAddressEntity;
import com.atgc.cotton.event.RefreshAddress;
import com.atgc.cotton.http.HttpUrl;
import com.atgc.cotton.presenter.AddAddressPresenter;
import com.atgc.cotton.presenter.view.IBaseView;
import com.atgc.cotton.presenter.view.INormalView;
import com.atgc.cotton.util.L;
import com.atgc.cotton.util.OkManager;
import com.atgc.cotton.widget.wheel.ArrayWheelAdapter;
import com.atgc.cotton.widget.wheel.OnWheelChangedListener;
import com.atgc.cotton.widget.wheel.WheelView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 添加地址
 * Created by liw on 2017/7/11.
 */
public class EditAddressActivity extends MvpActivity<AddAddressPresenter> implements View.OnClickListener, OnWheelChangedListener, INormalView {
    private LocalAddressEntity jsonDate;


    private Dialog dialog; //省市区dialog


    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;


    private List<LocalAddressEntity.ThreeChild> proviceList;       //省
    private List<LocalAddressEntity.FourChild> cityList;      //市
    private List<LocalAddressEntity.LastChild> districtList;   //县

    protected String[] mProvinceDatas;//所有省名字
    protected String[] mProvinceDatasId;//所有省id

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mCitisDatasMap2 = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap2 = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";


    private String province_id;
    private String city_id;
    private String county_id;

    private TextView tv_content;

    private EditText et_people;
    private EditText et_phone;
    private EditText et_address;
    private ToggleButton tb_default;

    private int isDefault = 1; //是否默认 1默认 0不默认
    private String contact;   //手机号
    private String consignee; //收件人
    private String address; //详细地址
    private int addressId;

    @Override
    protected void initUI() {
        super.initUI();
        Intent intent = getIntent();
        if (intent != null) {
            contact = intent.getStringExtra("contact");
            consignee = intent.getStringExtra("consignee");
            address = intent.getStringExtra("address");
            isDefault = intent.getIntExtra("isDefault", -1);
            addressId = intent.getIntExtra("id", -1);
        }
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.rl_address).setOnClickListener(this);
        findViewById(R.id.tv_choose).setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);

        et_people = (EditText) findViewById(R.id.et_people);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);

        tb_default = (ToggleButton) findViewById(R.id.tb_default);
        tb_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDefault = 1;
                } else {
                    isDefault = 0;
                }
            }
        });

        et_people.setText(consignee);
        et_phone.setText(contact);
        et_address.setText(address);
        if (isDefault == 1) {
            tb_default.setChecked(true);
        } else {
            tb_default.setChecked(false);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        initCityData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected AddAddressPresenter createPresenter() {
        return new AddAddressPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_save: //编辑或保存
                if (consignee != null) {

                    editAddress();
                } else {

                    saveAddress();
                }
                break;
            case R.id.rl_address: //省市区
                showAddressSelection();

                break;
            case R.id.tv_choose: //省市区
                showAddressSelection();

                break;
        }


    }

    private void editAddress() {

        String people = et_people.getText().toString();
        String phone = et_phone.getText().toString();
        String address = et_address.getText().toString();
        String location = tv_content.getText().toString();

        if (people.length() == 0 || phone.length() == 0 || address.length() == 0 || location.length() == 0) {
            showToast("请完善资料");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("province", province_id);
        map.put("city", city_id);
        map.put("district", county_id);
        map.put("contact", phone);
        map.put("consignee", people);
        map.put("address", address);
        map.put("isdefault", isDefault + "");   //1是默认 不是   该参数可不传
        Log.i("liwya", map.toString());

        String token = App.getInstance().getToken();
        mPresenter.editAddress(token, addressId, map);
    }

    private void saveAddress() {

        String people = et_people.getText().toString();
        String phone = et_phone.getText().toString();
        String address = et_address.getText().toString();
        String location = tv_content.getText().toString();

        if (people.length() == 0 || phone.length() == 0 || address.length() == 0 || location.length() == 0) {
            showToast("请完善资料");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("province", province_id);
        map.put("city", city_id);
        map.put("district", county_id);
        map.put("contact", phone);
        map.put("consignee", people);
        map.put("address", address);
        map.put("isdefault", isDefault + "");   //1是默认 不是   该参数可不传
        Log.i("liwya", map.toString());

        String token = App.getInstance().getToken();
        mPresenter.addAddress(token, map);

    }


    private LocalAddressEntity initCityData() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("province.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            jsonDate = JSON.parseObject(stringBuilder.toString(), LocalAddressEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonDate;

    }


    private void showAddressSelection() {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Dialog_full);
            View view_dialog = View.inflate(this,
                    R.layout.dialog_shipping_address_select, null);
            // 省
            mViewProvince = (WheelView) view_dialog
                    .findViewById(R.id.id_province);
            // 市
            mViewCity = (WheelView) view_dialog.findViewById(R.id.id_city);
            // 区或县
            mViewDistrict = (WheelView) view_dialog.findViewById(R.id.id_district);
            TextView btn_cancle = (TextView) view_dialog.findViewById(R.id.btn_cancle);
            TextView mBtnConfirm = (TextView) view_dialog.findViewById(R.id.btn_confirm);
            btn_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            // 添加onclick事件
            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("liwya", province_id + "-" + city_id + "-" + county_id);
                    tv_content.setText(mCurrentProviceName + "  " + mCurrentCityName + "  " + mCurrentDistrictName);
                    dialog.dismiss();
                }
            });
            // 设置可见条目数量
            mViewProvince.setVisibleItems(7);
            mViewCity.setVisibleItems(7);
            mViewDistrict.setVisibleItems(7);
            // 添加change事件
            mViewProvince.addChangingListener(this);
            // 添加change事件
            mViewCity.addChangingListener(this);
            // 添加change事件
            mViewDistrict.addChangingListener(this);

            dialog.setContentView(view_dialog);

            Window dialogWindow = dialog.getWindow();
            dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            dialogWindow.setAttributes(lp);
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.show();
        setUpData();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            county_id = mDistrictDatasMap2.get(mCurrentCityName)[newValue];
        }
    }


    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(this,
                mProvinceDatas));

        updateCities();
        updateAreas();
    }


    protected void initProvinceDatas() {
        LocalAddressEntity.SecondChild result = jsonDate.getResult();
        proviceList = result.getList();
        mProvinceDatas = new String[proviceList.size()];

        mProvinceDatasId = new String[proviceList.size()];
        for (int i = 0; i < proviceList.size(); i++) {
            mProvinceDatas[i] = proviceList.get(i).getRegion_name(); //省
            mProvinceDatasId[i] = proviceList.get(i).getRegion_id();

            cityList = proviceList.get(i).getList();
            String[] cityNames = new String[cityList.size()];
            String[] cityIds = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getRegion_name();     //市
                cityIds[j] = cityList.get(j).getRegion_id();     //市

                //县
                districtList = cityList.get(j).getList();

                String[] distrinctArray = new String[districtList
                        .size()];
                String[] distrinctIds = new String[districtList
                        .size()];
                for (int k = 0; k < districtList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    String districtModel = new String(districtList.get(k).getRegion_name());
                    String districtModel2 = new String(districtList.get(k).getRegion_id());

                    distrinctArray[k] = districtModel; // 县
                    distrinctIds[k] = districtModel2; // 县

                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mDistrictDatasMap.put(cityNames[j], distrinctArray);
                mDistrictDatasMap2.put(cityNames[j], distrinctIds);
            }
            mCitisDatasMap.put(proviceList.get(i).getRegion_name(), cityNames);
            mCitisDatasMap2.put(proviceList.get(i).getRegion_name(), cityIds);

        }


    }


    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem(); //拿到位置
        cityList = proviceList.get(pCurrent).getList();
        mCurrentProviceName = mProvinceDatas[pCurrent];    //通过位置拿到name
        province_id = mProvinceDatasId[pCurrent];   //拿到id

        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        districtList = cityList.get(pCurrent).getList();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        city_id = mCitisDatasMap2.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(
                this, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        county_id = mDistrictDatasMap2.get(mCurrentCityName)[0];
    }


    /**
     * 保存成功 (修改成功)
     *
     * @param s
     */
    @Override
    public void getDataSuccess(String s) {
        showToast(s);
        finish();
        EventBus.getDefault().post(new RefreshAddress(""));
    }

    /**
     * 保存失败
     */
    @Override
    public void getDataFail() {
        showToast("保存失败");

    }
}
