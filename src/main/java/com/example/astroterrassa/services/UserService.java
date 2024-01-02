package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.DAO.UsersRolesRepository;
import com.example.astroterrassa.model.AuthenticationType;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.UsersRoles;
import com.example.astroterrassa.security.oauth.CustomOAuth2User;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UsuariServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepository repo;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        repo.updateAuthenticationType(username, authType);
        System.out.println("Updated user's authentication type to " + authType);
    }

    @Override
    @Transactional
    public void guardarUser(User usuari) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuari.setPassword(passwordEncoder.encode(usuari.getPassword()));
        usuari.setIntents(3);
        this.userRepository.save(usuari);

    }

    // Método para guardar el rol del usuario
    @Override
    public void guardarRol(User usuari, UsersRoles rol) {
        rol.setUserId(usuari.getUser_id());
        rol.setRoleId(rol.getRoleId());
        rol.setRolNombre(rol.getRolNombre());
        usersRolesRepository.save(rol);
        System.out.println("Rol guardado");
    }

    public List<User> getAllUsers()  {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Optional<UsersRoles> role = usersRolesRepository.findById(user.getPermisos());
            role.ifPresent(user::setUsersRoles);
        }
        return users;
    }

    @Transactional(readOnly = true)
    public List<User> getBlockedUsers() {
        return userRepository.blockedUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);

    }

    @Override
    public void processOAuthPostLogin(CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        User user = userRepository.findByMail(email);

        if (user == null) {
            // Si el usuario no existe en la base de datos, lo guardamos
            saveUserAfterOAuthLoginSuccess(oAuth2User);
        } else {
            // Si el usuario ya existe en la base de datos, puedes proceder como desees
            // Por ejemplo, puedes actualizar la última fecha de inicio de sesión del usuario
            user.setRegisterDt(Date.from(ZonedDateTime.now().toInstant()));
            userRepository.save(user);
        }
    }

    @Override
    public void saveUserAfterOAuthLoginSuccess(CustomOAuth2User oAuth2User) {

        User user = new User();
        user.setNombre(oAuth2User.getName());
        user.setMail(oAuth2User.getEmail());
        user.setRegisterDt(Date.from(ZonedDateTime.now().toInstant()));
        user.setIntents(3);
        user.setUsername(oAuth2User.getEmail()); // or any other field you want to use for username
        user.setAuthType(AuthenticationType.GOOGLE);
        user.setEnabled(true);
        userRepository.save(user); // Guarda el usuario en la base de datos

        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserId(user.getUser_id());
        usersRoles.setRoleId(0);
        usersRoles.setRolNombre("usuario");
        usersRolesRepository.save(usersRoles);

    }

    @Override
    public void logoutUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setLastDt(new Date());
            userRepository.save(user);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void registrarPersona(User user, UsersRoles usersRoles) {
        // Crea y guarda el usuario
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPermisos(1);
        user.setIntents(3);
        User savedUser = userRepository.save(user); // Guarda el usuario en la base de datos

        // Asigna el User_id y el Role_id a UsersRoles y lo guarda
        usersRoles.setUserId(savedUser.getUser_id());
        usersRoles.setRoleId(1);
        usersRoles.setRolNombre("usuario");
        usersRolesRepository.save(usersRoles); // Guarda el rol del usuario

        System.out.println("User guardado");
    }

    @Override
    public void desbloquejarUsuari(Long id, User user) {
        user = getUserById(id);
        user.setIntents(3);
        userRepository.save(user);
        System.out.println("S'ha desbloquejat l'usuari");
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UsersRoles save(UsersRoles usersRoles) {
        return usersRolesRepository.save(usersRoles);
    }

    public boolean userExists(String username) {
        return repo.getUserByUsername(username) != null;
    }

    public void create(User user) {
        repo.createUser(user.getUsername(), user.getPassword(), user.getAuthType());
    }
}