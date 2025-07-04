package at.hochschule.ineni_pt.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //List<Employee> findByDepartmentId(Long departmentId);
}