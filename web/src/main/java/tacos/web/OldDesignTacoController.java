package tacos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor
public class OldDesignTacoController {
    private final IngredientRepository ingredientRepo;
    private final TacoRepository designRepo;


    @ModelAttribute
    public Order order() {
        return new Order();
    }

    @ModelAttribute
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        Map<Type, List<Ingredient>> ingredientsByType = groupByType(ingredientRepo.findAll());
        Stream.of(Ingredient.Type.values())
                .forEach(type -> addToModel(model, type, ingredientsByType));
        return "design";
    }

    private Map<Type, List<Ingredient>> groupByType(Iterable<Ingredient> ingredients) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .collect(groupingBy(
                        Ingredient::getType,
                        () -> new EnumMap<>(Type.class),
                        toList()));
    }

    private void addToModel(Model model, Ingredient.Type type, Map<Type, List<Ingredient>> ingredientsByType) {
        model.addAttribute(type.getLowerCaseName(), ingredientsByType.get(type));
    }

    @PostMapping
    public String processDesign(@Valid Taco taco,
                                Errors errors,
                                @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        log.info("Processing design: " + taco);
        order.addTaco(designRepo.save(taco));
        return "redirect:/orders/current";
    }
}
