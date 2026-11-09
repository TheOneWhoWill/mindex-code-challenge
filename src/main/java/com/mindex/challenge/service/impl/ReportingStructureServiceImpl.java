package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Computing reporting structure for id [{}]", employeeId);

        // Delegates to EmployeeService so an unknown employeeId fails the same way as the other endpoints.
        Employee employee = employeeService.read(employeeId);

        return new ReportingStructure(employee, countReports(employee));
    }

    /**
     * Breadth-first walk of the employee graph. We start with the root, check its direct reports, and then check their direct reports, and so on.
	 * This is essentially a breadth-first search (BFS) of the employee graph, counting distinct employees as we go.
	 * We have a set to track who we've already counted, so we don't double-count shared reports or get stuck in cycles.
     */
    private int countReports(Employee root) {
        // Seeded with the root so a malformed cycle back to it is not counted as its own report.
        Set<String> visited = new HashSet<>();
        visited.add(root.getEmployeeId());

        Deque<String> pending = new ArrayDeque<>();
        enqueueDirectReports(root, pending, visited);

        int count = 0;
        while (!pending.isEmpty()) {
            String id = pending.poll();

            // Adding to the set both de-duplicates shared reports and guards against cycles.
            if (!visited.add(id)) {
                continue;
            }

            Employee current = employeeRepository.findByEmployeeId(id);
            if (current == null) {
                LOG.warn("Skipping dangling directReport reference [{}]", id);
                continue;
            }

            count++;
            enqueueDirectReports(current, pending, visited);
        }

        return count;
    }

	// Helper method to enqueue the direct reports of an employee, avoiding duplicates and cycles.
    private void enqueueDirectReports(Employee employee, Deque<String> pending, Set<String> visited) {
        if (employee.getDirectReports() == null) {
            return;
        }

        for (Employee report : employee.getDirectReports()) {
            if (report != null && report.getEmployeeId() != null && !visited.contains(report.getEmployeeId())) {
                pending.add(report.getEmployeeId());
            }
        }
    }
}
