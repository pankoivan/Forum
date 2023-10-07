package org.forum.main.services.implementations;

import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.repositories.BanRepository;
import org.forum.main.services.interfaces.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;

@Service
public class BanServiceImpl implements BanService {

    private final BanRepository repository;

    @Autowired
    public BanServiceImpl(BanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ban empty() {
        /*User userWhoAssigned;
        try {
            userWhoAssigned = AuthenticationUtils.extractCurrentUser(authentication);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ServiceException("Current user cannot be extracted", e);
        }
        return Ban.builder()
                .userWhoAssigned(userWhoAssigned)
                .user(user)
                .build();*/
        return new Ban();
    }

    @Override
    public void save(Ban ban, User user, User userWhoAssigned) {
        ban.setUser(user);
        ban.setUserWhoAssigned(userWhoAssigned);
        ban.setStartDate(LocalDate.now());
        repository.save(ban);
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public boolean savingValidation(Ban validatedObject, BindingResult bindingResult) {
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

}
