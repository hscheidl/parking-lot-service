package assessment.parkinglot.services;

import static java.lang.String.format;

public class VehicleNotFoundException extends Exception {

  public VehicleNotFoundException(String vehicleId) {
    super(format("Vehicle %s not found", vehicleId));
  }
}
