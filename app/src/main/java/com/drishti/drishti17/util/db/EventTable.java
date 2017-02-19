package com.drishti.drishti17.util.db;

import com.drishti.drishti17.network.models.EventModel;
import com.orm.SugarRecord;

/**
 * Created by droidcafe on 2/12/2017
 */

public class EventTable extends SugarRecord {

    public int server_id, prize1, prize2, prize3,maxPerGroup,regFee;
    public String name,description,format,category,image,day,time;
    public String contactName1,contactPhone1,contactEmail1,contactName2,contactPhone2,contactEmail2;
    public String adminId;
    public boolean isWorkshop,isgroup;

    public EventTable() {

    }

    public EventTable(EventModel eventModel) {
        server_id = eventModel.id ;
        name = eventModel.name;
        description = eventModel.description;
        format = eventModel.format;
        prize1 = eventModel.prize1;
        prize2 = eventModel.prize2;
        prize3 = eventModel.prize3;
        category =  eventModel.category;
        isgroup = eventModel.group;
        regFee  = eventModel.regFee;
        day = eventModel.day;
        time  = eventModel.time;
        contactName1  = eventModel.contactName1;
        contactEmail1   = eventModel.contactEmail1;
        contactPhone1  = eventModel.contactPhone1;
        contactName2  = eventModel.contactName2;
        contactEmail2   = eventModel.contactEmail2;
        contactPhone2  = eventModel.contactPhone2;
        adminId = eventModel.adminId;
        isWorkshop = eventModel.isWorkshop;
        image = eventModel.image;
        maxPerGroup = eventModel.maxPerGroup;


    }
}
