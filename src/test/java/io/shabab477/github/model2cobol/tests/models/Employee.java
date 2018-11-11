package io.shabab477.github.model2cobol.tests.models;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.annotations.Embedded;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;
import io.shabab477.github.model2cobol.tests.generators.ArrayGenerator;

/**
 * @author shabab
 * @since 11/11/18
 */
@DataEntity(dataName = "WS-EMP", length = 96, level = 1, strategy = CalculationStrategy.INFERRED)
public class Employee {

    @DataMember(dataName = "WS-EMP-NAME", length = 90, level = 2, type = "AN")
    private String name;

    @DataMember(dataName = "WS-EMP-SAL", length = 96, level = 2, type = "BI")
    private int salary;

    @DataMember(dataName = "WS-LINE", length = 2, level = 4, type = "BI", parseStrategy = ArrayGenerator.class)
    private String children[];

    @Embedded
    private Address address;

    public Employee() {
    }

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }
}
