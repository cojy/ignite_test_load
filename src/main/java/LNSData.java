
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class LNSData {
	@QuerySqlField(index = true)
    public Long id;

	@QuerySqlField(index = true)
    public String devEUI;
	
	@QuerySqlField
	private Double gpsLatitude;

	@QuerySqlField
	private Double gpsLongitude;
	
	@QuerySqlField
	private Double gpsAltitude;

	@QuerySqlField
	private	Double gatewayLat;

	@QuerySqlField
	private Double gatewayLong;

	@QuerySqlField
	private Double gatewayAlt;	

    @JsonIgnore
    @QuerySqlField(index=true)
    Geometry gwGeoCoordinates;
   
    @JsonIgnore
    @QuerySqlField(index=true)
    Geometry gpsGeoCoordinates;
    
	public LNSData(String devEUI, Double gpsLatitude, Double gpsLongitude, Double gpsAltitude, Double gatewayLat, Double gatewayLong,
			Double gatewayAlt) {
		super();
		this.devEUI = devEUI;
		this.gpsLatitude = gpsLatitude==null?0:gpsLatitude;
		this.gpsLongitude = gpsLongitude==null?0:gpsLongitude;
		this.gpsAltitude = gpsAltitude==null?0.0:gpsAltitude;
		this.gatewayLat = gatewayLat;
		this.gatewayLong = gatewayLong;
		this.gatewayAlt = gatewayAlt;
		
		WKTReader r = new WKTReader();
		try {
		    gwGeoCoordinates = r.read("POINT(" + gatewayLong + " " + gatewayLat + " " + gatewayAlt +")");
		} catch (ParseException e) {
		    System.err.println(e);
		}
		try {
		    gpsGeoCoordinates = r.read("POINT(" + this.gpsLongitude + " " + this.gpsLatitude + " " + this.gpsAltitude +")");
		} catch (ParseException e) {
			System.err.println(e);
		}
		
	}
	
	public LNSData(LNSData2 data2) {
		super();
		this.id = data2.getId();
		this.devEUI = data2.getDevEUI();
		this.gpsLatitude = data2.getGpsLatitude();
		this.gpsLongitude = data2.getGpsLongitude();
		this.gpsAltitude = data2.getGpsAltitude();
		this.gatewayLat = data2.getGatewayLat();
		this.gatewayLong = data2.getGatewayLong();
		this.gatewayAlt = data2.getGatewayAlt();
		
		WKTReader r = new WKTReader();
		try {
		    gwGeoCoordinates = r.read("POINT(" + gatewayLong + " " + gatewayLat + " " + gatewayAlt +")");
		} catch (ParseException e) {
		    System.err.println(e);
		}
		try {
		    gpsGeoCoordinates = r.read("POINT(" + this.gpsLongitude + " " + this.gpsLatitude + " " + this.gpsAltitude +")");
		} catch (ParseException e) {
			System.err.println(e);
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevEUI() {
		return devEUI;
	}

	public void setDevEUI(String devEUI) {
		this.devEUI = devEUI;
	}

	public Double getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(Double gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public Double getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(Double gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Double getGpsAltitude() {
		return gpsAltitude;
	}

	public void setGpsAltitude(Double gpsAltitude) {
		this.gpsAltitude = gpsAltitude;
	}

	public Double getGatewayLat() {
		return gatewayLat;
	}

	public void setGatewayLat(Double gatewayLat) {
		this.gatewayLat = gatewayLat;
	}

	public Double getGatewayLong() {
		return gatewayLong;
	}

	public void setGatewayLong(Double gatewayLong) {
		this.gatewayLong = gatewayLong;
	}

	public Double getGatewayAlt() {
		return gatewayAlt;
	}

	public void setGatewayAlt(Double gatewayAlt) {
		this.gatewayAlt = gatewayAlt;
	}

	public Geometry getGwGeoCoordinates() {
		return gwGeoCoordinates;
	}

	public void setGwGeoCoordinates(Geometry gwGeoCoordinates) {
		this.gwGeoCoordinates = gwGeoCoordinates;
	}

	public Geometry getGpsGeoCoordinates() {
		return gpsGeoCoordinates;
	}

	public void setGpsGeoCoordinates(Geometry gpsGeoCoordinates) {
		this.gpsGeoCoordinates = gpsGeoCoordinates;
	}
	
    
}
