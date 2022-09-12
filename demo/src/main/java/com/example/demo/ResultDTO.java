package com.example.demo;

public class ResultDTO {
    private int n;
    private Double result;
    private Double progression;

    public ResultDTO(int n, Double result, Double progression) {
        this.n = n;
        this.result = result;
        this.progression = progression;
    }

    public int getN() {
        return this.n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Double getResult() {
        return this.result;
    }

    public void setResult(Double result) {
        this.result = result;
    }


    public Double getProgression() {
        return this.progression;
    }

    public void setProgression(Double progression) {
        this.progression = progression;
    }
}
