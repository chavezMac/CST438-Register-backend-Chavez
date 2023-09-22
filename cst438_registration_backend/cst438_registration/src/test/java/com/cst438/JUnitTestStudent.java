package com.cst438;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class JUnitTestStudent {
	@Autowired
	private MockMvc mvc;

	/*
	 * JUnit for Adding a new student mac, mactest@test.com
	 */
	@Test
	public void addStudent() throws Exception {

		MockHttpServletResponse response;

		response = mvc.perform(
			MockMvcRequestBuilders
				.post("/student/addStudent")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		
		assertEquals(200,response.getStatus());

		Student result = fromJsonString(response.getContentAsString(),Student.class);
		assertEquals("mac",result.getName());
	}

	/*
	 * JUnit for Deleting a student with id 1
	 */
	@Test
	public void deleteStudent() throws Exception {

		MockHttpServletResponse response;

		response = mvc.perform(
			MockMvcRequestBuilders
				.get("/student/mac@mac.edu")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		
		//Verify student exists
		Student result = fromJsonString(response.getContentAsString(),Student.class);
		assertEquals("mac",result.getName());
		
		//drop student
		response = mvc.perform(
			MockMvcRequestBuilders
				.delete("/student/1"))
			.andReturn().getResponse();
		
		//Verify return status
		assertEquals(200,response.getStatus());

		response = mvc.perform(
			MockMvcRequestBuilders
				.get("/student/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		
		//Verify student does not exist
		result = fromJsonString(response.getContentAsString(),Student.class);
		assertNotEquals(1,result.getStudent_id());
	}
	
	/*
	 * JUnit for Getting a student with id 1
	 */

	@Test
	public void getStudent() throws Exception {

		MockHttpServletResponse response;

		response = mvc.perform(
			MockMvcRequestBuilders
				.get("/student/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		
		//Verify student exists
		Student result = fromJsonString(response.getContentAsString(),Student.class);
		assertEquals(1,result.getStudent_id());
	}

	/*
	 * JUnit test for updating student with student id 2
	 */
	@Test
	public void updateStudent() throws Exception {

		MockHttpServletResponse response;

		response = mvc.perform(
			MockMvcRequestBuilders
				.put("/student/2")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		
		//Verify student exists
		Student result = fromJsonString(response.getContentAsString(),Student.class);
		assertEquals("mac",result.getName());
		
		//Update student
		StudentDTO studentDTO = new StudentDTO(11111, "mac", "mac@mac.edu", 0, "");
		response = mvc.perform(
			MockMvcRequestBuilders
				.put("/student/11111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(studentDTO))
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		//Verify return status
		assertEquals(200,response.getStatus());

		response = mvc.perform(
			MockMvcRequestBuilders
				.get("/student/11111")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		//Verify student updated
		result = fromJsonString(response.getContentAsString(),Student.class);
		assertEquals("mac",result.getName());
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




