package window;

import main.Main;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

	public Socket socket;
	public String s;
	public int chartX = 0;

	@FXML
	public TextField chatInput;
	@FXML
	public TextField inputField;
	@FXML
	public TextArea chatOutput;
	@FXML
	public Label wrongSyntax;
	@FXML
	public Label statsLabel;
	@FXML
	public Label fishIng;
	@FXML
	private NumberAxis popX;
	@FXML
	private NumberAxis popY;
	@FXML
	private LineChart<Number, Number> popChart;
    @FXML
    private NumberAxis priceX;
    @FXML
    private NumberAxis priceY;
	@FXML
	private LineChart<Number, Number> priceChart;
	
	XYChart.Series<Number, Number> popXY = new XYChart.Series<Number, Number>();
	XYChart.Series<Number, Number> priceXY = new XYChart.Series<Number, Number>();
	
	

	static PrintWriter out;

	public MainController() {
		new Thread(new InputHandler()).start();
		new Thread(new StatsReload()).start();
		
	}

	public static void setPrintWriter() {
		try {
			PrintWriter out1 = new PrintWriter(Main.socket.getOutputStream());
			out = out1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// MenuBar mainframe

	// Buttons mainframe

	public void sendChat() {
		out.write("SAY " + chatInput.getText() + "\n");
		out.flush();
		chatInput.clear();

	}

	public void fishAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("FISH " + inputField.getText() + "\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buyAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("BUY " + inputField.getText() + "\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sellGAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("SELL " + inputField.getText() + "\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sellNAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("SELL " + inputField.getText() + "\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mineAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("MINE " + inputField.getText() + "\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("STATS\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pauseAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("PAUSE\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unpauseAction() {
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("UNPAUSE\n");
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStats() {
		statsLabel.setText(s);
	}
	
	public void setDialog() {
		wrongSyntax.setText(s);
	}
	
	public void setfishIng() {
		fishIng.setText(s);
	}
	
	public void setCharts() {
		String[] a = s.split("°");
		
		
		
		popXY.getData().add(new Data<Number, Number>(chartX, Double.parseDouble(a[0])));
		priceXY.getData().add(new Data<Number, Number>(chartX, Double.parseDouble(a[1])));
		
		popChart.getData().add(popXY);
		priceChart.getData().add(priceXY);
		
		chartX++;
		
	}

	class InputHandler implements Runnable {

		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(Main.socket.getInputStream()));
				PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
				String output;
				String[] clientInput;

				while (true) {

					output = in.readLine();
					clientInput = output.split("~");

					switch (clientInput[0]) {

					case "SAY":
						chatOutput.setText(chatOutput.getText() + clientInput[1] + "\n");
						break;

					case "DIALOG":
						s = clientInput[1];
						Platform.runLater(() -> {
							setDialog();
						});
						break;

					case "STATS":
						s = clientInput[1];
						Platform.runLater(() -> {
							setStats();
						});
						break;
					case "KICK":
						Main.socket.close();
						Platform.exit();
						System.exit(0);
						
					case "POP":
						s = clientInput[1];
						Platform.runLater(() -> {
							setCharts();
						});
						break;
					case "DIALOG1":
						s = clientInput[1];
						Platform.runLater(() -> {
							setfishIng();
						});
						break;
					case "DIALOG2":
						s = clientInput[1];
						new Thread(new DialogTwoAnimation()).start();
						Platform.runLater(() -> {
							setfishIng();
						});
						break;
						

					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	class StatsReload implements Runnable {

		@Override
		public void run() {
			while (true){
				try {
					PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
					out.write("STATS\n");
					out.flush();
					Thread.sleep(1000);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
		}

	}
	
	class DialogTwoAnimation implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Platform.runLater(() -> {
				fishIng.setText("");
			});
		}

	}
	
	

}