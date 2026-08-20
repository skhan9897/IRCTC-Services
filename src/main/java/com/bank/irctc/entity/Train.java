package com.bank.irctc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trainNumber;

    @Column(nullable = false)
    private String trainName;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    private String departureTime;

    private String arrivalTime;

    private Integer totalSeats;

    private Integer availableSeats;

    private Double sleeperFare;

    private Double ac3Fare;

    private Double ac2Fare;

    private Double ac1Fare;

    public Train() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getSleeperFare() {
        return sleeperFare;
    }

    public void setSleeperFare(Double sleeperFare) {
        this.sleeperFare = sleeperFare;
    }

    public Double getAc3Fare() {
        return ac3Fare;
    }

    public void setAc3Fare(Double ac3Fare) {
        this.ac3Fare = ac3Fare;
    }

    public Double getAc2Fare() {
        return ac2Fare;
    }

    public void setAc2Fare(Double ac2Fare) {
        this.ac2Fare = ac2Fare;
    }

    public Double getAc1Fare() {
        return ac1Fare;
    }

    public void setAc1Fare(Double ac1Fare) {
        this.ac1Fare = ac1Fare;
    }
}