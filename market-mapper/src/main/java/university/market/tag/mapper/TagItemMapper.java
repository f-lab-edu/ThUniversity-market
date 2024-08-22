package university.market.tag.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import university.market.tag.domain.TagItemVO;

@Mapper
public interface TagItemMapper {

    void insertTagItem(TagItemVO tagItem);

    List<TagItemVO> findTagItemsByTagId(long tagId);

    List<TagItemVO> findTagItemsByItemId(long itemId);

    Optional<TagItemVO> findTagItemById(long id);

    void deleteTagItem(long tagId);
}
