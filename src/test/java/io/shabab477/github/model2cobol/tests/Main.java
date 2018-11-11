package io.shabab477.github.model2cobol.tests;

import io.shabab477.github.model2cobol.processor.CobolBuilder;
import io.shabab477.github.model2cobol.tests.models.Address;
import io.shabab477.github.model2cobol.tests.models.Contact;
import io.shabab477.github.model2cobol.tests.models.Employee;

public class Main {

    public static void main(String[] args) {
        String value = CobolBuilder
                .with(createEmployee())
                .build();

        System.out.println(value);
    }

    static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setName("James Gosling");
        employee.setSalary(2000);

        Contact contact = new Contact();
        contact.setMobile("012345678910");
        contact.setMobile("019876543210");

        Address address = new Address();
        address.setCity("Dhaka");
        address.setZip("1100");
        address.setContact(contact);

        employee.setAddress(address);
        String children[] = new String[3];
        for (int i = 0; i < 3; i++) {
            children[i] = String.valueOf(330 * (i + 1));
        }

        employee.setChildren(children);

        return employee;
    }
}
