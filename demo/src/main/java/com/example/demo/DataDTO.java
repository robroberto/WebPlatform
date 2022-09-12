package com.example.demo;

public class DataDTO {
    private String[] date;
    private double[] close;
    private String[] name;

    public DataDTO(String[] date, double[] close) {
        this.date = date;
        this.close = close;
    }
    
    public DataDTO(String[] date, double[] close, String[] name) {
        this.date = date;
        this.close = close;
        this.setName(name);
    }

	public String[] getDate() {
		return date;
	}

	public void setDate(String[] date) {
		this.date = date;
	}

	public double[] getClose() {
		return close;
	}

	public void setClose(double[] close) {
		this.close = close;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

}
