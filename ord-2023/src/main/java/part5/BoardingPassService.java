package part5;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import part1.Aircraft;
import part3.Flight;
import part4.Booking;
import shared.BookingClasses;
import shared.IBooking;
import shared.IFlight;
import shared.Passenger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class BoardingPassService {
    /**
     * Writes the boarding pass for a booking to an OutputStream in the following
     * format:
     * Name of the passenger
     * Booking Class
     * For each flight in the booking:
     * Origin - Destination - Duration
     * 
     * See boarding_pass.txt in this package for an example.
     *
     * @param booking      The booking for which to write the boarding pass.
     * @param outputStream The OutputStream to which to write the boarding pass.
     */
    public static void printBoardingPass(IBooking booking, OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            String pass = passBuilder(booking);
            writer.write(pass);
        } catch (Exception e) {
            throw e;
        }

    }

    public static String passBuilder(IBooking booking) {
        if (booking == null) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        Iterator<IFlight> iterator = booking.iterator();
        while (iterator.hasNext()) {
            IFlight flight = iterator.next();
            sb.append(flight.getOrigin() + " - " + flight.getDestination() + " - " + flight.getDuration() + "\n");
        }
        return booking.getPassenger().getName() + "\n" + booking.getBookingClass() + "\n" + sb.toString();
    }

    /**
     * Scans a boarding pass for a flight from an InputStream and throws an
     * IllegalArgumentException if there does not exist
     * a passenger with that name and booking class on the given input flight. 
     *
     * @param flight      The flight for which to scan the boarding pass.
     * @param inputStream The InputStream from which to scan the boarding pass.
     * 
     * @throws IllegalArgumentException If there does not exist a passenger with
     *                                  that name and booking class on the given
     *                                  flight.
     */
    public static void scanBoardingPass(IFlight flight, InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            int isValid = 0;
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String[] parts = sb.toString().toLowerCase().trim().split("\n");
            String checkName = parts[0].toLowerCase().trim();
            ArrayList<String> validNames = new ArrayList<>(flight.getBookings().stream().map(b -> b.getPassenger().getName()).toList());
            for (String validName : validNames) if (validName.toLowerCase().trim().equals(checkName)) isValid += 1;
            
            String checkClass = parts[1].toLowerCase().trim();
            List<String> validClasses = new ArrayList<>(BookingClasses.getValidBookingClasses());
            for (String validClass : validClasses) if (validClass.toLowerCase().trim().equals(checkClass)) isValid += 1;
            
            List<String> checkFlights = Arrays.asList(parts).subList(2, parts.length);
            String validFlight = flight.getOrigin() + " - " + flight.getDestination() + " - " + flight.getDuration();
            for (String checkFlight : checkFlights) if (checkFlight.toLowerCase().trim().equals(validFlight)) isValid += 1;

            if (isValid < 3) throw new IllegalArgumentException();
            
        } catch (Exception e) {
            throw e;
        }

    }

    public static void main(String[] args) {
        // Create sample data for testing
        Passenger passenger = new Passenger("John Doe", "john.doe@example.com");
        Aircraft aircraft = new Aircraft("Boeing 747", 400, 0.05, 200000);
        Flight flight = new Flight("London", "New York", "BA001", aircraft, 420, 5567,
                LocalDateTime.of(2024, 3, 26, 12, 0));
        Booking booking = new Booking(passenger, Collections.singletonList(flight), BookingClasses.ECONOMY, 1000);

        // Add the booking to the flight, commentint out this line should yield an
        // IllegalArgumentException below when reading
        flight.addBooking(booking);

        String basePath = "src/main/java";

        /** Comment this next section out if you just want to test loading */
        String packageName = BoardingPassService.class.getPackageName().replace('.', '/');

        // Write the boarding pass to a text file, change the filename here to test with the provided example
        String fileName = basePath + "/" + packageName + "/my_boarding_pass.txt";
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            printBoardingPass(booking, outputStream);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        // Read the boarding pass from the text file and scan it. If you just want to test with the provided example, change the filename to "/boarding_pass.txt" above
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            scanBoardingPass(flight, inputStream);
            System.out.println("Boarding pass scanned successfully!");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error scanning boarding pass: " + e.getMessage());
        }

        // Deletes the text file, uncomment this to check how the text file ends up.
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("Error deleting the file: " + e.getMessage());
        }
    }
}