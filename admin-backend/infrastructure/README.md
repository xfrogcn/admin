# JDBC Repository代码示例
```java
package com.xfrog.platform.infrastructure.persistent.dao.jdbc;

import com.xfrog.platform.infrastructure.persistent.dataobject.UserDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserJDBCRepository extends PagingAndSortingRepository<UserDto, String>, CrudRepository<UserDto, String> {
    @Query("SELECT * FROM users ORDER BY user_name")
    List<UserDto> findByPage();
}
```
# MyBatis Repository代码示例
```java
package com.xfrog.platform.infrastructure.persistent.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xfrog.platform.infrastructure.persistent.dataobject.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserDto> {
    @Select("SELECT * FROM users WHERE user_name = #{userName}")
    List<UserDto> findAll(IPage<UserDto> page, @Param("userName")String userName);

    List<UserDto> findAllByMapper(IPage<UserDto> page, @Param("userName") String userName);

    @SelectProvider(type = UserMapperSelectProvider.class, method = "findAll")
    List<UserDto> findAllBySelectProvider(IPage<UserDto> page, @Param("userName") String userName);

    @Select("SELECT * FROM users WHERE user_name = #{userName}")
    List<UserDto> findAllByPageHelper(@Param("userName") String userName);

    class UserMapperSelectProvider {
        public static String findAll(IPage<UserDto> page, @Param("userName") String userName) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("users a");
            if (StringUtils.hasText(userName)) {
                sql.WHERE("a.user_name = #{userName}");
            }
            return "<script>" + sql + "</script>";
        }
    }
}

```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTO Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xfrog.platform.infrastructure.persistent.dao.mapper.UserMapper">
    <select id="findAllByMapper" resultType="com.xfrog.platform.infrastructure.persistent.dataobject.UserDto">
        SELECT  * FROM users a
        <where>
            <if test="userName != null and userName != ''">
                a.user_name = #{userName}
            </if>
        </where>
    </select>
</mapper>
```
```java
package com.xfrog.platform.infrastructure.persistent.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.xfrog.platform.domain.aggregate.User;
import com.xfrog.platform.domain.repository.UserDomainRepository;
import com.xfrog.platform.infrastructure.persistent.converter.UserDtoConverter;
import com.xfrog.platform.infrastructure.persistent.dao.jdbc.UserJDBCRepository;
import com.xfrog.platform.infrastructure.persistent.dao.mapper.UserMapper;
import com.xfrog.platform.infrastructure.persistent.dataobject.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDomainRepositoryImpl implements UserDomainRepository {
    private final UserJDBCRepository userJDBCRepository;
    private final UserMapper userMapper;
    @Override
    public List<User> saveAll(List<User> users) {

        List<UserDto> userDtos = UserDtoConverter.INSTANCE.toDtoList(users);

        return UserDtoConverter.INSTANCE.toDomainList(userJDBCRepository.saveAll(userDtos));
    }

    @Override
    public List<User> findByPage() {
        Page<UserDto> page = Page.of(1, 10);

       // var list = userMapper.findAll(page, "un");
       // var list = userMapper.findAllBySelectProvider(page, "un");
        PageHelper.startPage(1, 10)
                .doSelectPage(() -> userMapper.findAllByPageHelper("un"));
        return null;
    }

}

```