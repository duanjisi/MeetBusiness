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

import com.atgc.cotton.R;
import com.atgc.cotton.activity.goodsDetail.EditAddressActivity;
import com.atgc.cotton.entity.AddressListEntity;

/**
 * Created by liw on 2017/7/12.
 */

public class ChooseAddressAdapter extends BaseRecycleViewAdapter {

    private Context context;

    public ChooseAddressAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindItemHolder(RecyclerView.ViewHolder holder, final int position) {
        ChooseAddressHolder holder1 = (ChooseAddressHolder) holder;

        final AddressListEntity.DataBean item = (AddressListEntity.DataBean) datas.get(position);
        holder1.tv_people.setText(item.getConsignee() + "        " + item.getContact());
        holder1.tv_address.setText(item.getAddress()); //后期加上省市区，看是后台改还是我这改。
        if (item.getIsDefault() == 1) {
            holder1.img_choose.setImageResource(R.drawable.selected);

        } else {
            holder1.img_choose.setImageResource(R.drawable.unchecked);
        }
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
                //TODO            地区没传递过去
                int isDefault = item.getIsDefault(); //是否默认
                int addressId = item.getAddressId();

                Intent intent = new Intent(context, EditAddressActivity.class);
                intent.putExtra("contact",contact);
                intent.putExtra("consignee",consignee);
                intent.putExtra("address",address);
                intent.putExtra("isDefault",isDefault);
                intent.putExtra("id",addressId);

                context.startActivity(intent);


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

    public interface onDeleteListener{
        void onDel(int position);
    }
    public onDeleteListener mOnDeleteListener;
    public void setDeleteListener(onDeleteListener mOnDeleteListener){
        this.mOnDeleteListener = mOnDeleteListener;
    }

}
