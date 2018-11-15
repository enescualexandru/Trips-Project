package ro.scoalainformala.trips.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.scoalainformala.trips.entity.TripContainer;
import ro.scoalainformala.trips.service.MasterService;

import java.util.List;

@Controller
public class MyAppController {
    private final MasterService masterService;

    @Autowired
    public MyAppController(MasterService masterService) {
        this.masterService = masterService;
    }

    /**
     * Method mapped to index(home). It returns the template home.html if there is any trip in the database,
     * otherwise redirecting to 'add trip'.
     * @param flash message to be sent back, once a delete or edit has taken place.
     * @param tripID trip database id, based on which the 'Trips' list is sent to the html template
     * @param model model
     * @return returns either home.html or redirects to addtrip
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homeGet(@ModelAttribute("msg")String flash,@RequestParam(value = "id", required = false) Long tripID, Model model) {
        List<TripContainer> returnedList = masterService.getAllEntireTripsList();
       // model.addAttribute(flash);
        if (returnedList.size() > 0) {

            if (tripID != null) {
                returnedList = masterService.getAllEntireTripsList(tripID);
                model.addAttribute("tripsList", returnedList);
            } else {
                model.addAttribute("tripsList", returnedList);

            }
            return "home";
        } else {
            return "redirect: addtrip";
        }
    }

    /**
     * Method meant to return to the user the add trip page. Is injects the thymeleaf template with
     * a new TripContainer object, subject to be populated by the 'post' request.
     * @param model model
     * @return template addtrip.html, which contains the form required for a trip details.
     */
    @RequestMapping(value = "/addtrip", method = RequestMethod.GET)
    public String returnTripForm(Model model) {
        model.addAttribute("tc", new TripContainer());
        return "addtrip";

    }

    /**
     * Method which is adding a trip entry to the database.
     * @param tc TripContainer object, populated with the users input details.
     * @param model model
     * @param redirectAttributes handles the message confirming entry is successfully, handing over further to home.
     * @return redirects to the main page.
     */
    @RequestMapping(value = "/addtrip", method = RequestMethod.POST)
    public String createFullTrip(@ModelAttribute TripContainer tc, Model model,RedirectAttributes redirectAttributes) {
        masterService.createEntireTrip(tc);
        redirectAttributes.addAttribute("msg","Succesfully added trip "+tc.getTrip().getTitle());
        return "redirect:";

    }

    /**
     * Method meant to return to the user edit form. It is populated with the details of the trip subject
     * to be edited.
     * @param tc represents a TripContainer object, populated with the trip details, subject to be edited
     * @param tripId the database id of the object to be edited.
     * @param model model
     * @return edittrip.html. Contains form requestinng user input for (edited) trip details.
     */
    @GetMapping(value = "/edit")
    public String returnTripForm(@ModelAttribute TripContainer tc, @RequestParam(value = "id", required = true) Long tripId, Model model) {
        model.addAttribute("id", tripId);
        model.addAttribute("tc", masterService.getAllEntireTripsList(tripId).get(0));
        return "edittrip";
    }

    /**
     * Method which is editing a trip into the database. It adds the edited trip as a
     * new trip, and deletes the previous one(before editing).
     * @param tc TripContainer object containg the edited trip details
     * @param tripId previous(before edit) trip database id. Trip which will be deleted.
     * @param redirectAttributes handles the message confirming editing is successfully, handing over further to home.
     * @return redirects to the main page
     */
    @PostMapping(value = "/edit")
    public String editFormAndDelete(@ModelAttribute TripContainer tc, @RequestParam(value = "id", required = true) Long tripId, RedirectAttributes redirectAttributes) {
        masterService.createEntireTrip(tc);
        masterService.deleteEntireTrip(tripId);
        redirectAttributes.addAttribute("msg","Succesfully edited the trip "+tc.getTrip().getTitle());
        return "redirect:";
    }

    /**
     * Method meant do delete a trip from the database.
     * @param tripId trip database id, based on which deletion will be executed
     * @param model model
     * @param redirectAttributes handles the message confirming deletion is successfully, handing over further to home.
     * @return redirects to the main page. Is displaying the main page post a delete operation
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteTrip(@RequestParam(value = "id", required = true) Long tripId, Model model, RedirectAttributes redirectAttributes) {
        masterService.deleteEntireTrip(tripId);
        redirectAttributes.addAttribute("msg","Succesfully removed the trip ");
        model.addAttribute("tripsList", masterService.getAllEntireTripsList());
        return "redirect:";
    }


    /**
     *  Mapped to /profile
     * @return profile.html. Page containing user profile details
     */
    @GetMapping(value = "/profile")
    public String getProfilePage() {
        return "profile";
    }


    /**
     * Method which return the template containing all the database entries. for debugging only
     * @param model
     * @return resutls.html.
     */
    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String displayTrip(Model model) {
        model.addAttribute("tripsMap", masterService.getAllEntireTripsMap());
        return "results";

    }
}