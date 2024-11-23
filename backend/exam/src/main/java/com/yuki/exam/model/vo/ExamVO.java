package com.yuki.exam.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExamVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String room_number;
    private String location;
    private Integer capacity;
    private Integer status;

}
