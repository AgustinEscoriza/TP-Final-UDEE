package utn.tpFinal.UDEE.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utn.tpFinal.UDEE.exceptions.UnauthorizedToAddEmployeeException;
import utn.tpFinal.UDEE.exceptions.UserNotFoundException;
import utn.tpFinal.UDEE.model.Dto.UserDto;
import utn.tpFinal.UDEE.model.User;
import utn.tpFinal.UDEE.repository.FeeTypeRepository;
import utn.tpFinal.UDEE.repository.UserRepository;
import utn.tpFinal.UDEE.util.TestConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp(){
        this.userRepository = mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }
    @Test
    public void addUser_TestOk() throws UnauthorizedToAddEmployeeException {
        User user = TestConstants.getUserClient(1);


        when(userRepository.save(user)).thenReturn(user);

        UserDto userDtoresponse = userService.addUser(user);

        assertEquals(user.getId(),userDtoresponse.getId());
        assertEquals(user.getIsEmployee(),userDtoresponse.getIsEmployee());
    }
    @Test
    public void addUser_TestUnauthorizedToAddEmployeeException() {
        User user = TestConstants.getUserAdmin(1);

        when(userRepository.save(user)).thenReturn(user);
        assertThrows(UnauthorizedToAddEmployeeException.class,()->userService.addUser(user));
    }

    @Test
    public void deleteUser_TestOk() throws UserNotFoundException {
        Integer idUser= 1;
        when(userRepository.existsById(idUser)).thenReturn(true);
        Boolean deleted = userService.deleteUser(idUser);
        assertTrue(deleted);
    }
    @Test
    public void deleteUser_TestUserNotFoundException() {
        Integer idUser= 1;
        when(userRepository.existsById(idUser)).thenReturn(false);
        assertThrows(UserNotFoundException.class,()->userService.deleteUser(idUser));
    }
    @Test
    public void login(){
        String email= "email@email.com";
        String password = "123";
        User user = User.builder().id(1).password("123").email("email@email.com").build();

        when(userRepository.findByEmailAndPassword(email,password)).thenReturn(user);

        userService.login(email,password);

        assertEquals(email,user.getEmail());
        assertEquals(password,user.getPassword());
    }
}
