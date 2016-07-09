package com.theironyard;

import org.springframework.data.repository.CrudRepository;


public interface CustomerRepo extends CrudRepository<Customer,Integer> {

}
