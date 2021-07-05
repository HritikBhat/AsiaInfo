package com.hritik.asiainfo.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hritik.asiainfo.Database.Tables.Borders;
import com.hritik.asiainfo.Database.Tables.Countries;
import com.hritik.asiainfo.Database.Tables.Languages;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Countries.class, Borders.class, Languages.class},version = 1,exportSchema = false)
public abstract class AsianCountryDatabase  extends RoomDatabase
{

    public abstract DBInterface myDao();
}