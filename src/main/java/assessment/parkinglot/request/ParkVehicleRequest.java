package assessment.parkinglot.request;

import assessment.parkinglot.domain.VehicleType;
import jakarta.validation.constraints.NotNull;

public class ParkVehicleRequest {

  @NotNull
  private String id;

  @NotNull
  private VehicleType type;

  protected ParkVehicleRequest() {}

  public ParkVehicleRequest(String id, VehicleType type) {
    this.id = id;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public VehicleType getType() {
    return type;
  }
}
