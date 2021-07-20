package reactive.oncology.server;

import reactive.ontology.server.ReactiveOncologyServerApplication;
import reactive.ontology.server.controller.OntologyController;
import reactive.ontology.server.dto.OntologyDto;
import reactive.ontology.server.entity.Ontology;
import reactive.ontology.server.mapper.OntologyMapper;
import reactive.ontology.server.repository.OntologyRepository;
import reactive.ontology.server.service.OntologyService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;

@TestPropertySource("classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveOncologyServerApplication.class)
@Log4j2
class OntologyTest {

    @ClassRule
    @Container
    final static MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo"))
        .withReuse(true)
        .withEnv("MONGO_INITDB_DATABASE", "test");

    @Autowired
    OntologyRepository ontologyRepository;

    @Autowired
    OntologyService ontologyService;

    @Autowired
    OntologyController ontologyController;

    @Autowired
    WebTestClient webTestClient;

    private static final String API_ROOT = "/api/ontology";

    @DynamicPropertySource
    @SneakyThrows
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @AfterEach
    void cleanUp() {
        this.ontologyRepository.deleteAll().block();
    }

    @BeforeEach
    void fillWithCannedData() {
        ontologyRepository.save(Ontology.builder()
            .ontologyId("testId_1")
            .title("Test title 1")
            .description("Test description 1")
            .definitionProperties(Collections.singletonList("Test definition 1"))
            .synonymProperties(List.of("Synonym property 1", "Synonym property 2"))
            .build()).block();
    }

    /**
     * When counting of existing documents then 1.
     */
    @Test
    void When_countingOfExistingDocuments_Then_1() {
        Assertions.assertEquals(1, ontologyService.listAllOntologyIdsJson().get("ontologyIds").size());
    }

    /**
     * When get by id exists then DTO.
     */
    @Test
    void When_getByIdExists_Then_Dto() {
        Assertions.assertEquals("Test description 1", ontologyService.getByOntologyId("testId_1").block().getDescription());
    }

    /**
     * When ontology not exists on local and ols then 404.
     */
    @Test
    void When_ontologyNotExistsOnLocalAndOLS_Then_404() {
        webTestClient
            .get()
            .uri(API_ROOT + "/impossibleOntology")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    /**
     * When ontology not exists on local but exists on ols then dto.
     */
    @Test
    void When_ontologyNotExistsOnLocalButExistsOnOLS_Then_Dto() {
        webTestClient
            .get()
            .uri(API_ROOT + "/mondo")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody(OntologyDto.class)
            .value(ontologyDto -> ontologyDto.getOntologyId().equals("mondo"));
    }

    /**
     * When create existing ontology then 409.
     */
    @Test
    void When_createExistingOntology_Then_409() {
        webTestClient
            .post()
            .uri(API_ROOT)
            .body(BodyInserters.fromValue(OntologyMapper.toDto(Ontology.builder()
                .ontologyId("testId_1")
                .title("any")
                .description("any")
                .build())))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .value(status -> Assertions.assertEquals(HttpStatus.CONFLICT.value(), status.intValue()));
    }

    /**
     * When create non existing ontology then 201.
     */
    @Test
    void When_createNonExistingOntology_Then_201() {
        webTestClient
            .post()
            .uri(API_ROOT)
            .body(BodyInserters.fromValue(OntologyMapper.toDto(Ontology.builder()
                .ontologyId("new_testId")
                .title("Any title")
                .description("Any description")
                .definitionProperties(List.of("Definition property 1", "Definition property 2"))
                .synonymProperties(List.of("Synonym property 1", "Synonym property 2"))
                .build())))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isCreated();
    }

}
