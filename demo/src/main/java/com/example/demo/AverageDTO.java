package com.example.demo;

import java.sql.Date;

public class AverageDTO {
    private Date[] dates;
    private double[] data;
    private double[] avg;


    public AverageDTO(Date[] dates, double[] data, double[] avg) {
        this.dates = dates;
        this.data = data;
        this.avg = avg;
    }


    public Date[] getDates() {
        return this.dates;
    }

    public void setDates(Date[] dates) {
        this.dates = dates;
    }

    public double[] getData() {
        return this.data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public double[] getAvg() {
        return this.avg;
    }

    public void setAvg(double[] avg) {
        this.avg = avg;
    }

}
