package com.slatoolapi.authservice.service;

import com.slatoolapi.authservice.dto.TokenDto;
import com.slatoolapi.authservice.dto.UserLoginDto;
import com.slatoolapi.authservice.dto.UserRegisterDto;
import com.slatoolapi.authservice.entity.User;
import com.slatoolapi.authservice.repository.UserRepository;
import com.slatoolapi.authservice.security.JwtProvider;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;
        
    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User regiter(UserRegisterDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) {
            return null;
        }
        String password = passwordEncoder.encode(dto.getPassword());
        User userNew = User.builder().
                email(dto.getEmail()).
                password(password).
                name(dto.getName()).
                lastName(dto.getLastName()).
                build();

        return userRepository.save(userNew);
    }
    
    	public User findById(int id) {
		
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent())
			throw new RuntimeException("id-ul "+  + id+  " no existe");

		return user.get();
	}
    	
    	public User save(User user){
    		userRepository.save(user);
    		return user;
    }   
    
    	public void deleteById(int id) {
    		Optional<User> user = userRepository.findById(id);
    		if (!user.isPresent())
    			throw new RuntimeException("user "+  + id+  " no existe");

    		userRepository.deleteById(id);
    }

    public TokenDto login(UserLoginDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (!user.isPresent()) {
            return null;
        }
        if (passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            TokenDto tokenDto = new TokenDto();
            tokenDto.setEmail(dto.getEmail());
            tokenDto.setId(user.get().getId());
            tokenDto.setToken(jwtProvider.createToken(user.get()));
            return tokenDto;
        }

        return null;
    }

    public TokenDto validate(String token) {
        if (!jwtProvider.validate(token)) {
            return null;
        }
        String email = jwtProvider.getEmailFromToken(token);
        if (!userRepository.findByEmail(email).isPresent()) {
            return null;
        }
        Optional<User> user = userRepository.findByEmail(email);
        Integer userId = user.get().getId();
        return new TokenDto(token, email, userId);
    }	
	
}
