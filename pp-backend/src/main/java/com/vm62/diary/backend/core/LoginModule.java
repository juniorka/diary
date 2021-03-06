package com.vm62.diary.backend.core;

import com.google.inject.Inject;
import com.vm62.diary.backend.core.bean.UserBean;
import com.vm62.diary.backend.core.dao.UserDAO;
import com.vm62.diary.backend.core.entities.User;
import com.vm62.diary.common.ErrorType;
import com.vm62.diary.common.ExceptionFactory;
import com.vm62.diary.common.ServiceException;
import com.vm62.diary.common.constants.Gender;
import com.vm62.diary.common.password.Password;
import com.vm62.diary.common.utils.ValidationUtils;

import java.util.Date;
import java.util.UUID;

public class LoginModule {

    private static final String MEN = "men";

    @Inject
    private UserBean userBean;

    @Inject
    private UserDAO userDAO;


    public User createUser(String firstName, String lastName, Password password, String sex, String studyGroup, Date birthday, String email) throws ServiceException {
        ValidationUtils.ifNullOrEmpty(firstName, ErrorType.CANNOT_BE_NULL_OR_EMPTY, "first name");
        ValidationUtils.ifNullOrEmpty(lastName, ErrorType.CANNOT_BE_NULL_OR_EMPTY, "last name");
        ValidationUtils.ifNullOrEmpty(studyGroup, ErrorType.CANNOT_BE_NULL_OR_EMPTY, "study group");
        ValidationUtils.ifNullOrEmpty(email, ErrorType.CANNOT_BE_NULL_OR_EMPTY, "email");

        if (isUserEmailExist(email)) {
            ExceptionFactory.throwServiceException(ErrorType.CANNOT_CREATE_USER_EMAIL_ALREADY_EXIST, email);
        }

        User user = new User(firstName.trim(),
                lastName.trim(),
                password.encode(),
                MEN.equals(sex) ? Gender.M : Gender.W,
                studyGroup.trim(),
                birthday,
                email.trim(),
                UUID.randomUUID().toString(),
                Boolean.FALSE);

        userBean.createUser(user);
        return user;
    }

    public boolean isUserEmailExist(String email) throws ServiceException {
        ValidationUtils.ifNullOrEmpty(email, ErrorType.CANNOT_BE_NULL_OR_EMPTY, email);

        return userDAO.isUserEmailExists(email);
    }

}
