/*
 * Class: Restaurant
 *
 * Data descriptions:
 * 1. trackNumber: An integer indicates its unique tracking number
 * 2. restaurantName: A String Data type shows the name of the restaurant
 * 3. physicalAddress: A String Data type shows the physical address of the restaurant
 * 4. physicalCity: A String Data type shows the physical city of the restaurant
 * 5. facType: A String Data type shows the type of the restaurant
 * 6. latitude: A double Data type shows the latitude of the restaurant
 * 7. longitude: A double Data type shows the longitude of the restaurant
 * 8. data: An Inspection Data type, which stores all the details of a restaurant object
 * 9. icon: An integer indicates id of the restaurant icon
 *
 * Functions:
 * 1. Getters
 * 2. Setters
 * 3. Default & Non-Default Constructor
 * 4. Display
 *
 * */
package ca.sfu.cmpt_276_project.Model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Comparable<Restaurant> {
    private String trackNumber;
    private String restaurantName;
    private String physicalAddress;
    private String physicalCity;
    private String facType;
    private double latitude;
    private double longitude;
    private int icon;
    private Boolean isFavourite;
    private List<InspectionData> inspectionDataList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;


    //Default Constructor
    public Restaurant() {
        this.trackNumber = null;
        this.restaurantName = null;
        this.physicalAddress = null;
        this.physicalCity = null;
        this.facType = null;
        this.latitude = 0;
        this.longitude = 0;
        this.icon = 0;
        this.isFavourite = false;
    }

    //Non-Default Constructor
    public Restaurant(String trackNumber, String restaurantName, String physicalAddress,
                      String physicalCity, String facType, double latitude, double longitude,
                      List<InspectionData> inspectionDataList) {
        this.trackNumber = trackNumber;
        this.restaurantName = restaurantName;
        this.physicalAddress = physicalAddress;
        this.physicalCity = physicalCity;
        this.facType = facType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inspectionDataList = inspectionDataList;
        this.isFavourite = false;
    }

    //Getters
    public String getTrackNumber() {
        return trackNumber;
    }

    //Setters
    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public List<InspectionData> getInspectionDataList() {
        return inspectionDataList;
    }

    public void setInspectionDataList(List<InspectionData> inspectionDataList) {
        this.inspectionDataList = inspectionDataList;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalCity() {
        return physicalCity;
    }

    public void setPhysicalCity(String physicalCity) {
        this.physicalCity = physicalCity;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    //Display
    public void Display() {
        System.out.println
                ("\n--------------------------------------------------------\nRestaurant Tracking Number: " + this.getTrackNumber()
                        + "\nRestaurant Name: " + this.getRestaurantName()
                        + "\nPhysical Address: " + this.getPhysicalAddress()
                        + "\nPhysical City: " + this.getPhysicalCity()
                        + "\nFac Type: " + this.getFacType()
                        + "\nLatitude: " + this.getLatitude()
                        + "\nLongitude: " + this.getLongitude()
                        + "\nInspection Data: \n=========================================================\n");
        for (InspectionData inspectionData : this.inspectionDataList) {
            inspectionData.Display();
        }
    }

    @Override
    public int compareTo(Restaurant other) {
        return restaurantName.compareTo(other.restaurantName);
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
