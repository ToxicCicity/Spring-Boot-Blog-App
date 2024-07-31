package com.example.spring_boot_blog_application.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    //TODO: Comment out the following lines
    @JsonManagedReference // Prevents infinite recursion
    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "account_authority",
      joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + "'" +
                ", password='" + password + "'" +
                ", firstName='" + firstName + "'" +
                ", lastName='" + lastName + "'" +
                ", authorities=" + authorities +
                '}';
    }
}
