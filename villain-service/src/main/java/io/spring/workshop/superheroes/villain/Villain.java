package io.spring.workshop.superheroes.villain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "villain")
public class Villain {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(min = 3, max = 50)
    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "other_name")
    protected String otherName;

    @Min(1)
    @Column(name = "level", nullable = false)
    protected Integer level;

    @Column(name = "picture")
    protected String picture;

    @Column(name = "powers")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    protected String powers;

    @Override
    public String toString() {
        return "Villian{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", otherName='" + otherName + '\'' +
                ", level=" + level +
                ", picture='" + picture + '\'' +
                ", powers='" + powers + '\'' +
                '}';
    }

    public String getPowers() {
        return powers;
    }

    public void setPowers(String powers) {
        this.powers = powers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}