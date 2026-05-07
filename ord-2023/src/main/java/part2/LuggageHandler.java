package part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.Luggage;

public class LuggageHandler {

    private final int maxWeight;
    private Map<String, List<Luggage>> luggageMap = new HashMap<>();

    /**
     * Constructs a LuggageHandler object with a given maximum weight of luggage.
     * 
     * @param maxWeight The maximum weight of a luggage in
     *                  kilograms.
     * @throws IllegalArgumentException If the maximum weight is below 0
     */
    public LuggageHandler(int maxWeight) {
        if (maxWeight < 0) throw new IllegalArgumentException();
        this.maxWeight = maxWeight;
    }

    /**
     * Adds a luggage to a given flight.
     * If the luggage exceeds the maximum weight allowed, it throws an
     * IllegalArgumentException.
     * You do not need to take into consideration multiple luggages by the same
     * passenger
     * 
     * @param flightNumber The flight number to add the luggage to as a string.
     * @param luggage      The luggage to add as a Luggage object.
     * @throws IllegalArgumentException If the luggage exceeds the maximum weight
     *                                  allowed.
     */
    public void addLuggage(String flightNumber, Luggage luggage) {
        if (flightNumber == null || luggage == null || luggage.getWeight() > this.maxWeight) throw new IllegalArgumentException();
        if (luggageMap.containsKey(flightNumber)) {
            ArrayList<Luggage> inter = new ArrayList<>(luggageMap.get(flightNumber));
            inter.add(luggage);
            luggageMap.put(flightNumber, inter);
        }
        else {
            luggageMap.put(flightNumber, new ArrayList<>(List.of(luggage)));
        }
    }

    /**
     * Removes a luggage from the list of luggage for a given flight.
     * If the flight number or the luggage does not exist, it returns
     * false.
     * Otherwise, it returns true.
     * 
     * @param flightNumber The flight number to remove the luggage from as a string.
     * @param luggage      The luggage to remove as a Luggage object.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeLuggage(String flightNumber, Luggage luggage) {
        if (flightNumber != null && luggage != null) {
            if (luggageMap.containsKey(flightNumber) && luggageMap.get(flightNumber).contains(luggage)) {
                ArrayList<Luggage> inter = new ArrayList<>(luggageMap.get(flightNumber));
                inter.remove(luggage);
                luggageMap.put(flightNumber, inter);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the total weight of luggage for a given flight.
     * If the flight number does not exist, it returns zero.
     * 
     * @param flightNumber The flight number to get the total weight of luggage for
     *                     as a string.
     * @return The total weight of luggage for that flight in kilograms as an
     *         integer.
     */
    public int getTotalWeight(String flightNumber) {
        if (flightNumber == null || !luggageMap.containsKey(flightNumber)) return 0;
        ArrayList<Luggage> inter = new ArrayList<>(luggageMap.get(flightNumber));
        int weight = 0;
        for (Luggage l : inter) weight += l.getWeight();
        return weight;
    }

    public static void main(String[] args) {
        // Create a LuggageHandler with a maximum weight of 30 kg
        LuggageHandler luggageHandler = new LuggageHandler(30);

        // Create some Luggage objects
        Luggage luggage1 = new Luggage(20);
        Luggage luggage2 = new Luggage(10);
        Luggage luggage3 = new Luggage(5);
        Luggage luggage4 = new Luggage(35);

        // Add luggage to the flight SK364
        luggageHandler.addLuggage("SK364", luggage1);
        luggageHandler.addLuggage("SK364", luggage2);
        luggageHandler.addLuggage("SK364", luggage3);

        // Try to add luggage4 that exceeds the maximum weight
        try {
            luggageHandler.addLuggage("SK364", luggage4);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Print the total weight of luggage for flight SK364, expected 35
        System.out
                .println("Total weight of luggage for flight SK364: " + luggageHandler.getTotalWeight("SK364") + " kg");

        System.out.println(luggageHandler.removeLuggage("SK364", luggage3));
        System.out.println(luggageHandler.removeLuggage("SK364", luggage4));
        // Print the total weight of luggage for flight SK364, expected 30
        System.out
                .println("Total weight of luggage for flight SK364: " + luggageHandler.getTotalWeight("SK364") + " kg");

    }
}
