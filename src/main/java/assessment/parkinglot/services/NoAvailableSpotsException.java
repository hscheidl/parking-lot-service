package assessment.parkinglot.services;

public class NoAvailableSpotsException extends Exception {

  public NoAvailableSpotsException() {
    super("No available spots");
  }
}
