package io.shabab477.github.model2cobol.tests.models;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;

/**
 * @author shabab
 * @since 11/11/18
 */
@DataEntity(dataName = "WS-EMP-CONTACT", length = 96, level = 1, strategy = CalculationStrategy.INFERRED)
public class Contact {

    @DataMember(dataName = "WS-EMP-PHONE", length = 11, level = 2, type = "AN")
    private String phone;

    @DataMember(dataName = "WS-EMP-MOB", length = 11, level = 2, type = "AN")
    private String mobile;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
