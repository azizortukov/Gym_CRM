package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.entity.Trainer;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraineeMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "specialization", target = "specialization")
    TrainerBaseDto toTrainerBaseDto(Trainer trainer);

    @InheritInverseConfiguration(name = "toGetDto")
    Trainee toEntity(GetTraineeDto getTraineeDto);

    @Mapping(source = "trainers", target = "trainersList")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    GetTraineeDto toGetDto(Trainee trainee);

    @InheritInverseConfiguration(name = "toPostDto")
    Trainee toEntity(PostTraineeDto traineeDto);

    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    PostTraineeDto toPostDto(Trainee trainee);

    @InheritInverseConfiguration(name = "toPutDto")
    Trainee toEntity(PutTraineeDto traineeDto);

    @InheritConfiguration(name = "toGetDto")
    @Mapping(source = "user.username", target = "username")
    PutTraineeDto toPutDto(Trainee trainee);

}