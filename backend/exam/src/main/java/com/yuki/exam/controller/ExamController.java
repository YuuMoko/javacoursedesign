package com.yuki.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuki.exam.common.BaseResponse;
import com.yuki.exam.common.ResultUtils;
import com.yuki.exam.model.domain.ExamRoom;
import com.yuki.exam.model.dto.ExamQuery;
import com.yuki.exam.service.ExamRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Resource
    ExamRoomService examRoomService;

    @GetMapping("/get")
    public BaseResponse<List<ExamRoom>> getExam(ExamQuery examQuery) {
        QueryWrapper<ExamRoom> queryWrapper = new QueryWrapper<>();
        String type = examQuery.getType();
        List<ExamRoom> examRoomList;
        String content = examQuery.getContent();

        if (type == null || content == null) {
            examRoomService.list(queryWrapper);
            examRoomList = examRoomService.list(queryWrapper);
            return ResultUtils.success(examRoomList);
        }

        queryWrapper.like(type, content);
        queryWrapper.like(type, content);
        examRoomList = examRoomService.list(queryWrapper);
        return ResultUtils.success(examRoomList);
    }

    @PostMapping("/remove")
    public BaseResponse<Boolean> delete(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        boolean result = examRoomService.removeById(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<ExamRoom> update(@RequestBody ExamRoom examRoom) {
        boolean result = examRoomService.updateById(examRoom);
        if (result) {
            return ResultUtils.success(examRoom);
        }
        return ResultUtils.error("更新失败");
    }

    @PostMapping("/create")
    public BaseResponse<ExamRoom> create(@RequestBody ExamRoom examRoom) {
        boolean result = examRoomService.save(examRoom);
        if (result) {
            return ResultUtils.success(examRoom);
        }
        return ResultUtils.error("创建失败");
    }



}
