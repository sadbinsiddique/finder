package com.market.finder.config;

import com.market.finder.entity.*;
import com.market.finder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * SRP: Bootstraps initial sample seed data on application startup if the database is empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final InstructorService instructorService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final StaffService staffService;
    private final AttendanceService attendanceService;
    private final EnrollmentService enrollmentService;
    private final GradebookService gradebookService;

    public DataInitializer(UserService userService,
                           RoleService roleService,
                           PermissionService permissionService,
                           DepartmentService departmentService,
                           CourseService courseService,
                           InstructorService instructorService,
                           StudentService studentService,
                           EmployeeService employeeService,
                           StaffService staffService,
                           AttendanceService attendanceService,
                           EnrollmentService enrollmentService,
                           GradebookService gradebookService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.departmentService = departmentService;
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.studentService = studentService;
        this.employeeService = employeeService;
        this.staffService = staffService;
        this.attendanceService = attendanceService;
        this.enrollmentService = enrollmentService;
        this.gradebookService = gradebookService;
    }

    @Override
    public void run(String... args) {
        seedRolesAndPermissions();
        seedUsers();
        seedDepartments();
        seedCourses();
        seedInstructors();
        seedStudents();
        seedEmployeesAndStaff();
        seedEnrollmentsAndGrades();
    }

    private void seedRolesAndPermissions() {
        if (roleService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding default roles and permissions...");
            
            Role adminRole = new Role();
            adminRole.setRoleName("ROLE_ADMIN");
            roleService.save(adminRole);

            Role userRole = new Role();
            userRole.setRoleName("ROLE_USER");
            roleService.save(userRole);

            Role instructorRole = new Role();
            instructorRole.setRoleName("ROLE_INSTRUCTOR");
            roleService.save(instructorRole);

            Role studentRole = new Role();
            studentRole.setRoleName("ROLE_STUDENT");
            roleService.save(studentRole);
        }
    }

    private void seedUsers() {
        Role adminRole = roleService.findAll().stream()
                .filter(r -> "ROLE_ADMIN".equals(r.getRoleName()))
                .findFirst().orElse(null);

        Role instructorRole = roleService.findAll().stream()
                .filter(r -> "ROLE_INSTRUCTOR".equals(r.getRoleName()))
                .findFirst().orElse(null);

        Role studentRole = roleService.findAll().stream()
                .filter(r -> "ROLE_STUDENT".equals(r.getRoleName()))
                .findFirst().orElse(null);

        if (userService.findByUsername("admin").isEmpty()) {
            logger.info("[SEED] Seeding admin user...");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEnabled(true);
            if (adminRole != null) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);
            }
            userService.save(admin);
        }

        if (userService.findByUsername("instructor1").isEmpty()) {
            logger.info("[SEED] Seeding instructor users...");
            User inst1 = new User();
            inst1.setUsername("instructor1");
            inst1.setPassword("password123");
            inst1.setEnabled(true);
            if (instructorRole != null) {
                Set<Role> roles = new HashSet<>();
                roles.add(instructorRole);
                inst1.setRoles(roles);
            }
            userService.save(inst1);
        }

        if (userService.findByUsername("student1").isEmpty()) {
            logger.info("[SEED] Seeding student users...");
            User st1 = new User();
            st1.setUsername("student1");
            st1.setPassword("password123");
            st1.setEnabled(true);
            if (studentRole != null) {
                Set<Role> roles = new HashSet<>();
                roles.add(studentRole);
                st1.setRoles(roles);
            }
            userService.save(st1);
        }
    }

    private void seedDepartments() {
        if (departmentService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding departments...");
            Department cs = new Department();
            cs.setName("Computer Science");
            departmentService.save(cs);

            Department ee = new Department();
            ee.setName("Electrical Engineering");
            departmentService.save(ee);

            Department ba = new Department();
            ba.setName("Business Administration");
            departmentService.save(ba);

            Department math = new Department();
            math.setName("Mathematics");
            departmentService.save(math);
        }
    }

    private void seedCourses() {
        if (courseService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding courses...");
            Course c1 = new Course();
            c1.setTitle("Introduction to Computer Science");
            courseService.save(c1);

            Course c2 = new Course();
            c2.setTitle("Data Structures and Algorithms");
            courseService.save(c2);

            Course c3 = new Course();
            c3.setTitle("Circuit Analysis");
            courseService.save(c3);

            Course c4 = new Course();
            c4.setTitle("Business Ethics");
            courseService.save(c4);
        }
    }

    private void seedInstructors() {
        if (instructorService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding instructors...");

            Instructor i1 = new Instructor();
            i1.setFirstName("John");
            i1.setLastName("Doe");
            i1.setEmail("john.doe@university.edu");
            i1.setUsername("instructor1");
            instructorService.save(i1);

            Instructor i2 = new Instructor();
            i2.setFirstName("Jane");
            i2.setLastName("Smith");
            i2.setEmail("jane.smith@university.edu");
            i2.setUsername("instructor2");
            instructorService.save(i2);
        }
    }

    private void seedStudents() {
        if (studentService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding students...");
            Department cs = departmentService.findAll().stream().findFirst().orElse(null);

            Student s1 = new Student();
            s1.setFirstName("Alice");
            s1.setLastName("Johnson");
            s1.setEmail("alice.johnson@student.edu");
            s1.setDepartment(cs);
            s1.setUsername("student1");
            studentService.save(s1);

            Student s2 = new Student();
            s2.setFirstName("Bob");
            s2.setLastName("Williams");
            s2.setEmail("bob.williams@student.edu");
            s2.setDepartment(cs);
            s2.setUsername("student2");
            studentService.save(s2);
        }
    }

    private void seedEmployeesAndStaff() {
        if (employeeService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding employees...");
            Employee e1 = new Employee("Mark", "Taylor", "mark.taylor@company.com");
            employeeService.save(e1);

            Employee e2 = new Employee("Sarah", "Conner", "sarah.conner@company.com");
            employeeService.save(e2);
        }

        if (staffService.findAll().isEmpty()) {
            logger.info("[SEED] Seeding staff...");
            Staff st1 = new Staff("michael.scott@paper.com", "Scott", "Michael", 65000, "Regional Manager", 45);
            staffService.save(st1);

            Staff st2 = new Staff("dwight.schrute@paper.com", "Schrute", "Dwight", 55000, "Assistant Regional Manager", 40);
            staffService.save(st2);
        }
    }

    private void seedEnrollmentsAndGrades() {
        if (studentService.findAll().size() >= 2 && courseService.findAll().size() >= 2) {
            Student s1 = studentService.findAll().get(0);
            Course c1 = courseService.findAll().get(0);

            if (enrollmentService.findAll().isEmpty()) {
                logger.info("[SEED] Seeding enrollments...");
                Enrollment en = new Enrollment();
                EnrollmentId id = new EnrollmentId(s1.getId(), c1.getId());
                en.setId(id);
                en.setStudent(s1);
                en.setCourse(c1);
                en.setEnrollmentDate(LocalDate.now());
                enrollmentService.save(en);
            }

            if (attendanceService.findAll().isEmpty()) {
                logger.info("[SEED] Seeding attendance...");
                Attendance att = new Attendance();
                AttendanceId id = new AttendanceId(s1.getId(), c1.getId(), LocalDate.now());
                att.setId(id);
                att.setStudent(s1);
                att.setCourse(c1);
                att.setStatus(AttendanceStatus.Present);
                attendanceService.save(att);
            }

            if (gradebookService.findAll().isEmpty()) {
                logger.info("[SEED] Seeding gradebook...");
                Gradebook gb = new Gradebook();
                GradebookId id = new GradebookId(s1.getId(), c1.getId(), "Midterm Exam");
                gb.setId(id);
                gb.setStudent(s1);
                gb.setCourse(c1);
                gb.setScore(BigDecimal.valueOf(92.50));
                gradebookService.save(gb);
            }
        }
    }
}
