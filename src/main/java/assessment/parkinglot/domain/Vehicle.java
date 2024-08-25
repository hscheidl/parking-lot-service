package assessment.parkinglot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private VehicleType type;

  protected Vehicle() {}

  public Vehicle(String id, VehicleType type) {
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
