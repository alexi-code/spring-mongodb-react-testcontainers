package reactive.ontology.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactive.ontology.server.dto.OntologyDto;
import reactive.ontology.server.entity.Ontology;
import reactive.ontology.server.exception.AlreadyExistException;
import reactive.ontology.server.exception.BadRequestException;
import reactive.ontology.server.exception.BadResponseException;
import reactive.ontology.server.exception.NotFoundException;
import reactive.ontology.server.mapper.OntologyMapper;
import reactive.ontology.server.repository.OntologyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class OntologyServiceImpl implements OntologyService {

    @Value("${ols.uri}")
    private String olsUri;

    private OntologyRepository ontologyRepository;
    private WebClient webClient;

    public OntologyServiceImpl(OntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    @Autowired
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private static final String SN = "[OntologyService]";

    @Autowired
    public void setOntologyRepository(OntologyRepository ontologyRepository) {
        this.ontologyRepository = ontologyRepository;
    }

    public Mono<OntologyDto> getByOntologyId(String ontologyId) {
        log.info("{} getByOntologyId: id={}", SN, ontologyId);
        return ontologyRepository.findFirstByOntologyId(ontologyId)
            .flatMap(ontology -> {
                if(ontology != null) {
                    log.info("{} getByOntologyId: retrieved from local DB, ontology={}", SN, ontologyId);
                    return Mono.just(OntologyMapper.toDto(ontology));
                } else {
                    return Mono.empty();
                }
            })
            .switchIfEmpty(
                retrieveOntologyFromOLS(ontologyId)
            );
    }

    @SuppressWarnings("java:S5411")
    public Mono<OntologyDto> createOntology(OntologyDto ontologyDto) {
        if (ontologyDto.getOntologyId().isEmpty()) {
            return Mono.error(new BadRequestException("Not possible to create ontology without id"));
        }
        return ontologyRepository.findFirstByOntologyId(ontologyDto.getOntologyId())
            .hasElement()
            .flatMap(isPresent -> isPresent
                ? Mono.error(new AlreadyExistException(
                    "Ontology with id \"" + ontologyDto.getOntologyId() + "\" already exists"))
                : ontologyRepository.save(OntologyMapper.toOntology(ontologyDto)).map(OntologyMapper::toDto));
    }

    public Flux<String> listAllOntologyIds() {
        log.info("{} listAllOntologyIds", SN);
        return ontologyRepository.findAll().map(Ontology::getOntologyId);
    }

    public Map<String, List<String>> listAllOntologyIdsJson() {
        return Collections.singletonMap("ontologyIds", listAllOntologyIds().collectList().block());
    }

    private Mono<OntologyDto> retrieveOntologyFromOLS(String ontologyId) {
        WebClient.RequestBodySpec request = webClient
            .method(HttpMethod.GET)
            .uri(olsUri + ontologyId)
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8);

        return request
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse ->
                Mono.error(new NotFoundException("Ontology with id \"" + ontologyId + "\" not found in OLS")))
            .bodyToMono(JsonNode.class)
            .flatMap(json -> {
                try {
                    return Mono.just(olsJsonDeserializer(json));
                } catch (Exception e) {
                    Mono.error(new BadResponseException("Deserialization issue with oncologyId \"" + ontologyId + "\" in OLS response"));
                }
                return Mono.empty();
            })
            .doOnNext(i -> log.info("{} getByOntologyId: retrieved from OLS, ontologyId={}", SN, ontologyId));
    }

    private OntologyDto olsJsonDeserializer(JsonNode json) {
        OntologyDto ontologyDto = new OntologyDto();
            ontologyDto.setOntologyId(json.at("/ontologyId").asText());
            ontologyDto.setTitle(json.at("/config/title").asText());
            ontologyDto.setDescription(json.at("/config/description").asText());
            ontologyDto.setDefinitionProperties(jsonNodesToStringList(json.at("/config").get("definitionProperties").elements()));
            ontologyDto.setSynonymProperties(jsonNodesToStringList(json.at("/config").get("synonymProperties").elements()));
        return ontologyDto;
    }

    private List<String> jsonNodesToStringList(Iterator<JsonNode> jsonNodes) {
        List<String> elements = new LinkedList<>();
        jsonNodes.forEachRemaining(e -> elements.add(e.asText()));
        return elements;
    }

}
