package com.example.project_software_mobile;

public class PopulationData {
    private int year;
    private int population;
    private int populationIncrease;

    public PopulationData(int year, int population, int populationIncrease){
        this.year = year;
        this.population = population;
        this.populationIncrease = populationIncrease;
    }

    public int getPopulationIncrease() {
        return populationIncrease;
    }

    public int getPopulation() {
        return population;
    }

    public int getYear() {
        return year;
    }

    public void setPopulationIncrease(int populationIncrease) {
        this.populationIncrease = populationIncrease;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
