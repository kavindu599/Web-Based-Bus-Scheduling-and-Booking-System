package com.busbooking.seatbooking.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PricingService {

    private final Map<String, String[]> cityRoutes;
    private final Random random;

    public PricingService() {
        this.random = new Random();
        this.cityRoutes = initializeCityRoutes();
    }

    private Map<String, String[]> initializeCityRoutes() {
        Map<String, String[]> routes = new HashMap<>();
        
        // Major cities with their connected destinations
        routes.put("New York", new String[]{"Boston", "Philadelphia", "Washington DC", "Buffalo", "Albany"});
        routes.put("Los Angeles", new String[]{"San Francisco", "Las Vegas", "San Diego", "Phoenix", "Sacramento"});
        routes.put("Chicago", new String[]{"Detroit", "Milwaukee", "Indianapolis", "St. Louis", "Minneapolis"});
        routes.put("Houston", new String[]{"Dallas", "Austin", "San Antonio", "New Orleans", "Oklahoma City"});
        routes.put("Miami", new String[]{"Orlando", "Tampa", "Jacksonville", "Fort Lauderdale", "West Palm Beach"});
        routes.put("Seattle", new String[]{"Portland", "Vancouver", "Spokane", "Tacoma", "Bellingham"});
        routes.put("Denver", new String[]{"Colorado Springs", "Boulder", "Fort Collins", "Pueblo", "Grand Junction"});
        routes.put("Atlanta", new String[]{"Savannah", "Augusta", "Columbus", "Macon", "Albany"});
        routes.put("Boston", new String[]{"New York", "Portland ME", "Worcester", "Springfield", "Hartford"});
        routes.put("Phoenix", new String[]{"Los Angeles", "Tucson", "Flagstaff", "Yuma", "Prescott"});
        
        return routes;
    }

    public String[] getAllCities() {
        return cityRoutes.keySet().toArray(new String[0]);
    }

    public String[] getDestinationsForOrigin(String origin) {
        return cityRoutes.getOrDefault(origin, new String[0]);
    }

    public double calculateTicketPrice(String origin, String destination, String busType) {
        // Base price calculation
        double basePrice = calculateBasePrice(origin, destination);
        
        // Apply bus type multiplier
        double multiplier = getBusTypeMultiplier(busType);
        
        // Add some randomness (±10%)
        double randomFactor = 0.9 + (random.nextDouble() * 0.2); // 0.9 to 1.1
        
        double finalPrice = basePrice * multiplier * randomFactor;
        
        // Round to 2 decimal places
        return Math.round(finalPrice * 100.0) / 100.0;
    }

    private double calculateBasePrice(String origin, String destination) {
        // Simple distance-based pricing
        int hashCode = Math.abs((origin + destination).hashCode());
        
        // Base prices between $25 and $150
        double basePrice = 25.0 + (hashCode % 125);
        
        return basePrice;
    }

    private double getBusTypeMultiplier(String busType) {
        if (busType == null) return 1.0;
        
        String upperBusType = busType.toUpperCase();
        
        if (upperBusType.contains("DELUXE")) {
            return 1.3; // 30% premium
        } else if (upperBusType.contains("EXPRESS")) {
            return 1.2; // 20% premium
        } else if (upperBusType.contains("SLEEPER")) {
            return 1.5; // 50% premium
        } else if (upperBusType.contains("AC")) {
            return 1.4; // 40% premium
        } else {
            return 1.0; // Regular service
        }
    }

    public boolean isValidRoute(String origin, String destination) {
        if (origin == null || destination == null || origin.equals(destination)) {
            return false;
        }
        
        String[] destinations = getDestinationsForOrigin(origin);
        for (String dest : destinations) {
            if (dest.equals(destination)) {
                return true;
            }
        }
        
        return false;
    }
}
