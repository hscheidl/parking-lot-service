package assessment.parkinglot.domain;

public enum VehicleType {

  MOTORCYCLE(1, ParkingSpotType.MOTORCYCLE),
  CAR(1, ParkingSpotType.COMPACT, ParkingSpotType.REGULAR),
  VAN(3, ParkingSpotType.REGULAR);

  private int takeUpSpotCount;
  private ParkingSpotType[] takeUpSpotTypes;

  VehicleType(int takeUpSpotCount, ParkingSpotType... takeUpSpotTypes) {
    this.takeUpSpotCount = takeUpSpotCount;
    this.takeUpSpotTypes = takeUpSpotTypes;
  }

  public int takeUpSpots() {
    return takeUpSpotCount;
  }

  public ParkingSpotType[] getTakeUpSpotTypes() {
    return takeUpSpotTypes;
  }
}
