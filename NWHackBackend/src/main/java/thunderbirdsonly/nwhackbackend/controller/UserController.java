package thunderbirdsonly.nwhackbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thunderbirdsonly.nwhackbackend.DOT.User;
import thunderbirdsonly.nwhackbackend.Pojo.Result;
import thunderbirdsonly.nwhackbackend.Utility.JwtUtils;
import thunderbirdsonly.nwhackbackend.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public Result login(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("Password cannot be empty");
        }
        try {
            User u = userService.login(user);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getUserId());
            claims.put("userName", u.getUsername());

            String jwt = JwtUtils.generateToken(claims);
            return Result.success(jwt);
        } catch (Exception e) {
            return Result.error("Invalid username or password");
        }
    }


    @PostMapping("/api/register")
    public Result register(@RequestBody User user) {
        // 检查字段是否为空
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return Result.error("Email cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("Password cannot be empty");
        }

        try {
            userService.register(user);
            return Result.success("Registration successful");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("Registration failed: " + e.getMessage());
        }
    }



    @PostMapping("/api/logout")
    public Result logout() {
        return Result.success("byebye");
    }

}
