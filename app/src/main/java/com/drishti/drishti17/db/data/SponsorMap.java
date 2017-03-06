package com.drishti.drishti17.db.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nirmal on 3/5/2017
 */

public class SponsorMap {

    final static Sponsor otherSponsors[] = {new Sponsor("TCS", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Ftcs.jpg?alt=media&token=e4692bf4-ed1b-45b8-975f-1302d9311557"),
            new Sponsor("Shastra Bhavan", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fshastra.jpg?alt=media&token=8cbed23a-dfec-4133-a23b-93acd0abce8e"),
            new Sponsor("Riya", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Friya.png?alt=media&token=89cf0cff-e7f5-43a8-b9cc-48b5deddc2cb")};

    final static Sponsor mobilitySponsor[] = {new Sponsor("Uber","https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2FUber.png?alt=media&token=474548ef-546d-4272-9dc0-c37017899f20")};

    static Map<String, Sponsor[]> otherMap = new HashMap<>();

    public static Map<String, Sponsor[]> getMap() {
        otherMap.put("Mobility Sponsors", mobilitySponsor);
        otherMap.put("Other Sponsors", otherSponsors);

        return otherMap;
    }

    public static class Sponsor {

        public Sponsor(String title, String image) {
            this.title = title;
            this.image = image;
        }

        String title, image;

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }
    }

}
