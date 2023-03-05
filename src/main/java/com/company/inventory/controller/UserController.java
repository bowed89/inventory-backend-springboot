package com.company.inventory.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.security.TokenUtils;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/user")
public class UserController {
	@GetMapping("/verify/{token}")
	public boolean verifyToken(@PathVariable String token) {
		UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(token);
		if(usernamePAT == null) {
			return false;
		}
		return true;
	}
}
