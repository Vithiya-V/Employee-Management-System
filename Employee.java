package com.ems.entity;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ems.entity.Employee;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ems.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;private String firstName;private String lastName;private String email;private String department;private Double salary;
    public Employee() {}
}
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}
@Service
public class EmployeeService {
  @Autowired
  private EmployeeRepository repository;
  public List<Employee> getAllEmployees() {
    return repository.findAll();
    }
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }
    public Employee getEmployee(Long id) {
        return repository.findById(id).orElse(null);
    }
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees",
                service.getAllEmployees());
        return "employee-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("employee",
                new Employee());
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(
            @ModelAttribute Employee employee) {
        service.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("employee",
                service.getEmployee(id));
        return "employee-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id) {

        service.deleteEmployee(id);
        return "redirect:/employees";
    }
}
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/employees/new",
                                 "/employees/edit/**",
                                 "/employees/delete/**")
                .hasRole("ADMIN")

                .requestMatchers("/employees/**")
                .hasAnyRole("ADMIN","USER")

                .anyRequest()
                .authenticated()
            )

            .formLogin(login -> login
                .loginPage("/login")
                .permitAll()
            )

            .logout(logout -> logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

