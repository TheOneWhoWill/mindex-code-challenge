package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    // John Lennon from employee_database.json
    private static final String EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testReadReportingStructure() {
        ReportingStructure reportingStructure =
                restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, EMPLOYEE_ID).getBody();

        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(EMPLOYEE_ID, reportingStructure.getEmployee().getEmployeeId());

        // John -> Paul, Ringo -> Pete, George
        assertEquals(4, reportingStructure.getNumberOfReports());
    }
}
