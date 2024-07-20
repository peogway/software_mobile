package com.example.project_software_mobile;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MunicipalityDataViewHolder extends RecyclerView.ViewHolder {
    TextView year, population, populationIncrease,workplaceSS, employmentRate;

    public MunicipalityDataViewHolder ( @NonNull View itemView){
        super(itemView);
        year = itemView.findViewById(R.id.editYear);
        population = itemView.findViewById(R.id.editPopulation);
        populationIncrease = itemView.findViewById(R.id.editPopulationIncrease);
        workplaceSS = itemView.findViewById(R.id.editWorkplaceSelfSufficiency);
        employmentRate = itemView.findViewById(R.id.editEmploymentRate);
    }
}
