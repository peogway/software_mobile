package com.example.project_software_mobile;

public class MunicipalityData {

    private PopulationData populationData;
    private WorkspaceSelfSufficiencyData workplaceSelfSufficiency;
    private EmploymentRateData employmentRateData;


    public MunicipalityData(PopulationData populationData, WorkspaceSelfSufficiencyData workplaceSelfSufficiency, EmploymentRateData employmentRateData) {
        this.populationData = populationData;
        this.workplaceSelfSufficiency = workplaceSelfSufficiency;
        this.employmentRateData = employmentRateData;
    }

    public EmploymentRateData getEmploymentRateData() {
        return employmentRateData;
    }

    public PopulationData getPopulationData() {
        return populationData;
    }

    public WorkspaceSelfSufficiencyData getWorkplaceSelfSufficiency() {
        return workplaceSelfSufficiency;
    }

    public void setWorkplaceSelfSufficiency(WorkspaceSelfSufficiencyData workplaceSelfSufficiency) {
        this.workplaceSelfSufficiency = workplaceSelfSufficiency;
    }

    public void setEmploymentRateData(EmploymentRateData employmentRate) {
        this.employmentRateData = employmentRate;
    }

    public void setPopulationData(PopulationData populationData) {
        this.populationData = populationData;
    }
}
