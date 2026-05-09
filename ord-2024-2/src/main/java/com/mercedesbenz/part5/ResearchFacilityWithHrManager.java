package com.mercedesbenz.part5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import no.ntnu.tdt4100.part2.IResearchFacility;
import no.ntnu.tdt4100.part2.Person;
import no.ntnu.tdt4100.part2.Project;
import no.ntnu.tdt4100.part5.CanNotStartProjectException;
import no.ntnu.tdt4100.part5.IHasHumanResourceManager;
import no.ntnu.tdt4100.part5.IHumanResourceManager;

/**
 * The ResearchFacilityWithHrManager is a research facility that use 
 * a delegate {@link IResearchFacility} to forward some of its method calls to.
 * 
 * Read the unit tests and the JavaDoc description in the interface to implement the methods correctly.
 * 
 * @see ResearchFacilityWithHrManagerTests
 */
public class ResearchFacilityWithHrManager implements IResearchFacility, IHasHumanResourceManager {
    private IResearchFacility delegate;
    private IHumanResourceManager humanResourceManager;
    /**
     * Constructor - creates a ResearchFacilityWithHrManager object
     * 
     * @param delegate the {@link no.ntnu.tdt4100.part2.IResearchFacility} delegate
     * @param humanResourceManager the {@link IHumanResourceManager} 
     */

    public ResearchFacilityWithHrManager(IResearchFacility delegate, IHumanResourceManager humanResourceManager) {
        if (delegate == null || humanResourceManager == null) throw new IllegalArgumentException();
        this.delegate = delegate;
        this.humanResourceManager = humanResourceManager;
    }

    /**
     * Returns the name of the research facility
     * 
     * This method must delegate to {@link IResearchFacility#getName()} of the delegate in the constructor.
     * 
     * @return the name of the facility
     */
    @Override
    public String getName() {
        return delegate.getName();
    }


    /**
     * Returns the yearly budget of the research facility
     * 
     * This method must delegate to {@link IResearchFacility#getYearlyBudgetInMillions()} of the delegate in the constructor.
     * 
     * @return the yearly budget of the facility
     */
    @Override
    public double getYearlyBudgetInMillions() {
        return delegate.getYearlyBudgetInMillions();
    }


    /**
     * Returns the list of projects at the research facility
     * 
     * This method must delegate to {@link IResearchFacility#getProjects()} of the delegate in the constructor.
     * 
     * @return the list of projects at the research facility
     */
    @Override
    public List<Project> getProjects() {
        return delegate.getProjects();
    }


    /**
     * Returns the {@link IHumanResourceManager} for <code>this</code> factory
     * 
     * @return the {@link IHumanResourceManager} for <code>this</code> factory.
     */
    @Override
    public IHumanResourceManager getHumanResourceManager() {
        return humanResourceManager;
    }


    /**
     * Starts a project by registering the project in the list of projects at this facility 
     * and hires the people from the given map of people.
     * 
     * Note that all employees must be hired {@link IHumanResourceManager#hire(Person, int)} so that the project can start.
     * If it is not possible to hire everyone, no employees should be hired, and the project can not start.
     * 
     * In particular the following criteria are in place to start a projeect:
     * <ul>
     * <li>will any of the people in the map exceed the salary cap for a single employee {@link no.ntnu.tdt4100.part5.IHumanResourceManager#getSalaryCapForSingleEmployee()}</li>
     * <li>is some of the people in the map already hired?  {@link IHumanResourceManager#isHired(Person)}</li>
     * <li>will the reasearch facility exceed the total salary cap by hiring all people in the map</li>
     * Eg if the salary cap is 10 000 000, current total salary {@link no.ntnu.tdt4100.part5.IHumanResourceManager#getTotalSalary()} is 7 500 000 and 3 new people with a salary of 1 000 000 each
     * are to work on this project, the total salary cap will be exceeded
     * 
     * If any of the criteria above is true, a {@link no.ntnu.tdt4100.part5.CanNotStartProject} exception must be thrown.
     * 
     * @param project the project that starts. 
     * @param peopleAndSalaries the people to hire for the project. the key in the map is the person, the value in the map is the yearly salary of the person
     * 
     * @throws no.ntnu.tdt4100.part5.CanNotStartProjectException if any of the criteria for starting a project is not met
     * 
     * @see IHumanResourceManager#hire(Person, int)
     */
    public void startProject(Project project, Map<Person, Integer> peopleAndSalaries) throws CanNotStartProjectException {
        if (project == null || peopleAndSalaries == null) throw new CanNotStartProjectException();
        boolean exceedsIndCap = peopleAndSalaries.entrySet().stream()
            .filter(entry -> entry.getValue() <= getHumanResourceManager().getYearlySalaryCapForSingleEmployee())
            .toList().size() != peopleAndSalaries.size();
        boolean isHired = peopleAndSalaries.entrySet().stream()
            .filter(entry -> !getHumanResourceManager().isHired(entry.getKey()))
            .toList().size() != peopleAndSalaries.size();
        boolean exceedsTotCap = peopleAndSalaries.values().stream()
            .mapToInt(Integer::intValue).sum()
             + humanResourceManager.getTotalYearlySalaryForAllEmployees() > humanResourceManager.getYearlySalaryCapForAllEmployees();
        if (exceedsIndCap || isHired || exceedsTotCap) throw new CanNotStartProjectException(); // total salaries exceeds cap
        getProjects().add(project);
        peopleAndSalaries.entrySet().stream().forEach(entry -> humanResourceManager.hire(entry.getKey(), entry.getValue())); // hiring all enrolled for the project
    }

    /**
     * Ends the project with the given project ID by removing it from the 
     * list of projects at this facility and by firing the given list of people.
     * 
     * If the project ID doesn't exist in the list of projects at this facility, 
     * do not fire any people.
     * If some of the people in the list arent hired, skip them.
     * 
     * @param projectId the project ID of the project to end
     * @param people the people to fire
     * 
     * @see IHumanResourceManager#fire(Person)
     */
    public void endProject(UUID projectId, List<Person> people){
        if (getProjects().stream().filter(p -> p.projectId().equals(projectId)).toList().isEmpty()) return; // no project with this id in the registra
        for (Person person : people) {
            if (humanResourceManager.isHired(person)) humanResourceManager.fire(person);
        }
        Optional<Project> project = getProjects().stream().filter(p -> p.projectId().equals(projectId)).findAny();
        getProjects().remove(project.get());
    }
}
