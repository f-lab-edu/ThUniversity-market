package university.market.chat.room.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.chat.room.domain.chatauth.ChatAuthType;
import university.market.member.domain.MemberVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberVO {
    private Long id;

    private ChatAuthType chatAuth;

    private ChatVO chat;

    private MemberVO member;

    private Timestamp lastReadAt;

    private Timestamp createdAt;

    @Builder
    public ChatMemberVO(ChatAuthType chatAuth, ChatVO chat, MemberVO member) {
        this.chatAuth = chatAuth;
        this.chat = chat;
        this.member = member;
    }
}
