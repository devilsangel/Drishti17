package com.drishti.drishti17.db.data;

/**
 * Created by nirmal on 3/4/2017
 */

public class dbMetaData {
    public static final String DATABASE_SCHEMA = "db_drishti.db";
    public static int DATABASE_VERSION = 2;
    public static int DATABASE_VERSION_LEGACY = DATABASE_VERSION - 1;


    public static String[][] columnNames = {
          /*1*/{"_id", "server_id", "name", "description", "format", "category", "image", "day", "time"
            , "prize1", "prize2", "prize3", "maxPerGroup", "regFee",
            "contactName1", "contactPhone1", "contactEmail1", "contactName2", "contactPhone2", "contactEmail2"
            , "admin_id", "is_workshop", "is_group"},
            {
                    "_id", "server_id", "name", "promo", "image"
            }
    };


    public static String[][] columnTypes = {

           /*1*/{"INTEGER", "INTEGER", "VARCHAR(100)", "VARCHAR(10000)", "VARCHAR(20000)", "VARCHAR(20)", "VARCHAR(500)", "VARCHAR(20)", "VARCHAR(20)"
            , "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"
            , "VARCHAR(100)", "VARCHAR(20)", "VARCHAR(50)", "VARCHAR(100)", "VARCHAR(20)", "VARCHAR(50)"
            , "VARCHAR(20)", "BOOLEAN", "BOOLEAN"},
            {"INTEGER", "INTEGER", "VARCHAR(100)", "VARCHAR(1000)", "VARCHAR(1000)"}
    };
    public static int[] tableLength = {
          /*1*/23,5
    };


    public static String[] tableNames = {
            /*1*/"events","highlights"

    };


}
