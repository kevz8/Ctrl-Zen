package thunderbirdsonly.nwhackbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import thunderbirdsonly.nwhackbackend.DOT.User;
import thunderbirdsonly.nwhackbackend.mapper.UserMapper;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public User login (User user) {
        User storedUser = userMapper.getUserByUsername(user.getUsername());
        if (storedUser == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword(), storedUser.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return storedUser; //Verified Password
    }

    public void register(User register) {
        int count = userMapper.countByUsernameOrEmail(register);
        if (count > 0) {
            throw new IllegalArgumentException("Username or email already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        register.setPassword(encoder.encode(register.getPassword()));
        userMapper.insertUser(register);
    }
}
