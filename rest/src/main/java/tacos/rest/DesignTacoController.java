package tacos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Taco;
import tacos.data.TacoRepository;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(
        path = "/design",
        produces = "application/json")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DesignTacoController {
    private final TacoRepository tacoRepo;
    private final EntityLinks entityLinks;


    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        return tacoRepo
                .findAll(of(0, 12, by("createdAt").descending()))
                .getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        return tacoRepo
                .findById(id)
                .map(taco -> createResponseEntity(taco, OK))
                .orElseGet(() -> createResponseEntity(null, NOT_FOUND));
    }

    private ResponseEntity<Taco> createResponseEntity(Taco taco, HttpStatus ok) {
        return new ResponseEntity<>(taco, ok);
    }
}
