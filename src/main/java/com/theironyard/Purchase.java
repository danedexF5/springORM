package com.theironyard;


import javax.persistence.*;

@Entity
public class Purchase {

        @Id
        @GeneratedValue
        Integer id;

        String date;
        String credit_card;
        String cvv;
        String category;

        @ManyToOne
        Customer customer;

}
