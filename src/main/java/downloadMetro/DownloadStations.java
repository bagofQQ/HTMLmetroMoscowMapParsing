package downloadMetro;

public class DownloadStations {

    private String station;
    private String line;

    public DownloadStations(String station, String line){
        this.station = station;
        this.line = line;
    }

    public String getStation() {
        return station;
    }

    public String getLine() {
        return line;
    }
}
