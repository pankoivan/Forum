package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.forum.auxiliary.constants.sorting.DefaultSortingOptionConstants;
import org.forum.auxiliary.sorting.options.BanSortingOption;
import org.forum.auxiliary.utils.SearchingUtils;
import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.repositories.BanRepository;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
public class BanServiceImpl extends DefaultPaginationImpl<Ban> implements BanService {

    private final BanRepository repository;

    @Autowired
    public BanServiceImpl(BanRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Ban ban, User user, User userWhoAssigned) {
        if (user.hasRole("ROLE_OWNER")) {
            return;
        }
        ban.setUser(user);
        ban.setUserWhoAssigned(userWhoAssigned);
        ban.setStartDate(LocalDate.now());
        repository.save(ban);
    }

    @Override
    public void unban(User user) {
        Ban currentBan = user.getCurrentBan();
        currentBan.setEndDate(LocalDate.now());
        repository.save(currentBan);
    }

    @Override
    public Ban empty() {
        return new Ban();
    }

    @Override
    public boolean savingValidation(Ban validatedObject, BindingResult bindingResult) {
        if (validatedObject.getEndDate() == null) {
            bindingResult.addError(new ObjectError("endDateIsNull",
                    "Дата окончания бана должна быть установлена"));
            return true;
        }
        if (!validatedObject.getEndDate().isAfter(LocalDate.now())) {
            bindingResult.addError(new ObjectError("endDateIsNotAfterNow",
                    "Бан должен длиться хотя бы 1 день"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Ban validatedObject) {
        return null;
    }

    @Override
    public List<Ban> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Ban> findAllSorted(BanSortingOption option) {
        return mySwitch(option,
                () -> repository.findAll(Sort.by(option.getDirection(), "user.nickname")),
                () -> repository.findAll(Sort.by(option.getDirection(), "startDate")),
                () -> repository.findAll(Sort.by(option.getDirection(), "endDate")),
                () -> repository.findAll(Sort.by(option.getDirection(), "reason")),
                () -> repository.findAll(Sort.by(option.getDirection(), "userWhoAssigned.nickname"))
        );
    }

    @Override
    public List<Ban> findAllByUserId(Integer id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public List<Ban> findAllByUserIdSorted(Integer id, BanSortingOption option) {
        return mySwitch(option,
                () -> repository.findAllByUserId(id, Sort.by(option.getDirection(), "user.nickname")),
                () -> repository.findAllByUserId(id, Sort.by(option.getDirection(), "startDate")),
                () -> repository.findAllByUserId(id, Sort.by(option.getDirection(), "endDate")),
                () -> repository.findAllByUserId(id, Sort.by(option.getDirection(), "reason")),
                () -> repository.findAllByUserId(id, Sort.by(option.getDirection(), "userWhoAssigned.nickname"))
        );
    }

    @Override
    public List<Ban> findAllByUserIdSorted(Integer id) {
        return findAllByUserIdSorted(id, DefaultSortingOptionConstants.FOR_BANS);
    }

    @Override
    public List<Ban> findAllByUserWhoAssignedId(Integer id) {
        return repository.findAllByUserWhoAssignedId(id);
    }

    @Override
    public List<Ban> findAllByUserWhoAssignedIdSorted(Integer id, BanSortingOption option) {
        return mySwitch(option,
                () -> repository.findAllByUserWhoAssignedId(id, Sort.by(option.getDirection(), "user.nickname")),
                () -> repository.findAllByUserWhoAssignedId(id, Sort.by(option.getDirection(), "startDate")),
                () -> repository.findAllByUserWhoAssignedId(id, Sort.by(option.getDirection(), "endDate")),
                () -> repository.findAllByUserWhoAssignedId(id, Sort.by(option.getDirection(), "reason")),
                () -> repository.findAllByUserWhoAssignedId(id, Sort.by(option.getDirection(), "userWhoAssigned.nickname"))
        );
    }

    @Override
    public List<Ban> findAllByUserWhoAssignedIdSorted(Integer id) {
        return findAllByUserWhoAssignedIdSorted(id, DefaultSortingOptionConstants.FOR_BANS);
    }

    @Override
    public List<Ban> onPage(List<Ban> items, int pageNumber) {
        return onPageImpl(items, pageNumber, PaginationConstants.FOR_BANS);
    }

    @Override
    public int pagesCount(List<Ban> items) {
        return pagesCountImpl(items, PaginationConstants.FOR_BANS);
    }

    @Override
    public BanSortingOption emptySortingOption() {
        return new BanSortingOption();
    }

    @Override
    public List<Ban> findAllSorted() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_BANS);
    }

    @Override
    public List<Ban> search(List<Ban> sourceList, String searchedString) {
        return sourceList.stream()
                .filter(ban -> SearchingUtils.search(
                        searchedString,
                        ban.getUser().getNickname(),
                        ban.getFormattedStartDate(),
                        ban.getFormattedEndDate(),
                        ban.getReason(),
                        ban.getUserWhoAssigned().getNickname()
                ))
                .toList();
    }

    @SafeVarargs
    private List<Ban> mySwitch(BanSortingOption option, Supplier<List<Ban>>... suppliers) {
        return switch (option.getProperty()) {
            case BY_BANNED_USER_NICKNAME -> suppliers[0].get();
            case BY_START_DATE -> suppliers[1].get();
            case BY_END_DATE -> suppliers[2].get();
            case BY_REASON -> suppliers[3].get();
            case BY_AUTHOR_NICKNAME -> suppliers[4].get();
        };
    }

}
