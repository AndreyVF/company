package com.company.api;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeStateDto;
import com.company.dto.IdDto;
import com.company.enums.EmployeeState;
import com.company.utils.EmployeeTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void initSetup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testUpdateEmployeeStatus() throws Exception {
        //create employee
        MvcResult result = this.mockMvc.perform(
                post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EmployeeTestUtils.buildEmployeeDto())))
            .andExpect(status().isCreated()).andReturn();
        IdDto idDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class);
        assertNotNull(idDto.getId());

        //checking state
        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        EmployeeDetailsDto employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.ADDED), employeeDetailsDto.getStates());

        //update state ADDED -> IN_CHECK
        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.IN_CHECK.name()))))
            .andExpect(status().isNoContent());
        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.IN_CHECK, EmployeeState.SECURITY_CHECK_STARTED, EmployeeState.WORK_PERMIT_CHECK_STARTED), employeeDetailsDto.getStates());

        //update state SECURITY_CHECK_STARTED -> SECURITY_CHECK_FINISHED
        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.SECURITY_CHECK_FINISHED.name()))))
            .andExpect(status().isNoContent());
        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.IN_CHECK, EmployeeState.SECURITY_CHECK_FINISHED, EmployeeState.WORK_PERMIT_CHECK_STARTED), employeeDetailsDto.getStates());

        //update state WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_FINISHED
        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.WORK_PERMIT_CHECK_FINISHED.name()))))
            .andExpect(status().isNoContent());
        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.APPROVED), employeeDetailsDto.getStates());

        //update state WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_FINISHED
        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.ACTIVE.name()))))
            .andExpect(status().isNoContent());
        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.ACTIVE), employeeDetailsDto.getStates());
    }

    @Test
    void testEmployeeStateUpdateWithInternalEmployeeState() throws Exception {
        //create employee
        MvcResult result = this.mockMvc.perform(
                post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EmployeeTestUtils.buildEmployeeDto())))
            .andExpect(status().isCreated()).andReturn();
        IdDto idDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class);
        assertNotNull(idDto.getId());

        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.IN_CHECK_DONE.name()))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEmployeeStateUpdateWithNotSupportedValue() throws Exception {
        //create employee
        MvcResult result = this.mockMvc.perform(
                post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EmployeeTestUtils.buildEmployeeDto())))
            .andExpect(status().isCreated()).andReturn();
        IdDto idDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class);
        assertNotNull(idDto.getId());

        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto("something"))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEmployeeStateUpdateWithWrongTransitionState() throws Exception {
        //create employee
        MvcResult result = this.mockMvc.perform(
                post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EmployeeTestUtils.buildEmployeeDto())))
            .andExpect(status().isCreated()).andReturn();
        IdDto idDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class);
        assertNotNull(idDto.getId());

        this.mockMvc.perform(
                put("/api/employees/" + idDto.getId() + "/updateState")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EmployeeStateDto(EmployeeState.ACTIVE.name()))))
            .andExpect(status().isNoContent());

        result = this.mockMvc.perform(
                get("/api/employees/" + idDto.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        EmployeeDetailsDto employeeDetailsDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeDetailsDto.class);
        assertEquals(Set.of(EmployeeState.ADDED), employeeDetailsDto.getStates());
    }
}
