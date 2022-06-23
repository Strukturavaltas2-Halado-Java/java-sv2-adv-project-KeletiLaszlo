package trainticket.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import trainticket.dtos.CreateTrainCommand;
import trainticket.dtos.CreateTrainPeriodicalCommand;
import trainticket.dtos.ModifyTrainCommand;
import trainticket.dtos.TrainDto;
import trainticket.exceptions.IllegalTimesGivenExceptions;
import trainticket.exceptions.InvalidPeriodTimeGivenException;
import trainticket.exceptions.TrainConstraintFailsException;
import trainticket.exceptions.TrainNotFoundException;
import trainticket.model.Train;
import trainticket.repositories.TrainRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException erdae) {
            throw new TrainNotFoundException(id);
        } catch (DataIntegrityViolationException dive) {
            throw new TrainConstraintFailsException(id);
        }
    }

    public void deleteAllTrains() {
        try {
            repository.deleteAll();
        } catch (DataIntegrityViolationException dive) {
            throw new TrainConstraintFailsException();
        }
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

    public List<TrainDto> createTrainPeriodical(CreateTrainPeriodicalCommand createTrainPeriodicalCommand) {
        validateTimes(createTrainPeriodicalCommand.getDepartureTime(), createTrainPeriodicalCommand.getArrivalTime());
        int plusHours = createTrainPeriodicalCommand.getPlusHours();
        int plusMinutes = createTrainPeriodicalCommand.getPlusMinutes();
        int period = createTrainPeriodicalCommand.getNumberOfPeriods();

        LocalDateTime startTime = createTrainPeriodicalCommand.getDepartureTime();
        LocalDateTime endTime = createTrainPeriodicalCommand.getArrivalTime();

        if (plusHours == 0 && plusMinutes == 0) {
            throw new InvalidPeriodTimeGivenException(plusHours, plusMinutes);
        }

        Type targetListType = new TypeToken<List<TrainDto>>(){}.getType();
        List<Train> newTrains = new ArrayList<>();
        for (int i = 0; i < period; i++) {
            Train train = new Train(
                    createTrainPeriodicalCommand.getNameOfTrain(),
                    createTrainPeriodicalCommand.getTrainType(),
                    startTime,
                    createTrainPeriodicalCommand.getDeparturePlace(),
                    endTime,
                    createTrainPeriodicalCommand.getArrivalPlace(),
                    createTrainPeriodicalCommand.getDistance()
            );
            startTime = startTime.plusHours(plusHours).plusMinutes(plusMinutes);
            endTime = endTime.plusHours(plusHours).plusMinutes(plusMinutes);
            newTrains.add(repository.save(train));
        }
        return modelMapper.map(newTrains, targetListType);
    }
}
