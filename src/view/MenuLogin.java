package view;

import controller.BasketManagement;
import controller.ProductManagement;
import controller.UserManagement;
import model.Basket;
import model.Product;
import model.User;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuLogin {
    public static final String PATH_USER = "user.txt";
    public static final String PATH_PRODUCT = "product.txt";
    public static final String PATH_BASKET = "basket.txt";
    public static UserManagement userManager = new UserManagement();
    public static ProductManagement productManager = new ProductManagement();
    public static BasketManagement basketManager = new BasketManagement();

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        readUserFile();
        readProductFile();
        do {
            menuLogin();
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    isLogin(scanner);
                    break;
                }
                case 2: {
                    isRegister(scanner);
                    break;
                }
                case 0: {
                    System.exit(0);
                    break;
                }
            }
        } while (choice != 0);
    }

    public void readUserFile() {
        userManager.readFile(PATH_USER);
    }

    public void writeUserFile() {
        userManager.writeFile(PATH_USER);
    }

    public void readProductFile() {
        productManager.readFile(PATH_PRODUCT);
    }

    public void writeProductFile() {
        productManager.writeFile(PATH_PRODUCT);
    }

    public void readBasketFile() {
        basketManager.readFile(PATH_BASKET);
    }

    public void writeBasketFile() {
        basketManager.writeFile(PATH_BASKET);
    }

    private void isLogin(Scanner scanner) {
        System.out.println("Nhập username :");
        scanner.nextLine();
        String username = scanner.nextLine();
        System.out.println("Nhập password :");
        String password = scanner.nextLine();
        User userLogin = new User(username, password);
        int index = userManager.findUserByUserName(username);
        User user = userManager.getUserList().get(index);
        if (userManager.isLogin(userLogin) == 1) {
            menuAdmin(username, scanner);
        } else if (userManager.isLogin(userLogin) == 0) {
            menuUser(scanner, username, user);
        } else {
            System.err.println("Sai tên tài khoản hoặc mật khẩu!");
        }
    }

    private void menuUser(Scanner scanner, String username, User user) {
        readBasketFile();
        int index = userManager.findUserByUserName(username);
        int choice1;
        do {
            menuUser(username);
            choice1 = scanner.nextInt();
            switch (choice1) {
                case 1: {
                    productManager.showAll();
                    break;
                }
                case 2: {
                    addProductToBasket(scanner, user);
                    break;
                }
                case 3: {
                    viewBasket(scanner, user);
                    break;
                }
                case 4: {
                    checkout(scanner, index,user);
                    writeUserFile();
                    writeBasketFile();
                    break;
                }
                case 5: {
                    inputMoneyToAccount(scanner, username, index);
                    writeUserFile();
                    break;
                }
                case 6: {
                    showMoney(index);
                    break;
                }
            }
        } while (choice1 != 0);
    }

    private void checkout(Scanner scanner, int index,User user) {
        int choice2;
        int totalMoney = 0;
        do {
            menuCheckout();
            choice2 = scanner.nextInt();
            switch (choice2) {
                case 1: {
                    totalMoney = checkoutAll(scanner, index, totalMoney,user);
                    writeUserFile();
                    writeBasketFile();
                    break;
                }
                case 2: {
                    CheckoutForEachProduct(scanner, index,user);
                    writeUserFile();
                    writeBasketFile();
                    break;
                }
                case 0: {
                    break;
                }
            }
        } while (choice2 != 0);
    }

    private void CheckoutForEachProduct(Scanner scanner, int index,User user) {
        System.out.println("Giỏ hàng của bạn :");
        for (Basket baskets: user.getBasketList()) {
            System.out.println(baskets);
        }
        System.out.println("Nhập id sản phẩm cần thanh toán :");
        scanner.nextLine();
        String id = scanner.nextLine();
        int index1 = basketManager.findBasketByIdProduct(id);
        int totalMoney1 = 0;
        if (index1 != -1) {
            totalMoney1 = basketManager.getBasketList().get(index1).getProduct().getPrice() * user.getBasketList().get(index1).getAmount();
            System.out.println("Số tiền bạn phải thanh toán là " + totalMoney1);
            System.out.println("Bạn có chắc chắn muốn thanh toán sản phẩm này không ?(Y/N)");
            String choice3 = scanner.nextLine();
            switch (choice3) {
                case "y":
                case "Y": {
                    int check = user.getBalance() - totalMoney1;
                    if (check >= 0) {
                        System.out.println("Bạn đã thanh toán thành công");
                        user.setBalance(check);
                        basketManager.removeProductToBasket(index1);
                    }else {
                        System.err.println("Số dư tài khoản không đủ");
                    }
                    break;
                }
                case "n":
                case "N": {
                    break;
                }
            }
        }
    }

    private void viewBasket(Scanner scanner, User user) {
        List<Basket> basketList = user.getBasketList();
        if (basketList == null) {
            System.out.println("CHƯA CÓ ĐƠN HÀNG NÀO");
        } else {
            System.out.println("Giỏ hàng của bạn :");
            for (Basket basket : user.getBasketList()) {
                System.out.println(basket);
            }
        }
        int choice2;
        do {
            menuViewBasket();
            choice2 = scanner.nextInt();
            switch (choice2) {
                case 1: {
                    DeleteProductInBasket(scanner, user);
                    writeUserFile();
                    writeBasketFile();
                    break;
                }
                case 2: {
                    ChangeAmountProduct(scanner, user);
                    writeUserFile();
                    writeBasketFile();
                    break;
                }
            }
        } while (choice2 != 0);
    }

    private void addProductToBasket(Scanner scanner, User user) {
        productManager.showAll();
        System.out.println("--------------------------------------------------------------");
        System.out.println("Nhập id sản phẩm cần mua :");
        scanner.nextLine();
        String id = scanner.nextLine();
        int index1 = productManager.findProductById(id);
        Product product = productManager.getProductList().get(index1);
        System.out.println("Nhập số lượng");
        int amount = scanner.nextInt();
        Basket basket = new Basket(product, amount);
        basketManager.addProductToBasket(basket);
        List<Basket> basketList = basketManager.getBasketList();
        user.setBasketList(basketList);
        writeUserFile();
        System.out.println("Bạn đã thêm thành công sản phẩm " + productManager.getProductList().get(index1).getName() + " vào giỏ hàng.");
        return;
    }

    private int checkoutAll(Scanner scanner, int index, int totalMoney,User user) {
        for (int i = 0; i < basketManager.getBasketList().size(); i++) {
            totalMoney += basketManager.getBasketList().get(i).getProduct().getPrice() *  user.getBasketList().get(i).getAmount();
        }
        System.out.println("Tổng số tiền bạn phải thanh toán là : " + totalMoney);
        System.out.println("Bạn có chắc chắn muốn thanh toán tất cả ?(Y/N)");
        scanner.nextLine();
        String choice3 = scanner.nextLine();
        switch (choice3) {
            case "y":
            case "Y": {
                int check = userManager.getUserList().get(index).getBalance() - totalMoney;
                if (check >= 0) {
                    System.out.println("Bạn đã thanh toán thành công!");
                    userManager.getUserList().get(index).setBalance(check);
                    basketManager.removeAllProductToBasket();
                } else {
                    System.err.println("Số dư của bạn k đủ để thanh toán . Vui lòng nạp thêm tiền vào tài khoản!");
                }
                break;
            }
            case "n":
            case "N": {
                break;
            }
        }
        return totalMoney;
    }

    private void menuViewBasket() {
        System.out.println("---Xem giỏ hàng---");
        System.out.println("1. Xóa sản phẩm");
        System.out.println("2. Thay đổi số lượng sản phẩm");
        System.out.println("0. Quay lại");
    }

    private void DeleteProductInBasket(Scanner scanner, User user) {
        List<Basket> basketList = user.getBasketList();
        if (basketList == null) {
            System.out.println("CHƯA CÓ ĐƠN HÀNG");
        } else {
            System.out.println("Nhập id sản phẩm cần xóa");
            scanner.nextLine();
            String id = scanner.nextLine();
            int index1 = basketManager.findBasketByIdProduct(id);
            if (index1 != -1) {
                Basket basket = basketManager.getBasketList().get(index1);
                boolean checkProduct = checkProductToBasket(basketList, basket);
                if (checkProduct) {
                    for (int i = 0; i < basketList.size(); i++) {
                        if (basket.getProduct().getId().equals(basketList.get(i).getProduct().getId())) {
                            basketList.remove(i);
                        }
                    }
                    System.out.println("Đã xóa thành công sản phẩm");
                } else {
                    System.err.println("Bạn nhập sai id sản phẩm");
                }
            } else {
                System.err.println("Bạn nhập sai id của sản phẩm");
            }
        }
        return;
    }

    private boolean checkProductToBasket(List<Basket> basketList, Basket basket) {
        for (int i = 0; i < basketList.size(); i++) {
            if (basket.getProduct().getId().equals(basketList.get(i).getProduct().getId())) {
                return true;
            }
        }
        return false;
    }

    private void ChangeAmountProduct(Scanner scanner, User user) {
        List<Basket> basketList = user.getBasketList();
        if (basketList == null) {
            System.out.println("CHƯA CÓ ĐƠN HÀNG NÀO");
        } else {
            System.out.println("Nhập id sản phẩm cần thay đổi số lượng");
            scanner.nextLine();
            String id = scanner.nextLine();
            int index1 = basketManager.findBasketByIdProduct(id);
            if (index1 != -1) {
                Basket basket = basketManager.getBasketList().get(index1);
                boolean checkProduct = checkProductToBasket(basketList, basket);
                if (checkProduct) {
                    System.out.println("Nhập số lượng cần thay đổi");
                    int amount = scanner.nextInt();
                    for (int i = 0; i < basketList.size(); i++) {
                        if (basket.getProduct().getId().equals(basketList.get(i).getProduct().getId())) {
                            Basket basket1 = basketList.get(i);
                            basket1.setAmount(amount);
                            basketList.set(i,basket1);
                            System.out.println("Bạn đã thay đổi thành công !");
                        }
                    }
                } else {
                    System.err.println("Bạn nhập sai id sản phẩm");
                }
            } else {
                System.err.println("Bạn nhập sai id sản phẩm");
            }
        }
    }

    private void menuCheckout() {
        System.out.println("1. Thanh toán tất cả giỏ hàng");
        System.out.println("2. Thanh toán từng sản phẩm");
        System.out.println("0. Quay lại");
    }

    private void menuUser(String username) {
        System.out.println("Bạn đang đăng nhập với tài khoản :" + username);
        System.out.println("------------Menu User-----------|");
        System.out.println("1. Xem danh sách sản phẩm-------|");
        System.out.println("2. Mua sản phẩm-----------------|");
        System.out.println("3. Xem giỏ hàng-----------------|");
        System.out.println("4. Thanh toán-------------------|");
        System.out.println("5. Nạp tiền vào tài khoản-------|");
        System.out.println("6. Xem số dư tài khoản----------|");
        System.out.println("0. Đăng xuất--------------------|");
        System.out.println("--------------------------------|");
    }

    private void showMoney(int index) {
        System.out.println("Số dư hiện có của bạn là " + userManager.getUserList().get(index).getBalance() + " VND.");
    }

    private void inputMoneyToAccount(Scanner scanner, String username, int index) {
        System.out.println("Nhập số tiền muốn nạp :");
        int inputMoney = scanner.nextInt();
        userManager.getUserList().get(index).addBalance(inputMoney);
        System.out.println("Bạn đã nạp thành công " + inputMoney + " VND vào tài khoản " + username + ".");
        System.out.println("Số dư hiện tại : " + userManager.getUserList().get(index).getBalance());
    }

    private void menuAdmin(String username, Scanner scanner) {
        ProductManagement productManager = new ProductManagement();
        int choice1;
        do {
            menuProduct(username);
            choice1 = scanner.nextInt();
            switch (choice1) {
                case 1: {
                    productManager.showAll();
                    break;
                }
                case 2: {
                    inputProduct(productManager, scanner);
                    writeProductFile();
                    break;
                }
                case 3: {
                    removeProduct(productManager, scanner);
                    writeProductFile();
                    break;
                }
                case 4: {
                    findProductById(productManager, scanner);
                    break;
                }
                case 5: {
                    AccountManagement(scanner);
                    break;
                }
            }
        } while (choice1 != 0);
    }

    private void AccountManagement(Scanner scanner) {
        int choice;
        do {
            menuUserByAdmin();
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    showUser();
                    break;
                }
                case 2: {
                    removeUser(scanner);
                    writeUserFile();
                    break;
                }
                case 3: {
                    isRegister(scanner);
                    writeUserFile();
                    break;
                }
                case 4: {
                    UpgradeAccount(scanner);
                    writeUserFile();
                    break;
                }
                case 5: {
                    addMoneyToUserByAdmin(scanner);
                    writeUserFile();
                    break;
                }
            }
        } while (choice != 0);
    }

    private void addMoneyToUserByAdmin(Scanner scanner) {
        System.out.println("Nhập username cần thêm tiền :");
        scanner.nextLine();
        String username = scanner.nextLine();
        int index = userManager.findUserByUserName(username);
        if (index != -1) {
            System.out.println("Số dư hiện tại của tài khoản " + userManager.getUserList().get(index).getUsername() + " là : " + userManager.getUserList().get(index).getBalance() + " VND");
            System.out.println("Nhập số tiền cần thêm ");
            int balance = scanner.nextInt();
            int totalMoney = userManager.getUserList().get(index).getBalance() + balance;
            userManager.getUserList().get(index).setBalance(totalMoney);
            System.out.println("Bạn đã nạp thành công " + balance + " VND vào tài khoản : " + userManager.getUserList().get(index).getUsername());
            System.out.println("Số dư hiện tại của tài khoản " + userManager.getUserList().get(index).getUsername() + " là : " + userManager.getUserList().get(index).getBalance() + " VND");
        } else {
            System.err.println("Bạn nhận sai username !");
        }
    }

    private void UpgradeAccount(Scanner scanner) {
        showUser();
        System.out.println("Nhập username cần nâng cấp");
        scanner.nextLine();
        String username1 = scanner.nextLine();
        int index = userManager.findUserByUserName(username1);
        if (index != -1) {
            System.out.println("Bạn có chắc chắn muốn nâng cấp tài khoản :" + username1 + " ?(Y/N)");
            String choice2 = scanner.nextLine();
            switch (choice2) {
                case "y":
                case "Y": {
                    userManager.getUserList().get(index).setRole("admin");
                    System.out.println("Đã nâng cấp thành công tài khoản " + username1 + " lên admin");
                    break;
                }
                case "n":
                case "N": {
                    break;
                }
            }
        } else {
            System.err.println("Bạn nhập sai username.");
        }
    }

    private void menuUserByAdmin() {
        System.out.println("----------QUẢN LÝ USER----------|");
        System.out.println("1. Xem danh sách user-----------|");
        System.out.println("2. Xóa user---------------------|");
        System.out.println("3. Thêm user--------------------|");
        System.out.println("4. Nâng cấp tài khoản user------|");
        System.out.println("5. Thêm tiền vào tài khoản -----|");
        System.out.println("0. Quay lại---------------------|");
        System.out.println("--------------------------------|");
    }

    private void showUser() {
        for (User users : userManager.getUserList()) {
            System.out.println(users);
        }
    }

    private void removeUser(Scanner scanner) {
        System.out.println("Nhập UserName cần xóa:");
        scanner.nextLine();
        String userName = scanner.nextLine();
        int index = userManager.findUserByUserName(userName);
        if (index != -1) {
            userManager.removeById(index);
        } else {
            System.err.println("Bạn nhập sai UserName!");
        }
    }

    private void isRegister(Scanner scanner) {
        Matcher matcherUsername;
        Matcher matcherPassword;
        System.out.println("Tạo tài khoản mới :");
        scanner.nextLine();
        String username = "";
        boolean checkUsername;
        do {
            System.out.println("Nhập username:");
            username = scanner.nextLine();
            checkUsername = userManager.checkUserName(username);
            String regex = "^[A-Za-z][A-Za-z0-9_]{6,18}$";
            Pattern pattern = Pattern.compile(regex);
            matcherUsername = pattern.matcher(username);
            if (!matcherUsername.matches()) {
                System.err.println("Tên tài khoản phải có 6 ký tự trở lên");
            }
            if (checkUsername) {
                System.err.println("Tài khoản này đã có người sử dụng.");
            }
        } while (!matcherUsername.matches() || checkUsername);
        String password;
        do {
            System.out.println("Nhập password :");
            password = scanner.nextLine();
            String regex = "^[A-Za-z][A-Za-z0-9_]{6,18}$";
            Pattern pattern = Pattern.compile(regex);
            matcherPassword = pattern.matcher(password);
            if (!matcherPassword.matches()) {
                System.err.println("Mật khẩu không hợp lệ");
            }
        } while (!matcherPassword.matches());
        String role = "user";
        User user = new User(username, password, role);
        userManager.add(user);
        writeUserFile();
    }

    private void menuLogin() {
        System.out.println("-----------Menu Login-----------|");
        System.out.println("1. Đăng nhập--------------------|");
        System.out.println("2. Đăng ký----------------------|");
        System.out.println("0. Exit-------------------------|");
        System.out.println("--------------------------------|");
    }

    private static void findProductById(ProductManagement productManager, Scanner scanner) {
        System.out.println("Nhập id sản phầm cần tìm:");
        scanner.nextLine();
        String id = scanner.nextLine();
        int index = productManager.findProductById(id);
        if (index != -1) {
            System.out.println(productManager.getProductList().get(index));
        } else {
            System.err.println("Không tìm thấy id của sản phẩm!");
        }
    }

    private static void removeProduct(ProductManagement productManager, Scanner scanner) {
        System.out.println("Nhập id sản phầm cần xóa:");
        scanner.nextLine();
        String id = scanner.nextLine();
        int index = productManager.findProductById(id);
        if (index != -1) {
            productManager.removeById(index);
        } else {
            System.err.println("Không tìm thấy id của sản phẩm!");
        }
    }

    private static void inputProduct(ProductManagement productManager, Scanner scanner) {
        System.out.println("Nhập id sản phẩm:");
        scanner.nextLine();
        String id = scanner.nextLine();
        System.out.println("Nhập tên sản phẩm:");
        String name = scanner.nextLine();
        System.out.println("Nhập giá tiền sản phẩm:");
        int price = scanner.nextInt();
        Product product = new Product(id, name, price);
        productManager.add(product);
    }

    private static void menuProduct(String username) {
        System.out.println("Bạn đang đăng nhập với tài khoản : " + username);
        System.out.println("--------------Menu--------------|");
        System.out.println("1. Hiển thị tất cả sản phẩm-----|");
        System.out.println("2. Thêm sản phẩm----------------|");
        System.out.println("3. Xóa sản phẩm-----------------|");
        System.out.println("4. Tìm sản phẩm theo id---------|");
        System.out.println("5. Quản lý User-----------------|");
        System.out.println("0. Đăng xuất--------------------|");
        System.out.println("--------------------------------|");
    }
}