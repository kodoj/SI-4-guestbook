package com.codecool.controller;

import com.codecool.database.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class StudentController extends AbstractHandler implements HttpHandler {

    private static List<Student> students = new ArrayList<>();

    static {
        students.add(new Student(1, "Jan", "Kowalski", 20));
        students.add(new Student(2, "Krzysztof", "Odoj", 120));
        students.add(new Student(3, "Adam", "B", 1));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String[] uriElements = httpExchange.getRequestURI().toString().split("/");

        System.out.println(method);

        // "/students"
        if (uriElements.length == 2) {
            sendTemplateResponseStudentList(httpExchange, "students", students);
        }
        // "/students/add"
        else if (uriElements[2].equals("add")) {
            addStudent(httpExchange, method);
        }
    }

    private void addStudent(HttpExchange httpExchange, String method) {
        if(method.equals("GET")){
            sendTemplateResponse(httpExchange, "createStudent");
            System.out.println(method);
        }

        if(method.equals("POST")){
            Map inputs = readFormData(httpExchange);
            try {
                int id = Integer.parseInt((String) inputs.get("id"));
                String firstName = (String) inputs.get("firstname");
                String lastName = (String) inputs.get("lastname");
                int age = Integer.parseInt((String) inputs.get("age"));
                students.add(new Student(id, firstName, lastName, age));
            } catch (NumberFormatException e) {

            }

            redirectToLocation(httpExchange, "/students");
        }
    }
}
