package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.TraineePartialDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.dto.TraineeDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraineeMapper {

    @Mapping(source = "password", target = "user.password")
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "firstName", target = "user.firstName")
    Trainee toEntityPartialDto(TraineePartialDto traineeDto);

    @InheritInverseConfiguration(name = "toEntityPartialDto")
    TraineePartialDto toPartialDto(Trainee trainee);

    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "isActive", target = "user.isActive")
    @Mapping(source = "username", target = "user.username")
    Trainee toEntity(TraineeDto traineeResDto);

    @InheritInverseConfiguration(name = "toEntity")
    TraineeDto toDto(Trainee trainee);
}