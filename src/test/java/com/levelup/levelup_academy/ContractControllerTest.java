package com.levelup.levelup_academy;

import com.levelup.levelup_academy.Controller.ContractController;
import com.levelup.levelup_academy.DTO.ContractDTO;
import com.levelup.levelup_academy.Model.Contract;
import com.levelup.levelup_academy.Model.Pro;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.ContractService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.levelup.levelup_academy.Config.JwtAuthenticationFilter;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ContractController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetAllContractsByModeratorId() throws Exception {
        Pro pro = new Pro();
        pro.setId(10);

        Contract mockContract = new Contract();
        mockContract.setId(1);
        mockContract.setTeam("Alpha Team");
        mockContract.setEmail("alpha@team.com");
        mockContract.setCommercialRegister(123456);
        mockContract.setGame("Valorant");
        mockContract.setStartDate(LocalDate.of(2025, 1, 1));
        mockContract.setEndDate(LocalDate.of(2025, 12, 31));
        mockContract.setAmount(2000.0);
        mockContract.setPro(pro);

        List<Contract> contractList = List.of(mockContract);

        Mockito.when(contractService.getAllContract(10)).thenReturn(contractList);

        User mockUser = new User();
        mockUser.setId(99);
        mockUser.setPro(pro);
        org.springframework.security.core.context.SecurityContext context =
                org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new org.springframework.security.authentication.TestingAuthenticationToken(mockUser, null));
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);

        mockMvc.perform(get("/api/v1/contract/get")
                        .principal(() -> "user")
                        .requestAttr("org.springframework.security.core.annotation.AuthenticationPrincipal", mockUser)
                        .sessionAttr("SPRING_SECURITY_CONTEXT", context))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].team").value("Alpha Team"))
                .andExpect(jsonPath("$[0].email").value("alpha@team.com"))
                .andExpect(jsonPath("$[0].commercialRegister").value(123456))
                .andExpect(jsonPath("$[0].game").value("Valorant"))
                .andExpect(jsonPath("$[0].amount").value(2000.0));
    }

    @Test
    public void testAddContract() throws Exception {


        Mockito.doNothing().when(contractService).addContract(Mockito.eq(1), Mockito.any(ContractDTO.class));


        String contractJson = """
        {
            "id": 1,
            "team": "Alpha Team",
            "email": "alpha@team.com",
            "commercialRegister": 123456,
            "game": "Valorant",
            "startDate": "2025-01-01",
            "endDate": "2025-12-31",
            "amount": 2000.0,
            "proId": 10
        }
    """;


        mockMvc.perform(post("/api/v1/contract/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contract added and email sent successfully."));
    }



}
