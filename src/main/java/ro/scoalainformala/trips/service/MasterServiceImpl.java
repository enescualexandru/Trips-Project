package ro.scoalainformala.trips.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.scoalainformala.trips.entity.Spot;
import ro.scoalainformala.trips.entity.Trip;
import ro.scoalainformala.trips.entity.TripContainer;
import ro.scoalainformala.trips.repository.SpotRepository;
import ro.scoalainformala.trips.repository.TripRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Handles the logic tier of the entire application.
 */
@Service
public class MasterServiceImpl implements MasterService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private SpotRepository spotRepository;

    /**
     * absolute addresses for the images saved on the disk
     */
    private final String adressStatic = "C:\\Users\\Alex\\IdeaProjects\\trips\\src\\main\\resources\\public\\";
    private final String adressProduction = "C:\\Users\\Alex\\IdeaProjects\\trips\\out\\production\\resources\\public\\";

    /**
     * Method to write into the database trip details, as well as saving the images uploaded by the user.
     * @param tc TripContainer object, containing details of the trip.
     */
    @Override
    public void createEntireTrip(TripContainer tc) {
        Random random = new Random();
        Trip trip = tc.getTrip();
        Spot spot1;
        Spot spot2;

        spot1 = tc.getSpot1() != null ? tc.getSpot1() : tc.getSpotList().get(0);
        spot2 = tc.getSpot2() != null ? tc.getSpot2() : tc.getSpotList().get(1);

        MultipartFile file1Static = tc.getImg1();
        MultipartFile file2Static = tc.getImg2();
        InputStream file1 = null;
        InputStream file2 = null;
        try {
            file1 = file1Static.getInputStream();
            file2 = file2Static.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tripPhoto1Name = trip.getTitle().replace(" ", "")
                + spot1.getTitle().replace(" ", "") + random.nextInt(1000) + file1Static.getOriginalFilename();

        String tripPhoto2Name = trip.getTitle().replace(" ", "")
                + spot2.getTitle().replace(" ", "") + random.nextInt(1000) + file2Static.getOriginalFilename();

        File target1Static = new File(adressStatic, tripPhoto1Name);
        File target2Static = new File(adressStatic, tripPhoto2Name);
        File target1 = new File(adressProduction, tripPhoto1Name);
        File target2 = new File(adressProduction, tripPhoto2Name);

        try {
            Files.copy(file1, target1.toPath());
            Files.copy(file2, target2.toPath());
            file1Static.transferTo(target1Static);
            file2Static.transferTo(target2Static);
        } catch (IOException e) {
            e.printStackTrace();
        }

        spot1.setFilePath(tripPhoto1Name);
        spot2.setFilePath(tripPhoto2Name);

        spot1.setTrip(trip);
        spot2.setTrip(trip);

        tc.addSpot(spot1);
        tc.addSpot(spot2);

        tripRepository.save(trip);
        spotRepository.save(spot1);
        spotRepository.save(spot2);
    }

    /**
     * Method returning an object of TripContainer type
     * @param tc TripContainer object
     * @return and object of TripContainer type, populated with trip details.
     */
    @Override
    public TripContainer getEntireTrip(TripContainer tc) {
        Long tripid = tc.getTrip().getId();
        Long spot1id = tc.getSpot1().getId();
        Long spot2id = tc.getSpot2().getId();
        Trip trip = tripRepository.findById(tripid).orElse(null);
        Spot spot1 = spotRepository.findById(spot1id).orElse(null);
        Spot spot2 = spotRepository.findById(spot2id).orElse(null);
        tc.setTrip(trip);
        tc.setSpot1(spot1);
        tc.setSpot2(spot2);
        return tc;
    }


    /**
     * Method to delete a trip details from the database. It deletes also the photos from the disk.
     * @param tripId trip database id on which deletion will be executed.
     */
    @Override
    public void deleteEntireTrip(Long tripId) {
        List<Spot> allSpots = new ArrayList<>();
        List<Spot> spotsToDelete = new ArrayList<>();
        allSpots = spotRepository.findAll();
        for (Spot s : allSpots) {
            if (s.getTrip().getId().equals(tripId)) {
                spotsToDelete.add(s);
            }
        }
        try {
            for (Spot spot : spotsToDelete) {
                Files.deleteIfExists(Paths.get(adressProduction + spot.getFilePath()));
                Files.deleteIfExists(Paths.get(adressStatic + spot.getFilePath()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        spotRepository.deleteInBatch(spotsToDelete);
        tripRepository.deleteById(tripId);
    }

    /**
     * method to delete an entire trip from the database.
     * @param tc TripContainer object, of which details will be deleted.
     */
    @Override
    public void deleteEntireTrip(TripContainer tc) {
        Trip trip = tc.getTrip();
        Spot spot1 = tc.getSpotList().get(0);
        Spot spot2 = tc.getSpotList().get(1);
        try {
            Files.deleteIfExists(Paths.get(adressStatic + spot1.getFilePath()));
            Files.deleteIfExists(Paths.get(adressStatic + spot2.getFilePath()));
            Files.deleteIfExists(Paths.get(adressProduction + spot1.getFilePath()));
            Files.deleteIfExists(Paths.get(adressProduction + spot2.getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tripRepository.delete(trip);
        spotRepository.delete(spot1);
        spotRepository.delete(spot2);

    }

    /**
     * Method which gets all the trips from the database
     * @return a list with all TripContainer objects.
     */
    @Override
    public List<TripContainer> getAllEntireTripsList() {
        List<TripContainer> tcList = new ArrayList();
        Set<Trip> setTrip = new LinkedHashSet<>();

        List<Spot> allSpots = new LinkedList<>();
        allSpots = spotRepository.findAll();

        for (Spot s : allSpots) {
            setTrip.add(s.getTrip());
        }
        for (Trip trip : setTrip) {
            TripContainer tc = new TripContainer();
            tc.setTrip(trip);
            for (Spot spot : allSpots) {
                if (spot.getTrip().getTitle().equals(trip.getTitle())) {
                    tc.addSpot(spot);
                }
            }
            tcList.add(tc);

        }
        return tcList;
    }

    /**
     * Method which returns all the trips, specifically the trip with the specified
     * id will be in the head of the list(on index 0)
     * @param tripID trip database id which will be the first in the returned list
     * @return returns a list with all trips entries
     */
    @Override
    public List<TripContainer> getAllEntireTripsList(Long tripID) {
        List<TripContainer> tcList = new ArrayList<>();
        List<TripContainer> all = getAllEntireTripsList();

        for (TripContainer t : all) {
            if (t.getTrip().getId().equals(tripID)) {
                tcList.add(0, t);
                break;
            }
        }
        for (TripContainer t : all) {
            if (!t.getTrip().getId().equals(tripID)) {
                tcList.add(t);
            }
        }
        return tcList;
    }

    /**
     * Method to get all the trips entries.
     * @return a map containing all the trips. Key represents a trip, and the value is a list
     * with spots(places) for that trip
     */
    @Override
    public Map<Trip, List<Spot>> getAllEntireTripsMap() {
        Set<Trip> setTrip = new LinkedHashSet<>();
        List<Spot> listSpot = new LinkedList<>();
        Map<Trip, List<Spot>> returnedMap = new LinkedHashMap<>();
        listSpot = spotRepository.findAll();

        for (Spot s : listSpot) {
            setTrip.add(s.getTrip());
        }
        for (Trip trip : setTrip) {
            List<Spot> spots = new LinkedList<>();
            for (Spot spot : listSpot) {
                if (spot.getTrip().getTitle().equals(trip.getTitle())) {
                    spots.add(spot);
                }
            }
            returnedMap.put(trip, spots);

        }
        return returnedMap;
    }
}
