package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import ar.edu.utn.frc.tup.lciii.service.CountryServiceImpl;
import ar.edu.utn.frc.tup.lciii.service.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @MockBean
    private RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    private Country country;
    private Country country2;
    private CountryDto countryDto;
    private CountryDto countryDto2;
    private CountryDto countryDto3;
    private List<Country> countries = new ArrayList<>();
    private List<CountryDto> countryDtos = new ArrayList<>();
    private List<CountryDto> countryDtos2 = new ArrayList<>();
    private List<CountryDto> countryInContinent = new ArrayList<>();

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setCode("US");
        country.setName("United States");
        country2 = new Country();
        country2.setCode("ARG");
        country2.setName("Argentina");

        countries.add(country);
        countries.add(country2);

        countryDto = new CountryDto("US", "United States");
        countryDto2 = new CountryDto("ARG", "Argentina");
        countryDtos.add(countryDto);
        countryDtos.add(countryDto2);

        countryDto3 = new CountryDto("FR", "France");
        countryDtos2.add(countryDto3);

        countryInContinent.add(countryDto);
        countryInContinent.add(countryDto2);
        countryInContinent.add(countryDto3);

    }

    @Test
    void getAllCountriesSuccess() throws Exception {
        when(countryService.getAllCountriesWithSearch("")).thenReturn(countryDtos);

        List<CountryDto> check = countryService.getAllCountriesWithSearch("");

        MvcResult result = mockMvc.perform(get("/api/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(countryDtos)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200,result.getResponse().getStatus());
        assertEquals(countryDtos.size(),check.size());
        assertEquals(countryDtos.get(0),check.get(0));
        assertEquals(countryDtos.get(1),check.get(1));
        verify(countryService, times(1)).getAllCountriesWithSearch("");
    }

    @Test
    void getAllCountriesSuccessWithCode() throws Exception {
        when(countryService.getAllCountriesWithSearch("FR")).thenReturn(countryDtos2);

        List<CountryDto> check = countryService.getAllCountriesWithSearch("FR");

        MvcResult result = mockMvc.perform(get("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(countryDtos2)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200,result.getResponse().getStatus());
        assertEquals(countryDtos2.size(),check.size());
        assertEquals(countryDtos2.get(0),check.get(0));
        verify(countryService, times(1)).getAllCountriesWithSearch("FR");

    }

    @Test
    void getAllCountriesSuccessWithName() throws Exception {
        when(countryService.getAllCountriesWithSearch("France")).thenReturn(countryDtos2);

        List<CountryDto> check = countryService.getAllCountriesWithSearch("France");

        MvcResult result = mockMvc.perform(get("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(countryDtos2)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200,result.getResponse().getStatus());
        assertEquals(countryDtos2.size(),check.size());
        assertEquals(countryDtos2.get(0),check.get(0));
        verify(countryService, times(1)).getAllCountriesWithSearch("France");
    }
    @Test
    void getAllCountriesWithBadName() throws Exception {
        when(countryService.getAllCountriesWithSearch("Ascochinga")).thenReturn(null);

        List<CountryDto> check = countryService.getAllCountriesWithSearch("Ascochinga");

        mockMvc.perform(get("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertNull(check);
        verify(countryService, times(1)).getAllCountriesWithSearch("Ascochinga");
    }

    @Test
    void getAllCountriesSuccessWithContinent() throws Exception {
        when(countryService.getAllCountriesInContinent("Europa")).thenReturn(countryInContinent);

        List<CountryDto> check = countryService.getAllCountriesInContinent("Europa");

        MvcResult result = mockMvc.perform(get("/api/countries/{continent}", "Europa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(countryInContinent)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200,result.getResponse().getStatus());
        assertEquals(countryInContinent.size(),check.size());
        assertEquals(countryInContinent.get(0),check.get(0));
        assertEquals(countryInContinent.get(1),check.get(1));
        assertEquals(countryInContinent.get(2),check.get(2));
        verify(countryService, times(1)).getAllCountriesInContinent("Europa");
    }

    @Test
    void getContinentCountriesNotExists() throws Exception {
        when(countryService.getAllCountriesInContinent("Ascochinga")).thenReturn(null);

        List<CountryDto> check = countryService.getAllCountriesInContinent("Ascochinga");

        mockMvc.perform(get("/api/countries/{continent}", "Ascochinga")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();


        assertNull(check);
        verify(countryService, times(1)).getAllCountriesInContinent("Ascochinga");
    }

    @Test
    void getLanguagesSuccess() throws Exception {
        when(countryService.getAllCountriesSpeakLanguage("Espanol")).thenReturn(countryInContinent);

        List<CountryDto> check = countryService.getAllCountriesSpeakLanguage("Espanol");

        MvcResult result = mockMvc.perform(get("/api/countries/language/{language}", "Espanol")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();


        assertEquals(200,result.getResponse().getStatus());
        assertEquals(countryInContinent.size(),check.size());
        assertEquals(countryInContinent.get(0),check.get(0));
        assertEquals(countryInContinent.get(1),check.get(1));
        assertEquals(countryInContinent.get(2),check.get(2));
        verify(countryService, times(1)).getAllCountriesSpeakLanguage("Espanol");
    }

    @Test
    void getCountriesSpeakLanguageNotExists() throws Exception {
        when(countryService.getAllCountriesSpeakLanguage("Ascochinga")).thenReturn(null);

        List<CountryDto> check = countryService.getAllCountriesSpeakLanguage("Ascochinga");

        mockMvc.perform(get("/api/countries/language/{language}", "Ascochinga")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();


        assertNull(check);
        verify(countryService, times(1)).getAllCountriesSpeakLanguage("Ascochinga");
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}