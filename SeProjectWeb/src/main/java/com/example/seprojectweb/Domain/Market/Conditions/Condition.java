package com.example.seprojectweb.Domain.Market.Conditions;

import com.example.seprojectweb.Domain.Market.ShoppingBasket;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Condition {


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    protected String description;


    public Condition(String description) {
        this.description = description;
    }

    public Condition() {

    }


    public abstract boolean checkCondition(ShoppingBasket shoppingBasket);

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return id == condition.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
