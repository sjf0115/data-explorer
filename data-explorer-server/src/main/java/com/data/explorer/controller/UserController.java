package com.data.explorer.controller;

import com.data.explorer.common.Result;
import com.data.explorer.entity.User;
import com.data.explorer.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    private static final String SESSION_USER_KEY = "currentUser";

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request.getUserId(), request.getPassword());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 将用户信息存入Session
        session.setAttribute(SESSION_USER_KEY, user);
        
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUserName(user.getUserName());
        userVO.setCreator(user.getCreator());
        userVO.setGmtCreate(user.getGmtCreate());
        
        return Result.success(userVO);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        return Result.success();
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER_KEY);
        if (user == null) {
            return Result.error("用户未登录");
        }
        
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUserName(user.getUserName());
        userVO.setCreator(user.getCreator());
        userVO.setGmtCreate(user.getGmtCreate());
        
        return Result.success(userVO);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList(HttpSession session) {
        // 检查登录状态
        User currentUser = (User) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        
        return Result.success(userService.getAllUsers());
    }

    /**
     * 创建用户
     */
    @PostMapping("/create")
    public Result<Void> createUser(@Valid @RequestBody CreateUserRequest request, HttpSession session) {
        // 检查登录状态
        User currentUser = (User) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());

        boolean success = userService.createUser(user, currentUser.getUserId());
        if (!success) {
            return Result.error("用户ID已存在");
        }
        
        return Result.success();
    }

    /**
     * 禁用用户
     */
    @PostMapping("/disable/{userId}")
    public Result<Void> disableUser(@PathVariable String userId, HttpSession session) {
        // 检查登录状态
        User currentUser = (User) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        // 不能禁用自己
        if (userId.equals(currentUser.getUserId())) {
            return Result.error("不能禁用当前登录用户");
        }

        boolean success = userService.disableUser(userId, currentUser.getUserId());
        if (!success) {
            return Result.error("禁用用户失败");
        }
        
        return Result.success();
    }

    // ========== 请求/响应对象 ==========

    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        
        @NotBlank(message = "用户名称不能为空")
        private String userName;
        
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class UserVO {
        private String userId;
        private String userName;
        private String creator;
        private java.time.LocalDateTime gmtCreate;
    }
}
