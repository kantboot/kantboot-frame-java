package com.kantboot.util.location.util;

import com.kantboot.util.location.domain.Location;
import com.kantboot.util.location.domain.LocationMaxMin;
import org.junit.Test;

import java.math.BigDecimal;

public class TestLocationUtil {

    @Test
    public void testCalculateDistance(){
        Location location1 = new Location().setLatitude(new BigDecimal("116.12619400"))
                .setLongitude(new BigDecimal("24.33086300"));
        Location location2 = new Location().setLatitude(new BigDecimal("116.12628800"))
                .setLongitude(new BigDecimal("24.33079300"));
        BigDecimal bigDecimal = LocationUtil.calculateDistance(location1, location2);
        System.out.println("Distance: " + bigDecimal + " meters");
    }

    @Test
    public void testCalculateDistanceWithNull(){
        Location location = new Location().setLatitude(new BigDecimal("34.0522"))
                .setLongitude(new BigDecimal("-118.2437"));
        LocationMaxMin maxMinLocationByDistance = LocationUtil.getMaxMinLocationByDistance(location, new BigDecimal("1000"));
        System.out.println("Max Latitude: " + maxMinLocationByDistance.getLatitudeMax());
        System.out.println("Min Latitude: " + maxMinLocationByDistance.getLatitudeMin());
        System.out.println("Max Longitude: " + maxMinLocationByDistance.getLongitudeMax());
        System.out.println("Min Longitude: " + maxMinLocationByDistance.getLongitudeMin());
    }

}
