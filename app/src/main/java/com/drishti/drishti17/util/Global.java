package com.drishti.drishti17.util;

/**
 * Created by nirmal on 2/9/2017
 */

public class Global {

    public static final String SHARED_PREF = "drishti_shared_pref";
    public static boolean isDownloading = false;
    public static boolean isHighlightDownloading = false;
    public static boolean isguest=true;


    public final static int SUCCESS_CODE = 200;

    //Shared preference keys


    public static final String PREF_DB_STATUS = "db_status";
    public static final String PREF_DB_VERSION = "db_version";
    public static final String PREF_DB_VERSION_LEGACY = "db_version_legacy";
    public static final String PREF_EVENT_CURRENT_VERSION = "event_current_version";
    public static final String PREF_HIGHLIGHT_CURRENT_VERSION = "high_light_current_version";
    public static final String PREF_EVENT_UPDATE_VERSION = "event_update_version";
    public static final String PREF_HIGHLIGHT_UPDATE_VERSION = "hightlight_update_version";
    public static final String PREF_HOME_PROMPT_SHOWN = "help_prompt_home";
    public static final String PREF_EVENTS_LIST_PROMPT_SHOWN = "help_prompt_event_list";
    public static final String PREF_EVENTS_PAGE_PROMPT_SHOWN = "help_prompt_event_page";
}
