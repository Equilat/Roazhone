package com.example.roazhone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UndergroundParkingDetails implements Serializable {
    private final static long serialVersionUID = -4000423003554111796L;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tarif_1h30")
    @Expose
    private String tarif1h30;
    @SerializedName("tarif_30")
    @Expose
    private String tarif30;
    @SerializedName("tarif_1h")
    @Expose
    private String tarif1h;
    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("orgahoraires")
    @Expose
    private String orgahoraires;
    @SerializedName("tarif_3h")
    @Expose
    private String tarif3h;
    @SerializedName("tarif_2h")
    @Expose
    private String tarif2h;
    @SerializedName("free")
    @Expose
    private Integer free;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("tarif_4h")
    @Expose
    private String tarif4h;
    @SerializedName("geo")
    @Expose
    private List<Double> geo = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tarif_15")
    @Expose
    private String tarif15;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTarif1h30() {
        return tarif1h30;
    }

    public void setTarif1h30(String tarif1h30) {
        this.tarif1h30 = tarif1h30;
    }

    public String getTarif30() {
        return tarif30;
    }

    public void setTarif30(String tarif30) {
        this.tarif30 = tarif30;
    }

    public String getTarif1h() {
        return tarif1h;
    }

    public void setTarif1h(String tarif1h) {
        this.tarif1h = tarif1h;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getOrgahoraires() {
        return orgahoraires;
    }

    public void setOrgahoraires(String orgahoraires) {
        this.orgahoraires = orgahoraires;
    }

    public String getTarif3h() {
        return tarif3h;
    }

    public void setTarif3h(String tarif3h) {
        this.tarif3h = tarif3h;
    }

    public String getTarif2h() {
        return tarif2h;
    }

    public void setTarif2h(String tarif2h) {
        this.tarif2h = tarif2h;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTarif4h() {
        return tarif4h;
    }

    public void setTarif4h(String tarif4h) {
        this.tarif4h = tarif4h;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarif15() {
        return tarif15;
    }

    public void setTarif15(String tarif15) {
        this.tarif15 = tarif15;
    }

}