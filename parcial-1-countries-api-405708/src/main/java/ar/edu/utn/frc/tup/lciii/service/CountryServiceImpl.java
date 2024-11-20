package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryEntity;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;

    @Autowired
    private final RestClient restClient;

    @Override
    public List<CountryDto> getAllCountriesWithSearch(String search) {
        List<CountryDto> allCountries = new ArrayList<>();

        List<Country> countries = restClient.getAllCountries();

        for (Country country : countries) {
            if(search == null){
                CountryDto countryDto = mapToDTO(country);
                allCountries.add(countryDto);
            }
            else if(country.getCode().equalsIgnoreCase(search) || country.getName().equalsIgnoreCase(search)){
                CountryDto countryDto = mapToDTO(country);
                allCountries.add(countryDto);
            }
        }

        return allCountries;
    }

    @Override
    public List<CountryDto> getAllCountriesInContinent(String continent) {
        List<CountryDto> allCountries = new ArrayList<>();

        List<Country> countries = restClient.getAllCountries();

        for (Country country : countries) {
            if(country.getRegion().equalsIgnoreCase(continent)){
                CountryDto countryDto = mapToDTO(country);
                allCountries.add(countryDto);
            }
        }
        return allCountries;
    }

//    @Override
//    public List<CountryDto> getAllCountriesSpeakLanguage(String languageToObtain) {
//        List<CountryDto> allCountries = new ArrayList<>();
//
//        List<Country> countries = restClient.getAllCountries();
//
//        for (Country country : countries) {
//            System.out.println(country.getLanguages());
//
//            List<String> strings = new ArrayList<>();
//            String string = country.getLanguages().get(languageToObtain);
//            strings.add(string);
//
//            if(strings.contains(languageToObtain)){
//                    CountryDto countryDto = mapToDTO(country);
//                    allCountries.add(countryDto);
//                    strings.clear();
//            }
//        }
//        return allCountries;
//    }
@Override
public List<CountryDto> getAllCountriesSpeakLanguage(String languageToObtain) {
    List<CountryDto> allCountries = new ArrayList<>();

    // Obtener la lista de todos los países
    List<Country> countries = restClient.getAllCountries();

    // Iterar por cada país
    for (Country country : countries) {
        // Obtener el mapa de idiomas del país
        Map<String, String> languages = country.getLanguages();

        // Comprobar si alguno de los idiomas del país coincide con el idioma pasado como parámetro
        if (languages != null && languages.containsValue(languageToObtain)) {
            // Mapear el Country a CountryDto
            CountryDto countryDto = mapToDTO(country);
            // Agregar a la lista de países que hablan el idioma
            allCountries.add(countryDto);
        }
    }

    return allCountries;
}

    @Override
    public CountryDto getCountryWithMostBorders() {

        List<Country> countries = restClient.getAllCountries();

        Integer maximo = 0;
        CountryDto countryDto = null;
        for (Country country : countries) {
            if(country.getBorders().size()>maximo){
                countryDto = mapToDTO(country);
                maximo = country.getBorders().size();
            }
        }
        return countryDto;
    }

    @Override
    public List<CountryDto> postCountries(PostCountryDto country) {
        if(country.getAmountOfCountryToSave() > 10){
            return null;
        }

        List<Country> countries = restClient.getAllCountries();
        List<CountryDto> allCountries = new ArrayList<>();
        CountryEntity countryEntity;
        for (int i = 0; i < country.getAmountOfCountryToSave(); i++) {
            Country selectedCountry = countries.get(i);

            countryEntity = new CountryEntity();
            countryEntity.setName(selectedCountry.getName());
            countryEntity.setCode(selectedCountry.getCode());
            countryEntity.setPopulation(selectedCountry.getPopulation());
            countryEntity.setArea(selectedCountry.getArea());

            System.out.println(countryEntity);

            countryRepository.save(countryEntity);

            CountryDto newCountry = mapToDTO(countries.get(i));
            allCountries.add(newCountry);
        }

        return allCountries;
    }


    private CountryDto mapToDTO(Country country) {
        return new CountryDto(country.getCode(), country.getName());
    }
}
