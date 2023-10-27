package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.repositories.BanRepository;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.List;

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
    public Ban empty() {
        return new Ban();
    }

    @Override
    public List<Ban> findAllByUserId(Integer id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public List<Ban> findAllByUserWhoAssignedId(Integer id) {
        return repository.findAllByUserWhoAssignedId(id);
    }

    @Override
    public void unban(User user) {
        Ban currentBan = user.getCurrentBan();
        currentBan.setEndDate(LocalDate.now());
        repository.save(currentBan);
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
    public List<Ban> onPage(List<Ban> items, int pageNumber) {
        return onPageImpl(items, pageNumber, PaginationConstants.FOR_BANS);
    }

    @Override
    public int pagesCount(List<Ban> items) {
        return pagesCountImpl(items, PaginationConstants.FOR_BANS);
    }

}
