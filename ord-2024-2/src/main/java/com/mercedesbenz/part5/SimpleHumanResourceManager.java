package com.mercedesbenz.part5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import no.ntnu.tdt4100.part2.Person;
import no.ntnu.tdt4100.part5.AlreadyHiredException;
import no.ntnu.tdt4100.part5.EmploymentStatus;
import no.ntnu.tdt4100.part5.ExceedsSalaryCapForAllEmployeesException;
import no.ntnu.tdt4100.part5.ExceedsSingleEmployeeSalaryCapException;
import no.ntnu.tdt4100.part5.IEmployementListener;
import no.ntnu.tdt4100.part5.IHumanResourceManager;

 /**
  * This class must implement the {@link IHumanResourceManager} interface.
  * Read the unit tests and the JavaDoc description in the interface to implement the methods correctly.
  *
  * @see IHumanResourceManager
  * @see SimpleHumanResourceManagerTests
  */
public class SimpleHumanResourceManager implements IHumanResourceManager {
    private int yearlySalaryCapForAllEmployees;
    private int yearlySalaryCapForSingleEmployee;
    // private int yearlySalaryAccumulated;
    private List<IEmployementListener> listeners = new ArrayList<>();
    private Map<Person, Integer> employees = new HashMap<>();
    /**
     * @param yearlySalaryCapForAllEmployees the maximum total yearly salary for all employees managed by <code>this</code> HR manager
     * @param yearlySalaryCapForSingleEmployee the maximum total yearly salary for a single employee managed by <code>this</code> HR manager
     */
    public SimpleHumanResourceManager(
        int yearlySalaryCapForAllEmployees, 
        int yearlySalaryCapForSingleEmployee) {
            if (yearlySalaryCapForAllEmployees < 0 || yearlySalaryCapForSingleEmployee < 0 
            //    || yearlySalaryCapForAllEmployees < yearlySalaryCapForSingleEmployee) i find it very odd that this check is not required, such a weird configuration and it is critical for the tests...
            ) throw new IllegalArgumentException();
        this.yearlySalaryCapForAllEmployees = yearlySalaryCapForAllEmployees;
        this.yearlySalaryCapForSingleEmployee = yearlySalaryCapForSingleEmployee;
        // TODO Implement constructor to behave according to JavaDoc
    }

    /**
     * Adds a listener that observes changes to employements (when a person is hired or fired)
     * 
     * The addListener method adds a listener.
     * The same listener can not be added multiple times.
     * 
     * @param listener the {@link IEmployementListener} to add 
     */
    public void addListener(IEmployementListener listener) {
        if (listener == null) throw new IllegalArgumentException();
        if (this.listeners.contains(listener)) return;
        this.listeners.add(listener);
    }

    /**
     * Removes a previously registered listener.
     * 
     * @param listener the {@link IStockChangeListener} to remove 
     */
    public void removeListener(IEmployementListener listener) {
        if (listener == null || !this.listeners.contains(listener)) return;
        this.listeners.remove(listener);
    }

    /**
     * Returns the number of added listeners.
     * 
     * @return number of added {@link IEmployementListener} objects
     */
    public int getNumberOfListeners() {
        return listeners.size();
    }

    /**
     * Hires a person.
     * When a person is hired, trigger all listeners {@link IEmployementListener#trigger(Person, EmploymentStatus)}.
     * 
     * @param person the person to hire
     * @param yearlySalary the persons yearly salary
     * 
     * @throws AlreadyHiredException if this person is already hired
     * @throws ExceedsSingleEmployeeSalaryCapException if this persons salary
     * exceeds the {@link #getSalaryCapForSingleEmployee()}
     * @throws ExceedsSalaryCapForAllEmployeesException if hiring this person would exceed the total salary cap for 
     * this HR manager {@link #getYearlySalaryCap()}
     */
    public void hire(Person person, int yearlySalary) {
        if (person == null) throw new IllegalArgumentException();
        if (isHired(person)) throw new AlreadyHiredException();
        if (yearlySalary > getYearlySalaryCapForSingleEmployee()) throw new ExceedsSingleEmployeeSalaryCapException();
        if (yearlySalary + getTotalYearlySalaryForAllEmployees() > getYearlySalaryCapForAllEmployees()) throw new ExceedsSalaryCapForAllEmployeesException();
        this.employees.put(person, yearlySalary);
        this.listeners.stream().forEach(l -> l.trigger(person, EmploymentStatus.HIRED));
    }

    /**
     * Returns whether the records indicate that the given person is hired
     * @return the whether the records indicate that the given person is hired
     */
    public boolean isHired(Person person) {
        return this.employees.containsKey(person);
    }

    /**
     * Fires a person.
     * When a person is fired, trigger all listeners {@link IEmployementListener#trigger(Person, EmploymentStatus)}.
     * When a person is fired, a subsequent call to isHired should return false
     * @param person the person to fire
     */
    public void fire(Person person) {
        if (isHired(person)) {
            this.employees.remove(person);
            this.listeners.stream().forEach(l -> l.trigger(person, EmploymentStatus.FIRED));
        }
    }

    /**
     * Returns the number of employees managed by the implementation of this
     * HR manager
     * 
     * @return the number of employees managed by this HR manager
     */
    public int getNumberOfEmployees() {
        if (employees == null) return 0;
        return employees.size();
    }

    /**
     * Returns the yearly salary cap for the sum of all employees
     * @return the yearly salary cap for the sum of all employees
     */
    public int getYearlySalaryCapForAllEmployees() {
        return this.yearlySalaryCapForAllEmployees;
    }

    /** 
     * Returns the yearly salary cap for a single employee
     * @return the yearly salary cap for a single employee
     */
    public int getYearlySalaryCapForSingleEmployee() {
        return this.yearlySalaryCapForSingleEmployee;
    }

    /**
     * Returns the total yearly salary for all hired employees
     * @return the total yearly salary for all hired employees
     */
    public int getTotalYearlySalaryForAllEmployees() {
        return this.employees.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** 
     * Returns the yearly salary for the given person
     * If this person is not hired at this facility, returns {@link Optional#empty()}
     * @return the yearly salary for the given person
     */
    public Optional<Integer> getSalaryFor(Person person) {
        Optional<Person> employee = this.employees.keySet().stream().filter(p -> p.equals(person)).findAny();
        if (employee.isEmpty()) return Optional.empty();
        return Optional.of(employees.get(employee.get()));
    }
}
