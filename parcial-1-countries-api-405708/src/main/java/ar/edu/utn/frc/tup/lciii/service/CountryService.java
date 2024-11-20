package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface CountryService {

        List<CountryDto> getAllCountriesWithSearch(String search);
        List<CountryDto> getAllCountriesInContinent(String continent);
        List<CountryDto> getAllCountriesSpeakLanguage(String language);
        CountryDto getCountryWithMostBorders();
        List<CountryDto> postCountries(PostCountryDto country);
}