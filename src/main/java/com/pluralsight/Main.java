package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // requires we pass in a username and password
        if (args.length != 2) {
            // displays a message to the user
            System.out.println("Application requires username and password to run database.");
            // exits the app if args[] is not correct input
            System.exit(0);
        }

        // gets the username and password from args[]
        String username = args[0];
        String password = args[1];

        // imports scanner
        Scanner input = new Scanner(System.in);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        while (true) {

            System.out.println("""
                    What would you want to do?
                        1) Display All Products
                        2) Display All Customers
                        3) Display All Employees
                        4) Display All Suppliers
                        0) Exit program
                    """);

            switch (input.nextInt()) {
                case 1 -> displayAllProducts(dataSource);
                case 2 -> displayAllCustomers(dataSource);
                case 3 -> displayAllEmployees(dataSource);
                case 4 -> displayAllSuppliers(dataSource);
                case 0 -> {
                    System.out.println("EXITING PROGRAM...");
                    System.exit(1);
                }
                default -> System.out.println("That is not a valid menu option, please try again.");
            }

        }
    }

    public static void printResults(ResultSet results) throws SQLException {

        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        while(results.next()){
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String value = results.getString(i);
                System.out.println(i + ". " + columnName + ": " + value + " ");
            }
            System.out.println();
        }
    }

    public static void displayAllProducts(DataSource dataSource) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
                     SELECT
                        ProductName,
                        UnitPrice,
                        UnitsInStock
                     FROM
                        Products
                     ORDER BY
                        ProductName
                     """);
        ) {
            ResultSet results = preparedStatement.executeQuery();
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Error: could not retrieve product information.");
            System.exit(1);
        }
    }

    public static void displayAllCustomers(DataSource dataSource) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
                     SELECT
                        ContactName,
                        CompanyName,
                        City,
                        Country,
                        Phone
                     FROM
                        Customers
                     ORDER BY
                        Country,
                        City,
                        ContactName
                     """);
        ) {
            ResultSet results = preparedStatement.executeQuery();
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Error: could not retrieve customer information.");
            System.exit(1);
        }
    }

    public static void displayAllEmployees(DataSource dataSource) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("""
                     SELECT
                        FirstName,
                        LastName,
                        Title,
                        Salary
                     FROM
                        Employees
                     ORDER BY
                        LastName,
                        FirstName
                     """);
        ) {
            ResultSet results = preparedStatement.executeQuery();
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Error: could not retrieve employee information.");
            System.exit(1);
        }
    }



}