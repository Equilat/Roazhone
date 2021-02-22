package com.example.roazhone.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class to map information about underground parking's dataset that come from remote API.
 */
public class UndergroundParkingRecord implements Serializable {

    private final static long serialVersionUID = -5991800548026056414L;
    @SerializedName("datasetid")
    @Expose
    private String datasetid;
    @SerializedName("recordid")
    @Expose
    private String recordid;
    @SerializedName("fields")
    @Expose
    private UndergroundParkingDetails undergroundParkingDetails;
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

    public UndergroundParkingDetails getUndergroundParkingDetails() {
        return undergroundParkingDetails;
    }

    public void setUndergroundParkingDetails(UndergroundParkingDetails undergroundParkingDetails) {
        this.undergroundParkingDetails = undergroundParkingDetails;
    }

    public String getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(String recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }

}