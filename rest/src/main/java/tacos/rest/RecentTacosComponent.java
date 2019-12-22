package tacos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;
import tacos.data.TacoRepository;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
@RequiredArgsConstructor
public class RecentTacosComponent {
    private final TacoRepository tacoRepo;


    public CollectionModel<TacoResource> recentTacos(Object invocationValue) {
        var page = of(0, 12, by("createdAt").descending());
        var tacos = tacoRepo
                .findAll(page)
                .getContent();
        var tacoResources = new TacoResourceAssembler().toCollectionModel(tacos);
        var link = linkTo(invocationValue).withRel("recents");
        tacoResources.add(link);
        return tacoResources;
    }
}
