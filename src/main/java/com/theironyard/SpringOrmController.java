package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Controller
public class SpringOrmController {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    PurchaseRepo purchaseRepo;

    //In your controller, create a @PostConstruct method called init to parse the CSV files
    @PostConstruct
    public void init() throws FileNotFoundException {

        //and add it to a repository
        if (customerRepo.count() == 0) {
            //create new file from csv
            File f = new File("customers.csv");
            //create scanner for the file
            Scanner scanner = new Scanner(f);
            //scan next line while it's there
            scanner.nextLine();
            //todo: something isn't working in here.- UPDATE, looks good now.

            //For each CSV file, you should loop over each line, parse each column into a Customer or Purchase object
            while (scanner.hasNext()) {
                //get the next line
                String line = scanner.nextLine();
                //split columns at the comma
                String[] columns = line.split(",");
                //create new cust
                Customer newCustomer = new Customer();

                newCustomer.name = columns[0];
                newCustomer.email = columns[1];
                //save cust into repo
                customerRepo.save(newCustomer);
            }

        }

        if (purchaseRepo.count() == 0) {
            //create new file from csv
            File f = new File("purchases.csv");
            //create new scanner for file
            Scanner scanner = new Scanner(f);
            //scan next line while it's there
            scanner.nextLine();

            while (scanner.hasNext()) {

                String line = scanner.nextLine();

                String[] columns = line.split(",");

                Purchase newPurchase = new Purchase();

                newPurchase.date = columns[1];
                newPurchase.credit_card = columns[2];
                newPurchase.cvv = columns[3];
                newPurchase.category = columns[4];


                int customerID = Integer.valueOf(columns[0]);

                Customer customer = customerRepo.findOne(customerID);

                //this was holding it all up, took a while
                newPurchase.customer = customer;
                
                purchaseRepo.save(newPurchase);
            }
        }
    }


    @RequestMapping("/")
    //Modify your / route to take a category parameter
    public String home(Model model, String category, @RequestParam(defaultValue = "0") int page) {

        PageRequest pr = new PageRequest(page, 20);

        Page p;

        if (category != null) {
            //If the parameter isn't null, call the method you made instead of findAll
            p = purchaseRepo.findPurchaseByCategory(pr, category);
        } else {

            p = purchaseRepo.findAll(pr);
        }

        //add it to the model
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("category", category);
        model.addAttribute("purchases", p.getContent());
        model.addAttribute("showNext", p.hasNext());

        //return the home template
        return "home";
    }

}

