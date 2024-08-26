package university.market.chat.message.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.chat.room.domain.ChatVO;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {
    private Long id;

    private String content;

    private MemberVO sender;

    private ChatVO chat;

    private Timestamp createdAt;

    @Builder
    protected MessageVO(String content, MemberVO sender, ChatVO chat) {
        this.content = content;
        this.sender = sender;
        this.chat = chat;
    }
}
