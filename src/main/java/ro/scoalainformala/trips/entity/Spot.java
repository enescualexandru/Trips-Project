package ro.scoalainformala.trips.entity;

import javax.persistence.*;

@Entity
@Table(name = "SPOT")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION", columnDefinition="text")
    private String description;

    @Column(name = "FILEPATH", length = 2048)
    private String filePath;

    @ManyToOne(targetEntity = Trip.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "TRIPID", nullable = true)
    private Trip trip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
