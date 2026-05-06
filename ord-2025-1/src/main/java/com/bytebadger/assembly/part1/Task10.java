package com.bytebadger.assembly.part1;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
// import java.util.Set;
import java.util.stream.Collectors;


class Employee {
    private final String name;
    private final int employeeId;

    public Employee(String name, int employeeId) {
        if (name == null || employeeId < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.employeeId = employeeId;
    }
    public String getName() { return name; }
    public int getEmployeeId() { return employeeId; }

     @Override
    public String toString() {
        return "" + this.name + ": " + this.employeeId; 
    }

}


class EmployeeSorter {
    /**
     * Sort the employees by the last letter of their name (a–z).
     * If multiple employees have the same last letter, 
     * sort them by their employeeId from lowest to highest.
     * 
     * @param employees the list of Employee objects to sort
     * @return a sorted collection of Employee objects
     */
    
    public Collection<Employee> sortEmployees(List<Employee> employees) {

        // TODO: Implement the sorting logic here, by uncommenting appropriate lines.
        
        // NB! You are not allowed to rearrange lines, only uncomment specific lines.
        
        return employees
            .stream()
            // .filter(e -> e.getEmployeeId() > 0)
            .sorted(Comparator.comparing(Employee::getEmployeeId))
            // .sorted(Comparator.comparing(Employee::getName))
            // .sorted(Comparator.comparingInt(e -> e.getName().charAt(0)))
            .sorted(Comparator.comparingInt(e -> e.getName().charAt(e.getName().length() - 1)))
            // .sorted(Comparator.comparingInt(Employee::getEmployeeId))
            // .forEach(System.out::println)
            // .collect(Collectors.toSet())
            .collect(Collectors.toList())
            ;
    
    }

    public static void main(String[] args) {
        Employee x = new Employee("Kristoffea", 11);
        Employee y = new Employee("Kristoffer", 10);
        Employee z = new Employee("Kristoffer", 19);
        EmployeeSorter s = new EmployeeSorter();
        Collection<Employee> set = s.sortEmployees(List.of(x, y, z));
        System.out.println(set);
    }
}

