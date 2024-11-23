package com.yuki.exam.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName exam_room
 */
@TableName(value ="exam_room")
@Data
public class ExamRoom implements Serializable {
    /**
     * 考场id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 考场编号（也是考场的唯一标识，但相比于id，拥有更好的可读性
     */
    private String roomNumber;

    /**
     * 考场位置
     */
    private String location;

    /**
     * 考场容量（能容纳多少人数）
     */
    private Integer capacity;

    /**
     * 考场的状态信息（空闲、占用）
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}