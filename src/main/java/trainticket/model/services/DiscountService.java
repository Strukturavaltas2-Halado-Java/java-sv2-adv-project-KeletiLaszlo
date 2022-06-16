package trainticket.model.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import trainticket.dtos.DiscountDto;
import trainticket.exceptions.DiscountNotFoundException;
import trainticket.model.Discount;
import trainticket.repositories.DiscountRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class DiscountService {

    private ModelMapper modelMapper;
    private DiscountRepository repository;

    public List<DiscountDto> listAllDiscounts() {
        Type targetListType = new TypeToken<List<DiscountDto>>(){}.getType();
        return modelMapper.map(repository.findAll(), targetListType);
    }

    public DiscountDto findDiscountById(long id) {
        Discount discount = repository.findById(id).orElseThrow(
                () -> new DiscountNotFoundException(id)
        );
        return modelMapper.map(discount, DiscountDto.class);
    }

    public DiscountDto findDiscountByAge(int age) {
        return modelMapper.map(repository.findDiscountByAge(age), DiscountDto.class);
    }
}
