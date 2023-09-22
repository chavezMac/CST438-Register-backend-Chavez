package com.cst438.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	GradebookService gradebookService;

	/*
	 * list all students
	 */
	@GetMapping("/student")
	public StudentDTO[] getAllStudents() {
		System.out.println("/student called.");
		List<Student> students = (List<Student>) studentRepository.findAll();
		Student stud = null;
		
		if(students.isEmpty()) {
			return new StudentDTO[0];
		}else {
			StudentDTO[] dto = new StudentDTO[students.size()];
			
			for(int i = 0;i < students.size(); i++) {
				stud = students.get(i);
				dto[i] = createStudentDTO(stud);
			}
			return dto;
		}
	}

	/*
	 * add a student
	 */
	@PostMapping("/student/addStudent")
	public String createStudent(@RequestBody StudentDTO studentDTO) {
		Student stu = new Student();
		stu.setName(studentDTO.name());
		stu.setEmail(studentDTO.email());
		
		studentRepository.save(stu);
		return stu.getName() + " added.";
	}
	
	/*
	 * Update a student
	 */
	/*
	 * Update a student
	 */
	@PutMapping("/student/{student_email}")
	public void updateStudent(@RequestBody StudentDTO studentDTO) {
		List<Student> students = (List<Student>) studentRepository.findAll();
		Student stud = null;
		
		if(students.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
		}else {
			StudentDTO[] dto = new StudentDTO[students.size()];
			
			for(int i = 0;i < students.size(); i++) {
				stud = students.get(i);
				if(stud.getEmail().equals(studentDTO.email())) {
					Student myStudent = new Student();
					myStudent.setStudent_id(stud.getStudent_id());
					myStudent.setName(studentDTO.name());
					myStudent.setEmail(stud.getEmail());
					myStudent.setStatusCode(stud.getStatusCode());
					myStudent.setStatus(stud.getStatus());
					studentRepository.delete(stud);
					studentRepository.save(myStudent);
				}
			}
		}
		
		
	}




	/*
	 * delete a student, but give warning if student has courses enrolled. Have Optional force 
	 * parameter to force delete student even if enrolled in courses.
	 */

	@DeleteMapping("/student/{student_email}")
	public void deleteStudent(@PathVariable("student_email") String student_email, @RequestParam(value = "force",required = false) Optional<String> force) {
		Student student = studentRepository.findByEmail(student_email);
		if (student == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");
		}
		if (force.isPresent()) {
			// delete student and all enrollments
			studentRepository.delete(student);
		} else {
			//Check if student is enrolled in any classes
			List<Enrollment> enrollments = (List<Enrollment>) enrollmentRepository.findAll();

			//Loop through enrollments and check if student is enrolled in any classes
			for (int i = 0; i < enrollments.size(); i++) {
				if (enrollments.get(i).getStudent().getEmail().equals(student_email)) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is enrolled in a class.");
				}
			}
			studentRepository.delete(student);
		}
		
	}


	/*
	 * get student information based on student id
	 */
	@GetMapping("/student/{student_id}")
	@Transactional
	public Student getStudent(@PathVariable("student_id") int student_id) {
		Student student = studentRepository.findByStudent_id(student_id);
		if (student == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");
		}
		return student;
	}
	
	private StudentDTO createStudentDTO(Student s) {
		StudentDTO dto = new StudentDTO(
				s.getStudent_id(),
				s.getName(),
				s.getEmail(),
				s.getStatusCode(),
				s.getStatus());
		return dto;
	}
	
}


