package reactive.ontology.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Ontology {
    @Id
    String ontologyId;
    String title;
    String description;
    List<String> definitionProperties;
    List<String> synonymProperties;
}
