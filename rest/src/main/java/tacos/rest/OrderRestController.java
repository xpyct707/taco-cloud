package tacos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.Order;
import tacos.data.OrderRepository;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/orders",
                produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderRepository orderRepo;


    @GetMapping
    public Iterable<Order> allOrders() {
        return orderRepo.findAll();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Order postOrder(@RequestBody Order order) {
        return orderRepo.save(order);
    }

    @PutMapping(path = "/{orderId}",
                consumes = APPLICATION_JSON_VALUE)
    public Order putOrder(@RequestBody Order order) {
        return orderRepo.save(order);
    }

    @PatchMapping(path = "/{orderId}",
                  consumes = APPLICATION_JSON_VALUE)
    public Order patchOrder(@PathVariable("orderId") Long orderId,
                            @RequestBody Order patch) {
        return orderRepo
                .findById(orderId)
                .map(this::patch)
                .map(orderRepo::save)
                .orElse(null);
    }

    private Order patch(Order order) {
        setIfNonNull(order::getName, order::setName);
        setIfNonNull(order::getStreet, order::setStreet);
        setIfNonNull(order::getCity, order::setCity);
        setIfNonNull(order::getState, order::setState);
        setIfNonNull(order::getZip, order::setZip);
        setIfNonNull(order::getCcNumber, order::setCcNumber);
        setIfNonNull(order::getCcExpiration, order::setCcExpiration);
        setIfNonNull(order::getCcCVV, order::setCcCVV);
        return order;
    }

    private void setIfNonNull(Supplier<String> getter, Consumer<String> setter) {
        ofNullable(getter.get()).ifPresent(setter);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        orderRepo.deleteById(orderId);
    }
}
