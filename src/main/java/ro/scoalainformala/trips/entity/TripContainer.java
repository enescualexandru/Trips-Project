package ro.scoalainformala.trips.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class TripContainer {
    private Trip trip;
    private Spot spot1;
    private Spot spot2;
    MultipartFile img1;
    MultipartFile img2;
    private List<Spot> spotList = new ArrayList<>();


    public MultipartFile getImg1() {
        return img1;
    }

    public void setImg1(MultipartFile  img1) {
        this.img1 = img1;
    }

    public MultipartFile  getImg2() {
        return img2;
    }

    public void setImg2(MultipartFile  img2) {
        this.img2 = img2;
    }

    public void addSpot(Spot spot) {
        spotList.add(spot);
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public void setSpotList(List<Spot> spotList) {
        this.spotList = spotList;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Spot getSpot1() {
        return spot1;
    }

    public void setSpot1(Spot spot1) {
        this.spot1 = spot1;
    }

    public Spot getSpot2() {
        return spot2;
    }

    public void setSpot2(Spot spot2) {
        this.spot2 = spot2;
    }
}
