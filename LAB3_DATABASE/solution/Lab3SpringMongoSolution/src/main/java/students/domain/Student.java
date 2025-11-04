package students.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Student {
    @Id
    private int studentNumber;
    private String name;
    private String phone;
    private String email;
    private Address address;

    public Student(int studentNumber, String name, String phone, String email) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentNumber=" + studentNumber +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
