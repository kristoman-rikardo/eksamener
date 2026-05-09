package com.mercedesbenz.part2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import no.ntnu.tdt4100.part2.Project;

/**
 * Mercedes has various research facilities around the world.
 * 
 * This class represents a research facility
 */
// TODO Implement the ResearchFacility class and its methods according to JavaDoc

public class ResearchFacility {
    private String name;
    private List<Project> projects;
    private double yearlyBudgetInMillions;


    /**
     * Constructs the research facility object
     * 
     * Behavioural requirements:
     * <ul>
     * <li>The name must be at least 3 characters and at most 30</li>
     * <li>The yearly budget cannot be negative</li>
     * <li>The accumulated sum of budgets in the projects list cannot exceed
     * the yearly budget of the research facility
     * </ul>
     * 
     * @param name                   the name of the research facility, type of {@link String}
     * @param projects               the list of {@link Project} currently in development
     *                               at this research facility
     * @param yearlyBudgetInMillions the yearly budget in millions, type of double
     * @throws IllegalArgumentException if the name is not valid or the yearly
     *                                  budget is negative or the accumulated sum 
     *                                  of budgets in the projects list exceeds the 
     *                                  yearly budget of the facility
     * 
     * @see Project
     */
    // TODO Implement the constructor

    public ResearchFacility(String name, List<Project> projects, double yearlyBudgetInMillions) {
        if (name == null || projects == null || yearlyBudgetInMillions < 0 || name.length() < 3 
            || name.length() > 30 || projects.stream().mapToDouble(p -> p.budgetInMillions()).sum() > yearlyBudgetInMillions) throw new IllegalArgumentException();
        this.name = name;
        this.projects = projects;
        this.yearlyBudgetInMillions = yearlyBudgetInMillions;
    }

    /**
     * Returns the name of the research facility
     * @return the name of the research facility, type of {@link String}
     */
    // TODO Implement the getName() method

    public String getName() {return this.name;}

    /**
     * Returns the yearly budget in millions
     * @return the yearly budget in millions
     */
    // TODO Implement the getYearlyBudgetInMillions() method, type of double

    public double getYearlyBudgetInMillions() {return this.yearlyBudgetInMillions;}

    /**
     * Returns the list of projects currently in development at this research facility
     * 
     * @return the list of projects currently in development at this research facility
     */
    // TODO Implement the getProjects() method according to JavaDoc

    public List<Project> getProjects() {return this.projects;}

    /**
     * Returns the number of days from today until estimated end time for 
     * the project
     * 
     * If there are no projects at this facility with the given name an empty 
     * Optional should be returned.
     * 
     * @param projectName the name of the project
     * @return an {@link Optional} of type Integer describing the number of days until
     *         estimated end time for the project
     * 
     * @see Project#estimatedEndDate()
     */
    // TODO Implement the getNumberOfDaysUntilEndTime method according to JavaDoc

    public Optional<Project> findProject(String projectName) {
        return getProjects().stream().filter(p -> p.name().equals(projectName)).findAny();
    }

    public OptionalInt getNumberOfDaysUntilEndTime(String projectName) {
        Optional<Project> project = findProject(projectName);
        if (project.isEmpty()) return OptionalInt.empty();
        LocalDate start = project.get().startDate();
        LocalDate end = project.get().estimatedEndDate();
        return OptionalInt.of((int)ChronoUnit.DAYS.between(start, end));
    }


}
