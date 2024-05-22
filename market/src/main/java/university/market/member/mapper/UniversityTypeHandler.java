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
        ps.setString(i, parameter.name());
    }

    @Override
    public UniversityType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return UniversityType.fromString(name);
    }

    @Override
    public UniversityType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        return UniversityType.fromString(name);
    }

    @Override
    public UniversityType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        return UniversityType.fromString(name);
    }
}
