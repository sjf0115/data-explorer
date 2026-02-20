package com.data.explorer.service;

import com.data.explorer.entity.User;
import com.data.explorer.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    /**
     * 用户登录
     */
    public User login(String userId, String password) {
        String encryptedPassword = encryptPassword(password);
        return userMapper.findByUserIdAndPassword(userId, encryptedPassword);
    }

    /**
     * 根据用户ID查询用户
     */
    public User findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    /**
     * 获取所有用户列表
     */
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    /**
     * 创建用户
     */
    public boolean createUser(User user, String operator) {
        // 检查用户ID是否已存在
        if (userMapper.countByUserId(user.getUserId()) > 0) {
            log.warn("用户ID已存在: {}", user.getUserId());
            return false;
        }

        // 加密密码
        user.setPassword(encryptPassword(user.getPassword()));
        user.setCreator(operator);
        user.setModifier(operator);

        int result = userMapper.insert(user);
        return result > 0;
    }

    /**
     * 禁用用户
     */
    public boolean disableUser(String userId, String operator) {
        int result = userMapper.disableUser(userId, operator);
        return result > 0;
    }

    /**
     * 密码加密（MD5）
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }
}
