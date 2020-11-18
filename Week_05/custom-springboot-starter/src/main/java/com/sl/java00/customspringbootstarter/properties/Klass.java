package com.sl.java00.customspringbootstarter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "custom.klass")
public class Klass { 
    
    List<Student> students;
    
    public void dong(){
        System.out.println(this.getStudents());
    }
    
}
