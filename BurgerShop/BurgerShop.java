
import java.util.*;

class BurgerShop {

    final static double BURGERPRICE = 500;

    public static Order[] order = new Order[0];
    // Order status
    public static final int CANCEL = 0;
    public static final int PREPARING = 1;
    public static final int DELIVERED = 2;

    // console clear
    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            // Handle any exceptions.
        }
    }

    // validation Customer ID
    public static boolean validationcustomerId(String customerId) {
        if (customerId.length() == 10) {
            if (customerId.startsWith("0")) {
                try {
                    int i = Integer.parseInt(customerId);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    // generate order Id
    public static String generateOrderId() {
        if (order.length==0){
            return "B0001";
        }
        String lastOrderId = order[order.length - 1].getOrderID();
        int number = Integer.parseInt(lastOrderId.split("B")[1]); //1
        number++;//2
        return String.format("B%04d",number); //printf("",) //B0002
    }

    // placeOrder
    public static void placeOrder() {
        Scanner input = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tPLACE ORDER\t\t\t\t|");
        System.out.println("-------------------------------------------------------------------------\n\n");
        System.out.print("ORDER ID - ");
        String orderId = generateOrderId();
        System.out.println(orderId + "\n================\n\n");

        L1: do {
            System.out.print("Enter Customer ID (phone no.): ");
            String customerId = input.next();
            if (customerId.charAt(0)!='0' || customerId.length()!=10){
                continue L1;
            }
            boolean isExistCustomer = false;
            String customerName = "";
            for (int i = 0; i < order.length; i++) {
                if (customerId.equals(order[i].getPhone())) {
                    isExistCustomer = true;
                    System.out.println("Enter Customer Name: " + order[i].getCustomerName());
                    customerName = order[i].getCustomerName();
                    break;
                }
            }
            if (!isExistCustomer) {
                System.out.print("\nEnter Customer Name: ");
                customerName = input.next();
            }
            System.out.print("Enter Burger Quantity - ");
            int qty = input.nextInt();
            if (qty > 0) {
                double billValue = qty * BURGERPRICE;
                System.out.printf("Total value - %.2f", billValue);
                System.out.println();
                L3:
                do {
                    System.out.print("\tAre you confirm order - ");
                    String option = input.next().toUpperCase();
                    if (option.equalsIgnoreCase("Y")) {
                        int size = order.length + 1;
                        Order[] temp = new Order[size];
                        for (int i = 0; i < order.length; i++) {
                            temp[i] = order[i];
                        }
                        order = temp;
                        order[order.length - 1] = new Order(customerName, orderId, qty, customerId, billValue,PREPARING);
                        System.out.println("\n\tYour order is enter to the system successfully...");
                        break L1;
                    } else if (option.equalsIgnoreCase("N")) {
                        System.out.println("\n\tYour order is not enter the system...");
                        clearConsole();
                        return;
                    } else {
                        System.out.println("\tInvalid option..input again...");
                        break L1;
                    }
                } while (true);
            }
        } while (true);
        L4: do {
            System.out.println();
            System.out.print("Do you want to place another order (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                placeOrder();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                homePage();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L4;
            }
        } while (true);

    }

    // Search best customer
    public static void searchBestCustomer() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tBEST Customer\t\t\t\t|");
        System.out.println("-------------------------------------------------------------------------\n");
        Order[] sortOrder = new Order[0];
        for (int i = 0; i < order.length; i++) {
            boolean isExist = false;
            for (int j = 0; j < sortOrder.length; j++) {
                if (sortOrder[j].getPhone().equals(order[i].getPhone())) {
                    if (order[i].getStatus()!=CANCEL){
                        sortOrder[j].setTotal(sortOrder[j].getTotal()+order[i].getTotal());
                    }
                    isExist = true;
                }
            }
            if (!isExist) {
                Order[] tempSort = new Order[sortOrder.length+1];
                for (int j = 0; j < sortOrder.length; j++) {
                    tempSort[j] = sortOrder[j];
                }
                sortOrder = tempSort;
                sortOrder[sortOrder.length-1] = new Order(order[i].getCustomerName(),order[i].getOrderID(),order[i].getBurgerQty(),order[i].getPhone(),order[i].getTotal(),order[i].getStatus());
            }
        }
        // sort
        for (int i = 1; i < sortOrder.length; i++) {
            for (int j = 0; j < i; j++) {
                if (sortOrder[j].getTotal() < sortOrder[i].getTotal()) {
                    Order tempObj = sortOrder[j];
                    sortOrder[j] = sortOrder[i];
                    sortOrder[i] = tempObj;
                }
            }
        }
        System.out.println("\n----------------------------------------");
        String line1 = String.format("%-14s%-15s%8s", " CustomerID", "Name", "Total");
        System.out.println(line1);
        System.out.println("----------------------------------------");
        for (int i = 0; i < sortOrder.length; i++) {
            String line = String.format("%1s%-14s%-15s%8.2f", " ", sortOrder[i].getOrderID(), sortOrder[i].getCustomerName(), sortOrder[i].getTotal());
            System.out.println(line);
            System.out.println("----------------------------------------");

        }
        L: do {
            Scanner input = new Scanner(System.in);
            System.out.print("\n\tDo you want to go back to main menu? (Y/N)> ");
            String exitOption = input.nextLine();
            if (exitOption.equalsIgnoreCase("Y")) {
                clearConsole();
                homePage();
            } else if (exitOption.equalsIgnoreCase("N")) {
                clearConsole();
                searchBestCustomer();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L;
            }
        } while (true);

    }

    // search order
    public static void searchOrder() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tSEARCH ORDER DETAILS\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        L1: do {
            System.out.print("Enter order Id - ");
            String orderId = input.next();
            System.out.println();
            for (int i = 0; i < order.length; i++) {
                if (orderId.equals(order[i].getOrderID())) {
                    String status = "";
                    switch (order[i].getStatus()) {
                        case CANCEL:
                            status = "Cancel";
                            break;
                        case PREPARING:
                            status = "Preparing";
                            break;
                        case DELIVERED:
                            status = "Delivered";
                            break;
                    }
                    System.out.println("---------------------------------------------------------------------------");
                    String line1 = String.format("%-10s%-14s%-12s%-10s%-14s%-10s", " OrderID", " CustomerID", " Name",
                            "Quantity", "  OrderValue", "  OrderStatus");
                    System.out.print(line1);
                    System.out.println(" |");
                    System.out.println("---------------------------------------------------------------------------");
                    String line = String.format("%1s%-10s%-14s%-15s%-10d%-14.2f%-10s", " ", order[i].getOrderID(),
                            order[i].getPhone(), order[i].getCustomerName(), order[i].getBurgerQty(), order[i].getTotal(), status);
                    System.out.print(line);
                    System.out.println("|");
                    System.out.println("---------------------------------------------------------------------------");
                    break L1;
                }
            }
            L2: do {
                System.out.print("\n\nInvalid Order ID. Do you want to enter again? (Y/N)>");
                String option = input.next();
                if (option.equalsIgnoreCase("Y")) {
                    clearConsole();
                    searchOrder();
                } else if (option.equalsIgnoreCase("N")) {
                    clearConsole();
                    return;
                } else {
                    System.out.println("\tInvalid option..input again...");
                    continue L2;
                }
            } while (true);
        } while (true);
        L3: do {
            System.out.println();
            System.out.print("Do you want to search another order details (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                searchOrder();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                homePage();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L3;
            }
        } while (true);
    }

    // search Customer
    public static void searchCustomer() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tSEARCH CUSTOMER DETAILS\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        L1: do {
            System.out.print("Enter customer Id - ");
            String customerId = input.next();
            System.out.println("\n");
            for (int i = 0; i < order.length; i++) {
                if (customerId.equals(order[i].getPhone())) {
                    System.out.println("CustomerID - " + order[i].getPhone());
                    System.out.println("Name       - " + order[i].getCustomerName());
                    System.out.println("\nCustomer Order Details");
                    System.out.println("========================\n");
                    System.out.println("----------------------------------------------");
                    String line = String.format("%-12s%-18s%-14s", " Order_ID", "Order_Quantity", "Total_Value  ");
                    System.out.println(line);
                    System.out.println("----------------------------------------------");
                    for (int j = 0; j < order.length; j++) {
                        if (order[j].getPhone().equals(customerId)) {
                            String line2 = String.format("%1s%-12s%-18d%-14.2f", " ", order[j].getOrderID(), order[j].getBurgerQty(),
                                    order[j].getTotal());
                            System.out.println(line2);
                        }
                    }
                    System.out.println("----------------------------------------------");
                    break L1;
                }

            }
            L2: do {
                System.out.print("\n\nInvalid Customer ID. Do you want to enter again? (Y/N)>");
                String option = input.next();
                if (option.equalsIgnoreCase("Y")) {
                    clearConsole();
                    searchCustomer();
                } else if (option.equalsIgnoreCase("N")) {
                    clearConsole();
                    return;
                } else {
                    System.out.println("\tInvalid option..input again...");
                    continue L2;
                }
            } while (true);
        } while (true);
        L3: do {
            System.out.println();
            System.out.print("Do you want to search another customer details (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                searchCustomer();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                homePage();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L3;
            }
        } while (true);
    }

    // View Order list
    public static void viewOrders() {
        Scanner input = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tVIEW ORDER LIST\t\t\t\t|");
        System.out.println("-------------------------------------------------------------------------\n");
        System.out.println("[1] Delivered Order");
        System.out.println("[2] Preparing Order");
        System.out.println("[3] Cancel Order");

        System.out.print("\nEnter an option to continue > ");
        int option = input.nextInt();
        switch (option) {
            case 1:
                clearConsole();
                deliverOrder();
                break;
            case 2:
                clearConsole();
                preparingOrder();
                break;
            case 3:
                clearConsole();
                cancelOrder();
                break;
        }
    }

    // Delivered Order
    public static void deliverOrder() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tDELIVERED ORDER\t\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        System.out.println("\n--------------------------------------------------------------");
        String line1 = String.format("%-10s%-15s%-13s%-10s%12s", " OrderID", " CustomerID", " Name", "Quantity",
                "  OrderValue");
        System.out.println(line1);
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < order.length; i++) {
            if (order[i].getStatus() == DELIVERED) {
                String line = String.format("%1s%-10s%-15s%-15s%-10d%8.2f", " ", order[i].getOrderID(), order[i].getPhone(),
                        order[i].getCustomerName(), order[i].getBurgerQty(), order[i].getTotal());
                System.out.println(line);
                System.out.println("--------------------------------------------------------------");
            }
        }
        L1: do {
            System.out.print("\nDo you want to go to home page (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                homePage();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                deliverOrder();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L1;
            }
        } while (true);
    }

    // Preparing Order
    public static void preparingOrder() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tPREPARING ORDER\t\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        System.out.println("\n--------------------------------------------------------------");
        String line1 = String.format("%-10s%-15s%-13s%-10s%12s", " OrderID", " CustomerID", " Name", "Quantity",
                "  OrderValue");
        System.out.println(line1);
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < order.length; i++) {
            if (order[i].getStatus() == PREPARING) {
                String line = String.format("%1s%-10s%-15s%-15s%-10d%8.2f", " ", order[i].getOrderID(), order[i].getPhone(),
                        order[i].getCustomerName(), order[i].getBurgerQty(), order[i].getTotal());
                System.out.println(line);
                System.out.println("--------------------------------------------------------------");
            }
        }
        L1: do {
            System.out.print("\nDo you want to go to home page (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                homePage();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                preparingOrder();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L1;
            }
        } while (true);
    }

    // Cancel Order
    public static void cancelOrder() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tCANCEL ORDER\t\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        System.out.println("\n--------------------------------------------------------------");
        String line1 = String.format("%-10s%-15s%-13s%-10s%12s", " OrderID", " CustomerID", " Name", "Quantity",
                "  OrderValue");
        System.out.println(line1);
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < order.length; i++) {
            if (order[i].getStatus() == CANCEL) {
                String line = String.format("%1s%-10s%-15s%-15s%-10d%8.2f", " ", order[i].getOrderID(), order[i].getPhone(),
                        order[i].getCustomerName(), order[i].getBurgerQty(), order[i].getTotal());
                System.out.println(line);
                System.out.println("--------------------------------------------------------------");
            }
        }
        L1: do {
            System.out.print("\nDo you want to go to home page (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                homePage();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                cancelOrder();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L1;
            }
        } while (true);
    }

    // Update order details
    public static void updateOrderDetails() {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tUPDATE ORDER DETAILS\t\t\t\t|");
        System.out.println("--------------------------------------------------------------------------------\n");
        L1: do {
            System.out.print("Enter order Id - ");
            String orderId = input.next();
            System.out.println();
            for (int i = 0; i < order.length; i++) {
                if (orderId.equals(order[i].getOrderID())) {
                    String status = "";
                    switch (order[i].getStatus()) {
                        case 0:
                            status = "Cancel";
                            break;
                        case 1:
                            status = "Preparing";
                            break;
                        case 2:
                            status = "Delivered";
                            break;
                    }
                    if (status == "Cancel") {
                        System.out.println("This Order is already cancelled...You can not update this order...");
                    } else if (status == "Delivered") {
                        System.out.println("This Order is already delivered...You can not update this order...");
                    } else {
                        System.out.println("OrderID    - " + order[i].getOrderID());
                        System.out.println("CustomerID - " + order[i].getPhone());
                        System.out.println("Name       - " + order[i].getCustomerName());
                        System.out.println("Quantity   - " + order[i].getBurgerQty());
                        System.out.printf("OrderValue - %.2f", order[i].getTotal());
                        System.out.println("\nOrderStatus- " + status);

                        System.out.println("\nWhat do you want to update ? ");
                        System.out.println("\t(01) Quantity ");
                        System.out.println("\t(02) Status ");
                        System.out.print("\nEnter your option - ");
                        int option = input.nextInt();
                        switch (option) {
                            case 1:
                                clearConsole();
                                System.out.println("\nQuantity Update");
                                System.out.println("================\n");
                                System.out.println("OrderID    - " + order[i].getOrderID());
                                System.out.println("CustomerID - " + order[i].getPhone());
                                System.out.println("Name       - " + order[i].getCustomerName());
                                System.out.print("\nEnter your quantity update value - ");
                                int qty = input.nextInt();
                                order[i].setBurgerQty(qty);
                                order[i].setTotal(qty*BURGERPRICE);
                                System.out.println("\n\tupdate order quantity success fully...");
                                System.out.println("\nnew order quantity - " + order[i].getBurgerQty());
                                System.out.printf("new order value - %.2f", order[i].getTotal());
                                break;
                            case 2:
                                clearConsole();
                                System.out.println("\nStatus Update");
                                System.out.println("==============\n");
                                System.out.println("OrderID    - " + order[i].getOrderID());
                                System.out.println("CustomerID - " + order[i].getPhone());
                                System.out.println("Name       - " + order[i].getCustomerName());
                                System.out.println("\n\t(0)Cancel");
                                System.out.println("\t(1)Preparing");
                                System.out.println("\t(2)Delivered");
                                System.out.print("\nEnter new order status - ");
                                int s = input.nextInt();
                                order[i].setStatus(s);
                                order[i].setTotal(0);
                                switch (order[i].getStatus()) {
                                    case 0:
                                        status = "Cancel";
                                        break;
                                    case 1:
                                        status = "Preparing";
                                        break;
                                    case 2:
                                        status = "Delivered";
                                        break;
                                }
                                System.out.println("\n\tUpdate order status successfully...");
                                System.out.println("\nnew order status - " + status);
                                break;
                        }
                    }
                    break L1;
                }
            }
            L3: do {
                System.out.print("\n\nInvalid Order ID. Do you want to enter again? (Y/N)>");
                String option = input.next();
                if (option.equalsIgnoreCase("Y")) {
                    System.out.println("\n");
                    continue L1;
                } else if (option.equalsIgnoreCase("N")) {
                    clearConsole();
                    return;
                } else {
                    System.out.println("\tInvalid option..input again...");
                    continue L3;
                }
            } while (true);
        } while (true);
        L3: do {
            System.out.println();
            System.out.print("Do you want to update another order details (Y/N): ");
            String option = input.next();
            if (option.equalsIgnoreCase("Y")) {
                clearConsole();
                updateOrderDetails();
            } else if (option.equalsIgnoreCase("N")) {
                clearConsole();
                homePage();
            } else {
                System.out.println("\tInvalid option..input again...");
                continue L3;
            }
        } while (true);
    }

    // exit
    public static void exit() {
        clearConsole();
        System.out.println("\n\t\tYou left the program...\n");
        System.exit(0);
    }

    // home page
    public static void homePage() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("|\t\t\t\tiHungry Burger\t\t\t\t|");
        System.out.println("-------------------------------------------------------------------------\n");
        System.out.println("[1] Place Order\t\t\t[2] Search Best Customer");
        System.out.println("[3] Search Order\t\t[4] Search Customer");
        System.out.println("[5] View Orders\t\t\t[6] Update Order Details");
        System.out.println("[7] Exit");

        Scanner input = new Scanner(System.in);
        do {

            System.out.print("\nEnter an option to continue > ");
            char option = input.next().charAt(0);

            switch (option) {
                case '1':
                    clearConsole();
                    placeOrder();
                    break;
                case '2':
                    clearConsole();
                    searchBestCustomer();
                    break;
                case '3':
                    clearConsole();
                    searchOrder();
                    break;
                case '4':
                    clearConsole();
                    searchCustomer();
                    break;
                case '5':
                    clearConsole();
                    viewOrders();
                    break;
                case '6':
                    clearConsole();
                    updateOrderDetails();
                    break;
                case '7':
                    exit();
                    break;
            }
        } while (true);
    }

    public static void main(String args[]) {
        homePage();
    }
}
