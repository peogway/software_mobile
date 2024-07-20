package com.example.project_software_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MunicipalityDataAdapter extends RecyclerView.Adapter<MunicipalityDataViewHolder>{
    private Context context;
    private ArrayList<MunicipalityData> municipalities = new ArrayList<>();

    public MunicipalityDataAdapter (Context context, ArrayList <MunicipalityData> municipalities){
        this.context = context;
        this.municipalities = municipalities;
    }


    public MunicipalityDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new MunicipalityDataViewHolder(LayoutInflater.from(context).inflate(R.layout.municipality_data_view, parent,false));
    }

    public void onBindViewHolder(@NonNull MunicipalityDataViewHolder holder, int position){
        holder.year.setText(String.valueOf(municipalities.get(position).getPopulationData().getYear()));
        holder.population.setText(String.valueOf(municipalities.get(position).getPopulationData().getPopulation()));
        holder.populationIncrease.setText(String.valueOf(municipalities.get(position).getPopulationData().getPopulationIncrease()));

        String wssString =municipalities.get(position).getWorkplaceSelfSufficiency().getWorkspaceSS()+"%";
        holder.workplaceSS.setText(wssString);

        String eRString = municipalities.get(position).getEmploymentRateData().getEmploymentRate() + "%";
        holder.employmentRate.setText(eRString);


    }

    public void setMunicipalities(ArrayList<MunicipalityData> municipalities) {
        this.municipalities = municipalities;
    }

    public int getItemCount(){return municipalities.size();}
}
