package com.market.finder.controller;

import com.market.finder.dao.AttendanceRepository;
import com.market.finder.dao.CourseRepository;
import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import com.market.finder.entity.AttendanceStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AttendanceController(AttendanceRepository attendanceRepository,
                                StudentRepository studentRepository,
                                CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // 1. Show all attendance records
    @GetMapping
    public String listAttendance(Model model) {
        model.addAttribute("attendanceRecords", attendanceRepository.findAll());
        return "attendance/list";
    }

    // 2. Show the form to record new attendance
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("statuses", AttendanceStatus.values());
        return "attendance/form";
    }

    // 3. Show the form to edit an existing attendance record
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        AttendanceId id = new AttendanceId(studentId, courseId, date);
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance record"));

        model.addAttribute("attendance", attendance);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("statuses", AttendanceStatus.values());
        return "attendance/form";
    }

    // 4. Save the attendance record
    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") Attendance attendance) {
        // Map the selected objects' IDs to the Composite Key before saving
        if (attendance.getId() == null) {
            attendance.setId(new AttendanceId());
        }
        if (attendance.getStudent() != null) {
            attendance.getId().setStudentId(attendance.getStudent().getId());
        }
        if (attendance.getCourse() != null) {
            attendance.getId().setCourseId(attendance.getCourse().getId());
        }

        attendanceRepository.save(attendance);
        return "redirect:/attendance";
    }

    // 5. Delete an attendance record
    @GetMapping("/delete")
    public String deleteAttendance(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        AttendanceId id = new AttendanceId(studentId, courseId, date);
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
        }
        return "redirect:/attendance";
    }
}