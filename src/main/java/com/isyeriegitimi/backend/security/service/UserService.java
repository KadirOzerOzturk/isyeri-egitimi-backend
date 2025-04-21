package com.isyeriegitimi.backend.security.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.security.dto.UserDto;
import com.isyeriegitimi.backend.security.model.User;
import com.isyeriegitimi.backend.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final String ALL_CHARACTERS = UPPER_CASE + LOWER_CASE + NUMBERS + SPECIAL_CHARACTERS;

    public static String generatePassword(int length) {
        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new SecureRandom();
        passwordBuilder.append(getRandomCharacter(UPPER_CASE, random));
        passwordBuilder.append(getRandomCharacter(LOWER_CASE, random));
        passwordBuilder.append(getRandomCharacter(NUMBERS, random));
        passwordBuilder.append(getRandomCharacter(SPECIAL_CHARACTERS, random));
        for (int i = 4; i < length; i++) {
            char randomChar = getRandomCharacter(ALL_CHARACTERS, random);
            passwordBuilder.append(randomChar);
        }

        // Shuffle the password to randomize the order of characters.
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = passwordBuilder.charAt(i);
            passwordBuilder.setCharAt(i, passwordBuilder.charAt(randomIndex));
            passwordBuilder.setCharAt(randomIndex, temp);
        }

        return passwordBuilder.toString();
    }

    private static char getRandomCharacter(String characterSet, Random random) {
        int index = random.nextInt(characterSet.length());
        return characterSet.charAt(index);
    }


    public Optional<User> getUserByUsernameAndTitle(String username, String title) {
        return userRepository.findByUsernameAndTitle(username, title);
    }

    @Transactional
    public String update(UserDto userDto) {
        try {
            Optional<User> existingUser = userRepository.findByUsernameAndTitle(userDto.getUsername(), userDto.getTitle());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setUsername(userDto.getUsername());
                user.setPassword(existingUser.get().getPassword());
                user.setRole(userDto.getRole());
                userRepository.save(user);
            } else {
                throw new ResourceNotFoundException("User", "username", userDto.getUsername());
            }
            return "User updated successfully";
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the user: " + e.getMessage());
        }
    }

    @Transactional
    public void updateUsernameByEmail(String oldEmail, String newEmail) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(oldEmail);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setUsername(newEmail);
                userRepository.save(user);
            } else {
                throw new ResourceNotFoundException("User", "email", oldEmail);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the user's username: " + e.getMessage());
        }
    }



}
