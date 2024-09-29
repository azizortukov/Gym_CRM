package uz.anas.gymcrm.model.mapper;

import org.mapstruct.*;
import uz.anas.gymcrm.model.dto.get.GetTraineeTrainingDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerTrainingDto;
import uz.anas.gymcrm.model.dto.post.PostTrainingDto;
import uz.anas.gymcrm.model.entity.Training;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingMapper {

    @Mapping(source = "trainingType", target = "trainingType.trainingTypeName")
    @Mapping(source = "trainerName", target = "trainer.user.firstName")
    Training toEntity(GetTraineeTrainingDto getTraineeTrainingDto);

    @InheritInverseConfiguration(name = "toEntity")
    GetTraineeTrainingDto toTraineeTrainingDto(Training training);

    @Mapping(source = "trainingType", target = "trainingType.trainingTypeName")
    @Mapping(source = "traineeName", target = "trainee.user.firstName")
    Training toEntity(GetTrainerTrainingDto getTrainerTrainingDto);

    @InheritInverseConfiguration(name = "toEntity")
    GetTrainerTrainingDto toTrainerTrainingDto(Training training);

    @Mapping(source = "trainerUserUsername", target = "trainer.user.username")
    @Mapping(source = "traineeUserUsername", target = "trainee.user.username")
    Training toEntity(PostTrainingDto postTrainingDto);

    @InheritInverseConfiguration(name = "toEntity")
    PostTrainingDto toPostTrainingDto(Training training);

}