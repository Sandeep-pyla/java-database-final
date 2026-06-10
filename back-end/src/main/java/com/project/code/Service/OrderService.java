package com.project.code.Service;


import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;
    private CustomerRepository customerRepository;
    private StoreRepository storeRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private OrderItemRepository orderItemRepository;

// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) throws Exception{

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.
        Customer customer = new Customer();
        if (customerRepository.findByEmail(placeOrderRequest.getCustomerEmail()) == null) {
            customer.setId(placeOrderRequest.getStoreId());
            customer.setEmail(placeOrderRequest.getCustomerEmail());
            customer.setName(placeOrderRequest.getCustomerName());
            customer.setPhone(placeOrderRequest.getCustomerPhone());
            customerRepository.save(customer);
        }else{
            customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
        }
// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.
        if(storeRepository.findByid(placeOrderRequest.getStoreId()) == null){
            throw new RuntimeException();
        }
// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.
            OrderDetails or = new OrderDetails();
            or.setCustomer(customer);
            or.setStore(storeRepository.findByid(placeOrderRequest.getStoreId()));
            or.setTotalPrice(placeOrderRequest.getTotalPrice());
            or.setDate(java.time.LocalDateTime.now());
            orderDetailsRepository.save(or);
// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.
        inventoryRepository.findByProductIdandStoreId(or.getId(), placeOrderRequest.getStoreId());

    }
}
