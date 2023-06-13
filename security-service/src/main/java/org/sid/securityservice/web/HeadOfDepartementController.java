package org.sid.securityservice.web;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.sid.securityservice.dtos.NotificationResponseDTO;
import org.sid.securityservice.dtos.UserDTO;
import org.sid.securityservice.dtos.UserResponseDTO;
import org.sid.securityservice.ennumeration.ERole;
import org.sid.securityservice.exceptions.RoleNotFoundException;
import org.sid.securityservice.exceptions.UserNotFoundException;
import org.sid.securityservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor

public class HeadOfDepartementController {
    private UserService userService;
    @GetMapping("/head_of_departement")
    public ResponseEntity<List<UserResponseDTO>> getHeadsOfDepartement() {
        try {
            List<UserResponseDTO> users = userService.getUsersByRole(ERole.HEAD_OF_DEPARTEMENT);
            return ResponseEntity.ok(users);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/head_of_departement/{id}")
    public ResponseEntity<UserResponseDTO> getHeadOfDepartementById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/head_of_departement/keyword/{keyword}")
    public ResponseEntity<List<UserResponseDTO>> getStudentsByMajor(@PathVariable String keyword) {
        List<UserResponseDTO> users = userService.getUsersByKeyword(keyword,String.valueOf(ERole.HEAD_OF_DEPARTEMENT));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/head_of_departement")
    public ResponseEntity<UserResponseDTO> saveHeadOfDepartement(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null) {
                throw new IllegalArgumentException("Required fields are missing.");
            }

            UserResponseDTO savedUser = userService.saveUser(userDTO,String.valueOf(ERole.HEAD_OF_DEPARTEMENT));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500 Internal Server Error
        }
    }

    @DeleteMapping("/head_of_departement/{id}")
    public ResponseEntity<String> removeHeadOfDepartement(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            Hibernate.initialize(user.getRoleDTOList());
            userService.removeUser(id);
            return ResponseEntity.ok("The head of departement with ID " + id + " is deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/head_of_departement/{id}")
    public ResponseEntity<UserResponseDTO> putHeadOfDepartement(@PathVariable Long id, @RequestBody UserDTO userDTO) throws RoleNotFoundException {
        UserResponseDTO savedUser = userService.saveUser(userDTO, String.valueOf(ERole.SCHOOLING));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/head_of_departement/{id}/notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByHeadOfDepartement(@PathVariable Long id) throws UserNotFoundException {
        List<NotificationResponseDTO> notifications = userService.getNotificationsByUser(id);
        return ResponseEntity.ok(notifications);
    }
}
