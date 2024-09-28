package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface TrainerMapper {

    @InheritInverseConfiguration(name = "toPostDto")
    Trainer toEntity(PostTrainerDto trainerDto);

    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    PostTrainerDto toPostDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toGetDto")
    Trainer toEntity(GetTrainerDto getTrainerDto);

    @Mapping(source = "trainees.user.username", target = "traineesList.username")
    @Mapping(source = "trainees.user.lastName", target = "traineesList.lastName")
    @Mapping(source = "trainees.user.firstName", target = "traineesList.firstName")
    GetTrainerDto toGetDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toGetDto")
    Trainer toEntity(PutTrainerDto putTrainerDto);

    @InheritConfiguration(name = "toGetDto")
    PutTrainerDto toPutDto(Trainer trainer);

}