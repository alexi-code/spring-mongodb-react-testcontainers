package reactive.ontology.server.repository;

import reactive.ontology.server.entity.Ontology;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OntologyRepository extends ReactiveMongoRepository<Ontology, String> {

    Mono<Ontology> findFirstByOntologyId(String ontologyId);

    Flux<Ontology> findAll();

}
