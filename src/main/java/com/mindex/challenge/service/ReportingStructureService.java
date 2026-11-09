package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

public interface ReportingStructureService {
	// This let's us get the reporting structure for a given employee
    ReportingStructure read(String employeeId);
}
