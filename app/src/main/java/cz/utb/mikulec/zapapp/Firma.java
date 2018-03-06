package cz.utb.mikulec.zapapp;

/**
 * Created by Jirik on 16.02.2018.
 */

public class Firma {


    private String nazev;
    private int IC;
    private String ulice;
    private int cisloPopisne;
    private int PSC;
    private String mesto;

    public String toString() {
        return getNazev()+", "+getUlice()+" "+getCisloPopisne()+", "+getPSC()+" "+getMesto();
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public int getIC() {
        return IC;
    }

    public void setIC(int IC) {
        this.IC = IC;
    }

    public String getUlice() {
        return ulice;
    }

    public void setUlice(String ulice) {
        this.ulice = ulice;
    }

    public int getCisloPopisne() {
        return cisloPopisne;
    }

    public void setCisloPopisne(int cisloPopisne) {
        this.cisloPopisne = cisloPopisne;
    }

    public int getPSC() {
        return PSC;
    }

    public void setPSC(int PSC) {
        this.PSC = PSC;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }
}
