package com.mosl.mobile.ekyc.database;

/**
 * Created by pramod on 25-11-2016.
 * Class to handle database for the collateral
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.model.CollateralModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ekyc.db";
    public static final String COLLATERAL_TABLE_NAME = "collateral";

    public static final String COLLATERAL_COLUMN_ID = "id";
    public static final String COLLATERAL_COLUMN_SECTION = "section";
    public static final String COLLATERAL_COLUMN_TITLE = "title";
    public static final String COLLATERAL_COLUMN_FILENAME = "filename";
    public static final String COLLATERAL_COLUMN_FILETYPE = "filetype";
    public static final String COLLATERAL_COLUMN_LOCAL_FILEPATH = "localFilePath";
    public static final String COLLATERAL_COLUMN_URL = "url";
    public static final String COLLATERAL_COLUMN_SUMMARY = "summary";
    public static final String COLLATERAL_COLUMN_VERSION = "version";
    public static final String COLLATERAL_COLUMN_SERVER_VERSION = "server_version";
    public static final String COLLATERAL_COLUMN_DOWNLOADED = "download_flag";  //Y or N

    private HashMap hp;
    private Context context;

    private static int DB_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + COLLATERAL_TABLE_NAME +
                        "("+COLLATERAL_COLUMN_ID+" integer primary key, " +
                        COLLATERAL_COLUMN_SECTION+" text," +
                        COLLATERAL_COLUMN_TITLE+" text," +
                        COLLATERAL_COLUMN_FILENAME+" text," +
                        COLLATERAL_COLUMN_FILETYPE+" text," +
                        COLLATERAL_COLUMN_LOCAL_FILEPATH+" text," +
                        COLLATERAL_COLUMN_URL+" text," +
                        COLLATERAL_COLUMN_SUMMARY+" text," +
                        COLLATERAL_COLUMN_SERVER_VERSION+" text," +
                        COLLATERAL_COLUMN_DOWNLOADED+" text," +
                        COLLATERAL_COLUMN_VERSION+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+COLLATERAL_TABLE_NAME);
        onCreate(db);
    }

    public CollateralModel insertCollateral (CollateralModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLLATERAL_COLUMN_ID, model.getId());
        contentValues.put(COLLATERAL_COLUMN_SECTION, model.getSection());
        contentValues.put(COLLATERAL_COLUMN_TITLE, model.getTitle());
        contentValues.put(COLLATERAL_COLUMN_FILENAME, model.getFileName());
        contentValues.put(COLLATERAL_COLUMN_FILETYPE, model.getFileType());
        contentValues.put(COLLATERAL_COLUMN_LOCAL_FILEPATH, model.getLocalFilePath());
        contentValues.put(COLLATERAL_COLUMN_URL, model.getUrl());
        contentValues.put(COLLATERAL_COLUMN_VERSION, model.getServerVersion()); //both local and server will have same version
        contentValues.put(COLLATERAL_COLUMN_SERVER_VERSION, model.getServerVersion());
        contentValues.put(COLLATERAL_COLUMN_DOWNLOADED, model.getDownloaded());
        contentValues.put(COLLATERAL_COLUMN_SUMMARY, model.getSummary());
        db.insert(COLLATERAL_TABLE_NAME, null, contentValues);
        model.setLocalVersion(model.getServerVersion());
        return model;
    }

    public CollateralModel getCollateralById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+COLLATERAL_TABLE_NAME+" where "+COLLATERAL_COLUMN_ID+"="+id+"", null );
        //res.moveToFirst();
        List<CollateralModel> list = getCollateralListByCursor(res);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public List<CollateralModel> getCollateralBySection(String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+COLLATERAL_TABLE_NAME+" where "+COLLATERAL_COLUMN_SECTION+"='"+section+"'", null );
        //res.moveToFirst();
        return getCollateralListByCursor(res);
    }
    public int getCollateralRowCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, COLLATERAL_TABLE_NAME);
        return numRows;
    }

    public CollateralModel updateServerVersion (int id, String serverVersion) {
        CollateralModel model = getCollateralById(id);
        model.setServerVersion(serverVersion);
        updateCollateral(model);
        return model;
    }
    public CollateralModel updateDownloadStatus (int id, String downloadStatus) {
        CollateralModel model = getCollateralById(id);
        model.setDownloaded(downloadStatus.toUpperCase());
        updateCollateral(model);
        return model;
    }
    public boolean updateCollateral (CollateralModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLLATERAL_COLUMN_SECTION, model.getSection());
        contentValues.put(COLLATERAL_COLUMN_TITLE, model.getTitle());
        contentValues.put(COLLATERAL_COLUMN_FILENAME, model.getFileName());
        contentValues.put(COLLATERAL_COLUMN_FILETYPE, model.getFileType());
        contentValues.put(COLLATERAL_COLUMN_LOCAL_FILEPATH, model.getLocalFilePath());
        contentValues.put(COLLATERAL_COLUMN_URL, model.getUrl());
        contentValues.put(COLLATERAL_COLUMN_VERSION, model.getLocalVersion());
        contentValues.put(COLLATERAL_COLUMN_SERVER_VERSION, model.getServerVersion());
        contentValues.put(COLLATERAL_COLUMN_DOWNLOADED, model.getDownloaded());
        contentValues.put(COLLATERAL_COLUMN_SUMMARY, model.getSummary());

        db.update(COLLATERAL_TABLE_NAME, contentValues, COLLATERAL_COLUMN_ID+" = ? ", new String[] { Integer.toString(model.getId()) } );
        return true;
    }

    public Integer deleteCollateral (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COLLATERAL_TABLE_NAME,
                COLLATERAL_COLUMN_ID+" = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<CollateralModel> getAllCollateral() {

        List<CollateralModel> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+COLLATERAL_TABLE_NAME, null );
        //res.moveToFirst();

        return getCollateralListByCursor(res);
    }

    private List<CollateralModel> getCollateralListByCursor(Cursor res){
        List<CollateralModel> data = new ArrayList<>();
        if (res .moveToFirst()) {
            while (res.isAfterLast() == false) {

                int id = res.getInt(res.getColumnIndex(COLLATERAL_COLUMN_ID));
                String section = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_SECTION));
                String title = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_TITLE));
                String fileName = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_FILENAME));
                String fileType = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_FILETYPE));
                String localFilePath = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_LOCAL_FILEPATH));
                String summary = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_SUMMARY));
                String localVersion = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_VERSION));
                String serverVersion = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_SERVER_VERSION));
                String downloaded = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_DOWNLOADED));
                String url = res.getString(res.getColumnIndex(COLLATERAL_COLUMN_URL));

                File file = new File(context.getFilesDir(), fileName);

                CollateralModel model = new CollateralModel(id, section, title, fileName, fileType, summary, localVersion, serverVersion, url, downloaded);

                if(localFilePath==null || MOSLCommon.isBlank(localFilePath)) {
                    localFilePath=file.getAbsolutePath();
                }
                model.setLocalFilePath(localFilePath);
                data.add(model);

                res.moveToNext();
            }
        }
        return data;
    }
}