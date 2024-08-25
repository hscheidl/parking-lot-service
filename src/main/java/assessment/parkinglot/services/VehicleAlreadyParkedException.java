package assessment.parkinglot.services;

import static java.lang.String.format;

public class VehicleAlreadyParkedException extends Exception {

  public VehicleAlreadyParkedException(String vehicleId) {
    super(format("Vehicle %s is already parked", vehicleId));
  }
}
