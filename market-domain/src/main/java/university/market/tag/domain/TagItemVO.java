package university.market.tag.domain;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import university.market.item.domain.ItemVO;

@Data
@NoArgsConstructor
public class TagItemVO {
    long id;

    TagVO tag;

    ItemVO item;

    Timestamp createdAt;

    @Builder
    public TagItemVO(TagVO tag, ItemVO item) {
        this.tag = tag;
        this.item = item;
    }
}
