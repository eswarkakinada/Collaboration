package com.niit.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.UserDAO;
import com.niit.model.User;

@RestController
public class UserController {
	
	private static final Logger logger	= LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserDAO userDAO;
	
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers(){
		logger.debug("calling method listAllUsers");
		List<User> user=userDAO.list();
		if(user.isEmpty()){
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(user,HttpStatus.OK);
	}

	@RequestMapping(value="/user/",method=RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user){
		logger.debug("calling method createUser" + user.getUser_id());
		if(userDAO.get(user.getUser_id())==null){
			userDAO.save(user);			
		}
		logger.debug("user already exists with id:" + user.getUser_id());
		user.setErrorMessage("user already exists with id:" + user.getUser_id());
		return new ResponseEntity<User>(user,HttpStatus.OK);
			}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable("id") int user_id,@RequestBody User user){
		logger.debug("calling method updateUser" + user.getUser_id());
		if(userDAO.get(user_id)==null){
			logger.debug("user does not exists with id:" + user.getUser_id());		
			user=new User();
			user.setErrorMessage("user does not exists with id:" + user.getUser_id());
			return new ResponseEntity<User> (user,HttpStatus.NOT_FOUND);
		}
		userDAO.update(user);
		logger.debug("user updated successfully");
		return new ResponseEntity<User> (user,HttpStatus.OK);		
	}

	@RequestMapping(value="/user/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable("id") int user_id){
		logger.debug("calling method deleteUser for user id: " + user_id);
		User user=userDAO.get(user_id);
		if(user==null){
			logger.debug("user does not exists with id:" + user_id);
			user=new User();
			user.setErrorMessage("user does not exists with id:" + user_id);
			return new ResponseEntity<User> (user,HttpStatus.NOT_FOUND);	
		}
		userDAO.delete(user_id);
		logger.debug("user deleted successfully");
		return new ResponseEntity<User> (user,HttpStatus.OK);		
	}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") int user_id){
		logger.debug("calling method getUser for user id: " + user_id);
		User user=userDAO.get(user_id);
		if(user==null){
			logger.debug("user does not exists with id:" + user_id);
			user=new User();
			user.setErrorMessage("user does not exists with id:" + user_id);
			return new ResponseEntity<User> (user,HttpStatus.NOT_FOUND);
		}
		logger.debug("user exists with id:" + user_id);
		return new ResponseEntity<User> (user,HttpStatus.OK);
	}


}


