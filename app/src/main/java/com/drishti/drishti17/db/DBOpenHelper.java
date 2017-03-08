package com.drishti.drishti17.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.drishti.drishti17.async.services.EventsSyncService;
import com.drishti.drishti17.async.services.HighlightSyncService;
import com.drishti.drishti17.db.data.dbMetaData;
import com.drishti.drishti17.util.Global;
import com.drishti.drishti17.util.Import;

/**
 * Created by nirmal on 3/4/2017.
 */

public class DBOpenHelper  extends SQLiteOpenHelper {

    private static final String TAG = DBOpenHelper.class.getSimpleName();

    private String[][] columnName;
    private String[][] columnTypes;
    private int[] tableLength;
    private String[] TABLE_NAME;
    private int ifsset = 0;
    private Context context;


    public static DBOpenHelper newInstance(Context context) {

        return new DBOpenHelper(context);
    }

    private DBOpenHelper(Context context) {
        super(context, dbMetaData.DATABASE_SCHEMA, null, dbMetaData.DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: creating database");

        if ((ifsset < 1)) {
            settingDatabase();
            Import.setSharedPref(context, Global.PREF_DB_VERSION, dbMetaData.DATABASE_VERSION);
            Import.setSharedPref(context, Global.PREF_DB_VERSION_LEGACY, dbMetaData.DATABASE_VERSION);
        }

        create(sqLiteDatabase);

        Import.setSharedPref(context, Global.PREF_DB_STATUS, Global.SUCCESS_CODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: upgrading db from " + oldVersion + " to " + newVersion);

        if ((ifsset < 1)) {
            settingDatabase();
            Import.setSharedPref(context, Global.PREF_DB_VERSION_LEGACY, oldVersion);
            Import.setSharedPref(context, Global.PREF_DB_VERSION, newVersion);
        }

        drop(sqLiteDatabase);
        onCreate(sqLiteDatabase);

        reloadDb();

    }

    private void reloadDb() {
        EventsSyncService.startDownload(context);
        HighlightSyncService.startDownload(context);
    }

    private void create(SQLiteDatabase db) {
        String[] CreateSQL = intialisecreatesql();
        for (int i = 0; i < CreateSQL.length; i++) {
            try {
                db.execSQL(CreateSQL[i]);
                Log.d(TAG, "created " + CreateSQL[i]);

            } catch (SQLException e) {
                Log.e(TAG, "oncreate exception ", e);
            }
        }
    }

    public void drop(SQLiteDatabase db) {

        String[] DropSQL = intialisedrop();
        for (int i = 0; i < DropSQL.length; i++) {
            try {

                db.execSQL(DropSQL[i]);
                Log.d(TAG, "Dropped " + DropSQL[i]);

            } catch (SQLException e) {
                Log.e(TAG, "exception onupgrade " ,e);
            }
        }
    }


    private String[] intialisecreatesql() {
        String[] CreateSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {
            CreateSQL[i] = "Create table if not exists " +
                    TABLE_NAME[i] + " ( "
                    + columnName[i][0] + " INTEGER PRIMARY KEY AUTOINCREMENT , ";

            for (int j = 1; j < tableLength[i]; j++) {
                String temp = columnName[i][j] + " " + columnTypes[i][j] + " ";

                if ((j + 1) != tableLength[i])
                    temp = temp.concat(" , ");

                CreateSQL[i] = CreateSQL[i].concat(temp);
            }
            CreateSQL[i] = CreateSQL[i].concat(" );");
            Log.d("Parentdb", "" + CreateSQL[i]);
        }

        return CreateSQL;
    }

    public String[] intialisedrop() {
        String[] DropSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {

            DropSQL[i] = "Drop table if exists " + TABLE_NAME[i] + " ;";
            Log.d("Parentdb", "" + DropSQL[i]);
        }
        return DropSQL;
    }

    public void settingDatabase() {
        ifsset = 1;
        columnName = dbMetaData.columnNames;
        columnTypes = dbMetaData.columnTypes;
        tableLength = dbMetaData.tableLength;
        TABLE_NAME = dbMetaData.tableNames;
    }

    public void settingDatabase(String[][] columnNames,
                                String[][] columnTypes,
                                String[] tableNames, int[] tableLenth) {
        ifsset = 1;
        this.columnName = columnNames;
        this.columnTypes = columnTypes;
        this.tableLength = tableLenth;
        this.TABLE_NAME = tableNames;
    }

    public SQLiteDatabase returnSQl() {
        return getWritableDatabase();
    }
}

