package com.hackathon.mentor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(    name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Getter
@Setter
@AllArgsConstructor
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

	@NotNull
	@Column(name = "telegram")
	private String telegram;
//	@NotBlank
//	@Size(max = 20)
//	private String username;
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

	@OneToOne
	@JoinColumn(name = "rating_id")
	private Rating rating;

	public User() {
	}

	public User(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstname='" + firstname +
		", lastname='" + lastname +
		", email='" + email +
		", roles=" + roles +
		", status=" + status +
				'}';
	}
}