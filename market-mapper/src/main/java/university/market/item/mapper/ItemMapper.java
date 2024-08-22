package university.market.item.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import university.market.item.domain.ItemVO;

@Mapper
public interface ItemMapper {
    void postItem(ItemVO itemVO);

    ItemVO getItemById(Long id);

    List<ItemVO> getItemsByUniversity(int universityType);

    void updateItem(Long id, ItemVO item);

    void deleteItem(Long id);
}
