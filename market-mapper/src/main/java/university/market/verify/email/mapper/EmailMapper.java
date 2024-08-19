package university.market.verify.email.mapper;

import org.apache.ibatis.annotations.Mapper;
import university.market.verify.email.domain.EmailVO;

@Mapper
public interface EmailMapper {

    void saveVerificationCode(EmailVO emailVO);

    EmailVO findEmailToVerification(String Email);
}
