package com.example.roazhone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class to map details about relay parking that come from remote API.
 */
public class RelayParkingDetails implements Serializable {

    private final static long serialVersionUID = 1307429908633312002L;
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
}
