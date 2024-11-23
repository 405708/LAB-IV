package ar.edu.utn.frc.tup.lc.iii.services;

import ar.edu.utn.frc.tup.lc.iii.models.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {

    Driver getDriverById(Long driverId);

    List<Driver> getAllDrivers();
}
