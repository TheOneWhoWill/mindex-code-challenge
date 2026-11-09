package com.mindex.challenge.data;

public class ReportingStructure {
	private Employee employee;
	private int numberOfReports;

	/**
	 * Default constructor for ReportingStructure
	 * Needed because defing a constructor with parameters removes the default constructor
	 */
	public ReportingStructure() {}

	/*
	 * Constructor for ReportingStructure
	 * @param employee - Employee object for which the reporting structure is being created
	 * @param numberOfReports - Number of reports for the employee
	 */
	public ReportingStructure(Employee employee, int numberOfReports) {
		this.employee = employee;
		this.numberOfReports = numberOfReports;
	}

	/*
	 * Getter for employee
	 * @return Employee object
	 */
	public Employee getEmployee() {
		return employee;
	}

	/*
	 * Setter for employee
	 * @param employee - Employee object
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/*
	 * Getter for numberOfReports
	 * @return Number of reports for the employee
	 */
	public int getNumberOfReports() {
		return numberOfReports;
	}

	/*
	 * Setter for numberOfReports
	 * @param numberOfReports - Number of reports for the employee
	 */
	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}
}
