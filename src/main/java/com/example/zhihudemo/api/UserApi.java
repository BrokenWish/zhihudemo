package com.example.zhihudemo.api;

import com.example.zhihudemo.model.Result;
import com.example.zhihudemo.model.User;
import com.example.zhihudemo.service.UserService;
import com.example.zhihudemo.util.SnowFlakeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Broken wish
 * @coding utf-8
 * @create 2023-09-03-22:05
 * @Name com.example.zhihudemo.api
 * @Projrct zhihudemo
 */
@Controller
public class UserApi {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/login")
    @ResponseBody
    public Result<User> login(@RequestParam("loginName")String loginName, @RequestParam("loginPwd")String loginPwd, Model model, HttpServletRequest request){
        Result<User> result = new Result<>();
        User user = userService.queryByPhone(loginName);

        if (user.getPhoneAndEmail() == null){
            System.out.println("6666666666666666666666666666666666666666666666666666");
            user.setUserId(SnowFlakeUtil.getSnowFlakeId());
            user.setPhoneAndEmail(loginName);
            user.setPwd(loginPwd);
            String date = formatter.format(LocalDateTime.now());

            user.setGmtCreated(LocalDateTime.parse(date, formatter));
            user.setGmtModified(LocalDateTime.parse(date, formatter));

            userService.add(user);

            result.setData(user);
            result.setSuccess(true);
            result.setMessage("用户名不存在，已帮助注册");
            model.addAttribute("info", "注册成功！");
            return result;
        }

        if (user.getPwd().equals(loginPwd)){
            result.setData(user);
            result.setSuccess(true);
            result.setMessage("登陆成功");
            model.addAttribute("info", "登录成功！");
            return result;
        }else{
            result.setMessage("密码错误");
            model.addAttribute("info", "登录失败！");
            return result;
        }
    }


}
