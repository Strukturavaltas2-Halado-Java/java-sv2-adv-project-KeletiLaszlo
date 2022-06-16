package trainticket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import trainticket.dtos.DiscountDto;
import trainticket.exceptions.IllegalAgeGivenExceptions;
import trainticket.model.services.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@AllArgsConstructor
@Tag(name = "Operations on discounts")
public class DiscountController {

    private DiscountService service;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "List all discounts",
            description = "List all discounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
    })
    public List<DiscountDto> listAllDiscounts() {
        return service.listAllDiscounts();
    }

    @GetMapping("/age")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Find discount by age",
            description = "Find discount by age.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Invalid age was given!")
    })
    public DiscountDto findDiscountByAge(@RequestParam int age) {
        if (age <0) {
            throw new IllegalAgeGivenExceptions(age);
        }

        return service.findDiscountByAge(age);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Find discount by id",
            description = "Find discount by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted"),
            @ApiResponse(responseCode = "404",
                    description = "Discount not found")
    })
    public DiscountDto findDiscountById(@PathVariable long id) {
        return service.findDiscountById(id);
    }
}
