package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.properties.BanSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BanSortingOption {

    private Sort.Direction direction;

    private BanSortingProperties property;

}
