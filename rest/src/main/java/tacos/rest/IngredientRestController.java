package tacos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        path = "/ingredients",
        produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientRepository ingredientRepo;


    @GetMapping
    public Iterable<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }
}
