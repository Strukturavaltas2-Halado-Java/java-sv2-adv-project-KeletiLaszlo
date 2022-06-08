package trainticket.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import trainticket.dtos.DiscountDto;
import trainticket.model.Discount;
import trainticket.repositories.DiscountRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
//@AllArgsConstructor
public class DiscountService {

    private ModelMapper modelMapper;
    private DiscountRepository repository;

    public DiscountService(ModelMapper modelMapper, DiscountRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;

        //TODO put it all to flyway migration
        repository.save(new Discount(0,6,100));
        repository.save(new Discount(6,14,50));
        repository.save(new Discount(14,26,33));
        repository.save(new Discount(26,65,0));
        repository.save(new Discount(65,999,100));
    }

    public List<DiscountDto> listAllDiscounts() {
        Type targetListType = new TypeToken<List<DiscountDto>>(){}.getType();
        return modelMapper.map(repository.findAll(), targetListType);
    }

    public DiscountDto findDiscountById(long id) {
        return modelMapper.map(repository.findById(id), DiscountDto.class);
    }

    public DiscountDto findDiscountByAge(int age) {
        return modelMapper.map(repository.findDiscountByAge(age), DiscountDto.class);
    }
}
