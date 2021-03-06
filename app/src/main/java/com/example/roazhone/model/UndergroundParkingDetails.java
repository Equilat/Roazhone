package com.example.roazhone.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Class to map details about underground parkings that come from remote API.
 */
public class UndergroundParkingDetails extends Parking implements Serializable, Parcelable {
    public static final Creator<UndergroundParkingDetails> CREATOR = new Creator<UndergroundParkingDetails>() {
        @Override
        public UndergroundParkingDetails createFromParcel(Parcel source) {
            return new UndergroundParkingDetails(source);
        }

        @Override
        public UndergroundParkingDetails[] newArray(int size) {
            return new UndergroundParkingDetails[size];
        }
    };
    private final static long serialVersionUID = -4000423003554111796L;
    public static Comparator<UndergroundParkingDetails> undergroundFreePlacesComparator = new Comparator<UndergroundParkingDetails>() {
        @Override
        public int compare(UndergroundParkingDetails o1, UndergroundParkingDetails o2) {
            if (o1.placesLibres != null && o2.placesLibres != null) {
                return o2.placesLibres.compareTo(o1.placesLibres);
            }
            return 0;
        }
    };
    public static Comparator<UndergroundParkingDetails> undergroundUserDistanceComparator = new Comparator<UndergroundParkingDetails>() {
        @Override
        public int compare(UndergroundParkingDetails o1, UndergroundParkingDetails o2) {
            if (o1.userDistance != null) {

                return o1.userDistance.compareTo(o2.userDistance);
            }
            return 0;
        }
    };
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
    private Integer placesMax;
    @SerializedName("orgahoraires")
    @Expose
    private String horaires;
    @SerializedName("tarif_3h")
    @Expose
    private String tarif3h;
    @SerializedName("tarif_2h")
    @Expose
    private String tarif2h;
    @SerializedName("free")
    @Expose
    private Integer placesLibres;
    @SerializedName("key")
    @Expose
    private String nomParking;
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
    private Double userDistance;

    public UndergroundParkingDetails(Parcel in) {
        this.status = in.readString();
        this.tarif1h30 = in.readString();
        this.tarif30 = in.readString();
        this.tarif1h = in.readString();
        this.placesMax = in.readInt();
        this.horaires = in.readString();
        this.tarif3h = in.readString();
        this.tarif2h = in.readString();
        this.placesLibres = in.readInt();
        this.nomParking = in.readString();
        this.tarif4h = in.readString();
        in.readList(this.geo, Double.class.getClassLoader());
        this.id = in.readString();
        this.tarif15 = in.readString();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Integer getPlacesMax() {
        return placesMax;
    }

    public void setPlacesMax(Integer placesMax) {
        this.placesMax = placesMax;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
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

    public Integer getPlacesLibres() {
        return placesLibres;
    }

    public void setPlacesLibres(Integer placesLibres) {
        this.placesLibres = placesLibres;
    }

    public String getNomParking() {
        return nomParking;
    }

    public void setNomParking(String nomParking) {
        this.nomParking = nomParking;
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

    public Double getUserDistance() {
        return userDistance;
    }

    public void computeUserDistance(double userLat, double userLong) {
        Location loc1 = new Location("");
        loc1.setLatitude(userLat);
        loc1.setLongitude(userLong);
        Location loc2 = new Location("");
        loc2.setLatitude(this.geo.get(0));
        loc2.setLongitude(this.geo.get(1));

        this.userDistance = (double) (loc1.distanceTo(loc2) / 1000);
        BigDecimal bigDecimal = new BigDecimal(Double.toString(this.userDistance));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        this.userDistance = bigDecimal.doubleValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(tarif1h30);
        dest.writeString(tarif30);
        dest.writeString(tarif1h);
        dest.writeInt(placesMax);
        dest.writeString(horaires);
        dest.writeString(tarif3h);
        dest.writeString(tarif2h);
        dest.writeInt(placesLibres);
        dest.writeString(nomParking);
        dest.writeString(tarif4h);
        dest.writeList(geo);
        dest.writeString(id);
        dest.writeString(tarif15);
    }
}