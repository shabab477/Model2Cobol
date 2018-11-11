package io.shabab477.github.model2cobol.tests.models;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.annotations.Embedded;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;

/**
 * @author shabab
 * @since 11/11/18
 */
@DataEntity(dataName = "WS-EMP", length = 96, level = 1, strategy = CalculationStrategy.INFERRED)
public class Address {

    @DataMember(dataName = "WS-EMP-CITY", length = 90, level = 2, type = "AN")
    private String city;

    @DataMember(dataName = "WS-EMP-ZIP", length = 90, level = 2, type = "AN")
    private String zip;

    @Embedded
    private Contact contact;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
