package com.sl.java00.springboot.homework.lesson10.mapper;

import com.sl.java00.springboot.homework.lesson10.model.Student;
import com.sl.java00.springboot.homework.lesson10.model.StudentExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface StudentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    long countByExample(StudentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    int deleteByExample(StudentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    @Delete({
        "delete from tb_student",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    @Insert({
        "insert into tb_student (name)",
        "values (#{name,jdbcType=VARCHAR,typeHandler=com.sl.java00.springboot.homework.lesson10.handler.NameTypeHandler})"
    })
    @SelectKey(statement="CALL IDENTITY()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Student record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    int insertSelective(Student record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    List<Student> selectByExampleWithRowbounds(StudentExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    List<Student> selectByExample(StudentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    @Select({
        "select",
        "id, name",
        "from tb_student",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.sl.java00.springboot.homework.lesson10.mapper.StudentMapper.BaseResultMap")
    Student selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    int updateByExampleSelective(@Param("record") Student record, @Param("example") StudentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    int updateByExample(@Param("record") Student record, @Param("example") StudentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    int updateByPrimaryKeySelective(Student record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_student
     *
     * @mbg.generated Wed Nov 18 21:23:44 CST 2020
     */
    @Update({
        "update tb_student",
        "set name = #{name,jdbcType=VARCHAR,typeHandler=com.sl.java00.springboot.homework.lesson10.handler.NameTypeHandler}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Student record);
}