package com.company.financeflowapp.main;

import com.company.financeflowapp.leaderboard.LeaderboardComponents;
import com.company.financeflowapp.mySQL.ConnectToDB;
import com.company.financeflowapp.leaderboard.UserProfitScore;
import com.company.financeflowapp.portfolio.PortfolioComponents;
import com.company.financeflowapp.trading.ChartFXCandleStickManager;
import com.company.financeflowapp.trading.StockTradingComponents;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;//grid pane import

import java.sql.*;


import java.util.Stack;

/**
  FinanceFlow app as you see was my first project name, then I changed it to Stocky trades;
 but didn't want t change that due to fear of problems being created in my project.
  This class serves as the main entry point for the Stocky Trades application. It sets up the user interface
  for the login screen and the main application environment where various components of the application
  are managed including the user's portfolio, the leaderboard, and stock trading functionalities.

  Key functionalities:
  - Initialise and display the login page where users authenticate themselves.
  - On successful login, display the main application screen which includes a menu with options
    to access the portfolio, leaderboard, and stock trading functionalities.
  - Handle user actions such as logging in, navigating between different parts of the application,
    and logging out.
  - Setup the overall application's stage and scenes, incorporating styles and layout adjustments.
 */

public class Main extends Application {
    private final String API_KEY = "H76DCE4272RR1A0C";//B- not in seq
    //H76DCE4272RR1A0C - when other is at max capacity for the day
    //2FKETPGNLPJ4HY0
    //BIHF254BQF2WV6X8
    GridPane backgroundGrid;
    BackgroundSize backgroundSize;
    Stage stage;
    Scene scene;
    Scene sceneLoggedInMenu;
    Scene tradingStocks;
    static final String connectionUrl = "jdbc:mysql://localhost:3306/mydb?user=root&password=Kheyaan@12";

    Scene userPortfolio;

    Scene userLeaderboard;
    private StackPane dynamicContentArea = new StackPane();


    /**
      Initializes and displays the login screen. This screen includes fields for username and password entry,
      and a login button to authenticate the user. Upon successful authentication, the main application screen
      is loaded.
     */
    public void loginPage() {
        //Screen 1 landing login Page with GridPane
        // horizontal & vertical gap between rows & columns
        backgroundGrid = new GridPane();
        backgroundGrid.setAlignment(Pos.CENTER);

        backgroundGrid.setHgap(10);
        backgroundGrid.setVgap(10);
        backgroundGrid.setPadding(new Insets(25, 25, 25, 25));

        //text-cannot be edited. label-can be edited
        //backgroundGrid.add()-add text to the Column and row
        Text landingTitle = new Text("Stocky Trading");
        landingTitle.setFont(Font.font("Tahoma", FontWeight.EXTRA_LIGHT, 20));
        backgroundGrid.add(landingTitle, 0, 0, 3,1 );

        Label userName = new Label("    Username:");
        userName.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
        backgroundGrid.add(userName,0, 1);

        TextField userNameField= new TextField();
        backgroundGrid.add(userNameField,1 , 1);


        Label userPassword = new Label("    Password:");
        userPassword.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));

        backgroundGrid.add(userPassword, 0, 2);

        PasswordField userPasswordField = new PasswordField();
        backgroundGrid.add(userPasswordField,1,2);


        //backgroundGrid.setGridLinesVisible(true);
        //button-log in. text-display message after button
        //Hbox spacing of 20 aligns to different side
        Button LogInBtn = new Button("Log In");
        HBox hbSignIn = new HBox(20);
        hbSignIn.setAlignment(Pos.BOTTOM_RIGHT);
        hbSignIn.getChildren().add( LogInBtn);
        backgroundGrid.add(hbSignIn, 1,3);

        //handle the event when button pressed
        //event handled in the subprogram
        final Text actionTarget = new Text();
        backgroundGrid.add(actionTarget,1,7);


        LogInBtn.setOnAction(e->{
                actionTarget.setFill(Color.FIREBRICK);
                actionTarget.setText("Log in button pressed");
                System.out.println("Log in button pressed");
                String userLogName = userNameField.getText();
                // can only get text once the button pressed
                String userLogPassword =userPasswordField.getText();
                //Login Check NOW
            boolean loginSuccessful = userLogin(userLogName, userLogPassword);

            // Provide feedback based on login result
            if (loginSuccessful) {
                actionTarget.setFill(Color.GREEN);
                actionTarget.setText("Login Successful. Welcome, " + userLogName);
                System.out.println("Connection to DB established");
                System.out.println("User Verified");
                System.out.println("Login Successful. Welcome, " + userLogName);
                mainProgramScreen(userLogName, stage);
            } else {
                actionTarget.setFill(Color.RED);
                actionTarget.setText("Invalid username or password");
            }
            mainProgramScreen(userLogName, stage);

        });

        scene = new Scene( backgroundGrid, 1250, 650);
    }

    /**
      Attempts to authenticate a user based on the provided username and password.
     username - The username entered by the user.
     password - The password entered by the user.
     return true if authentication is successful, false otherwise.
     */
    private boolean userLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        String SQLQuery = "SELECT * FROM user WHERE Username = ? AND Password = ?";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement statement = connection.prepareStatement(SQLQuery)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            return rs.next(); // If a row is returned, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     Main Program Screen
      Sets up the main application screen after a user logs in successfully. This screen includes navigation
      for accessing the portfolio, leaderboard, and stock trading components.
      userName - The username of the user who has logged in.
      stage -The primary stage of the application where the user interface is set up.
     */

    public void mainProgramScreen(String userName, Stage stage) {
        //left side of the (menu)
        VBox menuPane;
        menuPane = new VBox();
        menuPane.setSpacing(20); // Spacing between items in Vbox
        menuPane.setAlignment(Pos.TOP_CENTER);
        menuPane.setPadding(new Insets(15));//padding around the vbox

        //Welcome the User
        Label WelcomeTxt = new Label("Welcome " );
        WelcomeTxt.setAlignment(Pos.CENTER_LEFT);
        WelcomeTxt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));

        Label MenuTxt = new Label("Menu:");
        MenuTxt.setAlignment(Pos.CENTER_LEFT);
        MenuTxt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));

        Button myPortfolioBtn = new Button("My Portfolio"); //portfolio button
        myPortfolioBtn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20)); //characteristics
        myPortfolioBtn.setOnAction(e->{
                //Connects to My Portfolio method
                myPortfolio();
            });

        Button leaderBoardBtn = new Button("Leader Board");
        leaderBoardBtn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        leaderBoardBtn.setOnAction(e->{
                //Connect to LeaderboardComponents class
                UserLeaderBoard();
             });

        Button tradeStocksBtn = new Button("Trade Stocks ");
        tradeStocksBtn.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        tradeStocksBtn.setOnAction(e->{
                //Connect to Trade Stocks class
                tradeStockMethod();
          });


        Button logoutButton = new Button("Logout");
        logoutButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        logoutButton.setOnAction(e -> {
            //Connect to setup Logout Button
            setupLogoutButton();
                });

                    //adding all buttons/operations to the screen
        menuPane.getChildren().addAll(WelcomeTxt, MenuTxt, myPortfolioBtn,
                leaderBoardBtn, tradeStocksBtn, logoutButton );
        //Border Pane and Static pane allow me to select a menu choice without interrupting or opening a new scene.


        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(menuPane); //setting Vbox Menu details & buttons for left side of the scene

        //setting up the right side for the dynamic content
        mainLayout.setCenter(dynamicContentArea);

        Image backgroundImage = new Image("file:src/main/resources/com/company/financeflowapp/images/menuBackground.jpg");

        //creation of background for menu
        menuPane.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize)));

        sceneLoggedInMenu = new Scene( mainLayout, 900, 600);
        stage.setScene(sceneLoggedInMenu);
        stage.centerOnScreen();//set the tab to open in middle of screen
    }

    /**
      Configures the logout process, including a confirmation dialog that allows users to save their progress
      before logging out.
     */
    private void setupLogoutButton() {
        Alert confirmationDialog = new Alert(Alert.AlertType.
                CONFIRMATION, "Sad to see you go ;/ Save and Log out?", ButtonType.YES, ButtonType.CANCEL);
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Save the session or data TODO
                ChartFXCandleStickManager chartFXCandleStickManager = new ChartFXCandleStickManager();
                chartFXCandleStickManager.stopScheduler();
                System.out.println("Data saved");
                Platform.exit();
            }
        });
    }



    /**
      Displays the leaderboard by initializing the leaderboard components and populating them
      with user data. The top users by profit are displayed in the dynamic content area of the UI.
     */
    private void UserLeaderBoard() {
        System.out.println("showing leaderboardComponents");

        // Initialize your LeaderboardComponents instance
        LeaderboardComponents leaderboard = new LeaderboardComponents();

        leaderboard.addUserToLeaderboard("JohnDoe", 5000.00);
        leaderboard.addUserToLeaderboard("JaneSmith", 7000.00);
        leaderboard.addUserToLeaderboard("EmilyWilliams", 6500.00);
        leaderboard.addUserToLeaderboard("MichaelJohnson", 4800.00);
        leaderboard.addUserToLeaderboard("DavidBrown", 5500.00);


        // Fetch the top N users
        Stack<UserProfitScore> topUsers = leaderboard.getTopUsers(4);

        // Clear the dynamic content area once before adding new content
        dynamicContentArea.getChildren().clear();

        // Iterate through top users and add their information to the dynamic content area
        ListView<String> leaderboardListView = leaderboard.getLeaderboardListView();
        // Add the ListView to the layout
        VBox vbox = new VBox(leaderboardListView);
        dynamicContentArea.getChildren().add(vbox); // Add each user's info as a new label

    }

    /**
      Sets up and displays the portfolio section of the application. This method initializes the
      portfolio components, sets the appropriate styles, and adds it to the dynamic content area.
     */
    private void myPortfolio() {
        System.out.println("showing portfolio");

        BorderPane layouts = new BorderPane();
        layouts.setStyle("-fx-background-color: red;");
        userPortfolio = new Scene( layouts, 900, 650);

        PortfolioComponents portfolioComponents = new PortfolioComponents();
        layouts.setTop(portfolioComponents.getView());

        System.out.println("represent portfolio");
        //dynamicContent to be displayed
        dynamicContentArea.getChildren().clear();
        //use add for or use addAll   dynamicContentArea.getChildren().addAll()
        dynamicContentArea.getChildren().add(layouts);
    }

    /**
      Initializes and displays the stock trading section of the application. This method sets up
      the trading components and adds them to the dynamic content area, providing a user interface
      for trading stocks.
     */
    public void tradeStockMethod() {
        System.out.println("showing trading stocks");

        BorderPane layouts = new BorderPane();
        layouts.setStyle("-fx-background-color: #4ffa4f;");
        tradingStocks = new Scene( layouts, 1100, 650);

        StockTradingComponents stockTradingComponents = new StockTradingComponents();
        layouts.setRight(stockTradingComponents.getView()); //  Right-align Stock Trading Information


        dynamicContentArea.getChildren().clear();
        //use add for or use addAll   dynamicContentArea.getChildren().addAll()
        dynamicContentArea.getChildren().add(layouts);



    }

    /**
      This is the main entry point for the JavaFX application. It sets up the primary stage with a
      login page, configures the application icon, and initializes the background settings. It also
      sets the scene on the primary stage and displays it.
     Exception = if any issue occurs during the setup or display of the stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        loginPage();

        //Icon to application
        Image icon = new Image("file:src/main/resources/com/company/financeflowapp/images/FinanceFlowLogo5.jpg");
        primaryStage.getIcons().add(icon);

        backgroundSize = new BackgroundSize(1, 1, true,
                true, true, true);
        Image backgroundImage = new Image("file:src/main/resources/com/company/financeflowapp/images/background.jpg");//remember file:

        backgroundGrid.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize)));


        primaryStage.setTitle("Stocky Trades");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
