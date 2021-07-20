package reactive.ontology.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * With DTOs we provide required fields for each representation
 * + avoiding the direct entity exchanging (which is anti-pattern)
 */
@Data
@NoArgsConstructor
public class OntologyDto {

    String ontologyId;
    String title;
    String description;
    List<String> definitionProperties;
    List<String> synonymProperties;

}
