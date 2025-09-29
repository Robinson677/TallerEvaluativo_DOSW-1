package edu.dosw.TallerEvaluativo.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import edu.dosw.TallerEvaluativo.dtos.TransactionDTO;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    Transaction toEntity(TransactionDTO dto);

    TransactionDTO toDTO(Transaction entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    void updateEntity(@MappingTarget Transaction entity, TransactionDTO dto);
}