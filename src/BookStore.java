import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BookStore {
    private static List<Book> bookList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static UserInfo userInfo = new UserInfo();
    private static User currentUser;
    private static List<User> userList = new ArrayList<>();
    private static HashMap<Integer, Book> userBook= new HashMap<>();

    public static void main(String[] args)
    {

        bookList.add(new Book(1, "Don Quixote", "Miguel de Cervantes", 13.99, 50));
        bookList.add(new Book(2, "Fahrenheit 451", "Ray Bradbury", 8.50, 75));
        bookList.add(new Book(3, "The Alchemist", "Paulo Coelho", 9.00, 64));
        bookList.add(new Book(4, "Little Women", "Alcott Louisa May", 25.60, 87));
        bookList.add(new Book(5, "Crime and Punishment", "Dostoevsky Fyodor", 11.30, 68));
        bookList.add(new Book(6, "The Housemaid", "Freida McFadden", 9.99, 36));
        bookList.add(new Book(7, "Twisted games", "Ana Huang", 10.99, 55));
        bookList.add(new Book(8, "Gods of Eden", "William Bramley", 12.99, 23));
        bookList.add(new Book(9, "Secret History", "Donna Tartt", 9.99, 73));
        bookList.add(new Book(10, "1984", "George Orwell", 7.50, 72));

        userList.add(new User(1, "Mihaela123","mija35", "user"));
        userList.add(new User(2, "Mytro", "lukeskywalker2004", "user"));
        userList.add(new User(3, "damian", "chitara56", "user"));
        userList.add(new User(4, "cosminBoss", "cosmin2004", "user"));

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
            System.out.println("Acces denied! Admin privileges required!");
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
        findBookById(id);
        if(bookList.get(id)!= null) {
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
        findBookById(removeId);
        if (bookList.get(removeId-1) != null) {
            bookList.remove(bookList.get(removeId-1));
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
        String action = scanner.nextLine().trim();

            System.out.println("Enter the book Id for the book you want to modify details: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("What do you want to add?");
            String action2 = scanner.nextLine().trim();

                if(action2.equalsIgnoreCase("price"))
                {
                    System.out.println("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine();
                    bookList.get(id-1).setPrice(newPrice);
                    System.out.println("New price set!");
                }
                else if(action2.equalsIgnoreCase("quantity"))
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
                    System.out.println("Search successful! Book: " + book.toString());
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
                    System.out.println("Search successful! Book: " + book.toString());
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
                    System.out.println("Search successful! Book: " + book.toString());
                }
            }
        }
        else System.out.println("Type id, title or author");

        if(found = false)
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

            Book bookToIssue = null;
            for (Book book : bookList) {
                if (book.getId() == bookId) {
                    bookToIssue = book;
                    break;
                }
            }

            if (bookToIssue != null && bookToIssue.getQuantity() > 0) {
                System.out.println("Processing...");
                sleep(3);
                int temp = bookToIssue.getQuantity() - 1;
                bookToIssue.setQuantity(1);
                userBook.put(userList.get(userId-1).getUserId(), bookList.get(bookId-1));
                bookToIssue.setQuantity(temp);
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
                System.out.println("User " + userList.get(userId - 1).getUserName() + " has issued: " + userBook.get(userId));
            }
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

