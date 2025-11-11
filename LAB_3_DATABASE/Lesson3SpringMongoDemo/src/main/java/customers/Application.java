package customers;

import customers.domain.Address;
import customers.domain.CreditCard;
import customers.domain.Customer;
import customers.domain.Student;
import customers.repository.CustomerRepository;
import customers.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//createCustomer();
		createStudent();


	}

	private void createStudent() {
//		Student student1 = new Student("Alice", "alice@gmail.com", "642345678");
//		student1.setAddress(new Address("1000AA", "Amsterdam", "Main Street", "10"));
//		studentRepository.save(student1);
//		Student student2 = new Student("Leonardo", "leo@gmail.com", "6418190145");
//		student2.setAddress(new Address("2000BB", "Quito", "Pichincha", "21"));
//		studentRepository.save(student2);
//
//		Student student3 = new Student("Sophie", "sophie@hotmail.com", "6529102345");
//		student3.setAddress(new Address("3000CC", "Utrecht", "Sunset Blvd", "33"));
//		studentRepository.save(student3);
//
//		Student student4 = new Student("David", "david@yahoo.com", "6312345678");
//		student4.setAddress(new Address("4000DD", "The Hague", "Ocean Drive", "44"));
//		studentRepository.save(student4);
//
//		Student student5 = new Student("Maria", "maria@gmail.com", "6901234567");
//		student5.setAddress(new Address("5000EE", "Eindhoven", "Forest Lane", "55"));
//		studentRepository.save(student5);

		// Then perform the following queries:
		// • Get all students
		System.out.println("-----------All students ----------------");
		List<Student> students = studentRepository.findAll();
		for (Student student : students) {
			System.out.println(student);
		}
		//• Get all students with a certain name
		System.out.println("-----------Find by name ----------------");
		System.out.println(studentRepository.findByName("Alice"));
		//• Get a student with a certain phoneNumber
		System.out.println("-----------Find by phone ----------------");
		System.out.println(studentRepository.findByPhone("6418190145"));
		//• Get all students from a certain city
		System.out.println("-----------Find by city ----------------");
		List<Student> studentsInCity = studentRepository.findByAddress_City("Quito");
		for (Student student : studentsInCity) {
			System.out.println(student);
		}
	}

	public void createCustomer() {
		// create customer
		Customer customer = new Customer(101,"John doe", "johnd@acme.com", "0622341678");
		CreditCard creditCard = new CreditCard("12324564321", "Visa", "11/23");
		customer.setCreditCard(creditCard);
		customerRepository.save(customer);
		customer = new Customer(109,"John Jones", "jones@acme.com", "0624321234");
		creditCard = new CreditCard("657483342", "Visa", "09/23");
		customer.setCreditCard(creditCard);
		customerRepository.save(customer);
		customer = new Customer(66,"James Johnson", "jj123@acme.com", "068633452");
		creditCard = new CreditCard("99876549876", "MasterCard", "01/24");
		customer.setCreditCard(creditCard);
		customerRepository.save(customer);
		//get customers
		System.out.println(customerRepository.findById(66).get());
		System.out.println(customerRepository.findById(101).get());
		System.out.println("-----------All customers ----------------");
		System.out.println(customerRepository.findAll());
		//update customer
		customer = customerRepository.findById(101).get();
		customer.setEmail("jd@gmail.com");
		customerRepository.save(customer);
		System.out.println("-----------find by phone ----------------");
		System.out.println(customerRepository.findByPhone("0622341678"));
		System.out.println("-----------find by email ----------------");
		System.out.println(customerRepository.findByEmail("jj123@acme.com"));
		System.out.println("-----------find customers with a certain type of creditcard ----------------");
		List<Customer> customers = customerRepository.findByCreditCardType("Visa");
		for (Customer cust : customers){
			System.out.println(cust);
		}

		System.out.println("-----------find by name ----------------");
		System.out.println(customerRepository.findByName("John doe"));
	}

}
