package com.example.clients.controllers;

import com.example.clients.models.Employee;
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
public class EmployeeController {
    private final String REST_API_EMPLOYEE = "http://localhost:8080/employee";

    private static Client createJerseyRestClient() {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.register(
                new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO,
                LoggingFeature.Verbosity.PAYLOAD_ANY,
                10000));
        return ClientBuilder.newClient(clientConfig);
    }

    @GetMapping(value = "/employee")
    public String index(Model model) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_EMPLOYEE);
        List<Employee> ls = target.request(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE).get(List.class);
        model.addAttribute("employees", ls);
        return "index";
    }
    @GetMapping(value = "/employee/create")
    public String create() {
        return "create-employee";
    }

    @PostMapping(value = "/employee/save")
    public String save(@RequestParam String name,
                       @RequestParam Double salary
                       ) {
        Employee u = new Employee();
        u.setName(name);
        u.setSalary(salary);

        String jsonUser = convertToJson(u);

        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_EMPLOYEE);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(jsonUser, MediaType.APPLICATION_JSON));
        return "redirect:/employee";
    }

    @GetMapping(value = "/employee/edit/{id}")
    public String edit(@PathVariable(value = "id") Integer id, Model model) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_EMPLOYEE + '/' + id);
        Employee response = target.request(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE).get(Employee.class);
        model.addAttribute("employee", response);
        return "edit-employee";
    }

    @PostMapping(value = "/employee/update/{id}")
    public String update(@PathVariable(value = "id") Integer id,
                         @RequestParam Employee u) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_EMPLOYEE + '/' + id);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(u, MediaType.APPLICATION_JSON));
        return "redirect:/employee";
    }

    private static String convertToJson(Employee employee) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
