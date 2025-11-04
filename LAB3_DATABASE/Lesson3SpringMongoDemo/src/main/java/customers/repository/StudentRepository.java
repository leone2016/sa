package customers.repository;

import customers.domain.Customer;
import customers.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByPhone(String phone);
    Student findByName(String name);
    List<Student> findByAddress_City(String city);
}

