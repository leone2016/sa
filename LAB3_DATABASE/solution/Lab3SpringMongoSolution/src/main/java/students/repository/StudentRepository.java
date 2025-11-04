package students.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import students.domain.Student;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, Integer> {
    List<Student> findByName(String name);
    Student findByPhone(String phone);

    @Query("{'address.city' : ?0}")
    List<Student> findByCity(String city);


}

