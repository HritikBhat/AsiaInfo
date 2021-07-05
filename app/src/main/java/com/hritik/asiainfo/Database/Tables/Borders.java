package com.hritik.asiainfo.Database.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static com.hritik.asiainfo.Constants.DBConstant.BorderTable;


@Entity(tableName = BorderTable,foreignKeys = {@ForeignKey(entity = Countries.class,
        parentColumns = "cid",
        childColumns = "cid",
        onDelete = ForeignKey.CASCADE)
})
public class Borders
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bid")
    private int bid;

    @ColumnInfo(name = "cid", index = true)
    private int cid;

    @ColumnInfo(name = "name")
    private String name;


    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

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
}

