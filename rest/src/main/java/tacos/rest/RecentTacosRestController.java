package tacos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.OK;
import static tacos.rest.util.RestControllerUtils.createResponseEntity;

@RepositoryRestController
@RequiredArgsConstructor
public class RecentTacosRestController {
    private final RecentTacosComponent recentTacos;


    @GetMapping(path = "/tacos/recent", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<TacoResource>> recentTacos() {
        var tacos = recentTacos.recentTacos(methodOn(RecentTacosRestController.class).recentTacos());
        return createResponseEntity(tacos, OK);
    }
}
