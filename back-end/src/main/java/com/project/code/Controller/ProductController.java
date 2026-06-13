package com.project.code.Controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/product` URL using `@RequestMapping("/product")`.


// 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `ProductRepository` for CRUD operations on products.
//        - `ServiceClass` for product validation and business logic.
//        - `InventoryRepository` for managing the inventory linked to products.
    @Autowired
    private ProductRepository productRepository;
    private ServiceClass serviceClass;
    private InventoryRepository inventoryRepository;

// 3. Define the `addProduct` Method:
//    - Annotate with `@PostMapping` to handle POST requests for adding a new product.
//    - Accept `Product` object in the request body.
//    - Validate product existence using `validateProduct()` in `ServiceClass`.
//    - Save the valid product using `save()` method of `ProductRepository`.
//    - Catch exceptions (e.g., `DataIntegrityViolationException`) and return appropriate error message.
    @PostMapping("/addProduct")
    public Map<String, String> addProduct(@RequestBody Product product){
        Map<String, String> response = new HashMap<>();
        try{if(serviceClass.validateProduct(product)){
            productRepository.save(product);
            response.put("message", "Success: Product added");
        }else{
            response.put("message", "ERROR: product already exits");
        }}catch(Exception e){
            System.out.println(e.getMessage());
        }
        return response;
    }

// 4. Define the `getProductbyId` Method:
//    - Annotate with `@GetMapping("/product/{id}")` to handle GET requests for retrieving a product by ID.
//    - Accept product ID via `@PathVariable`.
//    - Use `findById(id)` method from `ProductRepository` to fetch the product.
//    - Return the product in a `Map<String, Object>` with key `products`.
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductById(@PathVariable("id") long id){
        Map<String, Object> output = new HashMap<>();
        output.put("products", productRepository.findByid(id));
        return output;
    }

 // 5. Define the `updateProduct` Method:
//    - Annotate with `@PutMapping` to handle PUT requests for updating an existing product.
//    - Accept updated `Product` object in the request body.
//    - Use `save()` method from `ProductRepository` to update the product.
//    - Return a success message with key `message` after updating the product.
    @PutMapping("/updateProduct")
    public Map<String, String> updateProduct(@RequestBody Product product){
        try{productRepository.save(product);}
        catch(Exception e){
            System.out.println("Error while product updation: "+e.getMessage());
        }
        return Map.of("message", "product got updated successfully");
    }

// 6. Define the `filterbyCategoryProduct` Method:
//    - Annotate with `@GetMapping("/category/{name}/{category}")` to handle GET requests for filtering products by `name` and `category`.
//    - Use conditional filtering logic if `name` or `category` is `"null"`.
//    - Fetch products based on category using methods like `findByCategory()` or `findProductBySubNameAndCategory()`.
//    - Return filtered products in a `Map<String, Object>` with key `products`.
    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterCategoryProduct(@PathVariable("name") String name, @PathVariable("category") String category){
        Map<String, Object> output = new HashMap<>();
        if (name == null || category == null) {
            if(name == null){
                output.put("products", productRepository.findByCategory(category));
            }else{
                output.put("products", productRepository.findProductBySubName(name));
            }
        }else{
            output.put("products", productRepository.findProductBySubNameAndCategory(name,category));
        }
        return output;
    }

 // 7. Define the `listProduct` Method:
//    - Annotate with `@GetMapping` to handle GET requests to fetch all products.
//    - Fetch all products using `findAll()` method from `ProductRepository`.
//    - Return all products in a `Map<String, Object>` with key `products`.
    @GetMapping("/allProducts")
    public Map<String, Object> listProduct(){
        Map<String, Object> output = new HashMap<>();
        output.put("products", productRepository.findAll());
        return output;
    }
// 8. Define the `getProductbyCategoryAndStoreId` Method:
//    - Annotate with `@GetMapping("filter/{category}/{storeid}")` to filter products by `category` and `storeId`.
//    - Use `findProductByCategory()` method from `ProductRepository` to retrieve products.
//    - Return filtered products in a `Map<String, Object>` with key `product`.
    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoyAndStoreId(@PathVariable("category") String category, @PathVariable("storeid") long storeid){
        return Map.of("products", productRepository.findByCategoryAndStoreId(storeid, category));
    }

// 9. Define the `deleteProduct` Method:
//    - Annotate with `@DeleteMapping("/{id}")` to handle DELETE requests for removing a product by its ID.
//    - Validate product existence using `ValidateProductId()` in `ServiceClass`.
//    - Remove product from `Inventory` first using `deleteByProductId(id)` in `InventoryRepository`.
//    - Remove product from `Product` using `deleteById(id)` in `ProductRepository`.
//    - Return a success message with key `message` indicating product deletion.
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable("id") long id){
        if(serviceClass.ValidateProductId(id)){
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            return Map.of("message", "Success: product is deleted");
        }else{
            return Map.of("message", "ERROR: product does not exist");
        }
    }

 // 10. Define the `searchProduct` Method:
//    - Annotate with `@GetMapping("/searchProduct/{name}")` to search for products by `name`.
//    - Use `findProductBySubName()` method from `ProductRepository` to search products by name.
//    - Return search results in a `Map<String, Object>` with key `products`.
    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable("name") String name){
        return Map.of("products", productRepository.findProductBySubName(name));
    }
}
