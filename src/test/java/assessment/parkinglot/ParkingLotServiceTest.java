package assessment.parkinglot;

import assessment.parkinglot.domain.ParkingSpot;
import assessment.parkinglot.domain.ParkingSpotType;
import assessment.parkinglot.domain.Vehicle;
import assessment.parkinglot.domain.VehicleType;
import assessment.parkinglot.factories.VehicleFactory;
import assessment.parkinglot.repositories.ParkingSpotRepository;
import assessment.parkinglot.repositories.VehicleRepository;
import assessment.parkinglot.request.ParkVehicleRequest;
import assessment.parkinglot.services.NoAvailableSpotsException;
import assessment.parkinglot.services.ParkingLotService;
import assessment.parkinglot.services.VehicleAlreadyParkedException;
import assessment.parkinglot.services.VehicleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class ParkingLotServiceTest {

  @Mock
  private VehicleFactory vehicleFactory;

  @Mock
  private ParkingSpotRepository parkingSpotRepository;

  @Mock
  private VehicleRepository vehicleRepository;

  @InjectMocks
  private ParkingLotService parkingLotService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void parkShouldThrowExceptionWhenAlreadyParked() {
    ParkVehicleRequest request = new ParkVehicleRequest("Motorcycle1", VehicleType.MOTORCYCLE);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(true);

    assertThrows(VehicleAlreadyParkedException.class, () -> parkingLotService.park(request));
  }

  @Test
  void parkShouldThrowExceptionWhenNoAvailableSpotsForMotorcycle() {
    ParkVehicleRequest request = new ParkVehicleRequest("Motorcycle1", VehicleType.MOTORCYCLE);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.MOTORCYCLE }))
        .thenReturn(List.of());

    assertThrows(NoAvailableSpotsException.class, () -> parkingLotService.park(request));
  }

  @Test
  void parkShouldThrowExceptionWhenNoAvailableSpotsForCar() {
    ParkVehicleRequest request = new ParkVehicleRequest("Car1", VehicleType.CAR);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.COMPACT, ParkingSpotType.REGULAR }))
        .thenReturn(List.of());

    assertThrows(NoAvailableSpotsException.class, () -> parkingLotService.park(request));
  }

  @Test
  void parkShouldThrowExceptionWhenNoAvailableSpotsForVan() {
    ParkVehicleRequest request = new ParkVehicleRequest("Van1", VehicleType.VAN);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.REGULAR }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(2L, ParkingSpotType.REGULAR, null)));

    assertThrows(NoAvailableSpotsException.class, () -> parkingLotService.park(request));
  }

  @Test
  void parkShouldSuccessWhenAvailableSpaceForMotorcycle() throws Exception {
    ParkVehicleRequest request = new ParkVehicleRequest("Motorcycle1", VehicleType.MOTORCYCLE);
    Vehicle vehicle = new Vehicle("Motorcycle1", VehicleType.MOTORCYCLE);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.MOTORCYCLE }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.MOTORCYCLE, null)));
    when(vehicleFactory.createVehicle(request))
        .thenReturn(vehicle);
    when(vehicleRepository.save(vehicle))
        .thenReturn(vehicle);

    parkingLotService.park(request);

    verify(vehicleRepository).save(vehicle);
    verify(parkingSpotRepository, times(1)).save(argThat((spot) -> spot.getVehicle().equals(vehicle)));
  }

  @Test
  void parkShouldSuccessWhenAvailableSpaceForCar() throws Exception {
    ParkVehicleRequest request = new ParkVehicleRequest("Car1", VehicleType.CAR);
    Vehicle vehicle = new Vehicle("Car1", VehicleType.CAR);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.COMPACT, ParkingSpotType.REGULAR }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.COMPACT, null)));
    when(vehicleFactory.createVehicle(request))
        .thenReturn(vehicle);
    when(vehicleRepository.save(vehicle))
        .thenReturn(vehicle);

    parkingLotService.park(request);

    verify(vehicleRepository).save(vehicle);
    verify(parkingSpotRepository, times(1)).save(argThat((spot) -> spot.getVehicle().equals(vehicle)));
  }

  @Test
  void parkShouldSuccessWhenAvailableSpaceForVan() throws Exception {
    ParkVehicleRequest request = new ParkVehicleRequest("Van1", VehicleType.VAN);
    Vehicle vehicle = new Vehicle("Van1", VehicleType.VAN);

    when(vehicleRepository.existsById(request.getId()))
        .thenReturn(false);
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.REGULAR }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(2L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(3L, ParkingSpotType.REGULAR, null)));
    when(vehicleFactory.createVehicle(request))
        .thenReturn(vehicle);
    when(vehicleRepository.save(vehicle))
        .thenReturn(vehicle);

    parkingLotService.park(request);

    verify(vehicleRepository).save(vehicle);
    verify(parkingSpotRepository, times(3)).save(argThat((spot) -> spot.getVehicle().equals(vehicle)));
  }

  @Test
  void leaveShouldThrowExceptionWhenNotFound() {
    when(vehicleRepository.existsById("Motorcycle1"))
        .thenReturn(false);

    assertThrows(VehicleNotFoundException.class, () -> parkingLotService.leave("Motorcycle1"));
  }

  @Test
  void leaveShouldSuccessWhenFoundForMotorcycle() throws Exception {
    String vehicleId = "Motorcycle1";

    when(vehicleRepository.existsById(vehicleId))
        .thenReturn(true);
    when(parkingSpotRepository.findByVehicleId(vehicleId))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.MOTORCYCLE, new Vehicle(vehicleId, VehicleType.MOTORCYCLE))));

    parkingLotService.leave(vehicleId);

    verify(vehicleRepository).deleteById(vehicleId);
    verify(parkingSpotRepository, times(1)).save(argThat((spot) -> spot.getVehicle() == null));
  }

  @Test
  void leaveShouldSuccessWhenFoundForCar() throws Exception {
    String vehicleId = "Car1";

    when(vehicleRepository.existsById(vehicleId))
        .thenReturn(true);
    when(parkingSpotRepository.findByVehicleId(vehicleId))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, new Vehicle(vehicleId, VehicleType.CAR))));

    parkingLotService.leave(vehicleId);

    verify(vehicleRepository).deleteById(vehicleId);
    verify(parkingSpotRepository, times(1)).save(argThat((spot) -> spot.getVehicle() == null));
  }

  @Test
  void leaveShouldSuccessWhenFoundForVan() throws Exception {
    String vehicleId = "Van1";
    Vehicle vehicle = new Vehicle(vehicleId, VehicleType.VAN);

    when(vehicleRepository.existsById(vehicleId))
        .thenReturn(true);
    when(parkingSpotRepository.findByVehicleId(vehicleId))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, vehicle),
            new ParkingSpot(2L, ParkingSpotType.REGULAR, vehicle),
            new ParkingSpot(3L, ParkingSpotType.REGULAR, vehicle)));

    parkingLotService.leave(vehicleId);

    verify(vehicleRepository).deleteById(vehicleId);
    verify(parkingSpotRepository, times(3)).save(argThat((spot) -> spot.getVehicle() == null));
  }

  @Test
  void getRemainingSpotsShouldReturnAvailableSpotsCount() {
    int remainingSpots = 1;

    when(parkingSpotRepository.countByVehicleIsNull())
        .thenReturn(remainingSpots);

    assertEquals(remainingSpots, parkingLotService.getRemainingSpots());
  }

  @Test
  void isFullShouldReturnTrueWhenNoAvailableSpace() {
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.REGULAR }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(2L, ParkingSpotType.REGULAR, null)));

    assertEquals(true, parkingLotService.isFull(VehicleType.VAN));
  }

  @Test
  void isFullShouldReturnFalseWhenAvailableSpace() {
    when(parkingSpotRepository.findByVehicleIsNullAndTypeIn(new ParkingSpotType[] { ParkingSpotType.REGULAR }))
        .thenReturn(List.of(
            new ParkingSpot(1L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(2L, ParkingSpotType.REGULAR, null),
            new ParkingSpot(3L, ParkingSpotType.REGULAR, null)));

    assertEquals(false, parkingLotService.isFull(VehicleType.VAN));
  }
}
