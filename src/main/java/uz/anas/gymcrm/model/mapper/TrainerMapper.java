package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.TraineeBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainerMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    TraineeBaseDto toTraineeBaseDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toPostDto")
    Trainer toEntity(PostTrainerDto trainerDto);

    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    PostTrainerDto toPostDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toGetDto")
    Trainer toEntity(GetTrainerDto getTrainerDto);

    @Mapping(source = "trainees", target = "traineesList")
    GetTrainerDto toGetDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toPutDto")
    Trainer toEntity(PutTrainerDto putTrainerDto);

    @InheritConfiguration(name = "toGetDto")
    PutTrainerDto toPutDto(Trainer trainer);

}