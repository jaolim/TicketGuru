package spagetti.tiimi.ticketguru.domain;

import spagetti.tiimi.ticketguru.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "users")
public class AppUser {

    @JsonView(Views.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userid;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Username is required")
    @Size(max = 32)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonView(Views.Internal.class)
    @NotBlank(message = "Password is required")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @JsonView(Views.Public.class)
    @NotBlank(message = "Firstname is required")
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @JsonView(Views.Limited.class)
    @NotBlank(message = "Lastname is required")
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotBlank
    @JsonView(Views.Limited.class)
    private String userRole;

    public AppUser() {

    }

    public AppUser(String username, String passwordHash, String firstname, String lastname, String userRole) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userRole = userRole;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "AppUser [userid=" + userid + ", username=" + username + ", passwordHash=" + passwordHash
                + ", firstname=" + firstname + ", lastname=" + lastname + ", userRole=" + userRole + "]";
    }

   

}
