package com.example.project_software_mobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;



import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Waterfall;
import com.example.project_software_mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class FragmentDataVisualization extends Fragment {


    private static final String ARG_CITY_NAME = "cityName";

    public static FragmentDataVisualization newInstance(String cityName) {
        FragmentDataVisualization fragment = new FragmentDataVisualization();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { 


        String cityName = getArguments().getString(ARG_CITY_NAME);
        View view = inflater.inflate(R.layout.fragment_data_visualization, container, false);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));



        Waterfall waterfall = AnyChart.waterfall();
        String tittleString = "Employment rate flow in " + cityName +" from 2010 to 2022";
        waterfall.title(tittleString);

        waterfall.yScale().minimum(0d);

        waterfall.yAxis(0).labels().format("{%Value}{scale:(100000)(1)|(mln)}%");
        waterfall.labels().enabled(true);
        waterfall.labels().format(
                "function() {\n" +
                        "      if (this['isTotal']) {\n" +
                        "        return anychart.format.number(this.absolute, {\n" +
                        "          scale: true\n" +
                        "        })\n" +
                        "      }\n" +
                        "\n" +
                        "      return anychart.format.number(this.value, {\n" +
                        "        scale: true\n" +
                        "      })\n" +
                        "    }");
        ArrayList <MunicipalityData> munis = Storage.currentMunicipalityData;
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("2010", munis.get(12).getEmploymentRateData().getEmploymentRate()));
        for (int i=11; i>=0; i--) {
            float gap = munis.get(i).getEmploymentRateData().getEmploymentRate() - munis.get(i+1).getEmploymentRateData().getEmploymentRate();

            @SuppressLint("DefaultLocale") float nGap = Float.parseFloat(String.format("%.1f", gap));
            data.add(new ValueDataEntry(String.valueOf(munis.get(i).getPopulationData().getYear()), nGap ));
        }

        DataEntry end = new DataEntry();
        end.setValue("x", "2022");
        end.setValue("isTotal", true);
        data.add(end);

        waterfall.data(data);

        anyChartView.setChart(waterfall);






        return view;
    }
}