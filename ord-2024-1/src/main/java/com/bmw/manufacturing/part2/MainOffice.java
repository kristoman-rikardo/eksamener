package com.bmw.manufacturing.part2;

import java.util.List;
import java.util.Optional;

import no.ntnu.tdt4100.ICarFactory;
import no.ntnu.tdt4100.part2.IMainOffice;

/**
 * Implement the class MainOffice.
 * 
 * The class must implement the interface {@link no.ntnu.tdt4100.part2.IMainOffice}. 
 * The class needs no constructor.
 * 
 * Read the documentation in the {@link no.ntnu.tdt4100.part2.IMainOffice} interface 
 * for detailed description on expected behaviour 
 * of the methods you need to implement.
 * 
 * @see no.ntnu.tdt4100.part2.IMainOffice
 * @see MainOfficeTests
 */

 // TODO Implement the MainOffice class here according to description in JavaDoc
public class MainOffice implements IMainOffice {

    /**
     * The average car price
     */
    // public static int AVERAGE_CAR_PRICE = 234127;
    
    /**
     * Returns the estimated installation hours for the given factory. 
     * The installation hours are calculated as follows:
     *
     * Employee count multiplied by Number of parts the factory uses
     * multiplied by {@link Math#PI}
     *
     * Example 
     * Number of Employees: 235 
     * Number of parts: 3 
     * Estimated installation hours:
     * 2214.8228207808042
     *
     * @param factory
     * @return the estimated installation hours
     * 
     * @see Math#PI
     */
    @Override
    public double calculateInstallationHours(ICarFactory factory) {
        return factory.getNumberOfEmployees() * factory.getParts().size() * Math.PI;
    }


    /**
     * Calculates the gross total estimated revenue of all BMW factories
     * for a specific year, rounded down to nearest thousand. 
     *
     * The total estimated revenue for a single factory for a specific year 
     * is calculated as follows:
     * 
     * number of produced units for the specific year
     * multiplied by the average car price.
     * 
     * This method will return the sum of revenues for all factories.
     *
     * @param factories the list of factories
     * @param year the year for
     * @return the summed estimated of all factories rounded down to nearest thousand
     * 
     * @see IMainOffice#AVERAGE_CAR_PRICE
     * @see ICarFactory#getNumberOfUnitsProduced(int)
     */
    @Override
    public long calculateTotalEstimatedRevenue(List<ICarFactory> factories, int year) {
        long estimatedRevenue = 0;
        for (ICarFactory factory : factories) {
            estimatedRevenue += (long) factory.getNumberOfUnitsProduced(year) * AVERAGE_CAR_PRICE;
        }
        return (estimatedRevenue / 1000) * 1000;
    }


    /**
     * Returns the factory with the highest number of employees.
     * If there are multiple factories with the same amount of workers, return either.
     * If the given list is empty, return an empty {@link Optional}.
     * 
     * @param factories the list of factories
     * @return the factory from the given list of factories with the highest number of employees
     */
    @Override
    public Optional<ICarFactory> findTopFactory(List<ICarFactory> factories) {
        if (factories == null || factories.size() < 1) return Optional.empty();
        return factories.stream().sorted((o1, o2) -> o2.getNumberOfEmployees() - o1.getNumberOfEmployees()).findFirst();
    }
    

    /**
     * Returns a list of CarFactory objects filtered by the given char.
     * 
     * The method must return all car factories whose {@link ICarFactory#getIsoCountryCode()}
     * starts with the letter supplied in the character parameter. 
     * The filter should be case-insensitive.
     * In case the supplied character is invalid (not within the range A-Z or a-z, 
     * represented in decimal as 65-90 and 97-122, return the unfiltered list
     * 
     * @param factories the list of car factories to filter
     * @param character the case-insensitive character to filter by for the ISO Country Code
     * @return a list of car factories filtered by the start letter of the ISO Country code
     * 
     * @see ICarFactory#getIsoCountryCode()
     */
    @Override
    public List<ICarFactory> filterFactories(List<ICarFactory> factories, char character) {
        return factories.stream().filter(f -> f.getIsoCountryCode().charAt(0).toUpperCase() == character.toUpperCase()).toList();
    }
}