Suppose I have an entity class such as this:
```
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
```
###### In order to convert entities to `COBOL` we use the following annotations:
- `@DataEntity`: This refers to the entity class that needs to be converted. The options available are:
   1. `dataName`: The name of the converted variable.
   2. `length`: The length of the data
   3. `level`: The level of the data
   4. `strategy`: The total length inferrence strategy. If `CalculationStrategy.INFERRED` is used then the length will be ignored and the converter will place its own calculated value. If `CalculationStrategy.PROVIDED` then the declared length will be used. 
- `@DataMember`: Similar to `@DataEntity` but has `type` and `value` declaration. Usages of these declarations are self-explanatory. For custom conversion use `parseStrategy` option to refer to a class declaration which implements `MemberValueGenerator`
- `@Embedded`: For parsing nested objects.
- 

### Usage
Considering the above entity a sample usage will be as follows:

```
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

```

With `CobolBuilder` we can also provide some settings along the chain. They are:
- `withStartingLevel()`: The starting level of the entity
- `withMaxDepth()`: Maximum depth for nested objects
- 
Based on the above usage we will get the following COBOL code generation:

```
1  WS-EMP                       396/GRP
  2  WS-EMP-NAME                  90/AN  James Gosling
  2  WS-EMP-SAL                   96/BI  2000
                                  2/BI  330
  4  WS-LINE(1)                   2/BI  330
  4  WS-LINE(2)                   2/BI  660
  4  WS-LINE(3)                   2/BI  990
  1  WS-EMP                       202/GRP
    2  WS-EMP-CITY                  90/AN  Dhaka
    2  WS-EMP-ZIP                   90/AN  1100
    1  WS-EMP-CONTACT               22/GRP
      2  WS-EMP-PHONE                 11/AN
      2  WS-EMP-MOB                   11/AN  019876543210
```

