package com.drishti.drishti17.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.drishti.drishti17.db.data.dbMetaData;
import com.drishti.drishti17.network.models.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nirmal on 3/4/2017
 */

public class EventsTable {

    private static final String TAG = EventsTable.class.getSimpleName();

    public final static String[][] columnNames = {
            dbMetaData.columnNames[0]
    };


    public final static String[][] columnTypes = {
            dbMetaData.columnTypes[0]
    };
    public final static int[] columnNos = {dbMetaData.tableLength[0]};
    public final static String[] tableNames = {dbMetaData.tableNames[0]};

    public static int insert(Context context, EventModel eventtable) {

        if(eventtable.day == null || eventtable.day.equals(""))
            eventtable.day = "To be announced";


        if(eventtable.time == null || eventtable.time.equals(""))
            eventtable.time = "To be announced";
        String[] passString = {eventtable.name, eventtable.description, eventtable.format,
                eventtable.category, eventtable.image, eventtable.day, eventtable.time,
                eventtable.contactName1, eventtable.contactPhone1, eventtable.contactEmail1,
                eventtable.contactName2, eventtable.contactPhone2, eventtable.contactEmail2, eventtable.adminId};

        int[] passInt = {eventtable.server_id, eventtable.prize1, eventtable.prize2, eventtable.prize3,
                eventtable.maxPerGroup, eventtable.regFee};
        boolean[] passBolean = {eventtable.isWorkshop, eventtable.group};
        return insert(context, passString, passInt, passBolean);
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
        Log.d(TAG, "inserted trip id " + insertedUri.toString());

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

    public static Cursor selectEventMinified(Context context, String selection, String[] selectionArgs, String orderby) {
        int[] columnno = {0, 1, 2,3, 5, 6};
        return select(context, 0, columnno, selection, selectionArgs, orderby);
    }

    public static int deleteAll(Context context) {
        return delete(context, 0, null, null);
    }

    public static EventModel getEvent(Context context, String selection, String[] selectionArgs, String orderby) {
        int columnNo[] = new int[columnNos[0]];
        for (int i = 0; i < columnNos[0]; i++) {
            columnNo[i] = i;
        }
        Cursor eventCursor = select(context, 0, columnNo, selection, selectionArgs, orderby);
        EventModel model = new EventModel();
        while (eventCursor.moveToNext()) {
            model = cursorToModel(eventCursor);
        }
        return model;
    }

    public static List<EventModel> getAllEventsMinified(Context context, String selection, String[] selectionArgs, String orderby) {
        Cursor eventCursor = selectEventMinified(context, selection, selectionArgs, orderby);
        List<EventModel> eventList = new ArrayList<>(eventCursor.getCount());
        while (eventCursor.moveToNext()) {
            EventModel model = new EventModel();
            model.id = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][0]));
            model.server_id = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][1]));
            model.name = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][2]));
            model.description = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][3]));
            model.category = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][5]));
            model.image = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][6]));

            eventList.add(model);

        }
        Log.d(TAG, "getAllEventsMinified: no of events " + eventList.size());
        return eventList;
    }

    public static EventModel cursorToModel(Cursor eventCursor) {
        EventModel model = new EventModel();
        model.id = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][0]));
        model.server_id = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][1]));
        model.name = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][2]));
        model.description = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][3]));
        model.format = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][4]));
        model.category = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][5]));
        model.image = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][6]));
        model.day = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][7]));
        model.time = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][8]));
        model.prize1 = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][9]));
        model.prize2 = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][10]));
        model.prize3 = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][11]));
        model.maxPerGroup = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][12]));
        model.regFee = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][13]));
        model.contactName1 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][14]));
        model.contactPhone1 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][15]));
        model.contactEmail1 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][16]));
        model.contactName2 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][17]));
        model.contactPhone2 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][18]));
        model.contactEmail2 = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][19]));
        model.adminId = eventCursor.getString(eventCursor.getColumnIndex(columnNames[0][20]));


        model.isWorkshop = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][21])) == 1;
        model.group = eventCursor.getInt(eventCursor.getColumnIndex(columnNames[0][22])) == 1;

        return model;

    }


}
