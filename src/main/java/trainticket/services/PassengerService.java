package trainticket.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import trainticket.dtos.CreatePassengerCommand;
import trainticket.dtos.ModifyPassengerCommand;
import trainticket.dtos.PassengerDto;
import trainticket.exceptions.PassengerNotFoundException;
import trainticket.model.Passenger;
import trainticket.repositories.PassengerRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class PassengerService {

    private ModelMapper modelMapper;
    private PassengerRepository repository;

    public List<PassengerDto> listAllPassengers() {
        Type targetListType = new TypeToken<List<Passenger>>() {
        }.getType();
        return modelMapper.map(repository.findAll(), targetListType);
    }

    public PassengerDto findPassengerById(long id) {
        return modelMapper.map(repository.findById(id).orElseThrow(
                () -> new PassengerNotFoundException(id)
        ), PassengerDto.class);
    }

    public PassengerDto createPassenger(CreatePassengerCommand createPassengerCommand) {
        Passenger passenger = new Passenger(
                createPassengerCommand.getName(),
                createPassengerCommand.getDateOfBirth()
        );

        return modelMapper.map(repository.save(passenger), PassengerDto.class);
    }

    @Transactional
    public PassengerDto modifyPassenger(long id, ModifyPassengerCommand modifyTrainCommand) {
        Passenger passenger = repository.findById(id).orElseThrow(
                () -> new PassengerNotFoundException(id)
        );

        passenger.setName(modifyTrainCommand.getName());
        passenger.setDateOfBirth(modifyTrainCommand.getDateOfBirth());
        return modelMapper.map(passenger,PassengerDto.class);
    }

    public void deletePassengerById(long id) {
        repository.deleteById(id);
    }

    public void deleteAllPassengers() {
        repository.deleteAll();
    }
}
