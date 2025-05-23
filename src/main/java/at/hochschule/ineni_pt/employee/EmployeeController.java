package at.hochschule.ineni_pt.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Represents a request body for creating/updating an employee
// This DTO helps in clearly defining the expected input structure
class EmployeeRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String position;
    public Long departmentId; // For creating or re-assigning department
}


@RestController
@RequestMapping("/api")
@CrossOrigin("**")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create a new employee and assign to a department
    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        if (employeeRequest.departmentId == null) {
            return ResponseEntity.badRequest().body("departmentId is required to create an employee.");
        }
        try {
            Employee employee = new Employee();
            employee.setFirstName(employeeRequest.firstName);
            employee.setLastName(employeeRequest.lastName);
            employee.setEmail(employeeRequest.email);
            employee.setPosition(employeeRequest.position);
            // Department will be set by the service
            Employee createdEmployee = employeeService.createEmployee(employee, employeeRequest.departmentId);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get a specific employee by ID
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all employees for a specific department
    @GetMapping("/departments/{departmentId}/employees")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable Long departmentId) {
        try {
            List<Employee> employees = employeeService.getEmployeesByDepartmentId(departmentId);
            return ResponseEntity.ok(employees);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update an employee
    // The request can optionally include a new departmentId to re-assign the employee
    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        try {
            Employee employeeDetails = new Employee();
            employeeDetails.setFirstName(employeeRequest.firstName);
            employeeDetails.setLastName(employeeRequest.lastName);
            employeeDetails.setEmail(employeeRequest.email);
            employeeDetails.setPosition(employeeRequest.position);

            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails, Optional.ofNullable(employeeRequest.departmentId));
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete an employee
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}