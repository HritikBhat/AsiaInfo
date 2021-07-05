package com.hritik.asiainfo.Database.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.hritik.asiainfo.Constants.DBConstant.CountryTable;


@Entity(tableName = CountryTable)
public class Countries
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cid")
    private int cid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "capital")
    private String capital;

    @ColumnInfo(name = "flag")
    private String flag;

    @ColumnInfo(name = "region")
    private String region;

    @ColumnInfo(name = "subRegion")
    private String subRegion;

    @ColumnInfo(name = "population")
    private String population;


    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

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
}
