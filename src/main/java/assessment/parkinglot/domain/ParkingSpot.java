package assessment.parkinglot.domain;

import jakarta.persistence.*;

@Entity
public class ParkingSpot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ParkingSpotType type;

  @ManyToOne
  private Vehicle vehicle;

  protected ParkingSpot() {}

  public ParkingSpot(Long id, ParkingSpotType type, Vehicle vehicle) {
    this.id = id;
    this.type = type;
    this.vehicle = vehicle;
  }

  public Long getId() {
    return id;
  }

  public ParkingSpotType getType() {
    return type;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }
}
