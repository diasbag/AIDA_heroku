package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
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
	@JsonIgnore
	@Column(name = "registration_date")
	private Date registrationDate;

	@Column(name = "reset_token")
	private String resetToken;

	public User() {
	}

	public User(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}

	public User(ERole unauthorized) {
		roles.add(new Role(unauthorized));
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}