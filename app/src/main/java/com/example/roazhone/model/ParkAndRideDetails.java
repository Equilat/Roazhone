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
 * Class to map details about park and ride parking that come from remote API.
 */
public class ParkAndRideDetails implements Serializable, Parcelable {

    public static final Creator<ParkAndRideDetails> CREATOR = new Creator<ParkAndRideDetails>() {
        @Override
        public ParkAndRideDetails createFromParcel(Parcel source) {
            return new ParkAndRideDetails(source);
        }

        @Override
        public ParkAndRideDetails[] newArray(int size) {
            return new ParkAndRideDetails[size];
        }
    };
    private final static long serialVersionUID = 1307429908633312002L;
    public static Comparator<ParkAndRideDetails> parkAndRideFreePlacesComparator = new Comparator<ParkAndRideDetails>() {
        @Override
        public int compare(ParkAndRideDetails o1, ParkAndRideDetails o2) {
            return o2.placesLibres.compareTo(o1.placesLibres);
        }
    };
    public static Comparator<ParkAndRideDetails> parkAndRideDetailsUserDistanceComparator = new Comparator<ParkAndRideDetails>() {
        @Override
        public int compare(ParkAndRideDetails o1, ParkAndRideDetails o2) {
            if (o1.userDistance != null) {
                return o1.userDistance.compareTo(o2.userDistance);
            }
            return 0;
        }
    };
    @SerializedName("etat")
    @Expose
    private String status;
    @SerializedName("lastupdate")
    @Expose
    private String lastUpdate;
    @SerializedName("nombreplacesdisponibles")
    @Expose
    private Integer placesLibres;
    @SerializedName("idparc")
    @Expose
    private String id;
    @SerializedName("nom")
    @Expose
    private String nomParking;
    @SerializedName("nombreplacesoccupees")
    @Expose
    private Integer placesOccupees;
    @SerializedName("nombreplacesdisponiblespmr")
    @Expose
    private Integer nombreLibresPMR;
    @SerializedName("capaciteactuelle")
    @Expose
    private Integer capaciteActuelle;
    @SerializedName("nombreplacesoccupeespmr")
    @Expose
    private Integer placesOccupeesPMR;
    @SerializedName("coordonnees")
    @Expose
    private List<Double> coordonnees = null;
    @SerializedName("capaciteactuellepmr")
    @Expose
    private Integer capaciteActuellePMR;
    private Double userDistance;

    public ParkAndRideDetails(Parcel in) {
        this.status = in.readString();
        this.lastUpdate = in.readString();
        this.placesLibres = in.readInt();
        this.id = in.readString();
        this.nomParking = in.readString();
        this.placesOccupees = in.readInt();
        this.nombreLibresPMR = in.readInt();
        this.capaciteActuelle = in.readInt();
        this.placesOccupeesPMR = in.readInt();
        in.readList(this.coordonnees, Double.class.getClassLoader());
        this.capaciteActuellePMR = in.readInt();
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getPlacesLibres() {
        return placesLibres;
    }

    public void setPlacesLibres(Integer placesLibres) {
        this.placesLibres = placesLibres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomParking() {
        return nomParking;
    }

    public void setNomParking(String nomParking) {
        this.nomParking = nomParking;
    }

    public Integer getPlacesOccupees() {
        return placesOccupees;
    }

    public void setPlacesOccupees(Integer placesOccupees) {
        this.placesOccupees = placesOccupees;
    }

    public Integer getNombreLibresPMR() {
        return nombreLibresPMR;
    }

    public void setNombreLibresPMR(Integer nombreLibresPMR) {
        this.nombreLibresPMR = nombreLibresPMR;
    }

    public Integer getCapaciteActuelle() {
        return capaciteActuelle;
    }

    public void setCapaciteActuelle(Integer capaciteActuelle) {
        this.capaciteActuelle = capaciteActuelle;
    }

    public Integer getPlacesOccupeesPMR() {
        return placesOccupeesPMR;
    }

    public void setPlacesOccupeesPMR(Integer placesOccupeesPMR) {
        this.placesOccupeesPMR = placesOccupeesPMR;
    }

    public List<Double> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(List<Double> coordonnees) {
        this.coordonnees = coordonnees;
    }

    public Integer getCapaciteActuellePMR() {
        return capaciteActuellePMR;
    }

    public void setCapaciteActuellePMR(Integer capaciteActuellePMR) {
        this.capaciteActuellePMR = capaciteActuellePMR;
    }

    public Double getUserDistance() {
        return userDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(lastUpdate);
        dest.writeInt(placesLibres);
        dest.writeString(id);
        dest.writeString(nomParking);
        dest.writeInt(placesOccupees);
        dest.writeInt(nombreLibresPMR);
        dest.writeInt(capaciteActuelle);
        dest.writeInt(placesOccupeesPMR);
        dest.writeList(coordonnees);
        dest.writeInt(capaciteActuellePMR);
    }

    public void computeUserDistance(double userLat, double userLong) {
        Location loc1 = new Location("");
        loc1.setLatitude(userLat);
        loc1.setLongitude(userLong);
        Location loc2 = new Location("");
        loc2.setLatitude(this.coordonnees.get(0));
        loc2.setLongitude(this.coordonnees.get(1));

        this.userDistance = (double) (loc1.distanceTo(loc2) / 1000);
        BigDecimal bigDecimal = new BigDecimal(Double.toString(this.userDistance));
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        this.userDistance = bigDecimal.doubleValue();
    }
}
