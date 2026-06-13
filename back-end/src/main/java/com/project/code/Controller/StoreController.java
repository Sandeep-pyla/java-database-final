package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/store` URL using `@RequestMapping("/store")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `StoreRepository` for managing store data.
//        - `OrderService` for handling order-related functionality.
    @Autowired
    private StoreRepository storeRepository;
    private OrderService orderService;

 // 3. Define the `addStore` Method:
//    - Annotate with `@PostMapping` to create an endpoint for adding a new store.
//    - Accept `Store` object in the request body.
//    - Return a success message in a `Map<String, String>` with the key `message` containing store creation confirmation.
    @PostMapping("/addStore")
    public Map<String, String> addStore(@RequestBody Store store){
        if(validateStore(store.getId())){
            storeRepository.save(store);
            return Map.of("message", "Success: Store with id" + store.getId() + "is added");
        }else{
            return Map.of("message", "ERROR: store already exists");
        }
    }

 // 4. Define the `validateStore` Method:
//    - Annotate with `@GetMapping("validate/{storeId}")` to check if a store exists by its `storeId`.
//    - Return a **boolean** indicating if the store exists.
    @GetMapping("validate/{storeid}")
    public boolean validateStore(@PathVariable("storeid") long storeid){
        return storeRepository.findByid(storeid) != null;
    }

 // 5. Define the `placeOrder` Method:
//    - Annotate with `@PostMapping("/placeOrder")` to handle order placement.
//    - Accept `PlaceOrderRequestDTO` in the request body.
//    - Return a success message with key `message` if the order is successfully placed.
//    - Return an error message with key `Error` if there is an issue processing the order.
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO){
        try{orderService.saveOrder(placeOrderRequestDTO);
        return Map.of("message", "Order placed successfully");}
        catch(Exception e){
            return Map.of("Error", "placing order failed" + e.getMessage());
        }
    }
}
