package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
    private CompensationRepository compensationRepository;


    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
	
	@Override
    public int exec(String id, int count) {
		
        Employee employee = read(id);

        if (employee.getDirectReports() != null) {
			for(Employee emp:employee.getDirectReports()) {
				count = exec(emp.getEmployeeId(), count+1);
			}
        }
	
		return count;
    }
	
	@Override
    public Compensation createComp(Compensation comp) {
        LOG.debug("Creating employee [{}]", comp);
		
		String id = comp.getEmployee().getEmployeeId();
		comp.setEmployee(employeeRepository.findByEmployeeId(id));
		compensationRepository.insert(comp);

        return comp;
    }

    @Override
    public Compensation readComp(String id) {
        LOG.debug("Creating employee with id [{}]", id);

		Compensation comp = compensationRepository.findByEmployeeId(id);

        if (comp == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return comp;
    }

}
