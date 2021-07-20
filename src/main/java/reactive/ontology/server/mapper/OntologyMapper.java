package reactive.ontology.server.mapper;

import reactive.ontology.server.dto.OntologyDto;
import reactive.ontology.server.entity.Ontology;

public class OntologyMapper {

    private OntologyMapper(){}

    public static OntologyDto toDto(Ontology ontology) {
        OntologyDto dto = new OntologyDto();
        dto.setOntologyId(ontology.getOntologyId());
        dto.setTitle(ontology.getTitle());
        dto.setDescription(ontology.getDescription());
        dto.setDefinitionProperties(ontology.getDefinitionProperties());
        dto.setSynonymProperties(ontology.getSynonymProperties());
        return dto;
    }

    public static Ontology toOntology(OntologyDto ontologyDto) {
        Ontology ontology = new Ontology();
        ontology.setOntologyId(ontologyDto.getOntologyId());
        ontology.setTitle(ontologyDto.getTitle());
        ontology.setDescription(ontologyDto.getDescription());
        ontology.setDefinitionProperties(ontologyDto.getDefinitionProperties());
        ontology.setSynonymProperties(ontologyDto.getSynonymProperties());
        return ontology;
    }

}
