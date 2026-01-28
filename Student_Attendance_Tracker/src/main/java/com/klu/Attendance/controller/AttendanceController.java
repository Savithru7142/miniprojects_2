package com.klu.Attendance.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    private final List<Attendance> attendanceList = new ArrayList<>();
    private int idCounter = 100;

    // Constructor – sample data
    public AttendanceController() {
        attendanceList.add(new Attendance(32427, "Sharvan", "CSE-HTE", 98.0, LocalDateTime.now().toString()));
        attendanceList.add(new Attendance(32309, "Abhi", "CSE-HTR", 90.0, LocalDateTime.now().toString()));
        idCounter = 102;
    }

    // GET ALL
    @GetMapping("/attendance")
    public List<Attendance> getAllAttendance() {
        return attendanceList;
    }

    // GET BY ID
    @GetMapping("/attendance/{id}")
    public Attendance getById(@PathVariable int id) {
        Attendance a = findById(id);
        return a;
    }

    // CREATE
    @PostMapping("/attendance")
    public String addAttendance(@RequestBody Attendance newAttendance) {
        if (findById(newAttendance.id) != null) {
            return "Attendance record already exists";
        }
        newAttendance.id = ++idCounter;
        newAttendance.lastUpdated = LocalDateTime.now().toString();
        attendanceList.add(newAttendance);
        return "Attendance record added successfully with id " + newAttendance.id;
    }

    // UPDATE
    @PutMapping("/attendance/{id}")
    public String updateAttendance(@PathVariable int id,
                                   @RequestBody Attendance updatedAttendance) {
        Attendance existing = findById(id);
        if (existing == null) {
            return "Record not found";
        }
        existing.name = updatedAttendance.name;
        existing.section = updatedAttendance.section;
        existing.attendancePercent = updatedAttendance.attendancePercent;
        existing.lastUpdated = LocalDateTime.now().toString();
        return "Attendance updated successfully for id " + id;
    }

    // DELETE
    @DeleteMapping("/attendance/{id}")
    public String deleteById(@PathVariable int id) {
        Attendance a = findById(id);
        if (a == null) {
            return "Attendance record not found";
        }
        attendanceList.remove(a);
        return "Attendance record deleted successfully : " + id;
    }

    // SEARCH (name contains / section)
    @GetMapping("/attendance/search")
    public List<Attendance> searchAttendance(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String section) {

        List<Attendance> result = new ArrayList<>();

        for (Attendance a : attendanceList) {
            if ((name == null || a.name.toLowerCase().contains(name.toLowerCase())) &&
                (section == null || a.section.equalsIgnoreCase(section))) {
                result.add(a);
            }
        }
        return result;
    }

    // FIND BY ID
    private Attendance findById(int id) {
        for (Attendance a : attendanceList) {
            if (a.id == id)
                return a;
        }
        return null;
    }
}

/* ================= POJO CLASS ================= */

class Attendance {
    public int id;
    public String name;
    public String section;
    public double attendancePercent;
    public String lastUpdated;

    public Attendance() {}

    public Attendance(int id, String name, String section,
                      double attendancePercent, String lastUpdated) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.attendancePercent = attendancePercent;
        this.lastUpdated = lastUpdated;
    }
}
