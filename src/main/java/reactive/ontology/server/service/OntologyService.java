package reactive.ontology.server.service;

import reactive.ontology.server.dto.OntologyDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface OntologyService {

    Mono<OntologyDto> getByOntologyId(String ontologyId);

    Mono<OntologyDto> createOntology(OntologyDto ontologyDto);

    Flux<OntologyDto> listAllOntologies();

    Map<String, List<String>> listAllOntologyIdsJson();

}
