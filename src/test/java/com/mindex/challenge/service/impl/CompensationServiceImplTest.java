package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    // John Lennon from employee_database.json
    private static final String EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    private String compensationUrl;
    private String compensationIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }

    @Test
    public void testCreateRead() {
        Compensation compensation = new Compensation();
        compensation.setEmployeeId(EMPLOYEE_ID);
        compensation.setSalary(new BigDecimal("100000.00"));
        compensation.setEffectiveDate(LocalDate.of(2024, 1, 1));

        // Create checks
        Compensation createdCompensation =
                restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();

        assertNotNull(createdCompensation);
        assertEquals(EMPLOYEE_ID, createdCompensation.getEmployeeId());

        // Read checks
        Compensation readCompensation =
                restTemplate.getForEntity(compensationIdUrl, Compensation.class, EMPLOYEE_ID).getBody();

        assertNotNull(readCompensation);
        assertEquals(EMPLOYEE_ID, readCompensation.getEmployeeId());
        assertEquals(0, readCompensation.getSalary().compareTo(new BigDecimal("100000.00")));
        assertEquals(LocalDate.of(2024, 1, 1), readCompensation.getEffectiveDate());
    }
}
