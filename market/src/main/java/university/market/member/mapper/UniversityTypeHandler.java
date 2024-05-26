package university.market.member.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import university.market.member.domain.university.UniversityType;

public class UniversityTypeHandler extends BaseTypeHandler<UniversityType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UniversityType parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public UniversityType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return UniversityType.fromValue(value);
    }

    @Override
    public UniversityType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return UniversityType.fromValue(value);
    }

    @Override
    public UniversityType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return UniversityType.fromValue(value);
    }
}
