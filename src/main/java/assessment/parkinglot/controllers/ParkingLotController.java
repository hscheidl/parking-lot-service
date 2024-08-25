package assessment.parkinglot.controllers;

import assessment.parkinglot.domain.VehicleType;
import assessment.parkinglot.request.ParkVehicleRequest;
import assessment.parkinglot.services.NoAvailableSpotsException;
import assessment.parkinglot.services.ParkingLotService;
import assessment.parkinglot.services.VehicleAlreadyParkedException;
import assessment.parkinglot.services.VehicleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("parking-lot")
public class ParkingLotController {

  @Autowired
  private ParkingLotService parkingLotService;

  @PostMapping("park")
  public void park(@Validated @RequestBody ParkVehicleRequest request) {
    try {
      parkingLotService.park(request);
    } catch (VehicleAlreadyParkedException | NoAvailableSpotsException e) {
      throw new ResponseStatusException(CONFLICT, e.getMessage());
    }
  }

  @PostMapping("leave/{vehicleId}")
  public void leave(@PathVariable String vehicleId) {
    try {
      parkingLotService.leave(vehicleId);
    } catch (VehicleNotFoundException e) {
      throw new ResponseStatusException(NOT_FOUND, e.getMessage());
    }
  }

  @GetMapping("remaining-spots")
  public Integer getRemainingSpots() {
    return parkingLotService.getRemainingSpots();
  }

  @GetMapping("{type}/is-full")
  public Boolean isFull(@PathVariable VehicleType type) {
    return parkingLotService.isFull(type);
  }
}
