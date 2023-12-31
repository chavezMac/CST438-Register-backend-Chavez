package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Student;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "mq")
public class GradebookServiceMQ implements GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	Queue gradebookQueue = new Queue("gradebook-queue", true);
	
	@Bean
	Queue createQueue() {
		return new Queue("registration-queue");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		System.out.println("Start Message "+ student_email +" " + course_id); 
		// create EnrollmentDTO, convert to JSON string and send to gradebookQueue
		// TODO
		
		Student s = studentRepository.findByEmail(student_email);
		Course c = courseRepository.findById(course_id).orElse(null);
		
		Enrollment e = new Enrollment();
		e.setCourse(c);
		e.setStudent(s);
		
		EnrollmentDTO dto = createEnrollmentDTO(e);
		
		String jsonEnrollmentDTO = asJsonString(dto);

        // Send the JSON message to the RabbitMQ exchange
        rabbitTemplate.convertAndSend(gradebookQueue.getName(), jsonEnrollmentDTO);
		
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(String message) {
		System.out.println("Receive grades :" + message);
		/*
		 * for each student grade in courseDTOG,  find the student enrollment 
		 * entity and update the grade.
		 */
		
		// deserialize the string message to FinalGradeDTO[] 
		FinalGradeDTO[] received = fromJsonString(message,FinalGradeDTO[].class);
		
		for (FinalGradeDTO grade : received) {
	        Enrollment e = enrollmentRepository.findByEmailAndCourseId(grade.studentEmail(), grade.courseId());
	        if (e != null) {
	            e.setCourseGrade(grade.grade());
	            enrollmentRepository.save(e);
	        }
	    }
		// TODO

	}
	
	private EnrollmentDTO createEnrollmentDTO(Enrollment e) {
		EnrollmentDTO dto = new EnrollmentDTO(
			e.getEnrollment_id(),
			e.getStudent().getEmail(),
			e.getStudent().getName(),
			e.getCourse().getCourse_id()
		);
		return dto;
	}
	
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
