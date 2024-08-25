package assessment.parkinglot;

import assessment.parkinglot.controllers.ParkingLotController;
import assessment.parkinglot.services.NoAvailableSpotsException;
import assessment.parkinglot.services.ParkingLotService;
import assessment.parkinglot.services.VehicleAlreadyParkedException;
import assessment.parkinglot.services.VehicleNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingLotController.class)
public class ParkingLotControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ParkingLotService parkingLotService;

  @Test
  void parkShouldReturnBadRequestStatusCodeWhenMissingId() throws Exception {
    String body = """
        {
          "type": "MOTORCYCLE"
        }
        """;

    this.mockMvc.perform(
        post("/parking-lot/park")
            .contentType(APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void parkShouldReturnBadRequestStatusCodeWhenMissingType() throws Exception {
    String body = """
        {
          "id": "Motorcycle1"
        }
        """;

    this.mockMvc.perform(
            post("/parking-lot/park")
                .contentType(APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void parkShouldReturnConflictStatusCodeWhenVehicleAlreadyParked() throws Exception {
    doThrow(VehicleAlreadyParkedException.class)
        .when(parkingLotService)
        .park(any());

    String body = """
        {
          "id": "Motorcycle1",
          "type": "MOTORCYCLE"
        }
        """;

    this.mockMvc.perform(
        post("/parking-lot/park")
            .contentType(APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict());
  }

  @Test
  void parkShouldReturnConflictStatusCodeWhenNoAvailableSpots() throws Exception {
    doThrow(NoAvailableSpotsException.class)
        .when(parkingLotService)
        .park(any());

    String body = """
        {
          "id": "Motorcycle1",
          "type": "MOTORCYCLE"
        }
        """;

    this.mockMvc.perform(
        post("/parking-lot/park")
            .contentType(APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict());
  }

  @Test
  void parkShouldReturnOkWhenSuccess() throws Exception {
    String body = """
        {
          "id": "Motorcycle1",
          "type": "MOTORCYCLE"
        }
        """;

    this.mockMvc.perform(
            post("/parking-lot/park")
                .contentType(APPLICATION_JSON)
                .content(body))
        .andExpect(status().isOk());
  }

  @Test
  void leaveShouldReturnNotFoundStatusCodeWhenVehicleNotFound() throws Exception {
    doThrow(VehicleNotFoundException.class)
        .when(parkingLotService)
        .leave(any());

    this.mockMvc.perform(
        post("/parking-lot/leave/Motorcycle1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void leaveShouldReturnOkWhenSuccess() throws Exception {
    this.mockMvc.perform(
        post("/parking-lot/leave/Motorcycle1"))
        .andExpect(status().isOk());
  }

  @Test
  void remainingSpotsShouldReturnInteger() throws Exception {
    Integer result = 5;

    when(parkingLotService.getRemainingSpots()).
        thenReturn(result);

    this.mockMvc.perform(
        get("/parking-lot/remaining-spots"))
        .andExpect(status().isOk())
        .andExpect(content().string(result.toString()));
  }

  @Test
  void isFullShouldReturnTrueWhenIsFull() throws Exception {
    when(parkingLotService.isFull(any())).thenReturn(true);

    this.mockMvc.perform(
            get("/parking-lot/MOTORCYCLE/is-full"))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  void isFullShouldReturnFalseWhenIsNotFull() throws Exception {
    when(parkingLotService.isFull(any())).thenReturn(false);

    this.mockMvc.perform(
            get("/parking-lot/MOTORCYCLE/is-full"))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }
}
