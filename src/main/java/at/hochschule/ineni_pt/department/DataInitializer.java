package at.hochschule.ineni_pt.department;

import at.hochschule.ineni_pt.employee.Employee;
import at.hochschule.ineni_pt.employee.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DataInitializer(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional // Important for operations involving multiple repositories or lazy loading
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");

        if (departmentRepository.count() == 0) {
            logger.info("Initializing department data...");
            Department hr = departmentRepository.save(new Department("Human Resources", "New York HQ, Floor 10"));
            Department eng = departmentRepository.save(new Department("Engineering", "San Francisco Tech Hub, Building A"));
            Department mark = departmentRepository.save(new Department("Marketing", "London Office, Suite 200"));
            departmentRepository.save(new Department("Sales", "Chicago Branch, Floor 5"));
            logger.info("Department data initialized.");

            if (employeeRepository.count() == 0) {
                logger.info("Initializing employee data...");
                employeeRepository.save(new Employee("Alice", "Smith", "alice.smith@example.com", "HR Manager", hr));
                employeeRepository.save(new Employee("Bob", "Johnson", "bob.johnson@example.com", "HR Specialist", hr));

                employeeRepository.save(new Employee("Charlie", "Brown", "charlie.brown@example.com", "Lead Engineer", eng));
                employeeRepository.save(new Employee("Diana", "Davis", "diana.davis@example.com", "Software Engineer", eng));
                employeeRepository.save(new Employee("Eve", "Wilson", "eve.wilson@example.com", "QA Engineer", eng));

                employeeRepository.save(new Employee("Frank", "Miller", "frank.miller@example.com", "Marketing Head", mark));
                employeeRepository.save(new Employee("Grace", "Lee", "grace.lee@example.com", "Digital Marketer", mark));
                logger.info("Employee data initialized.");
            }

        } else {
            logger.info("Data already exists, skipping initialization.");
        }

        }


}