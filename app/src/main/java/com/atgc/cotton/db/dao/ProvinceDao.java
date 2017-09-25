package com.atgc.cotton.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.atgc.cotton.App;
import com.atgc.cotton.db.DBHelper;
import com.atgc.cotton.db.ProvinceEntityColumn;
import com.atgc.cotton.db.helper.ColumnHelper;
import com.atgc.cotton.entity.RegionEntity;

import java.util.List;

/**
 * Created by Johnny on 2017-08-26.
 */
public class ProvinceDao extends ColumnHelper<RegionEntity> {
    private static ProvinceDao sAccountDao;
    private Context mContext;

    private ProvinceDao() {
        mContext = App.getInstance().getApplicationContext();
    }

    public static ProvinceDao getInstance() {
        if (sAccountDao == null) {
            synchronized (ProvinceDao.class) {
                if (sAccountDao == null) {
                    sAccountDao = new ProvinceDao();
                }
            }
        }
        return sAccountDao;
    }

    @Override
    protected ContentValues getValues(RegionEntity bean) {
        ContentValues values = new ContentValues();
        values.put(ProvinceEntityColumn.REGION_ID, bean.getRegion_id());
        values.put(ProvinceEntityColumn.REGION_NAME, bean.getRegion_name());
        return values;
    }

    @Override
    protected RegionEntity getBean(Cursor c) {
        RegionEntity entity = new RegionEntity();
        entity.setRegion_id(getString(c, ProvinceEntityColumn.REGION_ID));
        entity.setRegion_name(getString(c, ProvinceEntityColumn.REGION_NAME));
        return entity;
    }

    @Override
    public void save(List<RegionEntity> list) {

    }

    @Override
    public void save(RegionEntity entity) {
        String[] args = new String[]{entity.getRegion_id()};
        Cursor c = DBHelper.getInstance(mContext).rawQuery(
                getSelectSql(ProvinceEntityColumn.TABLE_NAME, new String[]{ProvinceEntityColumn.REGION_ID}), args);
        if (exist(c)) {
//            c.moveToFirst();
//            this.delete(entity.getUser_name());
            this.update(entity);
        } else {
            DBHelper.getInstance(mContext).insert(ProvinceEntityColumn.TABLE_NAME, getValues(entity));
        }
        c.close();
    }

    public String findByRegionId(String id) {//根据分类id查询组
        Cursor c = DBHelper.getInstance(mContext).rawQuery(
                "SELECT * FROM " + ProvinceEntityColumn.TABLE_NAME +
                        " WHERE " + ProvinceEntityColumn.REGION_ID + " = ?", new String[]{id});
        String str = null;
        if (exist(c, mContext)) {
            c.moveToFirst();
            if (c.getCount() == 1) {
                str = getBean(c).getRegion_name();
            }
        }
        c.close();
        return str;
    }

    @Override
    public RegionEntity query(int id) {
        return null;
    }

    @Override
    public List<RegionEntity> query() {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void delete(String str) {

    }

    @Override
    public void update(RegionEntity entity) {

    }

    /**
     * 删除表内所有数据
     */
    public void deleteAllDatas() {
        DBHelper.getInstance(mContext).delete(ProvinceEntityColumn.TABLE_NAME, null, null);
    }
}
