package com.drishti.drishti17.db.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nirmal on 3/5/2017
 */

public class SponsorMap {

    final static Sponsor eventSponors[] = {new Sponsor("Red Hat", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fredhat.png?alt=media&token=9e1ac6b9-3449-4c36-90c6-2ab020ed3ff6"),
            new Sponsor("TCS", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Ftcs.jpg?alt=media&token=e4692bf4-ed1b-45b8-975f-1302d9311557"),
    };

    final static Sponsor paymentPartner[] = {new Sponsor("Airtel Payments Bank", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fairtel.png?alt=media&token=47c094f6-8424-4589-9ff7-a18801d51867")};
    final static Sponsor otherSponsors[] = {
            new Sponsor("Shastra Bhavan", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fshastra.jpg?alt=media&token=8cbed23a-dfec-4133-a23b-93acd0abce8e"),
            new Sponsor("Riya", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Friya.png?alt=media&token=89cf0cff-e7f5-43a8-b9cc-48b5deddc2cb"),
            new Sponsor("Vivo", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fvivo.png?alt=media&token=f1643bbb-b3a5-4905-830d-9e4c1ab23995"),
            new Sponsor("Le Arabia", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Flearabia.png?alt=media&token=658571ac-2d73-48bf-9dc5-b85c990607eb"),
            new Sponsor("Entecity.com", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fentecity.png?alt=media&token=96d5f83d-c0dd-459c-b8a8-d71aa24c9e43"),
            new Sponsor("Chungath Jewellery", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fchungath.png?alt=media&token=296c780d-0773-4d9c-b971-e85aaa90562b"),
            new Sponsor("Big FM", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fbigfm.png?alt=media&token=55939aaa-48f7-475f-ba3b-3f5adae6321c"),
            new Sponsor("CETAA Bangalore", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fcetaa.jpeg?alt=media&token=ec9e19ac-c151-42ad-836f-ce42708ad067"),
            new Sponsor("SBT", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fsbt.png?alt=media&token=f7dc765b-a888-4527-92b1-6a116428bd03")
    };

    final static Sponsor mobilitySponsor[] = {new Sponsor("Uber", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2FUber.png?alt=media&token=474548ef-546d-4272-9dc0-c37017899f20")};
    final static Sponsor merchandise[] = {new Sponsor("Thoughtline", "https://firebasestorage.googleapis.com/v0/b/drishti-bd782.appspot.com/o/images%2Fsponsor%2Fthoughtline.png?alt=media&token=11aa95ff-74e7-4628-9e9b-f8ae4315331f")};


    static Map<String, Sponsor[]> otherMap = new HashMap<>();

    public static Map<String, Sponsor[]> getMap() {
        otherMap.put("Merchandise Sponsor", merchandise);
        otherMap.put("Mobility Sponsor", mobilitySponsor);
        otherMap.put("Event Sponsors", eventSponors);
        otherMap.put("Payments Partner", paymentPartner);
        otherMap.put("Sponsors", otherSponsors);

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
