package GUI;

import PL0analyzer.GrammarException;
import PL0analyzer.LexicalException;
import PL0analyzer.Pcode;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;

import java.io.*;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RootLayoutController
{
	private Main mainApp;

	// Is called by the main application to give a reference back to itself.
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
	}

	@FXML private TableView<Pcode> tableView;
	@FXML private TableColumn<Pcode, String> instruction;
	@FXML private TableColumn<Pcode, String> parg1;
	@FXML private TableColumn<Pcode, String> parg2;

	@FXML private void OpenResourceFile()
	{
		try
		{
			mainApp.CompileSourceCode();
			compiletext.setText("compile succeed.");
		}
		catch (LexicalException le)
		{
			String s = "Lexical error ";
			s += le.getRetCd();
			s += " : ";
			s += le.getMsgDes();
			compiletext.setText(s);
		}
		catch (GrammarException ge)
		{
			String s = "Grammar error ";
			s += ge.getRetCd();
			s += " : ";
			s += ge.getMsgDes();
			compiletext.setText(s);
		}
		finally
		{
			loadPcode();
			ObservableList<Pcode> data = FXCollections.observableArrayList();
			for (Pcode pcode : mainApp.mainConsole.pvm.codelist)
			{
				data.add(pcode);
			}
			tableView.setItems(data);
		}
	}

	@FXML
	private Text compiletext;

	@FXML private void handleExit()
	{
		Platform.exit();
	}

	@FXML private void handleAbout()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText(null);
		alert.setContentText("Author: " + getAuthor());

		alert.showAndWait();
	}

	private static String getAuthor()
	{
		String name = "Zhu Botong, Student ID: 15211036.\n";
		String address = "Colleage of Software, BUAA.\n" + "Beijing, China.\n";
		return name + "\n" + address;
	}

	private void loadPcode()
	{
		instruction.setCellValueFactory(cellData -> cellData.getValue().getActionStringProperty());
		parg1.setCellValueFactory(cellData -> cellData.getValue().getArg1StringProperty());
		parg2.setCellValueFactory(cellData -> cellData.getValue().getArg2StringProperty());
	}
}
