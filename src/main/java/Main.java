import core.Line;
import core.Station;
import downloadMetro.DownloadMetroInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static String dataFile = "src/main/resources/metro.json";

    private static StationIndex stationIndex;

    public static void main(String[] args) throws IOException {

        DownloadMetroInfo metroInfo = new DownloadMetroInfo();
        metroInfo.downloadInfoJson();

        createStationIndex();

        for(Map.Entry<String, Line> f : (stationIndex.getNumber2line()).entrySet()){
            System.out.println("Линия - " + f.getKey() +" -  количество станций - "+ ((f.getValue()).getStations()).size());
        }

        System.out.println("Количество переходов в метро - " + stationIndex.getSumConnections());

    }

    private static void createStationIndex()
    {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

            stationIndex = new StationIndex(((JSONArray) jsonData.get("connections")).size());

            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            parseLines(linesArray);

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            parseStations(stationsObject);

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            parseConnections(connectionsArray);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void parseConnections(JSONArray connectionsArray)
    {
        connectionsArray.forEach(connectionObject ->
        {
            JSONArray connection = (JSONArray) connectionObject;
            List<Station> connectionStations = new ArrayList<>();
            connection.forEach(item ->
            {
                JSONObject itemObject = (JSONObject) item;
                String lineNumber = (String) itemObject.get("line");
                String stationName = (String) itemObject.get("station");


                Station station = stationIndex.getStation(stationName, lineNumber);
                if(station == null)
                {
                    throw new IllegalArgumentException("core.Station " +
                            stationName + " on line " + lineNumber + " not found");
                }
                connectionStations.add(station);
            });
            stationIndex.addConnection(connectionStations);
        });
    }

    private static void parseStations(JSONObject stationsObject)
    {
        stationsObject.keySet().forEach(lineNumberObject ->
        {
            Line line = stationIndex.getLine((String)lineNumberObject);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            stationsArray.forEach(stationObject ->
            {
                Station station = new Station((String) stationObject, line);
                stationIndex.addStation(station);
                line.addStation(station);
            });
        });
    }

    private static void parseLines(JSONArray linesArray)
    {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line line = new Line(
                    (String) lineJsonObject.get("number"),
                    (String) lineJsonObject.get("name")
            );
            stationIndex.addLine(line);
        });
    }

    private static String getJsonFile()
    {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(line -> builder.append(line));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

}
