package org.sid.securityservice.services;

import lombok.AllArgsConstructor;
import org.sid.securityservice.config.PasswordEncoding;
import org.sid.securityservice.dtos.RoleDTO;
import org.sid.securityservice.dtos.UserDTO;
import org.sid.securityservice.dtos.UserResponseDTO;
import org.sid.securityservice.ennumeration.ERole;
import org.sid.securityservice.entities.Role;
import org.sid.securityservice.entities.User;
import org.sid.securityservice.exceptions.RoleAlreadyAssignedException;
import org.sid.securityservice.exceptions.RoleNotFoundException;
import org.sid.securityservice.exceptions.UserNotFoundException;
import org.sid.securityservice.mappers.RoleMapper;
import org.sid.securityservice.mappers.UserDTOMapper;
import org.sid.securityservice.mappers.UserResponseDTOMapperImpl;
import org.sid.securityservice.repositories.RoleRepository;
import org.sid.securityservice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDTOMapper userDTOMapper;
    private final UserResponseDTOMapperImpl userResponseDTOMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserResponseDTO saveUser(UserDTO userDTO, String role) {
        try {
            if (userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null) {
                throw new IllegalArgumentException("Required fields are missing.");
            }

            userDTO.setPassword(new PasswordEncoding().getEncodedPassword(userDTO.getPassword()));
            User user = userDTOMapper.fromUserDTO(userDTO);

            Role role1 = new Role();
            role1.setName(ERole.valueOf(role));
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role1);
            user.setRoles(roleSet);

            User savedUser = userRepository.save(user);
            return userResponseDTOMapper.fromUser(savedUser);
        } catch (IllegalArgumentException e) {
            throw e; // Rethrow the exception to handle it in the calling method
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user.", e);
        }
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO userDTO) throws UserNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (userDTO.getCne() != null) {
            existingUser.setCne(userDTO.getCne());
        } else {
            existingUser.setCne(existingUser.getCne());
        }
        if (userDTO.getAdresse() != null) {
            existingUser.setAdresse(userDTO.getAdresse());
        } else {
            existingUser.setAdresse(existingUser.getAdresse());
        }
        if (userDTO.getCni() != null) {
            existingUser.setCni(userDTO.getCni());
        } else {
            existingUser.setCni(existingUser.getCni());
        }
        if (userDTO.getGender() != null) {
            existingUser.setGender(userDTO.getGender());
        } else {
            existingUser.setGender(existingUser.getGender());
        }
        if (userDTO.getNationality() != null) {
            existingUser.setNationality(userDTO.getNationality());
        } else {
            existingUser.setNationality(existingUser.getNationality());
        }
        if (userDTO.getFirstName() != null) {
            existingUser.setFirstName(userDTO.getFirstName());
        } else {
            existingUser.setFirstName(existingUser.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            existingUser.setLastName(userDTO.getLastName());
        } else {
            existingUser.setLastName(existingUser.getLastName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        } else {
            existingUser.setEmail(existingUser.getEmail());
        }
        if (userDTO.getDateNaissance() != null) {
            existingUser.setDateNaissance(userDTO.getDateNaissance());
        } else {
            existingUser.setDateNaissance(existingUser.getDateNaissance());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        } else {
            existingUser.setPhone(existingUser.getPhone());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(new PasswordEncoding().getEncodedPassword(userDTO.getPassword()));
        } else {
            existingUser.setPassword(existingUser.getPassword());
        }

        User updatedUser = userRepository.save(existingUser);
        return userResponseDTOMapper.fromUser(updatedUser);
    }


    @Override
    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return userResponseDTOMapper.toUserResponseDTOs(users);
    }

    @Override
    public UserResponseDTO getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return userResponseDTOMapper.fromUser(user);
    }

    @Override
    public UserResponseDTO removeUser(Long idUser) throws UserNotFoundException {
        User existingUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + idUser));
        userRepository.delete(existingUser);
        return userResponseDTOMapper.fromUser(existingUser);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username: " + username));
        return userResponseDTOMapper.fromUser(user);
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(ERole roleName) throws RoleNotFoundException {
        List<User> users = userRepository.findByRoles_Name(roleName);
        return userResponseDTOMapper.toUserResponseDTOs(users);
    }


    @Override
    public UserResponseDTO addRoleToUser(Long idUser, String roleName) throws UserNotFoundException, RoleNotFoundException {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + idUser));

        // Check if the user already has the role
        if (user.getRoles().stream().anyMatch(existingRole -> existingRole.getName().equals(roleName))) {
            throw new RoleAlreadyAssignedException("Role already assigned to the user.");
        }

        Role newRole = roleRepository.findByName(ERole.valueOf(roleName))
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));

        user.getRoles().add(newRole);
        userRepository.save(user);

        return userResponseDTOMapper.fromUser(user);
    }



    @Override
    public UserResponseDTO removeRoleFromUser(Long idUser, RoleDTO roleDTO) throws UserNotFoundException, RoleNotFoundException {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + idUser));

        // Find the role by name
        Role role = roleRepository.findByName(ERole.valueOf(roleDTO.getName()))
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleDTO.getName()));

        // Remove the role from the user's roles
        user.getRoles().removeIf(existingRole -> existingRole.getName().equals(role.getName()));
        userRepository.save(user);

        return userResponseDTOMapper.fromUser(user);
    }

}
