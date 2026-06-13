package com.project.code.Controller;

import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/reviews` URL using `@RequestMapping("/reviews")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `ReviewRepository` for accessing review data.
//        - `CustomerRepository` for retrieving customer details associated with reviews.
    @Autowired
    private ReviewRepository reviewRepository;
    private CustomerRepository customerRepository;

// 3. Define the `getReviews` Method:
//    - Annotate with `@GetMapping("/{storeId}/{productId}")` to fetch reviews for a specific product in a store by `storeId` and `productId`.
//    - Accept `storeId` and `productId` via `@PathVariable`.
//    - Fetch reviews using `findByStoreIdAndProductId()` method from `ReviewRepository`.
//    - Filter reviews to include only `comment`, `rating`, and the `customerName` associated with the review.
//    - Use `findById(review.getCustomerId())` from `CustomerRepository` to get customer name.
//    - Return filtered reviews in a `Map<String, Object>` with key `reviews`.
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable("storeId") long storeId, @PathVariable("productId") long productId){
        Map<String, Object> output = new HashMap<>();
        List<Review> reviews =  reviewRepository.findByStoreIdAndProductId(storeId, productId);

        for(Review r: reviews){
            if(customerRepository.findByid(r.getCustomerId()) == null){
                output.put("reviews", customerRepository.findByid(r.getCustomerId()).getName()+","
                        + r.getComment() + "," +
                        r.getRating() );
            }else{
                output.put("reviews", "Unknown" +","
                        + r.getComment() + "," +
                        r.getRating() );
            }
        }
        return output;
    }
}
