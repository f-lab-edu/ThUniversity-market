package university.market.tag.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {
    long id;

    String title;

    Timestamp createdAt;

    Timestamp updatedAt;

    @Builder
    public TagVO(String title) {
        this.title = title;
    }
}
