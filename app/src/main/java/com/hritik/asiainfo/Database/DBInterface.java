package com.hritik.asiainfo.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hritik.asiainfo.Database.Tables.Countries;
import com.hritik.asiainfo.Database.Tables.Borders;
import com.hritik.asiainfo.Database.Tables.Languages;

import java.util.List;

import static com.hritik.asiainfo.Constants.DBConstant.BorderTable;
import static com.hritik.asiainfo.Constants.DBConstant.CountryTable;
import static com.hritik.asiainfo.Constants.DBConstant.LanguageTable;


@Dao
public interface DBInterface
{


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long addCountry(Countries country);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long addBorder(Borders border);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long addLanguage(Languages language);

    @Query("select * from "+CountryTable)
    public List<Countries> getCountries();

    @Query("SELECT * FROM "+BorderTable+" where cid = :cid")
    List<Borders> getCountryBorders(String cid);

    @Query("SELECT * FROM "+LanguageTable+" where cid = :cid")
    List<Languages> getCountryLanguages(String cid);

    @Query("SELECT name FROM "+BorderTable+" where cid = :cid")
    List<String> getCountryBordersName(String cid);

    @Query("SELECT name FROM "+LanguageTable+" where cid = :cid")
    List<String> getCountryLanguagesName(String cid);

    @Query("DELETE FROM "+CountryTable+";")
    void deleteAll();


    @Delete
    public void deleteCountry(Countries country);

    @Delete
    public void deleteBorder(Borders border);

    @Delete
    public void deleteLanguage(Languages language);


    /*
    @Update
    public void updateUser(User user);
    */
}