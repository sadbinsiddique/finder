package com.market.finder.controller;

import com.market.finder.entity.Attendance;
import com.market.finder.entity.AttendanceId;
import com.market.finder.entity.AttendanceStatus;
import com.market.finder.service.AttendanceService;
import com.market.finder.service.CourseService;
import com.market.finder.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * DIP: Depends on AttendanceService, StudentService, and CourseService abstractions.
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final CourseService courseService;

    public AttendanceController(AttendanceService attendanceService,
                                StudentService studentService,
                                CourseService courseService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // 1. Show all attendance records
    @GetMapping
    public String listAttendance(Model model) {
        model.addAttribute("attendanceRecords", attendanceService.findAll());
        return "attendance/list";
    }

    // 2. Show the form to record new attendance
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
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
        Attendance attendance = attendanceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance record"));

        model.addAttribute("attendance", attendance);
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("statuses", AttendanceStatus.values());
        return "attendance/form";
    }

    // 4. Save the attendance record
    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") Attendance attendance) {
        if (attendance.getId() == null) {
            attendance.setId(new AttendanceId());
        }
        if (attendance.getStudent() != null) {
            attendance.getId().setStudentId(attendance.getStudent().getId());
        }
        if (attendance.getCourse() != null) {
            attendance.getId().setCourseId(attendance.getCourse().getId());
        }

        attendanceService.save(attendance);
        return "redirect:/attendance";
    }

    // 5. Delete an attendance record
    @GetMapping("/delete")
    public String deleteAttendance(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("courseId") Integer courseId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        AttendanceId id = new AttendanceId(studentId, courseId, date);
        if (attendanceService.existsById(id)) {
            attendanceService.deleteById(id);
        }
        return "redirect:/attendance";
    }
}