package com.example.root.evanto;

/**
 * Created by root on 29/9/17.
 */

public class EachEvent {
    String EventName,Venue,City,StartDate,EndDate,Discription,Organizer,UserId,Mobile;

    public EachEvent(String eventName, String venue, String city, String startDate, String endDate, String discription ,String organizer, String mobile,String userId) {
        EventName = eventName;
        Venue = venue;
        City = city;
        StartDate = startDate;
        EndDate = endDate;
        Discription = discription;
        Organizer = organizer;
        UserId = userId;
        Mobile = mobile;
    }

    public EachEvent(String eventName, String venue, String city, String startDate, String endDate, String discription, String organizer,String mobile) {
        EventName = eventName;
        Venue = venue;
        City = city;
        StartDate = startDate;
        EndDate = endDate;
        Discription = discription;
        Organizer = organizer;
        Mobile = mobile;
    }
}
