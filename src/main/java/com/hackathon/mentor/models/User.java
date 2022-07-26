package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(    name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "firstname")
	private String firstname;

	@NotBlank
	@Size(max = 20)
	@Column(name = "lastname")
	private String lastname;


	@Size(max = 20)
	@Column(name = "middlename")
	private String middlename;
	@NotBlank
	@Size(max = 20)
	private String username;
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@OneToOne(cascade=CascadeType.PERSIST)
	private Image image;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<>();

	@Column(name = "status")
	private Boolean status;

	public User() {
	}

	public User(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = firstname+lastname;
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstname='" + firstname +
		", lastname='" + lastname +
		", username='" + username +
		", email='" + email +
		", roles=" + roles +
		", status=" + status +
				'}';
	}
}