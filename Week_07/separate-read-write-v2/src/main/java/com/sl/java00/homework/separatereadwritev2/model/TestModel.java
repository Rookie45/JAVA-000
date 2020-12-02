package com.sl.java00.homework.separatereadwritev2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
}
