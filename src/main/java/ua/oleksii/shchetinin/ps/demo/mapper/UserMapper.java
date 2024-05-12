package ua.oleksii.shchetinin.ps.demo.mapper;

import org.mapstruct.Mapper;
import ua.oleksii.shchetinin.ps.demo.dto.responce.UserResponseDto;
import ua.oleksii.shchetinin.ps.demo.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToDto(User user);
    List<UserResponseDto> userToDto(List<User> user);
}
