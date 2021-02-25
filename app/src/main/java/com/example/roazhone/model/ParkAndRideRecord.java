package com.example.roazhone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class to map information about relay parking's dataset that come from remote API.
 */
public class ParkAndRideRecord {

    private final static long serialVersionUID = -5991800116026056414L;
    @SerializedName("datasetid")
    @Expose
    private String datasetid;
    @SerializedName("recordid")
    @Expose
    private String recordid;
    @SerializedName("fields")
    @Expose
    private ParkAndRideDetails parkAndRideDetails;
    //    @SerializedName("geometry")
//    @Expose
    private transient String geometry;
    @SerializedName("record_timestamp")
    @Expose
    private String recordTimestamp;

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public ParkAndRideDetails getParkAndRideDetails() {
        return parkAndRideDetails;
    }

    public void setParkAndRideDetails(ParkAndRideDetails parkAndRideDetails) {
        this.parkAndRideDetails = parkAndRideDetails;
    }

    public String getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(String recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }
}
