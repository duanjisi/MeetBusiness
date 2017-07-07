package com.atgc.cotton.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.atgc.cotton.R;
import com.atgc.cotton.adapter.base.ListBaseAdapter;
import com.atgc.cotton.adapter.base.SuperViewHolder;
import com.atgc.cotton.entity.VendUploadEntity;
import com.atgc.cotton.widget.SwipeMenuView;

/**
 * Created by GMARUnity on 2017/6/19.
 */
public class VendUploadGoodsAdapter extends ListBaseAdapter<VendUploadEntity> {


    public VendUploadGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_vend_attr;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        final EditText et_title = holder.getView(R.id.et_title);
        final EditText et_content = holder.getView(R.id.et_content);
        final String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        final VendUploadEntity entity = mDataList.get(position);
        if (et_title.getTag() instanceof TextWatcher) {
            et_title.removeTextChangedListener((TextWatcher) et_title.getTag());
        }
        if (et_content.getTag() instanceof TextWatcher) {
            et_content.removeTextChangedListener((TextWatcher) et_content.getTag());
        }
        TextWatcher titlewatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    String s = editable.toString().trim();
                    if (!TextUtils.isEmpty(s)) {
                        entity.setTitle("" + s);
                    }
                    Log.i("title:", "et_title + afterTextChanged:" + s);
                }

                //confirmlist.get(position).setMessage(editable.toString());
            }
        };
        TextWatcher contentwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    String s = editable.toString().trim();
                    if (!TextUtils.isEmpty(s)) {
                        entity.setContent("" + s);
                    }
                    Log.i("title:", "et_content + afterTextChanged:" + s);
                }
                //confirmlist.get(position).setMessage(editable.toString());
            }
        };
        et_title.addTextChangedListener(titlewatcher);
        et_title.setTag(titlewatcher);
        et_content.addTextChangedListener(contentwatcher);
        et_content.setTag(contentwatcher);
        et_title.setText(entity.getTitle());
        et_content.setText(entity.getContent());
        Log.i("title:", "" + title + "  position:" + position + "  size:" + getItemCount()
                + "  list:" + mDataList.size());
//
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);
//
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
    }

    public void add(String text, int position) {
        int oldsize = getItemCount();
        VendUploadEntity entity = new VendUploadEntity();
        mDataList.add(entity);
        int size = getItemCount();
        notifyItemInserted(size);
        if (size >= 0)
            notifyItemRangeChanged(size, 1);
        //notifyItemInserted(getItemCount());
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

}
