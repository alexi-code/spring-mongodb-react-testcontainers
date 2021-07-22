package reactive.ontology.server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import reactive.ontology.server.dto.OntologyDto;
import reactive.ontology.server.service.OntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/ontology")
public class OntologyController {

    private OntologyService ontologyService;

    @Autowired
    public void setOntologyService(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    @GetMapping("/{ontologyId}")
    public Mono<OntologyDto> getByOntologyId(@PathVariable String ontologyId) {
        return ontologyService.getByOntologyId(ontologyId);
    }

    @PostMapping()
    public ResponseEntity<Mono<OntologyDto>> create(@RequestBody OntologyDto ontologyDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ontologyService.createOntology(ontologyDto));
    }

    @GetMapping("/detailed")
    public ResponseEntity<Flux<OntologyDto>> listOntologies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ontologyService.listAllOntologies());
    }

    @GetMapping()
    public ResponseEntity<Map<String, List<String>>> listOntologyIds() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ontologyService.listAllOntologyIdsJson());
    }

}
