package university.market.tag.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import university.market.tag.domain.TagVO;

@Mapper
public interface TagMapper {
    void insertTag(TagVO tagVO);

    Optional<TagVO> findTagById(long id);

    Optional<TagVO> findTagByTitle(String title);

    void deleteTag(long id);
}
