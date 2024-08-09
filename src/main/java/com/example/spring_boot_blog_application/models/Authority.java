package com.example.spring_boot_blog_application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements Serializable {

    @Id
    @Column(length = 16)
    private String name;

    @ManyToMany(mappedBy = "authority")
    @JsonIgnore
    private List<Account> account;

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + "'" +
                "}";
    }

}
