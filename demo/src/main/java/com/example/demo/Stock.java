package com.example.demo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {
    @Id
    private String id;

    @Column
    private Date date;

    @Column
    private double high;

    @Column
    private double low;

    @Column
    private double open;

    @Column
    private double close;

    @Column
    private double volume;

    @Column
    private double adjClose;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getHigh() {
        return this.high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return this.low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return this.open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return this.close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAdjClose() {
        return this.adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }    
}
