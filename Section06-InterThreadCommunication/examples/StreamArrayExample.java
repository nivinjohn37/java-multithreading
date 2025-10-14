package examples;

import java.util.Arrays;

class Employee {
    private final String name;
    private final int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}

public class StreamArrayExample {
    public static void main(String[] args) {
        // Create an array of custom Employee objects
        Employee[] employees = {
            new Employee("Alice", 30),
            new Employee("Bob", 25),
            new Employee("Charlie", 35)
        };

        // Use Arrays.stream() with a lambda expression to iterate
        System.out.println("Iterating with a lambda expression:");
        Arrays.stream(employees).forEach(employee -> System.out.println(employee.getName()));

        // Use Arrays.stream() with a method reference for the same result
        System.out.println("\nIterating with a method reference:");
        Arrays.stream(employees).map(Employee::getName).forEach(System.out::println);
    }
}
