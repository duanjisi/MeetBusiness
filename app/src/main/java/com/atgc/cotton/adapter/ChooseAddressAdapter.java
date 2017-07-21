package com.atgc.cotton.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.atgc.cotton.R;
import com.atgc.cotton.activity.goodsDetail.EditAddressActivity;
import com.atgc.cotton.entity.AddressListEntity;
import com.atgc.cotton.entity.LocalAddressEntity;
import com.atgc.cotton.event.ChangeAddressState;
import com.atgc.cotton.util.PreferenceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by liw on 2017/7/12.
 */

public class ChooseAddressAdapter extends BaseRecycleViewAdapter {
    private LocalAddressEntity jsonDate;
    private Context context;


    private List<LocalAddressEntity.ThreeChild> proviceList;       //省
    private List<LocalAddressEntity.FourChild> cityList;      //市
    private List<LocalAddressEntity.LastChild> districtList;   //县

    private Map<String, String> map1 = new HashMap<>();  //省市区对应的id和name
    private Map<String, String> map2 = new HashMap<>();
    private Map<String, String> map3 = new HashMap<>();


    public ChooseAddressAdapter(Context context) {
        this.context = context;
        initCityData();
        initProvinceDatas();
    }

    protected void initProvinceDatas() {
        LocalAddressEntity.SecondChild result = jsonDate.getResult();
        proviceList = result.getList();

        for (int i = 0; i < proviceList.size(); i++) {
            String region_id = proviceList.get(i).getRegion_id();
            String region_name = proviceList.get(i).getRegion_name();
            map1.put(region_id, region_name);

            cityList = proviceList.get(i).getList();
            for (int j = 0; j < cityList.size(); j++) {
                String region_id1 = cityList.get(j).getRegion_id();
                String region_name1 = cityList.get(j).getRegion_name();
                map2.put(region_id1, region_name1);

                districtList = cityList.get(j).getList();


                for (int k = 0; k < districtList.size(); k++) {
                    String region_id2 = districtList.get(k).getRegion_id();
                    String region_name2 = districtList.get(k).getRegion_name();

                    map3.put(region_id2, region_name2);
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
            }

        }


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


    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChooseAddressHolder holder1 = (ChooseAddressHolder) holder;

        final AddressListEntity.DataBean item = (AddressListEntity.DataBean) datas.get(position);
        holder1.tv_people.setText(item.getConsignee() + "        " + item.getContact());

        int province = item.getProvince();
        int city = item.getCity();
        int district = item.getDistrict();

        final String location = map1.get(province + "") + map2.get(city + "") + map3.get(district + "");

        String Myaddress = location + item.getAddress();
        holder1.tv_address.setText(Myaddress);
//        if (item.getIsDefault() == 1) {
//            holder1.img_choose.setImageResource(R.drawable.selected);
//
//        } else {
//            holder1.img_choose.setImageResource(R.drawable.unchecked);
//        }
        holder1.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeleteListener.onDel(position);
            }
        });

        holder1.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = item.getContact(); // 电话
                String consignee = item.getConsignee(); //收件人
                String address = item.getAddress(); //详细地址
                int isDefault = item.getIsDefault(); //是否默认
                int addressId = item.getAddressId();

                Intent intent = new Intent(context, EditAddressActivity.class);
                intent.putExtra("contact", contact);
                intent.putExtra("consignee", consignee);
                intent.putExtra("address", address);
                intent.putExtra("isDefault", isDefault);
                intent.putExtra("id", addressId);
                intent.putExtra("location", location);

                context.startActivity(intent);


            }
        });

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddressListEntity.DataBean dataBean = (AddressListEntity.DataBean) datas.get(position);

                dataBean.setLocation(location);     //把省市区，存进去

                String addressJson = JSON.toJSONString(dataBean);

                PreferenceUtils.putString(context,"addressJson",addressJson);
                EventBus.getDefault().post(new ChangeAddressState(""));

                itemListener.onItemClick(holder1.getLayoutPosition());
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_list, parent, false);
        return new ChooseAddressHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public class ChooseAddressHolder extends RecyclerView.ViewHolder {
        private TextView tv_people;
        private TextView tv_address;
        private ImageView img_choose;

        private Button btn_edit;
        private Button btn_delete;

        public ChooseAddressHolder(View itemView) {
            super(itemView);
            tv_people = (TextView) itemView.findViewById(R.id.tv_people);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            img_choose = (ImageView) itemView.findViewById(R.id.img_choose);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface onDeleteListener {
        void onDel(int position);
    }

    public onDeleteListener mOnDeleteListener;

    public void setDeleteListener(onDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

}
