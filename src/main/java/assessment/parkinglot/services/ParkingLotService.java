package assessment.parkinglot.services;

import assessment.parkinglot.domain.ParkingSpot;
import assessment.parkinglot.domain.Vehicle;
import assessment.parkinglot.domain.VehicleType;
import assessment.parkinglot.factories.VehicleFactory;
import assessment.parkinglot.repositories.ParkingSpotRepository;
import assessment.parkinglot.repositories.VehicleRepository;
import assessment.parkinglot.request.ParkVehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingLotService {

  @Autowired
  private VehicleFactory vehicleFactory;

  @Autowired
  private ParkingSpotRepository parkingSpotRepository;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Transactional
  public void park(ParkVehicleRequest request) throws VehicleAlreadyParkedException, NoAvailableSpotsException {
    if (vehicleRepository.existsById(request.getId())) {
      throw new VehicleAlreadyParkedException(request.getId());
    }
    VehicleType vehicleType = request.getType();
    List<ParkingSpot> availableSpots = findAvailableSpots(vehicleType);
    if (availableSpots.size() < vehicleType.takeUpSpots()) {
      throw new NoAvailableSpotsException();
    }
    Vehicle vehicle = vehicleRepository.save(vehicleFactory.createVehicle(request));
    availableSpots.stream().limit(vehicleType.takeUpSpots()).forEach(spot -> {
      spot.setVehicle(vehicle);
      parkingSpotRepository.save(spot);
    });
  }

  @Transactional
  public void leave(String vehicleId) throws VehicleNotFoundException {
    if (!vehicleRepository.existsById(vehicleId)) {
      throw new VehicleNotFoundException(vehicleId);
    }
    parkingSpotRepository.findByVehicleId(vehicleId).forEach(spot -> {
      spot.setVehicle(null);
      parkingSpotRepository.save(spot);
    });
    vehicleRepository.deleteById(vehicleId);
  }

  public Integer getRemainingSpots() {
    return parkingSpotRepository.countByVehicleIsNull();
  }

  public Boolean isFull(VehicleType vehicleType) {
    return findAvailableSpots(vehicleType).size() < vehicleType.takeUpSpots();
  }

  private List<ParkingSpot> findAvailableSpots(VehicleType vehicleType) {
    return parkingSpotRepository.findByVehicleIsNullAndTypeIn(vehicleType.getTakeUpSpotTypes());
  }
}
