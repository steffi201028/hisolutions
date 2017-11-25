package hisolutions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
public class Main2 {



	public static void main(String[] args) throws IndexOutOfBoundsException, FileNotFoundException {

		PrintStream out = new PrintStream(new FileOutputStream("test.html"));

		try {
			//PrintStream out = new PrintStream(new FileOutputStream("test.html"));
			Document doc = Jsoup.connect("http://www-03.ibm.com/software/sla/sladb.nsf/3f1f7af09a76f13f8525702d005422d8/006967fd414cdfee002570730044f9f7?OpenDocument").get();
			//Elements spanTags = doc.getElementsByAttributeValue("class", "ibm-data-table ibm-grid ibm-passing-small ibm-altrows");
			Element spanTags = doc.getElementsByTag("td").first();
			
			//for (Element p : spanTags) {
				String text = spanTags.ownText();
				out.println(text);
				out.close();
			}
		

		catch(Exception e) {
			e.printStackTrace();
		}
		//String command1 = "C:\\cygwin\\bin\\replace <br /> \r\n output1.html";
		//String command = "C:\\cygwin\\bin\\grep --after-context=9  'The following are Supporting Programs licensed with the Program:' test.html";
		String command = "C:\\cygwin\\bin\\sed -n '/The following are Supporting Programs licensed with the Program:/,/^\\s*$/{/The following are Supporting Programs licensed with the Program:/b;/^\\s*$/b;p}' test.html";
		String command2 = "C:\\cygwin\\bin\\grep   'Program Name:' test.html";

		try {
			Path path = Paths.get("test.html");
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll("<br>", "\r\n");
			Files.write(path, content.getBytes(charset));

			Process process2 = Runtime.getRuntime().exec(command2);


			BufferedReader reader2 = new BufferedReader(
					new InputStreamReader(process2.getInputStream()));
			String line2;
			while ((line2 = reader2.readLine()) != null) {
				System.out.println(line2); 
			}

			System.out.println("Program names complete");
			System.out.println();

			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				try {

					// Establish connection to DB
					Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
					// Create a statement
					Statement myStmt = myConn.createStatement();
					//Execute query
					String mdb = "insert into ibmproducts "
							+ " (hauptprodukt, bundles)" 
							+ " values ('NewProduct','"+line+"')";
					myStmt.executeUpdate(mdb);

				}catch (Exception exc){
					exc.printStackTrace();
				}
				//System.out.println("Linenr " + i + " "+  line);
				System.out.println(line);
				i++;
				//System.out.println();
			}

			System.out.println("Bundles completed");
			System.out.println("Anzahl Bundles:" + i);
			System.out.println("Databank Insert completed");
			out.flush();
			//out.close();
//			try {
//				Files.delete(path);
//			} catch (NoSuchFileException x) {
//				System.err.format("%s: no such" + " file or directory%n", path);
//			} catch (DirectoryNotEmptyException x) {
//				System.err.format("%s not empty%n", path);
//			} catch (IOException x) {
//				// File permission problems are caught here.
//				System.err.println(x);
//			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

