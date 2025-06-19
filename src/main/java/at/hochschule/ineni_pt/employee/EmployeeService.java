package at.hochschule.ineni_pt.employee;

import at.hochschule.ineni_pt.department.Department;
import at.hochschule.ineni_pt.department.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository; // To fetch Department for assignment

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Employee createEmployee(Employee employee, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        //employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new RuntimeException("Department not found with id: " + departmentId);
        }
        return null;//employeeRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails, Optional<Long> departmentIdOpt) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPosition(employeeDetails.getPosition());

/*
        departmentIdOpt.ifPresent(departmentId -> {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
           // employee.setDepartment(department);
        });
*/

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}