package com.hritik.asiainfo.Database.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static com.hritik.asiainfo.Constants.DBConstant.LanguageTable;


@Entity(tableName = LanguageTable,foreignKeys = {@ForeignKey(entity = Countries.class,
        parentColumns = "cid",
        childColumns = "cid",
        onDelete = ForeignKey.CASCADE)
})
public class Languages
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lid")
    private int lid;

    @ColumnInfo(name = "cid", index = true)
    private int cid;

    @ColumnInfo(name = "name")
    private String name;


    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
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

