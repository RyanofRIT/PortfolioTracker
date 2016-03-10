package View;

import Portfolio.UserAuthentication;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class View extends Application {
    public static void main(String[] args) {
        System.out.println("program Start");

        //create the login page
        View.launch(View.class);

    }

    @Override
    public void start(Stage primaryStage) {
        //create a instance to be able to call methods
        UserAuthentication userAuthentication = new UserAuthentication();

        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button loginBtn = new Button("Log In");
        HBox hbLoginBtn = new HBox(10);
        hbLoginBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbLoginBtn.getChildren().add(loginBtn);
        grid.add(hbLoginBtn, 1, 4);
        loginBtn.setOnAction((event -> {
            //call back to userAuth to check that id and pass are correct
            //use return type for errors or new state
            //TODO what to do when login is successful and if it is unsuccessful
            if (userAuthentication.checkPassword(userTextField.getText(), pwBox.getText())){
                System.out.println("Login is successful");
            }  else{
                System.out.println("Login is NOT successful");
            }

        }));

        Button registerBtn = new Button("Create Portfolio");
        HBox hbRegisterBtn = new HBox(10);
        hbRegisterBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbRegisterBtn.getChildren().add(registerBtn);
        grid.add(hbRegisterBtn, 0, 4);
        registerBtn.setOnAction((event -> {
            //call back to createId to make account
            //use return type for errors or new portfolio
            userAuthentication.createId(userTextField.getText(), pwBox.getText());
            //TODO determine what to do after the portfolio has been created

        }));

        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
