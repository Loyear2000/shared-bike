package com.loyear.springboot.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.common.Constants;
import com.loyear.springboot.common.Result;
import com.loyear.springboot.controller.dto.UserDTO;
import com.loyear.springboot.entity.CheckLog;
import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.service.ICheckLogService;
import com.loyear.springboot.service.IFaultSheetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.IUserService;
import com.loyear.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-01-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private ICheckLogService checkLogService;

    @Resource
    private IFaultSheetService faultSheetService;

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return new Result(Constants.CODE_400,"用户名或密码为空",null);
        }
        UserDTO dto = userService.login(userDto);
        return Result.success(dto);
    }

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return new Result(Constants.CODE_400,"用户名或密码为空",null);
        }
        return Result.success(userService.register(userDto));
    }

    //新增和修改数据
    @PostMapping
    public Result saveUser(@RequestBody User user){
        return Result.success(userService.saveOrUpdate(user));
    }

    //查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(userService.list());
    }

    @GetMapping("status")
    public Result status(@RequestParam String username){
        return Result.success(userService.getOne(new QueryWrapper<User>().eq("username",username)).getStatus());
    }

    //返回统计数据
    @GetMapping("data")
    public Result data(){
        int totalWorkers=0;
        int totalOper=0;
        int totalRepair=0;
        int onlineAll=0;
        int onlineOper=0;
        int onlineRepair=0;
        int repairCost=0;
        int moneyCost=0;
        int todayFault=0;



        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role","ROLE_OPER");
        List<User> list1 = userService.list(queryWrapper1);
        totalOper = list1.size();
        for(User user1:list1) {
            if (user1.getStatus() == 1) {
                onlineOper++;
            }
            moneyCost+=user1.getMoney();
        }


        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("role","ROLE_REPAIR");
        List<User> list2 = userService.list(queryWrapper2);
        totalRepair = list2.size();
        for(User user2:list2) {
            if (user2.getStatus() == 1) {
                onlineRepair++;
            }
            moneyCost+=user2.getMoney();
        }

        QueryWrapper<FaultSheet> queryWrapper3 = new QueryWrapper<>();
        QueryWrapper<FaultSheet> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.like("create_time", LocalDate.now());
        todayFault=faultSheetService.list(queryWrapper4).size();
        queryWrapper3.eq("fault_level",2);
        queryWrapper3.eq("status",4);
        List<FaultSheet> list3 = faultSheetService.list(queryWrapper3);
        for(FaultSheet faultSheet:list3){
            switch (faultSheet.getFaultParts()){
                case 1:repairCost+=1;break;
                case 2:repairCost+=2;break;
                case 3:repairCost+=3;break;
                case 4:repairCost+=4;break;
                case 5:repairCost+=5;break;
                case 6:repairCost+=6;break;
                case 7:repairCost+=7;break;
                case 8:repairCost+=8;break;
                case 9:repairCost+=9;break;
                case 10:repairCost+=10;break;
                case 11:repairCost+=11;break;
                case 12:repairCost+=12;break;
            }
        }


        totalWorkers=totalOper+totalRepair;
        onlineAll=onlineOper+onlineRepair;

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("totalOper",totalOper);
        hashMap.put("totalRepair",totalRepair);
        hashMap.put("totalWorkers",totalWorkers);
        hashMap.put("onlineAll",onlineAll);
        hashMap.put("onlineOper",onlineOper);
        hashMap.put("onlineRepair",onlineRepair);
        hashMap.put("moneyCost",moneyCost);
        hashMap.put("repairCost",repairCost);
        hashMap.put("todayFault",todayFault);


        return Result.success(hashMap);
    }

    //查询单个
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User one = userService.getOne(queryWrapper);
        return Result.success(one);
    }

    //删除数据
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }

    //批量删除数据
    @PostMapping("/del/batch")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids){
        return Result.success(userService.removeBatchByIds(ids));
    }


    //分页查询
    //传入pageNum和pageSize
    //page?pageNum=1&pageSize=10
//    @GetMapping("/page")
//    public Map<String,Object> findPage(@RequestParam Integer pageNum,
//                                       @RequestParam Integer pageSize,
//                                       @RequestParam String username){
//        pageNum=(pageNum-1)*pageSize;
//        Integer count = userMapper.selectCount(username);
//        List<User> data = userMapper.findPage(pageNum,pageSize,username);
//        Map<String,Object> res=new HashMap<>();
//        res.put("total",count);
//        res.put("data",data);
//        return res;
//    }

    //分页查询-mybatis-plus的方式
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String address){
        IPage<User> page=new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
        if(!"".equals(email)){
            queryWrapper.like("email",email);
        }
        if(!"".equals(address)){
            queryWrapper.like("address",address);
        }
        queryWrapper.orderByDesc("id");
//        queryWrapper.or().like("address",address);


//        //打印当前登录用户的用户名
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println(currentUser.getUsername());

        return Result.success(userService.page(page,queryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("avatarUrl", "头像");
//        writer.addHeaderAlias("createTime", "创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        return Result.success(true);

    }
    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来,且实体类需提供同toString方法
        List<User> list = reader.readAll(User.class);
        return Result.success(userService.saveBatch(list));

//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }
//
//        userService.saveBatch(users);



    }

    @GetMapping("/on")
    public Result on(@RequestParam String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        user.setStatus(1);
        userService.updateById(user);

        CheckLog checkLog=new CheckLog();
        checkLog.setUsername(username);
        checkLog.setInfo("打卡上班");
        checkLogService.save(checkLog);
//        queryWrapper.or().like("address",address);


//        //打印当前登录用户的用户名
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println(currentUser.getUsername());

        return Result.success();
    }

    @GetMapping("/off")
    public Result off(@RequestParam String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        user.setStatus(0);
        userService.updateById(user);

        CheckLog checkLog=new CheckLog();
        checkLog.setUsername(username);
        checkLog.setInfo("打卡下班");
        checkLogService.save(checkLog);
//        queryWrapper.or().like("address",address);


//        //打印当前登录用户的用户名
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println(currentUser.getUsername());

        return Result.success();
    }
}

