package com.petsshop.service;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petsshop.dto.UserDTO;
import com.petsshop.model.User;
import com.petsshop.util.UserNotFoundException;

@Service(value = "userService")
@Transactional
public class UserService {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public User getUser(UserDTO userDTO) throws UserNotFoundException {
		Session session = getSession();
		Query<User> query = session.createQuery("From User as user where user.login=:login and user.password=:password");
		query.setParameter("login", userDTO.getLogin());
		query.setParameter("password", userDTO.getPassword());
		if (query.list().size() == 0) {
			throw new UserNotFoundException("User does not exist.");
		} else {
			return query.getSingleResult();
		}

	}

	private Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		return session;
	}

}
