package com.example.project_software_mobile;

import java.util.ArrayList;
import java.util.Random;

public class Storage {
    public static ArrayList<String> latestSearches = null;

    public static ArrayList<String> recentCompare = new ArrayList<>();

    public static String [] municipalityNames;
    public static String currentMunicipalityName;

    public static ArrayList <String> years = new ArrayList<>();
    public static boolean checkYear = false;

    public static ArrayList<MunicipalityData> currentMunicipalityData = null;
    public static boolean checkNameRetrieve = false;
    public static WeatherData currentWeather;



    public static ArrayList<QuizData> getQuestionList(String cityName){

        ArrayList<QuizData> questionList = new ArrayList<>();

        ArrayList<MunicipalityData>  muniList = currentMunicipalityData;

        for (int i=0; i< muniList.size(); i++){

            MunicipalityData muni = muniList.get(i);

            int randomIndex;
            int ranNum;
            String year = String.valueOf(muni.getPopulationData().getYear());
            String population = String.valueOf(muni.getPopulationData().getPopulation());
            String populationInc = String.valueOf(muni.getPopulationData().getPopulationIncrease());
            String wSS = String.valueOf(muni.getWorkplaceSelfSufficiency().getWorkspaceSS());
            String employmentRate = String.valueOf(muni.getEmploymentRateData().getEmploymentRate());
            String title;
            String answer;
            String ques1;
            String ques2;
            String ques3;
            boolean tfQues;

            Random rand = new Random();

            tfQues = true;
            ques1 ="True";
            ques2 = "False";
            ques3 = "";

            int cpYear = Integer.parseInt(year) +5;

            if (cpYear > 2022) cpYear -= 7;

            int yearGap = Integer.parseInt(year)-cpYear;



            title = "Population increase in " + cityName +" in " + year +" higher than in " + cpYear+ "?";

            int cpYearPopulationIncrease = muniList.get(i + yearGap).getPopulationData().getPopulationIncrease();

            if (Integer.parseInt(populationInc) > cpYearPopulationIncrease){
                answer = "True";
            }else{
                answer = "False";
            }

            questionList.add(new QuizData(title, answer, ques1, ques2, ques3, tfQues));



            float newPopu = Integer.parseInt(population) +27;
            title = "Is " + newPopu +" the population of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "False", ques1, ques2, ques3, tfQues));

            title = "Is " + Integer.parseInt(population) +" the population of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "True", ques1, ques2, ques3, tfQues));



            float newPopuInc = Integer.parseInt(populationInc) +27;
            title = "Is " + newPopuInc +" the population increase of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "False", ques1, ques2, ques3, tfQues));

            title = "Is " + Integer.parseInt(populationInc) +" the population increase of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "True", ques1, ques2, ques3, tfQues));



            float newWSS = Float.parseFloat(wSS) +3;
            title = "Is " + newWSS +"% the workplace self-sufficiency of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "False", ques1, ques2, ques3, tfQues));

            title = "Is " + Float.parseFloat(wSS) +"% the workplace self-sufficiency of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "True", ques1, ques2, ques3, tfQues));




            float newER = Float.parseFloat(employmentRate) +3;
            title = "Is " + newER +"% the employment rate of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "False", ques1, ques2, ques3, tfQues));

            title = "Is " + Float.parseFloat(employmentRate) +"% the employment rate of "+ cityName +" in " + year+"?";
            questionList.add(new QuizData(title, "True", ques1, ques2, ques3, tfQues));







            tfQues = false;
            ranNum = rand.nextInt(3);
            ranNum++;


            title = "Population in " + cityName + " in "+ year +"?";

            if (ranNum == 1){
                ques1 = population;
                answer = ques1;

                ques2 = String.valueOf(Integer.parseInt(population)+23);

                ques3 = String.valueOf(Integer.parseInt(population)-24);

            }else if (ranNum==2){
                ques2 = population;
                answer = ques2;

                ques1 = String.valueOf(Integer.parseInt(population)+23);
                ques3 = String.valueOf(Integer.parseInt(population)-24);
            }else{
                ques1 = String.valueOf(Integer.parseInt(population)-24);
                ques2 = String.valueOf(Integer.parseInt(population)+23);

                ques3 = population;
                answer = ques3;
            }

            questionList.add(new QuizData(title, answer, ques1, ques2, ques3, tfQues));




            ranNum = rand.nextInt(3);
            ranNum++;

            title = "Population increase in " + cityName + " in "+ year +"?";

            if (ranNum == 1){
                ques1 = populationInc;
                answer = ques1;

                ques2 = String.valueOf(Integer.parseInt(populationInc)+23);

                ques3 = String.valueOf(Integer.parseInt(populationInc)-24);

            }else if (ranNum==2){
                ques2 = populationInc;
                answer = ques2;

                ques1 = String.valueOf(Integer.parseInt(populationInc)+23);
                ques3 = String.valueOf(Integer.parseInt(populationInc)-24);
            }else{
                ques1 = String.valueOf(Integer.parseInt(populationInc)-24);
                ques2 = String.valueOf(Integer.parseInt(populationInc)+23);

                ques3 = populationInc;
                answer = ques3;
            }

            questionList.add(new QuizData(title, answer, ques1, ques2, ques3, tfQues));






            ranNum = rand.nextInt(3);
            ranNum++;


            title = "Workplace self-sufficiency in " + cityName + " in "+ year +"?";

            if (ranNum == 1){
                ques1 = wSS;
                answer = ques1;
                ques2 = String.valueOf(Float.parseFloat(wSS)+5);

                ques3 = String.valueOf(Float.parseFloat(wSS)-3);

            }else if (ranNum==2){
                ques2 = wSS;
                answer = ques2;
                ques1 = String.valueOf(Float.parseFloat(wSS)+5);
                ques3 = String.valueOf(Float.parseFloat(wSS)-3);
            }else{
                ques1 = String.valueOf(Float.parseFloat(wSS)-3);
                ques2 = String.valueOf(Float.parseFloat(wSS)+5);

                ques3 = wSS;
                answer = ques3;
            }

            questionList.add(new QuizData(title, answer, ques1, ques2, ques3, tfQues));




            ranNum = rand.nextInt(3);
            ranNum++;


            title = "Employment rate in " + cityName + " in "+ year +"?";

            if (ranNum == 1){
                ques1 = employmentRate;
                answer = ques1;
                ques2 = String.valueOf(Float.parseFloat(employmentRate)+5);

                ques3 = String.valueOf(Float.parseFloat(employmentRate)-3);

            }else if (ranNum==2){
                ques2 = employmentRate;
                answer = ques2;
                ques1 = String.valueOf(Float.parseFloat(employmentRate)+5);
                ques3 = String.valueOf(Float.parseFloat(employmentRate)-3);
            }else{
                ques1 = String.valueOf(Float.parseFloat(employmentRate)-3);
                ques2 = String.valueOf(Float.parseFloat(employmentRate)+5);

                ques3 = employmentRate;
                answer = ques3;
            }

            questionList.add(new QuizData(title, answer, ques1, ques2, ques3, tfQues));




        }
        return questionList;
    }






    public static ArrayList<String> getLatestSearches() {
        return latestSearches;
    }

    public static void setLatestSearches(ArrayList<String> latestSearches) {
        Storage.latestSearches = latestSearches;
    }
}
