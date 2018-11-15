package ro.scoalainformala.trips.service;

import ro.scoalainformala.trips.entity.Spot;
import ro.scoalainformala.trips.entity.Trip;
import ro.scoalainformala.trips.entity.TripContainer;

import java.util.List;
import java.util.Map;

public interface MasterService {
    void createEntireTrip(TripContainer tc);

    TripContainer getEntireTrip(TripContainer tc);

    void deleteEntireTrip(TripContainer tc);

    void deleteEntireTrip(Long tripId);

    List<TripContainer> getAllEntireTripsList(Long tripID);

    List<TripContainer> getAllEntireTripsList();

    Map<Trip, List<Spot>> getAllEntireTripsMap();

}
