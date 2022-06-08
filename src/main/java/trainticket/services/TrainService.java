package trainticket.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import trainticket.dtos.CreateTrainCommand;
import trainticket.dtos.ModifyTrainCommand;
import trainticket.dtos.TrainDto;
import trainticket.exceptions.IllegalTimesGivenExceptions;
import trainticket.exceptions.TrainNotFoundException;
import trainticket.model.Train;
import trainticket.repositories.TrainRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainService {

    private ModelMapper modelMapper;
    private TrainRepository repository;

    public List<TrainDto> listAllTrains(Optional<String> departurePlace, Optional<String> arrivalPlace, Optional<LocalDateTime> departureTime) {
        Type targetListType = new TypeToken<List<TrainDto>>() {
        }.getType();
        return modelMapper.map(repository.findAllAndFilter(departurePlace, arrivalPlace, departureTime), targetListType);
    }

    public TrainDto findTrainById(long id) {
        return modelMapper.map(repository.findById(id).orElseThrow(
                () -> new TrainNotFoundException(id)
        ), TrainDto.class);
    }

    public void deleteTrainById(long id) {
        repository.deleteById(id);
    }

    public void deleteAllTrains() {
        repository.deleteAll();
    }

    public TrainDto createTrain(CreateTrainCommand createTrainCommand) {
        validateTimes(createTrainCommand.getDepartureTime(), createTrainCommand.getArrivalTime());
        Train train = new Train(
                createTrainCommand.getNameOfTrain(),
                createTrainCommand.getTrainType(),
                createTrainCommand.getDepartureTime(),
                createTrainCommand.getDeparturePlace(),
                createTrainCommand.getArrivalTime(),
                createTrainCommand.getArrivalPlace(),
                createTrainCommand.getDistance()
        );

        return modelMapper.map(repository.save(train), TrainDto.class);
    }

    @Transactional
    public TrainDto modifyTrain(long id, ModifyTrainCommand modifyTrainCommand) {
        validateTimes(modifyTrainCommand.getDepartureTime(), modifyTrainCommand.getArrivalTime());
        Train train = repository.findById(id).orElseThrow(
                () -> new TrainNotFoundException(id)
        );

        train.setNameOfTrain(modifyTrainCommand.getNameOfTrain());
        train.setTrainType(modifyTrainCommand.getTrainType());
        train.setDepartureTime(modifyTrainCommand.getDepartureTime());
        train.setDeparturePlace(modifyTrainCommand.getDeparturePlace());
        train.setArrivalTime(modifyTrainCommand.getArrivalTime());
        train.setArrivalPlace(modifyTrainCommand.getArrivalPlace());
        train.setDistance(modifyTrainCommand.getDistance());
        return modelMapper.map(train, TrainDto.class);
    }

    private void validateTimes(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (departureTime.isAfter(arrivalTime)) {
            throw new IllegalTimesGivenExceptions(departureTime, arrivalTime);
        }
    }
}
