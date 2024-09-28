package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.model.entity.Trainee;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface TraineeMapper {

    @InheritInverseConfiguration(name = "toGetDto")
    Trainee toEntity(GetTraineeDto getTraineeDto);

    @Mapping(source = "trainers.user.firstName", target = "trainersList.firstName")
    @Mapping(source = "trainers.user.lastName", target = "trainersList.lastName")
    @Mapping(source = "trainers.user.username", target = "trainersList.username")
    @Mapping(source = "trainers.specialization", target = "trainersList.specialization")
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
    @Mapping(source = "username", target = "user.username")
    Trainee toEntity(PutTraineeDto traineeDto);

    @InheritConfiguration(name = "toGetDto")
    @Mapping(source = "user.username", target = "username")
    PutTraineeDto toPutDto(Trainee trainee);

}