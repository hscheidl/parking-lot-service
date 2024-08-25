package assessment.parkinglot.factories;

import assessment.parkinglot.domain.Vehicle;
import assessment.parkinglot.request.ParkVehicleRequest;
import org.springframework.stereotype.Component;

@Component
public class VehicleFactory {

  public Vehicle createVehicle(ParkVehicleRequest request) {
    return new Vehicle(request.getId(), request.getType());
  }
}
