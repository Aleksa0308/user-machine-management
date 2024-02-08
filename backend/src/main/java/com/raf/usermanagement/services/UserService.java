package com.raf.usermanagement.services;

import com.raf.usermanagement.model.Permission;
import com.raf.usermanagement.model.User;
import com.raf.usermanagement.repositories.PermissionRepository;
import com.raf.usermanagement.repositories.UserRepository;
import com.raf.usermanagement.requests.UpdateUserRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService, UserDetailsService {
    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(email);

        if(myUser == null) throw new UsernameNotFoundException("User not found in the database");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        myUser.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName()) ));
        return  new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), authorities);
    }

    public User register(String firstname, String lastname, String email, String password, Set<String> permissionNames) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setEmail(email);

        Set<Permission> permissions = permissionRepository.findByNameIn(permissionNames);
        user.setPermissions(permissions);

        return this.userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(UpdateUserRequest user) {
        User newUser = userRepository.getById(user.getUserId());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(newUser.getPassword());
//        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        // go through the list of permissions and add them to the user
        // find the permission by name and add it to the user
        Set<Permission> permissions = new HashSet<>();
        for (String permissionName : user.getPermissions()) {
            Permission permission = permissionRepository.findByName(permissionName);
            permissions.add(permission);
        }
        newUser.setPermissions(permissions);
        return userRepository.save(newUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<String> getUserPermissionsByEmail(String email) {
        User user = userRepository.findByEmail(email);
        List<Permission> permissions = new ArrayList<>(user.getPermissions());
        List<String> permissionNames = new ArrayList<>();
        for (Permission permission : permissions) {
            permissionNames.add(permission.getName());
        }
        return permissionNames;
    }
}
