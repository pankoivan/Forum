package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserSortingOption implements SortingOption<UserSortingProperties> {

    private Sort.Direction direction;

    private UserSortingProperties property;

    @Override
    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public UserSortingProperties getProperty() {
        return property;
    }

}
