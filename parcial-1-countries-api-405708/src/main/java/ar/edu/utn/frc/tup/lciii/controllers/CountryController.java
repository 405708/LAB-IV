package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CountryController {

    @Autowired
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries(@RequestParam(required = false) String search) {
        List<CountryDto> countries = countryService.getAllCountriesWithSearch(search);
        if (countries == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/countries/{continent}")
    public ResponseEntity<List<CountryDto>> getAllCountriesInContinent(@PathVariable String continent) {
        List<CountryDto> countries = countryService.getAllCountriesInContinent(continent);
        if (countries == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/countries/language/{language}")
    public ResponseEntity<List<CountryDto>> getAllCountriesSpeakThisLanguage(@PathVariable String language) {
        List<CountryDto> countries = countryService.getAllCountriesSpeakLanguage(language);
        if (countries == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/countries/most-borders")
    public ResponseEntity<CountryDto> getMostBorderCountry() {
        CountryDto country = countryService.getCountryWithMostBorders();
        if (country == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(country);
    }

    @PostMapping("/countries")
    public ResponseEntity<List<CountryDto>> postCountries(@RequestBody PostCountryDto postCountryDto) {
        List<CountryDto> countries = countryService.postCountries(postCountryDto);
        if (countries == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countries);
    }



}