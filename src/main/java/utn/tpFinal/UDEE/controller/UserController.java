package utn.tpFinal.UDEE.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import utn.tpFinal.UDEE.exceptions.ApiError;
import utn.tpFinal.UDEE.exceptions.UnauthorizedToAddEmployeeException;
import utn.tpFinal.UDEE.exceptions.UserNotFoundException;
import utn.tpFinal.UDEE.model.Dto.LoginRequestDto;
import utn.tpFinal.UDEE.model.Dto.LoginResponseDto;
import utn.tpFinal.UDEE.model.Dto.UserDto;
import utn.tpFinal.UDEE.model.User;
import utn.tpFinal.UDEE.service.UserService;
import utn.tpFinal.UDEE.util.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "")
public class UserController {

    private UserService userService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper, ModelMapper modelMapper){
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/api/users")
    public  ResponseEntity<UserDto> addUser(@RequestBody  User user  ){
        UserDto userDTO = new UserDto();
        try{
            userDTO= userService.addUser(user);
        }catch(UnauthorizedToAddEmployeeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userDTO);
        }
        return ResponseEntity.ok().body(userDTO);
    }
    @PostMapping(value = "/api/users/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info(loginRequestDto.toString());
        User user = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (user!=null){
            UserDto dto = modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(dto)).build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping(value ="/backoffice/users/{userId}")
    public ResponseEntity delete(@RequestParam Integer userId) throws UserNotFoundException {
        Boolean deleted = false;
        deleted = userService.deleteUser(userId);
        if(!deleted){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    /*@GetMapping(value = "/details")
    public ResponseEntity<User> userDetails(Authentication auth) {
        return ResponseEntity.ok((User) auth.getPrincipal());
    }*/

    public String generateToken(UserDto userDto) {
        try {
            List<GrantedAuthority> grantedAuthorities ;
            if(userDto.getIsEmployee())
            {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("BACKOFFICE");
            }
            else {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("CLIENT");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(2023,11,11);
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(userDto.getEmail())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration( calendar.getTime())
                    .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET.getBytes()).compact();
            return  token;
        } catch(Exception e) {
            return "dummy";
        }
    }
}
