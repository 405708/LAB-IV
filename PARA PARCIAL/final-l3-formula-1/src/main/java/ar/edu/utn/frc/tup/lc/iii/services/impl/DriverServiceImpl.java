package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.models.Driver;
import ar.edu.utn.frc.tup.lc.iii.repositories.DriverRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Driver getDriverById(Long driverId) {
        return modelMapper.map(driverRepository.findById(driverId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Driver does not exist")
        ), Driver.class);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return modelMapper.map(driverRepository.findAll(), new TypeToken<List<Driver>>() {}.getType());
    }
}
