package com.drishti.drishti17.services;

/**
 * Created by kevin on 2/15/17.
 */

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.drishticet.foregroundservice.action.main";
        public static String CHANGE_STATE = "com.drishticet.foregroundservice.action.change";
        public static String LOADED = "com.drishticet.foregroundservice.action.loaded";
        public static String PLAY_ACTION = "com.drishticet.foregroundservice.action.play";
        public static String PAUSE_ACTION = "com.drishticet.foregroundservice.action.pause";
        public static String STARTFOREGROUND_ACTION = "com.drishticet.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.drishticet.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
