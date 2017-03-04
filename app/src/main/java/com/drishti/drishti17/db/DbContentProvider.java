package com.drishti.drishti17.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class DbContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.drishti.drishti17.dbprovider";
    public static final String BASE_PATH_DRISHTI = "dristhi";
    public static final Uri CONTENT_URI_DRISHTI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_DRISHTI);

    SQLiteDatabase database;

    public DbContentProvider() {
    }


    @Override
    public boolean onCreate() {
        database = DBOpenHelper.newInstance(getContext()).returnSQl();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String tableName = uri.getLastPathSegment();
        return database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String tableName = uri.getLastPathSegment();
        long id = database.insert(tableName, null, contentValues);
        return Uri.parse(BASE_PATH_DRISHTI + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = uri.getLastPathSegment();
        return database.update(tableName, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = uri.getLastPathSegment();
        return database.delete(tableName, selection, selectionArgs);
    }
}
