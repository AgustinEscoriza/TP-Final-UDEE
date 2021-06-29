package utn.tpFinal.UDEE.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import utn.tpFinal.UDEE.exceptions.UnauthorizedToAddEmployeeException;
import utn.tpFinal.UDEE.exceptions.UserNotFoundException;
import utn.tpFinal.UDEE.model.Dto.LoginRequestDto;
import utn.tpFinal.UDEE.model.Dto.LoginResponseDto;
import utn.tpFinal.UDEE.model.Dto.UserDto;
import utn.tpFinal.UDEE.model.User;
import utn.tpFinal.UDEE.service.UserService;
import utn.tpFinal.UDEE.util.TestConstants;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserService userService;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private UserController userController;
    private Object UnauthorizedToAddEmployeeException;

    @BeforeEach
    public void setUp(){
        this.userService = mock(UserService.class);
        this.modelMapper = mock(ModelMapper.class);
        this.objectMapper = mock(ObjectMapper.class);
        this.userController = new UserController(userService,objectMapper,modelMapper);
    }

    @Test
    public void addUser_Test200(){
        try{
            User user = TestConstants.getUserClient(1);
            UserDto userDto = TestConstants.getUserDto(1);

            when(userService.addUser(user)).thenReturn(userDto);

            ResponseEntity<UserDto> responseEntity = userController.addUser(user);

            assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
            assertEquals("email@email.com", responseEntity.getBody().getEmail());
            assertEquals(1, responseEntity.getBody().getId());
        }catch (UnauthorizedToAddEmployeeException e){
            Assert.fail(e.toString());
        }
    }
    @Test
    public void addUser_Test401(){
        try{
            User user = TestConstants.getUserAdmin(1);
            UserDto userDto = TestConstants.getUserDto(1);

            when(userService.addUser(user)).thenThrow(new UnauthorizedToAddEmployeeException("UnauthorizedToAddEmployeeException","addUser"));

            ResponseEntity<UserDto> responseEntity = userController.addUser(user);

            assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
        }catch (UnauthorizedToAddEmployeeException e){
            Assert.fail(e.toString());
        }
    }
    @Test
    public void login_Test200(){
        LoginRequestDto loginRequestDto = TestConstants.getLoginRequestDto();
        User user = TestConstants.getUserClient(1);
        UserDto userDto = TestConstants.getUserDto(1);

        when(modelMapper.map(user,UserDto.class)).thenReturn(userDto);
        when(userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword())).thenReturn(user);
        when(userController.generateToken(userDto)).thenReturn("3231asf41a2313asfasf");
        ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void login_Test204(){
        LoginRequestDto loginRequestDto = TestConstants.getLoginRequestDto();
        User user = TestConstants.getUserClient(1);
        UserDto userDto = TestConstants.getUserDto(1);

        when(userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword())).thenReturn(null);
        when(userController.generateToken(userDto)).thenReturn("3231asf41a2313asfasf");
        ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);

        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    public void delete_Test200() throws UserNotFoundException {
        when(userService.deleteUser(1)).thenReturn(true);
        ResponseEntity responseEntity = userController.delete(1);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void delete_Test204() throws UserNotFoundException {
        when(userService.deleteUser(1)).thenReturn(false);
        ResponseEntity responseEntity = userController.delete(1);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
}
