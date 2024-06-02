package university.market.email.mapper;

import org.apache.ibatis.annotations.Mapper;
import university.market.email.domain.EmailVO;

@Mapper
public interface EmailMapper {

    void saveVerificationCode(EmailVO emailVO);

    EmailVO findEmailToVerification(String Email);
}
