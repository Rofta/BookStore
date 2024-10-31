import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.io.FileWriter;

public class BookStore {
    private final static List<Book> bookList = new ArrayList<>();
    private final static Scanner scanner = new Scanner(System.in);
    private final static UserInfo userInfo = new UserInfo();
    private static User currentUser;
    private final static List<User> userList = new ArrayList<>();
    private final static HashMap<Integer, List<Book>> userBook= new HashMap<>();

    public static void main(String[] args)
    {
        loadBooks("BooksData.txt");
        loadUsers("UserData.txt");

        System.out.println("Book list: ");
        for(Book book : bookList)
        {
            System.out.println(book.toString());
        }

        if(login())
        {
            showMenu();
        }
        scanner.close();
    }
    private static void loadBooks(String fileName)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());
                String title = fields[1].trim();
                String author = fields[2].trim();
                double price = Double.parseDouble(fields[3].trim());
                int quantity = Integer.parseInt(fields[4].trim());

                bookList.add(new Book(id, title, author, price, quantity));
            }
        } catch (IOException e) {
            System.out.println("Error reading books file: " + e.getMessage());
        }
    }
    private static void loadUsers(String fileName)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());
                String userName = fields[1].trim();
                String passwords = fields[2].trim();
                String role = fields[3].trim();

                userList.add(new User(id, userName, passwords, role));
            }
        }
        catch(IOException e)
        {
            System.out.println("Error reading users file: " + e.getMessage());
        }
    }
    private static void showMenu()
    {
        boolean running = true;
        while(running) {

            System.out.println("          -MENU-        ");
            System.out.println("1) Book List");
            System.out.println("2) Add or Delete Book");
            System.out.println("3) Add, Update or Remove details about a book");
            System.out.println("4) Find a book by Id, Title or Author");
            System.out.println("5) Issue a book to a User");
            System.out.println("6) Check books issued to Users");
            System.out.println("7) Exit");


            char option = scanner.next().charAt(0);
            scanner.nextLine();
            switch (option) {
                case '1':
                    displayBooks();
                    break;
                case '2':
                    if(isAdmin())
                        addOrRemove();
                    break;
                case '3':
                    if(isAdmin())
                        details();
                    break;
                case '4':
                    search();
                    break;
                case '5':
                    if(isAdmin())
                        issueBook();
                    break;
                case '6':
                    if(isAdmin())
                        checkIssuedBooks();
                    break;
                case '7':
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
    private static boolean login()
    {
        System.out.println("\nWelcome to the bookstore! Please insert username: ");
        String username = scanner.nextLine();
        System.out.println("Please insert password: ");
        String password = scanner.nextLine();

        User user = userInfo.getUser(username);
        if(user != null && user.getPassword().equals(password))
        {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + currentUser.getUserName());
            return true;
        }
        else
        {
            System.out.println("Invalid username or password!");
            return false;
        }
    }
    private static boolean isAdmin()
    {
        if(!currentUser.getRole().equals("admin"))
        {
            System.out.println("Access denied! Admin privileges required!");
            return false;
        }
        return true;
    }
    private static void displayBooks()
    {
        System.out.println("Book list: ");
        bookList.sort(Comparator.comparingInt(Book::getId));
        for (Book book : bookList) System.out.println(book.toString());
    }
    private static void addOrRemove()
    {
        System.out.println("Would you like to add or remove a book? (add/remove)");
        String action = scanner.nextLine().trim();
        if (action.equalsIgnoreCase("add")) {
            addBook();
        }
        else if (action.equalsIgnoreCase("remove"))
        {
            removeBook();
        }
        else
        {
            System.out.println("Type add or remove");
        }
    }
    private static void addBook()
    {
        System.out.println("Enter id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Book foundBook = findBookById(id);
        if(foundBook != null) {
            System.out.println("This Id is already used for another book, shifting the books Id by 1.");
            for (Book book : bookList) {
                if (book.getId() >= id) {
                    book.setId(book.getId() + 1);
                }
            }
        }
        System.out.println("Enter title: ");
        String title = scanner.nextLine();
        System.out.println("Enter author: ");
        String author = scanner.nextLine();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        System.out.println("Enter quantity: ");
        int quantity = scanner.nextInt();

        System.out.println("Processing...");
        Book newBook = new Book(id, title, author, price, quantity);
        bookList.add(newBook);

        sleep(3);
        System.out.println("Book added");
    }
    private static void removeBook()
    {
        System.out.println("Enter id of the book you want to remove: ");
        int removeId = scanner.nextInt();
        scanner.nextLine();
        Book removeBook = findBookById(removeId);
        if (removeBook != null) {
            bookList.remove(removeBook);
            System.out.println("Processing...");
            sleep(3);
            System.out.println("Book removed");
        }
        else
        {
            System.out.println("Book with Id " +removeId+ " not found");
        }
    }
    private static void details()
    {
        System.out.println("Would you like to add, update or remove details? (add/update/remove)");
        String action = scanner.nextLine().trim();
        if(action.equalsIgnoreCase("add"))
        {
            addDetails();
        }
        else if (action.equalsIgnoreCase("update"))
        {
            updateDetails();
        }
        else if (action.equalsIgnoreCase("remove"))
        {
            removeDetails();
        }
        else System.out.println("Type add, update or remove");
    }
    private static void updateDetails()
    {
            System.out.println("Enter the book Id for the book you want to modify details: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("What do you want to add?");
            String action = scanner.nextLine().trim();

                if(action.equalsIgnoreCase("price"))
                {
                    System.out.println("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine();
                    bookList.get(id-1).setPrice(newPrice);
                    System.out.println("New price set!");
                }
                else if(action.equalsIgnoreCase("quantity"))
                {
                    System.out.println("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();
                    bookList.get(id-1).setQuantity(newQuantity);
                    System.out.println("New quantity set!");
                }
                else System.out.println("Type price or quantity");
    }
    private static void addDetails()
    {
        System.out.println("Enter the book Id for the book you want to add details to: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter publisher (leave blank to keep current): ");
        String publisher = scanner.nextLine().trim();
        if(!publisher.isEmpty())
        {
            bookList.get(id-1).setPublisher(publisher);
        }

        System.out.println("Enter genre (leave blank to keep current): ");
        String genre = scanner.nextLine().trim();
        if(!genre.isEmpty())
        {
            bookList.get(id-1).setGenre(genre);
        }

        System.out.println("Enter published year (enter 0 to keep current): ");
        int publishedYear = scanner.nextInt();
        scanner.nextLine();
        if(publishedYear != 0)
        {
            bookList.get(id-1).setYearPublished(publishedYear);
        }
        System.out.println("Processing...");
        sleep(3);
        System.out.println("Details updated successfully!");
    }
    private static void removeDetails()
    {
        System.out.println("Enter the book Id for the book you want to remove details from: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(bookList.get(id-1) == null)
        {
            System.out.println("Type a valid Id");
        }
        else
        {
            System.out.println("What do you want to remove?(publisher/genre/published year)");
            String action = scanner.nextLine().trim();
            if(action.equalsIgnoreCase("publisher"))
            {
                bookList.get(id-1).setPublisher("");
            }
            else if(action.equalsIgnoreCase("genre"))
            {
                bookList.get(id-1).setGenre("");
            }
            else if(action.equalsIgnoreCase("year"))
            {
                bookList.get(id-1).setYearPublished(0);
            }
            else System.out.println("Type publisher, genre or year");
            System.out.println("Processing...");
            sleep(3);
            System.out.println("Details removed successfully!");
        }
    }
    private static void search()
    {
        System.out.println("Do you want to search by id, title or author?(id/title/author)");
        String action = scanner.nextLine().trim();
        boolean found = false;
        if(action.equalsIgnoreCase("id"))
        {
            System.out.println("Enter the id you want to search it by: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Processing...");
            sleep(3);
            for(Book book : bookList)
            {
                if(book.getId() == id)
                {
                    found = true;
                    System.out.println("Search successful! Book: " + book);
                }
            }
        }
        else if (action.equalsIgnoreCase("title"))
        {
            System.out.println("Enter the title you want to search it by: ");
            String title = scanner.nextLine().trim();
            System.out.println("Processing...");
            sleep(3);
            for(Book book : bookList)
            {
                if(book.getTitle().equalsIgnoreCase(title))
                {
                    found = true;
                    System.out.println("Search successful! Book: " + book);
                }
            }
        }
        else if (action.equalsIgnoreCase("author"))
        {
            System.out.println("Enter the author you want to search it by: ");
            String author = scanner.nextLine().trim();
            System.out.println("Processing...");
            sleep(3);
            for(Book book : bookList)
            {
                if(book.getAuthor().equalsIgnoreCase(author))
                {
                    found = true;
                    System.out.println("Search successful! Book: " + book);
                }
            }
        }
        else System.out.println("Type id, title or author");

        if(!found)
        {
            System.out.println("Search unsuccessful! Please try again!");
        }
    }
    private static void issueBook()
    {
        System.out.println("Type a user ID to issue a book to: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        if (userId < 1 || userId > userList.size()) {
            System.out.println("Invalid user ID! Please try again.");
            return;
        }

        System.out.println("You want to send the book to user " + userList.get(userId-1).getUserName() + ", correct? (yes/no)");
        String action = scanner.nextLine().trim();
        if (action.equalsIgnoreCase("yes")) {
            System.out.println("Enter book ID to issue: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();

            Book bookToIssue = findBookById(bookId);

            if (bookToIssue != null && bookToIssue.getQuantity() > 0) {
                System.out.println("Processing...");
                sleep(4);

                List<Book> userBookList = userBook.getOrDefault(userId, new ArrayList<>());
                Book userCopy = null;
                for (Book b : userBookList) {
                    if (b.getId() == bookId) {
                        userCopy = b;
                        break;
                    }
                }
                if(userCopy != null) {
                    userCopy.setQuantity(userCopy.getQuantity() + 1);
                }
                else {
                    Book newUserBook = new Book(bookToIssue.getId(), bookToIssue.getTitle(), bookToIssue.getAuthor(), bookToIssue.getPrice(), 1);
                    userBookList.add(newUserBook);
                }
                userBook.put(userId, userBookList);
                bookToIssue.setQuantity(bookToIssue.getQuantity() - 1);
                try {
                    FileWriter administrationFile = new FileWriter("IssuedBooks.txt");
                    administrationFile.write(userBook.getOrDefault(userId, userBookList).toString());
                    administrationFile.close();
                }
                catch (IOException e)
                {
                    System.out.println("An error occurred: " + e.getMessage());
                }
                System.out.println("Book issued successfully! Book sent to user " + userList.get(userId - 1).getUserName());

            } else {
                System.out.println("Book issue unsuccessful! Book not found or not available.");
            }
        } else if (action.equalsIgnoreCase("no")) {
            System.out.println("Choose another user");
        } else {
            System.out.println("Type yes or no");
        }
    }
    private static void checkIssuedBooks()
    {
        System.out.println("Enter the id of the user you want to check for issued books: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

            if (userId >= 1 && userId <= userList.size()) {
                List<Book> issuedBooks = userBook.get(userId);
                if(userBook.get(userId) != null) {
                    System.out.println("User " + userList.get(userId - 1).getUserName() + " has issued: \n");
                    for(Book book : issuedBooks)
                    {
                        System.out.println(book.toString());
                    }
                }
                else System.out.println("User " + userList.get(userId - 1).getUserName() + " has no issued books");
            }
            else System.out.println("Insert a correct user Id");
    }
    private static Book findBookById(int id)
    {
        for(Book book : bookList)
        {
            if(book.getId() == id)
            {
                return book;
            }
        }
        return null;
    }
    private static void sleep(int seconds)
    {
        try
        {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch(InterruptedException e)
        {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }
}

