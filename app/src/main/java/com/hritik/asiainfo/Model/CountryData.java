package com.hritik.asiainfo.Model;

import java.util.ArrayList;

public class CountryData {
    private String name,capital,flag,region,subRegion,population;
    private ArrayList<String> bord_list;
    private ArrayList<String> lang_list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public ArrayList getBord_list() {
        return bord_list;
    }

    public void setBord_list(ArrayList bord_list) {
        this.bord_list = bord_list;
    }

    public ArrayList getLang_list() {
        return lang_list;
    }

    public void setLang_list(ArrayList lang_list) {
        this.lang_list = lang_list;
    }
}
