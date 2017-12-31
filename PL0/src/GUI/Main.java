package GUI;

import java.io.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;

public class Main extends Application {
    private Stage primaryStage;
    private VBox rootLayout;
    public MainConsole mainConsole = new MainConsole();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PL/0 Compiler");

        initRootLayout();
    }

	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("RootLayout.fxml"));
			rootLayout = loader.load();

			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 实现MainApplication与Controller的联动
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public void CompileSourceCode()
	{
		mainConsole.reset();
		BorderPane borderPane = (BorderPane) rootLayout.getChildren().get(1);
		TableView tableView = (TableView) borderPane.getCenter();
		for ( int i = 0; i<tableView.getItems().size(); i++)
		{
			tableView.getItems().clear();
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Source File");
		File file = fileChooser.showOpenDialog(primaryStage);
		// 避免用户打开选择文件窗口又取消造成的程序崩溃
		if(file == null)
		{
			//System.out.println("file cancelled");
			return;
		}
		mainConsole.openfile(file);
	}

    public static void main(String[] args) {
        launch(args);
    }
}
