package com.company.financeflowapp.leaderboard;

import com.company.financeflowapp.mySQL.ConnectToDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;


public class LeaderboardComponents {
    private final BorderPane view = new BorderPane();

    //Stack used to maintain the leaderboard and store user scores
    private Stack<UserProfitScore> leaderboard = new Stack<>(); //dynamic
    //Add user and their profits to LeaderboardComponents

    private GridPane leaderboardGrid = new GridPane();

    private ListView<String> leaderboardListView = new ListView<>();


    public ListView<String> getLeaderboardListView() {
        return leaderboardListView;
    }
    public LeaderboardComponents() {
        // Initialize the UI components
        initializeUI();
    }

    /**
      Initializes the leaderboard user interface and related controls.
      Styles are applied to the leaderboard grid and the refresh button.
      It also sets up the initial leaderboard view.
     */
    private void initializeUI() {
        // Inline CSS Styles
        String gridPaneStyle = "-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;";
        String buttonStyle = "-fx-background-color: #3079ab;" +
                "-fx-text-fill: white;";
        String labelStyle = "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;";

        // Apply styles to the leaderboardGrid and refreshButton
        leaderboardGrid.setStyle(gridPaneStyle);

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle);
        refreshButton.setOnAction(event -> refreshLeaderboard());

        VBox controls = new VBox(refreshButton); // Add more controls as needed
        controls.setSpacing(10); // Add spacing between controls
        view.setTop(controls);

        // Set initial leaderboard
        refreshLeaderboard();
    }

    /**
      Adds a user and their profit to the leaderboard.
      A new UserProfitScore instance is created and added to the leaderboard stack.
     username - The username of the user.
     profit - The profit amount of the user.
     */
    public void addUserToLeaderboard(String username, double profit){
        //new user instance with provided username and profit
        UserProfitScore userProfitScore = new UserProfitScore(username,profit);
        leaderboard.push(userProfitScore); //push score onto leaderboard
        System.out.println("new user added to leaderboard: ");
        System.out.println(leaderboard);
        System.out.println();

        //sort the leaderboard after adding the new user to the leaderboard
        sortLeaderboard();
        refreshLeaderboardDisplay();

    }
    /**
     Refreshes the display of the leaderboard on the UI.
      It clears the current leaderboard grid and repopulates it with sorted user profit scores.
      The sorting is done in descending order based on profit, and the results are displayed
      as labels within the grid.
     */
    private void refreshLeaderboardDisplay() {
        leaderboardGrid.getChildren().clear(); // Clear previous entries

        List<UserProfitScore> sortedLeaderboard = leaderboard.stream()
                .sorted(Comparator.comparingDouble(UserProfitScore::getProfit).reversed())
                .collect(Collectors.toList());

        int row = 0;
        for (UserProfitScore user : sortedLeaderboard) {
            Label userLabel = new Label(user.getUsername() + ": " + user.getProfit());
            // Here, you can apply CSS based on the user's profit, gains, etc.
            leaderboardGrid.add(userLabel, 0, row++);
        }

    }

    /**
     Retrieves the top N users from the leaderboard STACK.
     This method takes the number of top users required and returns a stack containing
      that many users from the leaderboard, sorted by their profits.
       topNUsers -  The number of top users to retrieve from the leaderboard.
      return A stack of UserProfitScore objects representing the top users.
     */
    public Stack<UserProfitScore> getTopUsers(int topNUsers) {
        Stack<UserProfitScore> topUsers = new Stack<>(); // new Stack to store top users
        //loop continues until (i) is less than the Top Number of users you need eg= 15,
        // and loop does not go beyond the leaderboard stack size to prevent out of bound errors.
        for (int i = 0; i < topNUsers && i < leaderboard.size() ; i++) {
            topUsers.push(leaderboard.get(i));
        }
        return topUsers; //return stack containing top users
        //view.setRight(getTopUsers(5));
    }

    /**
      Sorts the leaderboard by profit in descending order.
      It clears the current leaderboard and repopulates it with sorted data.
     */
    //Use the Merge sort to sort the leaderboard by profit in dsc order(meaning most profit first)
    private void sortLeaderboard() {
        System.out.println("next user data added");
        //convert stack to array for sorting
        UserProfitScore[] userProfitScoresArray = leaderboard.toArray(leaderboard.toArray(new UserProfitScore[0]));
        //user merger sort algorithm to sort the array
        mergeSort(userProfitScoresArray, 0, userProfitScoresArray.length -1);
        System.out.println("Merger Algorithm Completed");
        leaderboard.clear(); //clear og leaderboard Stack

        for (UserProfitScore userProfitScore: userProfitScoresArray ) {
            //push all sorted UserProfitScore onto clear leaderboard
            leaderboard.push(userProfitScore);
        }
        System.out.println("After Sorted the leaderboard is:");
        System.out.println(leaderboard);
        System.out.println();
    }

    private void refreshLeaderboard() {
        // fetch data from the database and add to leaderboardData
        fetchLeaderboardFromDatabase();
       refreshLeaderboardDisplay();

    }

    /**
      Fetches the leaderboard data from the database.
      It executes a SQL query to retrieve usernames and profits,
      then updates the leaderboard stack with the results.
     */
    private void fetchLeaderboardFromDatabase() {
        ConnectToDB dbConnection = new ConnectToDB();
        dbConnection.establishConnection();
        String query = "SELECT Username, Profits FROM user ORDER BY Profits DESC;"; // Adjust table and column names as necessary
        try {
            ResultSet rs = dbConnection.runQuery(query);
            leaderboard.clear(); // Clear existing data
            while (rs.next()) {
                String username = rs.getString("Username");
                double profit = rs.getDouble("Profit");
                leaderboard.push(new UserProfitScore(username, profit));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
    }

    /**
    MergeSort algorithm to sort the array of users profits
    because a merge uses Big O(nlog(n)) to split the data into individual forms then
     merge everything all back in order
      Merges two sub-arrays of UserProfitScore objects as part of the merge sort algorithm.
      userProfitArray - The array of UserProfitScore objects that contains the sub-arrays to be merged.
      left -The starting index of the first sub-array.
      mid -The ending index of the first sub-array.
      right - The ending index of the second sub-array.
     */
    private void mergeSort(UserProfitScore[] userProfitArray, int left, int right) {
        //check if more than one element in the array
        if (left < right) {
            //calc middle index of array
            int middle = left +(right - left)/2 ;

            // implemented the recursive merger sort to left and right halves of array
            mergeSort(userProfitArray,left,middle); //corrected mistake: put 'right' instead of 'middle'
            //mistake and made it recursive forever which I solved
            mergeSort(userProfitArray, middle +1, right);

            System.out.println( "We have the halved steps done");
            //merge the solved halves using the method
            merge(userProfitArray, left, middle, right);
        }
    }

    //MergeSort Algorithms re-merging halved steps
    private void merge(UserProfitScore[] userProfitArray, int left,int  mid, int right) {
        // Calculate the sizes of the two sub-arrays to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Create temporary arrays to hold the left and right sub-arrays
        UserProfitScore[] leftArray = new UserProfitScore[n1];
        UserProfitScore[] rightArray = new UserProfitScore[n2];

        // Copy data to temporary arrays
        for (int i = 0; i < n1; ++i)
            leftArray[i] = userProfitArray[left + i];
        for (int j = 0; j < n2; ++j)
            rightArray[j] = userProfitArray[mid + 1 + j];

        // Merge the two sub-arrays back into the original array
        int i = 0, j = 0;
        int k = left;

        // Compare elements from the left and right sub-arrays and merge them in sorted order
        while (i < n1 && j < n2) {
            if (leftArray[i].profit >= rightArray[j].profit) {
                userProfitArray[k] = leftArray[i];
                i++;
            } else {
                userProfitArray[k] = rightArray[j];
                j++;
            }
            k++;
        }

         // Copy any remaining elements from the left subarray, if any
        while (i < n1) {
            userProfitArray[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy any remaining elements from the right subarray, if any
        while (j < n2) {
            userProfitArray[k] = rightArray[j];
            j++;
            k++;
        }
        System.out.println("re-ordered now");
    }

    public BorderPane getView() {
        //load and apply CSS file to Javafx
       // view.getStylesheets().add(getClass().getResource("leaderboard.css").toExternalForm());

        return view;
}

}
