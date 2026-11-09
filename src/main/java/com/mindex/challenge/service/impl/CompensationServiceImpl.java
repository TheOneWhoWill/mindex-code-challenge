package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	// Our hard link to the mongodb collection
    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployeeId();
        if (employeeId == null || employeeId.isBlank()) {
            throw new RuntimeException("employeeId is required to create a compensation");
        }

        // Delegates to EmployeeService so an unknown employeeId fails the same way as the employee endpoints.
        employeeService.read(employeeId);

        if (compensationRepository.existsById(employeeId)) {
            throw new RuntimeException("Compensation already exists for employeeId: " + employeeId);
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employeeId [{}]", employeeId);

        return compensationRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("No compensation found for employeeId: " + employeeId));
    }
}
