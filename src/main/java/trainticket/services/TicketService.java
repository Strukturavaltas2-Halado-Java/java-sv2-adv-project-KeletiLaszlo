package trainticket.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import trainticket.dtos.CreateTicketCommand;
import trainticket.dtos.TicketDto;
import trainticket.exceptions.PassengerNotFoundException;
import trainticket.exceptions.TicketNotFoundException;
import trainticket.exceptions.TrainNotFoundException;
import trainticket.model.Discount;
import trainticket.model.Passenger;
import trainticket.model.Ticket;
import trainticket.model.Train;
import trainticket.repositories.DiscountRepository;
import trainticket.repositories.PassengerRepository;
import trainticket.repositories.TicketRepository;
import trainticket.repositories.TrainRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private ModelMapper modelMapper;
    private TicketRepository ticketRepository;
    private TrainRepository trainRepository;
    private PassengerRepository passengerRepository;
    private DiscountRepository discountRepository;

    private static final int TICKET_PRICE_PER_KM = 25;

    public List<TicketDto> listAllTickets() {
        Type targetListType = new TypeToken<List<TicketDto>>() {
        }.getType();
        return modelMapper.map(ticketRepository.findAll(), targetListType);

    }

    public TicketDto findTicketById(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new TicketNotFoundException(id)
        );
        return modelMapper.map(ticket, TicketDto.class);
    }

    @Transactional
    public TicketDto buyTicket(CreateTicketCommand createTicketCommand) {
        Train train = trainRepository.findById(createTicketCommand.getTrainId()).orElseThrow(
                () -> new TrainNotFoundException(createTicketCommand.getTrainId())
        );

        Passenger passenger = passengerRepository.findById(createTicketCommand.getPassengerId()).orElseThrow(
                () -> new PassengerNotFoundException(createTicketCommand.getPassengerId())
        );

        int age = Period.between(passenger.getDateOfBirth(), LocalDate.now()).getYears();
        Discount discount = discountRepository.findDiscountByAge(age);

        int fullPrice = (int) Math.ceil(
                TICKET_PRICE_PER_KM * train.getDistance() * train.getTrainType().getPriceMultiplier()
        );
        int priceWithDiscount = (int) Math.ceil(
                fullPrice - (fullPrice * discount.getDiscountValue() / 100.0)
        );

        Ticket ticket = new Ticket(
                fullPrice,
                priceWithDiscount,
                train,
                passenger,
                discount
        );

        train.addTicket(ticket);
        passenger.addTicket(ticket);
        return modelMapper.map(ticketRepository.save(ticket), TicketDto.class);
    }

    public void deleteTicketById(long id) {
        try {
            ticketRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erdae) {
            throw new TicketNotFoundException(id);
        }
    }

    public void deleteAllTickets() {
        ticketRepository.deleteAll();
    }
}
