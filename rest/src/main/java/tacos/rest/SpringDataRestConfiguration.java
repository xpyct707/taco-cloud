package tacos.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import tacos.Taco;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Configuration
@EnableHypermediaSupport(type = HAL)
public class SpringDataRestConfiguration {
    @Bean
    public RepresentationModelProcessor<CollectionModel<Taco>> tacoProcessor(EntityLinks links) {
        return new TacoListProcessor(links);
    }

    private static class TacoListProcessor implements RepresentationModelProcessor<CollectionModel<Taco>> {
        private final EntityLinks links;

        public TacoListProcessor(EntityLinks links) {
            this.links = links;
        }

        @Override
        public CollectionModel<Taco> process(CollectionModel<Taco> taco) {
            taco.add(links
                    .linkFor(Taco.class)
                    .slash("recent")
                    .withRel("recents")
            );
            return taco;
        }
    }
}
