package com.example.clients.controllers;

import com.example.clients.models.Product;
import com.example.clients.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class ProductController {
    private final String REST_API_PRODUCT = "http://localhost:8080/product";


    private static Client createJerseyRestClient() {
        ClientConfig clientConfig = new ClientConfig();

        // Config logging for client side
        clientConfig.register( //
                new LoggingFeature( //
                        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), //
                        Level.INFO, //
                        LoggingFeature.Verbosity.PAYLOAD_ANY, //
                        10000));

        return ClientBuilder.newClient(clientConfig);
    }

    @GetMapping(value = "/product")
    public String index(Model model) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_PRODUCT);
        List<Product> ls = target.request(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE).get(List.class);
        model.addAttribute("products", ls);
        return "list-product";
    }

    @GetMapping(value = "/product/create")
    public String create() {
        return "create-product";
    }

    @PostMapping(value = "/product/save")
    public String save(@RequestParam String name,
                       @RequestParam Double price,
                       @RequestParam Integer quantity) {
        Product u = new Product();
        u.setName(name);
        u.setPrice(price);
        u.setQuantity(quantity);

        String jsonUser = convertToJson(u);

        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_PRODUCT);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(jsonUser, MediaType.APPLICATION_JSON));
        return "redirect:/product";
    }

    @GetMapping(value = "/product/edit/{id}")
    public String edit(@PathVariable(value = "id") Integer id, Model model) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_PRODUCT + '/' + id);
        Product response = target.request(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE).get(Product.class);
        model.addAttribute("product", response);
        return "sell-product";
    }

    @PostMapping(value = "/product/update/{id}")
    public String update(@PathVariable(value = "id") Integer id,
                         @RequestParam Integer quantity) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_PRODUCT + '/' + id);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(quantity, MediaType.APPLICATION_JSON));
        return "redirect:/product";
    }

    private static String convertToJson(Product product) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
