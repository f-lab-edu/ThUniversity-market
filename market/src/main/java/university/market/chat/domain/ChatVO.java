package university.market.chat.domain;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.item.domain.ItemVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVO {
    private Long id;

    private String title;

    private ItemVO item;

    private boolean isDeleted;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public ChatVO(String title, ItemVO item) {
        this.title = title;
        this.item = item;
        isDeleted = false;
    }
}
