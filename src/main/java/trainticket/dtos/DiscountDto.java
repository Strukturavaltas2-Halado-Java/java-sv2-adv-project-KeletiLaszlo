package trainticket.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiscountDto {

    private Long id;

    private int fromAge;

    private int toAge;

    private int discountValue;
}
