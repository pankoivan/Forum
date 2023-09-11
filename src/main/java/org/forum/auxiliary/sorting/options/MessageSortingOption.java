package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MessageSortingOption implements SortingOption<MessageSortingProperties> {

    private Sort.Direction direction;

    private MessageSortingProperties property;

    @Override
    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public MessageSortingProperties getProperty() {
        return property;
    }

}
