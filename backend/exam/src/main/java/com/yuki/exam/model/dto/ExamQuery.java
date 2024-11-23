package com.yuki.exam.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;    // 查询类型
    private String content; // 查询内容
}
