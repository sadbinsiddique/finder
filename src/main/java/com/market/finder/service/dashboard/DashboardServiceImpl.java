package com.market.finder.service.dashboard;

import com.market.finder.repository.CourseRepository;
import com.market.finder.repository.DepartmentRepository;
import com.market.finder.repository.InstructorRepository;
import com.market.finder.repository.RoleRepository;
import com.market.finder.repository.StudentRepository;
import com.market.finder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardServiceImpl(UserRepository userRepository,
                                RoleRepository roleRepository,
                                StudentRepository studentRepository,
                                InstructorRepository instructorRepository,
                                CourseRepository courseRepository,
                                DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Map<String, Long> getSystemStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalRoles", roleRepository.count());
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalInstructors", instructorRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        return stats;
    }
}
