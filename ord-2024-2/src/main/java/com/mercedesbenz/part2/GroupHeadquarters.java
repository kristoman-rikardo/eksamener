package com.mercedesbenz.part2;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import no.ntnu.tdt4100.part2.IGroupHeadquarters;
import no.ntnu.tdt4100.part2.IResearchFacility;
import no.ntnu.tdt4100.part2.Project;

/**
 * Implement the class GroupHeadquarters
 * 
 * The class must implement the interface {@link no.ntnu.tdt4100.part2.IGroupHeadquarters}. 
 * 
 * @see GroupHeadquartersTests
 */
public class GroupHeadquarters implements IGroupHeadquarters { // class implements the interface and i pasted the Javadoc of the interface 

    /**
     * Returns the total budget for all research facilities 
     * rounded up to nearest whole number.
     *
     * @param facilities the research facilities
     * @return an {@link Integer} as the total budget for all facilities
     *         rounded up to nearest whole number
     * 
     * @see IResearchFacility#getYearlyBudgetInMillions()
     */
    public Integer getTotalBudgetForAllFacilities(List<IResearchFacility> facilities) {
        return (int) Math.ceil(facilities.stream().mapToDouble(IResearchFacility::getYearlyBudgetInMillions).sum());
    }


    /**
     * Given a list of randomly added research facilities, returns
     * a list of alphabetically sorted research facilities by their name. 
     *
     * @param facilities
     * @return a list of research facilities alphabetically sorted by their names
     */
    public List<IResearchFacility> sortFacilities(List<IResearchFacility> facilities) {
        return facilities.stream()
            .sorted(Comparator.comparing(IResearchFacility::getName))
            .toList();
    }


    /**
     * Returns the {@link Project} with the highest budget across all supplied research facilities.
     * If there are multiple Projects with the same budget, return either.
     * 
     * If no facilities are supplied or the facilities have no projects, return {@link Optional#empty()}
     * 
     * @param facilities
     * @return the most expensive project across all given research facilities
     * 
     * @see Project#budgetInMillions()
     */
    public Optional<Project> getMostExpensiveProject(List<IResearchFacility> facilities) {
        return facilities.stream()
            .flatMap(f -> f.getProjects().stream())
            .max(Comparator.comparingDouble(Project::budgetInMillions));
    }


    /**
     * Returns the research facilities that have at least 3 projects 
     * with a budget equal to or larger than the given budget filter.
     * If no research facility fulfills that requirement, return an empty list.
     * 
     * If the budget filter parameter is <code>null</code>, return all facilities.
     *  
     * @param facilities the list of car factories to filter
     * @param budgetFilter the lowpass budget filter in millions
     * @return a list of research facilities that have at least 3 projects with a budget
     * equal to or larger than the given budget filter.
     */
    public List<IResearchFacility> filterFacilities(List<IResearchFacility> facilities, Double budgetFilter) {
         return facilities.stream()
            .filter(f -> f.getProjects().size() > 2)
            .filter(f -> f.getYearlyBudgetInMillions() >= budgetFilter)
            .toList();
    }     
}
