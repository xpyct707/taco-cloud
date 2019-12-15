package tacos.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tacos.Ingredient;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(DesignTacoController.class)
@MockBean(OrderRepository.class)
@MockBean(UserDetailsService.class)
class DesignTacoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private List<Ingredient> ingredients;
    private Taco design;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private TacoRepository designRepository;

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        ingredients = asList(
                new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        );

        when(ingredientRepository.findAll())
                .thenReturn(ingredients);

        when(ingredientRepository.findById("FLTO"))
                .thenReturn(of(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP)));
        when(ingredientRepository.findById("GRBF"))
                .thenReturn(of(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN)));
        when(ingredientRepository.findById("CHED"))
                .thenReturn(of(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE)));

        design = new Taco();
        design.setName("Test Taco");

        design.setIngredients(asList(
                new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE)
        ));

        when(userRepository.findByUsername("testuser"))
                .thenReturn(of(new User("testuser", "testpass", "Test User", "123 Street", "Someville", "CO", "12345", "123-123-1234")));
    }

    @Test
    @WithMockUser(username="testuser", password="testpass")
    void testShowDesignForm() throws Exception {
        mockMvc
            .perform(get("/design"))
            .andExpect(status().isOk())
            .andExpect(view().name("design"))
            .andExpect(model().attribute("wrap", ingredients.subList(0, 2)))
            .andExpect(model().attribute("protein", ingredients.subList(2, 4)))
            .andExpect(model().attribute("veggies", ingredients.subList(4, 6)))
            .andExpect(model().attribute("cheese", ingredients.subList(6, 8)))
            .andExpect(model().attribute("sauce", ingredients.subList(8, 10)));
    }

    @Test
    @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
    void processDesign() throws Exception {
        when(designRepository.save(design))
                .thenReturn(design);

        when(userRepository
                .findByUsername("testuser"))
                .thenReturn(of(new User("testuser", "testpass", "Test User", "123 Street", "Someville", "CO", "12345", "123-123-1234")));

        mockMvc
            .perform(post("/design")
                    .with(csrf())
            .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
            .contentType(APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().stringValues("Location", "/orders/current"));
    }
}