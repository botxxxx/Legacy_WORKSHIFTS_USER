package iccl.workshifts.ubeta;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainSocketOutput extends Thread {
	private String msg;

	public MainSocketOutput(String msg) {
		this.msg = msg;
	}

	public void run() {
		try {
			PrintWriter out = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(new DataOutputStream(MainActivity.skt.getOutputStream()), "UTF-8")),
					true);
			out.println(msg);
			out.flush();
		} catch (IOException e) {
		}
	}
}
