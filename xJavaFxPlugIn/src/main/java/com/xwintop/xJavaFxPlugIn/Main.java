package com.xwintop.xJavaFxPlugIn;

import com.xwintop.xJavaFxPlugIn.controller.LinuxPathToWindowsPathController;
import com.xwintop.xJavaFxTool.utils.XJavaFxSystemUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        XJavaFxSystemUtil.initSystemLocal();
        FXMLLoader fXMLLoader = LinuxPathToWindowsPathController.getFXMLLoader();
        ResourceBundle resourceBundle = fXMLLoader.getResources();
        Parent root = fXMLLoader.load();
        primaryStage.setResizable(true);
        primaryStage.setTitle(resourceBundle.getString("Title"));
        primaryStage.getIcons().add(new Image("/images/icon.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}