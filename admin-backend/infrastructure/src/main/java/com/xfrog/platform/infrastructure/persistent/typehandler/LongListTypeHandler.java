package com.xfrog.platform.infrastructure.persistent.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        String value = parameter.stream().map(String::valueOf).collect(Collectors.joining(","));
        ps.setString(i, value);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return parseLongList(value);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getNString(columnIndex);
        return parseLongList(value);
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return parseLongList(value);
    }

    private List<Long> parseLongList(String value) {
        if (StringUtils.hasText(value)) {
            return Arrays.stream(value.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
