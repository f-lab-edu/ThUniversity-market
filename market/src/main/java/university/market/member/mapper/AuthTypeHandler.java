package university.market.member.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import university.market.member.domain.auth.AuthType;

public class AuthTypeHandler extends BaseTypeHandler<AuthType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AuthType parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public AuthType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return AuthType.fromString(name);
    }

    @Override
    public AuthType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        return AuthType.fromString(name);
    }

    @Override
    public AuthType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        return AuthType.fromString(name);
    }
}
