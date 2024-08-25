package assessment.parkinglot.repositories;

import assessment.parkinglot.domain.ParkingSpot;
import assessment.parkinglot.domain.ParkingSpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

  List<ParkingSpot> findByVehicleId(String vehicleId);

  Integer countByVehicleIsNull();

  List<ParkingSpot> findByVehicleIsNullAndTypeIn(ParkingSpotType[] types);
}
