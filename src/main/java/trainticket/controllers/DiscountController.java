package trainticket.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.DiscountDto;
import trainticket.services.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@AllArgsConstructor
public class DiscountController {

    private DiscountService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<DiscountDto> listAllDiscounts() {
        return service.listAllDiscounts();
    }

    @GetMapping("/age")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DiscountDto findDiscountByAge(@RequestParam int age) {
        return service.findDiscountByAge(age);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DiscountDto findDiscountById(@PathVariable long id) {
        return service.findDiscountById(id);
    }
}
