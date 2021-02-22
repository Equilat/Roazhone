package com.example.roazhone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RelayParkingDetails implements Serializable {

    private final static long serialVersionUID = 1307429908633312002L;
    @SerializedName("etat")
    @Expose
    private String etat;
    @SerializedName("lastupdate")
    @Expose
    private String lastupdate;
    @SerializedName("nombreplacesdisponibles")
    @Expose
    private Integer nombreplacesdisponibles;
    @SerializedName("idparc")
    @Expose
    private String idparc;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("nombreplacesoccupees")
    @Expose
    private Integer nombreplacesoccupees;
    @SerializedName("nombreplacesdisponiblespmr")
    @Expose
    private Integer nombreplacesdisponiblespmr;
    @SerializedName("capaciteactuelle")
    @Expose
    private Integer capaciteactuelle;
    @SerializedName("nombreplacesoccupeespmr")
    @Expose
    private Integer nombreplacesoccupeespmr;
    @SerializedName("coordonnees")
    @Expose
    private List<Double> coordonnees = null;
    @SerializedName("capaciteactuellepmr")
    @Expose
    private Integer capaciteactuellepmr;

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public Integer getNombreplacesdisponibles() {
        return nombreplacesdisponibles;
    }

    public void setNombreplacesdisponibles(Integer nombreplacesdisponibles) {
        this.nombreplacesdisponibles = nombreplacesdisponibles;
    }

    public String getIdparc() {
        return idparc;
    }

    public void setIdparc(String idparc) {
        this.idparc = idparc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNombreplacesoccupees() {
        return nombreplacesoccupees;
    }

    public void setNombreplacesoccupees(Integer nombreplacesoccupees) {
        this.nombreplacesoccupees = nombreplacesoccupees;
    }

    public Integer getNombreplacesdisponiblespmr() {
        return nombreplacesdisponiblespmr;
    }

    public void setNombreplacesdisponiblespmr(Integer nombreplacesdisponiblespmr) {
        this.nombreplacesdisponiblespmr = nombreplacesdisponiblespmr;
    }

    public Integer getCapaciteactuelle() {
        return capaciteactuelle;
    }

    public void setCapaciteactuelle(Integer capaciteactuelle) {
        this.capaciteactuelle = capaciteactuelle;
    }

    public Integer getNombreplacesoccupeespmr() {
        return nombreplacesoccupeespmr;
    }

    public void setNombreplacesoccupeespmr(Integer nombreplacesoccupeespmr) {
        this.nombreplacesoccupeespmr = nombreplacesoccupeespmr;
    }

    public List<Double> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(List<Double> coordonnees) {
        this.coordonnees = coordonnees;
    }

    public Integer getCapaciteactuellepmr() {
        return capaciteactuellepmr;
    }

    public void setCapaciteactuellepmr(Integer capaciteactuellepmr) {
        this.capaciteactuellepmr = capaciteactuellepmr;
    }
}
