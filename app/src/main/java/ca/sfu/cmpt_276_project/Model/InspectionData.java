/*
 * Class: InspectionData
 *
 * Data descriptions:
 * 1. trackingNumber: A String Data type indicates its unique tracking number
 * 2. inspectionDate: A Date Data type indicates its inspection date
 * 3. inspectionType: A enum Data type of two types - ROUTINE or FOLLOW_UP
 * 4. criticalViolations: An integer Data type indicates number of critical violations
 * 5. nonCriticalViolations: An integer Data type indicates number of non-critical violations
 * 6. hazard: A enum Data type of three types - LOW/MEDIUM/HIGH
 * 7. violation: A Violation Data type that includes information of a violation
 *
 * Functions:
 * 1. Getters
 * 2. Setters
 * 3. Default & Non-default constructor
 * 4. Display
 *
 * */
package ca.sfu.cmpt_276_project.Model;
import java.text.DateFormat;
import java.util.Date;

public class InspectionData {
    private String trackingNumber;
    private Date inspectionDate;
    private Type inspectionType;
    private int criticalViolations;
    private int nonCriticalViolations;
    private Hazard hazard;
    private Violation violation;


    //Getters
    public String getTrackingNumber() {
        return trackingNumber;
    }
    public Date getInspectionDate() {
        return inspectionDate;
    }
    public Type getInspectionType() {
        return inspectionType;
    }
    public int getCriticalViolations() {
        return criticalViolations;
    }
    public int getNonCriticalViolations() {
        return nonCriticalViolations;
    }
    public Hazard getHazard() {
        return hazard;
    }
    public Violation getViolation() {
        return violation;
    }

    //Setters

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }
    public void setInspectionType(Type inspectionType) {
        this.inspectionType = inspectionType;
    }
    public void setCriticalViolations(int criticalViolations) {
        this.criticalViolations = criticalViolations;
    }
    public void setNonCriticalViolations(int nonCriticalViolations) {
        this.nonCriticalViolations = nonCriticalViolations;
    }
    public void setHazard(Hazard hazard) {
        this.hazard = hazard;
    }
    public void setViolation(Violation violation) {
        this.violation = violation;
    }

    //Default Constructor
    public InspectionData() {
        this.trackingNumber = null;
        Date dummy_date = new Date(1970-01-01);
        this.inspectionDate = dummy_date;
        this.inspectionType = Type.ROUTINE;
        this.criticalViolations = 0;
        this.nonCriticalViolations = 0;
        this.hazard = Hazard.LOW;
        this.violation = new Violation();
    }

    //Non Default Constructor
    public InspectionData(String trackingNumber, Date inspectionDate, Type inspectionType, int criticalViolations, int nonCriticalViolations, Hazard hazard, Violation violation) {
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.criticalViolations = criticalViolations;
        this.nonCriticalViolations = nonCriticalViolations;
        this.hazard = hazard;
        this.violation = violation;
    }

    public void Display(){
        System.out.println("Inspection Tracking Number: "+this.getTrackingNumber()
                +"\nInspection Date: "+this.getInspectionDate()
                +"\nInspection Type: "+this.getInspectionType()
                +"\nCritical Violation: "+this.getCriticalViolations()
                +"\nNon Critical Violation: "+this.getNonCriticalViolations()
                +"\nHazard: "+this.getHazard()
                +"\n*****************************************\nViolation details: \n");
        this.getViolation().Display();
    }
}
