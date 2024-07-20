package com.example.project_software_mobile;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MunicipalityDataRetriever {
    private static final String NAME_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private static final String POPULATION_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private static final String EMPLOYMENT_RATE_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
    private static final String SELF_SUFFICIENCY_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";

    static ObjectMapper objectMapper = new ObjectMapper();

    static HashMap<String, String> municipalityNameToCodeMap = null;
    static HashMap<String, String> municipalityPopulationToCodesMap = null;
    static HashMap<String, String> municipalityWSSToCodeMap = null;
    static HashMap<String, String> municipalityEmploymentRateToCodeMap = null;



    /**
     * Get municipality codes, we need to do this only once
     *
     */

    public static void getMunicipalityNameToCodesMap() {
        if (municipalityNameToCodeMap == null) {
            JsonNode nameNode = readDataFromAPIURL(objectMapper, NAME_API_URL);
            municipalityNameToCodeMap = createMunicipalityToCodesMap(nameNode);
        }
    }

    public static void getMunicipalityPopulationCodesMap() {
        if (municipalityPopulationToCodesMap == null) {
            JsonNode populationNode = readDataFromAPIURL(objectMapper, POPULATION_API_URL);
            municipalityPopulationToCodesMap = createMunicipalityToCodesMap(populationNode);
        }
    }

    public static void getWSSToCodeMap() {
        if (municipalityWSSToCodeMap == null) {
            JsonNode wSSNode = readDataFromAPIURL(objectMapper, SELF_SUFFICIENCY_API_URL);
            municipalityWSSToCodeMap = createMunicipalityToCodesMap(wSSNode);
        }
    }
    
    public static void getMunicipalityEmploymentRateToCodeMap() {
        if (municipalityEmploymentRateToCodeMap == null) {
            JsonNode employmentRateNode = readDataFromAPIURL(objectMapper, EMPLOYMENT_RATE_API_URL);
            municipalityEmploymentRateToCodeMap = createMunicipalityToCodesMap(employmentRateNode);
        }
    }

////////////////////////////////////////////////////////////
    public String[] getNameData(Context context) {
    getMunicipalityNameToCodesMap();




    try {
        JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.query));

        //((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);

        HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, NAME_API_URL);


        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JsonNode responseData = objectMapper.readTree(response.toString());

            JsonNode namesNode = responseData.get("dimension").get("Alue").get("category").get("label");



            Iterator<Map.Entry<String, JsonNode>> fields = namesNode.fields();
            int size = responseData.get("size").get(1).asInt();
            int i = 0;

            String[] nameList = new String[size];
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String cityName = entry.getValue().asText();
                nameList[i++] = cityName;
            }

            return nameList;

        }catch (Exception e) {
            e.printStackTrace();
        }

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return null;
}


    public ArrayList<PopulationData> getPopulationData(Context context, String municipalityName) {
        getMunicipalityPopulationCodesMap();
        String code = municipalityPopulationToCodesMap.get(municipalityName);
        ArrayList<PopulationData> populationDataList = new ArrayList<>();


        try {
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.population));
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);
            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, POPULATION_API_URL);


            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode responseData = objectMapper.readTree(response.toString());
                JsonNode yearsIndexNode = responseData.get("dimension").get("Vuosi").get("category").get("index");
                JsonNode infoIndexNode = responseData.get("dimension").get("Tiedot").get("category").get("index");

                int populationIndex = infoIndexNode.get("vaesto").asInt(); //=1
                int populationIncreaseIndex = infoIndexNode.get("valisays").asInt();//=0

                JsonNode values = responseData.get("value");

                yearsIndexNode.fieldNames().forEachRemaining(yearString -> {
                    int year = Integer.parseInt(yearString);
                    int yearIndex = yearsIndexNode.get(yearString).asInt();
                    int populationIncreasePos = yearIndex* 2 + populationIncreaseIndex;
                    int populationPos =  yearIndex* 2 + populationIndex;
                    int population = values.get(populationPos).asInt();
                    int populationIncrease = values.get(populationIncreasePos).asInt();
                    populationDataList.add(new PopulationData(year, population, populationIncrease));
                });
                return populationDataList;

            }catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<EmploymentRateData> getEmploymentRateData(Context context, String municipalityName) {
        getMunicipalityEmploymentRateToCodeMap();
        String code = municipalityEmploymentRateToCodeMap.get(municipalityName);
        ArrayList<EmploymentRateData> employmentRateDataList = new ArrayList<>();


        try {
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentrate));
            //((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);
            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, EMPLOYMENT_RATE_API_URL);


            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode responseData = objectMapper.readTree(response.toString());

                JsonNode yearsIndexNode = responseData.get("dimension").get("Vuosi").get("category").get("index");
                JsonNode areaIndexNode = responseData.get("dimension").get("Alue").get("category").get("index");
                int sizeOfYear = responseData.get("size").get(1).asInt();



                JsonNode values = responseData.get("value");
                int areaIndex = areaIndexNode.get(code).asInt();

                // Loop through the years
                yearsIndexNode.fieldNames().forEachRemaining(yearString -> {
                    int year = Integer.parseInt(yearString);
                    int yearIndex = yearsIndexNode.get(yearString).asInt();
                    int employmentRatePosition = (areaIndex * sizeOfYear) + yearIndex;
                    float employmentRate = values.get(employmentRatePosition).floatValue();
                    employmentRateDataList.add(new EmploymentRateData(year, employmentRate));
                });

                return employmentRateDataList;

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<WorkspaceSelfSufficiencyData> getWorkplaceSelfSufficiencyData(Context context, String municipalityName) {
        getWSSToCodeMap();
        String code = municipalityWSSToCodeMap.get(municipalityName);
        ArrayList<WorkspaceSelfSufficiencyData> workspaceSelfSufficiencyDataList = new ArrayList<>();

        try {

            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.workspaceselfsufficiency));
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);
            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, SELF_SUFFICIENCY_API_URL);




            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode responseData = objectMapper.readTree(response.toString());

                JsonNode yearsIndexNode = responseData.get("dimension").get("Vuosi").get("category").get("index");
                JsonNode areaIndexNode = responseData.get("dimension").get("Alue").get("category").get("index");
                int sizeOfYear = responseData.get("size").get(1).asInt();


                JsonNode values = responseData.get("value");
                int areaIndex = areaIndexNode.get(code).asInt();



                yearsIndexNode.fieldNames().forEachRemaining(yearString -> {
                    int year = Integer.parseInt(yearString);
                    int yearIndex = yearsIndexNode.get(yearString).asInt();

                    int workspaceSelfSufficiencyPosition = (areaIndex * sizeOfYear) + yearIndex;

                    float workspaceSSValue = values.get(workspaceSelfSufficiencyPosition).floatValue();

                    workspaceSelfSufficiencyDataList.add(new WorkspaceSelfSufficiencyData(year, workspaceSSValue));
                });

            }

            return workspaceSelfSufficiencyDataList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    public ArrayList<MunicipalityData> getMunicipalityData(Context context, String municipalityName) {

        ArrayList<PopulationData> populationDataList = getPopulationData(context, municipalityName);
        ArrayList<EmploymentRateData> employmentRateDataList = getEmploymentRateData(context, municipalityName);
        ArrayList<WorkspaceSelfSufficiencyData> workplaceSelfSufficiencyDataList = getWorkplaceSelfSufficiencyData(context, municipalityName);
        ArrayList<MunicipalityData> mergedDataList = new ArrayList<>();



        HashMap<Integer, EmploymentRateData> employmentRateDataMap = new HashMap<>();
        for (EmploymentRateData data : employmentRateDataList) {
            employmentRateDataMap.put(data.getYear(), data);
        }

        HashMap<Integer, WorkspaceSelfSufficiencyData> workplaceSSDataMap = new HashMap<>();
        for (WorkspaceSelfSufficiencyData data : workplaceSelfSufficiencyDataList) {
            workplaceSSDataMap.put(data.getYear(), data);
        }

        for (int i = populationDataList.size() - 1; i >= 0; i--) {
            PopulationData populationData = populationDataList.get(i);
            int year = populationData.getYear();

            if (!Storage.checkYear) {
                if (year != 2023){
                    Storage.years.add(String.valueOf(year));
                }

            }

            // Find matching data based on the year
            EmploymentRateData employmentRateData = employmentRateDataMap.get(year);
            WorkspaceSelfSufficiencyData workspaceSelfSufficiencyData = workplaceSSDataMap.get(year);

            // If data exists for the given year in all three lists, create a new MunicipalityData object
            if (employmentRateData != null && workspaceSelfSufficiencyData != null) {
                MunicipalityData mergedData = new MunicipalityData(populationData, workspaceSelfSufficiencyData, employmentRateData);
                mergedDataList.add(mergedData);
            }
        }
        Storage.checkYear = true;

        return mergedDataList;
    }




    ////////////////////////////////////////////////////


    private static HashMap<String, String> createMunicipalityToCodesMap(JsonNode jsonNode) {
        JsonNode codes = null;
        JsonNode names = null;

        // Here we find the element "variables", and inside it we have the element "text", that has value "Area".
        // Within the same element, we have the keys "values" which contains the municipality codes (e.g. KU123) as a list
        // and "valueTexts" which contains the municipality names (e.g. Lahti) as a list
        for (JsonNode node : jsonNode.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                codes = node.findValue("values");
                names = node.findValue("valueTexts");
                break;
            }
        }

        // Let's store the municipality names as keys, and municipality codes as values in a HashMap

        HashMap<String, String> municipalityPopulationToCodesMap = new HashMap<>();

        // Here we can assume that the size of names and codes are equal, at there are as many municipality codes
        // as there are municipality names
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            municipalityPopulationToCodesMap.put(name, code);

        }
        return municipalityPopulationToCodesMap;
    }



    /**
     * Here we read the all the JSON from the URL to a JsonNode
     * <p>
     * How to improve this: instead of fetching the same data all over again when restarting the app, we could store
     * the areas JSON to a file and read it from there. Then we would only need to fetch it once, if the file does
     * not yet exist.
     *
     * @param objectMapper
     * @return JsonNode with municipality data
     */
    private static JsonNode readDataFromAPIURL(ObjectMapper objectMapper, String urlString) {
        JsonNode dataNode = null;
        try {
                dataNode = objectMapper.readTree(new URL(urlString));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataNode;
    }

    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery, String urlString)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);
        }
        return con;
    }

}
