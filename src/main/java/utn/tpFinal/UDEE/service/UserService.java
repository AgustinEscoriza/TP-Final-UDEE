package utn.tpFinal.UDEE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.tpFinal.UDEE.exceptions.UnauthorizedToAddEmployeeException;
import utn.tpFinal.UDEE.exceptions.UserNotFoundException;
import utn.tpFinal.UDEE.model.Dto.UserDto;
import utn.tpFinal.UDEE.model.User;
import utn.tpFinal.UDEE.repository.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){ this.userRepository = userRepository;}

    public UserDto addUser(User user) throws UnauthorizedToAddEmployeeException {
        if(user.getIsEmployee() == true){
            throw new UnauthorizedToAddEmployeeException(this.getClass().getSimpleName(),"Post");
        }
        User u= userRepository.save(user);
        UserDto userDto = UserDto.from(u);
        return userDto;
    }
    public Boolean deleteUser(Integer userId) throws UserNotFoundException {
        Boolean deleted = false;
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(this.getClass().getSimpleName(),"deleteUser");
        }
        userRepository.deleteById(userId);
        if(!userRepository.existsById(userId)){
            deleted = true;
        }
        return false;
    }

    public User login( String email, String password){
        return userRepository.findByEmailAndPassword(email,password);
    }
}
