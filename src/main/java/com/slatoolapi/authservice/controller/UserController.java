package com.slatoolapi.authservice.controller;

import java.util.List;

import com.slatoolapi.authservice.dto.TokenDto;
import com.slatoolapi.authservice.dto.UserLoginDto;
import com.slatoolapi.authservice.dto.UserRegisterDto;
import com.slatoolapi.authservice.entity.User;
import com.slatoolapi.authservice.repository.UserRepository;
import com.slatoolapi.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping(path="/users/{id}")
	public UserRegisterDto getUserById(@PathVariable int id) {
		return UserRegisterDto.convert(userService.findById(id));
	}

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto dto) {
        TokenDto tokenDto = userService.login(dto);
        if (tokenDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token) {
        TokenDto tokenDto = userService.validate(token);
        if (tokenDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(tokenDto);
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterDto dto) {
        User user = userService.regiter(dto);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(user);
    }
    
    @PutMapping(path="users/{id}")
	public UserRegisterDto update(@PathVariable int id, @RequestBody User user) {
		User nou = userService.findById(id);

		nou.setName(user.getName());
		nou.setLastName(user.getLastName());
		nou.setEmail(user.getEmail());		

		return UserRegisterDto.convert(userService.save(nou));
	}
    
    @DeleteMapping(path="/users/{id}")
	public void deleteUserById(@PathVariable int id) {
		userService.deleteById(id);
	}        
    
}
