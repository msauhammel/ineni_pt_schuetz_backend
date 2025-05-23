package at.hochschule.ineni_pt.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // JpaRepository provides CRUD methods like save(), findById(), findAll(), deleteById()
    // You can add custom query methods here if needed
}