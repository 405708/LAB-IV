package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleRestClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String baseResourceUrl = "https://679d36d887618946e654a137.mockapi.io/final-lciv/Vehicle/";

    public ResponseEntity<Vehicle> getVehicleById(String id) {
        // TODO: Implementar.
        Vehicle vehicle = restTemplate.getForObject(baseResourceUrl + id, Vehicle.class);
        if(vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicle);
    }
}
