package com.drishti.drishti17.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.drishti.drishti17.db.data.dbMetaData;
import com.drishti.drishti17.network.models.HighLightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by droidcafe on 3/5/2017
 */

public class HighlightsTable {

    private static final String TAG = HighlightsTable.class.getSimpleName();

    public final static String[][] columnNames = {
            dbMetaData.columnNames[1]
    };


    public final static String[][] columnTypes = {
            dbMetaData.columnTypes[1]
    };
    public final static int[] columnNos = {dbMetaData.tableLength[1]};
    public final static String[] tableNames = {dbMetaData.tableNames[1]};

    public static int insert(Context context, HighLightModel highLightModel) {

        String[] passString = {highLightModel.name, highLightModel.promo, highLightModel.image};

        int[] passInt = {highLightModel.server_serial_id, highLightModel.server_id};
        boolean[] passBoolean = {highLightModel.is_event};
        return insert(context, passString, passInt, passBoolean);
    }

    public static int insert(Context context, String[] passString,
                             int[] passInt, boolean[] passBoolean) {

        ContentValues contentTrip = new ContentValues();
        int int_index = 0, string_index = 0, boolean_index = 0;

        for (int i = 1; i < columnNos[0]; i++) {
            if (columnTypes[0][i].equals("INTEGER"))
                contentTrip.put(columnNames[0][i], passInt[int_index++]);
            else if (columnTypes[0][i].equals("BOOLEAN"))
                contentTrip.put(columnNames[0][i], passBoolean[boolean_index++]);
            else
                contentTrip.put(columnNames[0][i], passString[string_index++]);
        }

        Uri uri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI_DRISHTI, tableNames[0]);
        Uri insertedUri = context.getContentResolver().insert(uri, contentTrip);
        int eid = Integer.parseInt(insertedUri.getLastPathSegment());
        Log.d(TAG, "inserted hightlight id " + insertedUri.toString());

        return eid;
    }


    public static Cursor select(Context context, int tableno, int[] columnno,
                                String selection, String[] selectionArgs,
                                String orderby) {

        Uri uri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI_DRISHTI, tableNames[tableno]);
        String[] projection = new String[columnno.length];
        int j = 0;
        for (int i : columnno) {
            projection[j++] = columnNames[tableno][i];
        }
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, orderby);
    }

    public static int delete(Context context, int tableno, String selection, String[] selectionArgs) {
        Uri uri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI_DRISHTI, tableNames[tableno]);
        return context.getContentResolver().delete(uri, selection, selectionArgs);
    }

    public static List<HighLightModel> getHightLight(Context context, String selection,
                                                     String[] selectionArgs, String orderBy) {

        int column[] = {0, 1, 2, 3, 4, 5, 6};
        Cursor hightlightCursor = select(context, 0, column, selection, selectionArgs, orderBy);
        List<HighLightModel> lightModels = new ArrayList<>(hightlightCursor.getCount());

        while (hightlightCursor.moveToNext()) {

            int id = hightlightCursor.getInt(hightlightCursor.getColumnIndex(columnNames[0][0]));
            int server_serial_id = hightlightCursor.getInt(hightlightCursor.getColumnIndex(columnNames[0][1]));
            String name = hightlightCursor.getString(hightlightCursor.getColumnIndex(columnNames[0][2]));
            String promo = hightlightCursor.getString(hightlightCursor.getColumnIndex(columnNames[0][3]));
            String image = hightlightCursor.getString(hightlightCursor.getColumnIndex(columnNames[0][4]));
            boolean is_event = hightlightCursor.getInt(hightlightCursor.getColumnIndex(columnNames[0][5])) == 1;
            int server_id = hightlightCursor.getInt(hightlightCursor.getColumnIndex(columnNames[0][6]));

            HighLightModel model = new HighLightModel(name, promo, image, id, server_serial_id, is_event ,server_id);
            lightModels.add(model);
        }
        return lightModels;
    }

    public static int deleteAll(Context context) {
        return delete(context, 0, null, null);
    }

}
