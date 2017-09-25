package com.atgc.cotton.entity;

/**
 * Created by Johnny on 2017-08-26.
 */
public class RegionEntity {
    private String region_name;
    private String region_id;

    public RegionEntity() {
    }

    public RegionEntity(String region_id, String region_name) {
        this.region_id = region_id;
        this.region_name = region_name;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }
}
