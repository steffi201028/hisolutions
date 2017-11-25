package hisolutions;


import geb.Browser;
import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
//import java.net.URL;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.google.common.base.CharMatcher;

public class Main {
	
	private static Document site;
	//private static Document doc;


	public static void main(String[] args) throws IndexOutOfBoundsException, SQLException {
		//Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
		String text;
		
		Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
		

		try {



			int i;
			for(i=80000;i<80900;i+=29) {
				String url = new String("http://www-03.ibm.com/software/sla/sladb.nsf/lilookup?OpenView&Start=" + i);
				//Document doc = Jsoup.connect(url).get();

				site=Jsoup.connect(url).get();
				Elements tags= site.select("a");
				for(int j = 0; j <tags.size(); j++) {

					//Holt Dokumente
					String urlsub = new String(tags.get(j).ownText());
					String urldoc = "http://www-03.ibm.com/software/sla/sladb.nsf/lilookup/"+urlsub+"?OpenDocument";

					//Checkt, ob es die URL gibt
					
					URL u = new URL(urldoc); 
					//URL u = new URL("http://www-03.ibm.com/software/sla/sladb.nsf/c7134e107cf0624e86256738007531d7/"+urlsub+"?OpenDocument"); 
					HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
					huc.setRequestMethod("GET"); 
					huc.connect(); 
					huc.getResponseCode();

					//wenn ja, dann connect mit Jsoup
					if (huc.getResponseCode()==200) {

						//String urldoc = "http://www-03.ibm.com/software/sla/sladb.nsf/c7134e107cf0624e86256738007531d7/"+urlsub+"?OpenDocument";
						PrintStream out = new PrintStream(new FileOutputStream("test.html"));
						Path path = Paths.get("test.html");
						Document doc=Jsoup.connect(urldoc).get();
						Elements spanTags = doc.getElementsByTag("p");	
						
						for (Element p : spanTags) {
							text = p.outerHtml();
							out.println(text);

						}
						StringBuffer htmlStr = getStringFromFile("test.html", "ISO-8859-1");
						boolean isPresent = htmlStr.indexOf("The following are Supporting Programs licensed with the Program") != -1;
						System.out.println(urldoc);
						System.out.println("URL: http://www-03.ibm.com/software/sla/sladb.nsf/lilookup/"+urlsub+"?OpenDocument");
						//System.out.println(urlsub);
						System.out.println("Contains String The following are Supporting Programs licensed with the Program ? : " + isPresent);
						System.out.println();
						if(isPresent) {

						String command = "C:\\cygwin\\bin\\sed -n '/The following are Supporting Programs licensed with the Program:/,/^\\s*$/{/The following are Supporting Programs licensed with the Program:/b;/^\\s*$/b;p}' test.html";
						String command2 = "C:\\cygwin\\bin\\grep  'Program Name' test.html";
						//String command3 = "C:\\cygwin\\bin\\sed -n '/Program Name/{n;p;}' test.html"; //outputs Program number
						String command3 = "C:\\cygwin\\bin\\grep -o '[0-9]\\{4\\}-[A-Z0-9]\\{3\\}' test.html";                                
							
						//Release date of document
							Element meta = doc.select("meta[name=dcterms.date]").first();
							String content1 = meta.attr("content");
							System.out.println(content1);
							
							
							String untildate=content1;   
							SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
							Calendar cal = Calendar.getInstance();    
							cal.setTime( dateFormat.parse(untildate));    
							cal.add( Calendar.DATE, 1 );    
							String convertedDate=dateFormat.format(cal.getTime());    
							System.out.println("Date increase by one.."+convertedDate);
							
							//Path path = Paths.get("test.html");
							Charset charset = StandardCharsets.UTF_8;

							String content = new String(Files.readAllBytes(path), charset);
							content = content.replaceAll("<br>", "\r\n");
							Files.write(path, content.getBytes(charset));

							Process process2 = Runtime.getRuntime().exec(command2);
							BufferedReader reader2 = new BufferedReader(
									new InputStreamReader(process2.getInputStream()));
							String line2;
							while ((line2 = reader2.readLine()) != null) {
								System.out.println("Product: "+line2); 
								// Establish connection to DB
								//Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
								// Create a statement
								Statement myStmt = myConn.createStatement();
								//Execute query
								try {
									String mdb = "INSERT INTO ibmproducts (hauptprodukt, bundles,last_modified,link_to_document) VALUES ('"+line2+"',NULL,'"+content1+"','"+urldoc+"')";
									//String mdb2 = "INSERT INTO ibmproducts (bundles) VALUES ('"+line+"')WHERE hauptprodukt='NewProduct'";
											  
									myStmt.executeUpdate(mdb);
									
									
//									Process process3 = Runtime.getRuntime().exec(command3);
//									BufferedReader reader3 = new BufferedReader(
//											new InputStreamReader(process3.getInputStream()));
//									String line3;
//									while ((line3 = reader3.readLine()) != null) {
//										System.out.println("Productnumber: "+line3); 
//									}
								}catch (SQLException e) {
									//System.out.print("");
								}
								
								

							//}

							//System.out.println("Program names complete");
							//System.out.println();

							Process process = Runtime.getRuntime().exec(command);
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(process.getInputStream()));
							String line;
							//String res = new String();
							int k = 0;
							System.out.println("Bundles:");
							while ((line = reader.readLine()) != null) {
                                  System.out.println(line);
								// Establish connection to DB
								//Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
								// Create a statement
								Statement myStmt1 = myConn.createStatement();
								//Execute query
								try {
									String mdb = "UPDATE ibmproducts SET bundles = CONCAT_WS(CHAR(10 USING UTF8), bundles, '"+line+"') WHERE hauptprodukt='"+line2+"'";
									//String mdb2 = "INSERT INTO ibmproducts (bundles) VALUES ('"+line+"')WHERE hauptprodukt='NewProduct'";
											  
									myStmt1.executeUpdate(mdb);
								}catch (SQLException e) {
									//System.out.print("");
								}

								k++;
								//System.out.println(line);
							}

							}
							System.out.println("Bundles completed");

							//System.out.println("Anzahl Bundles:" + k);
							System.out.println("Databank Insert completed");
							
						}
						out.flush();
						out.close();
						try {
							Files.delete(path);
						} catch (NoSuchFileException x) {
							System.err.format("%s: no such" + " file or directory%n", path);
						} catch (DirectoryNotEmptyException x) {
							System.err.format("%s not empty%n", path);
						} catch (IOException x) {
							// File permission problems are caught here.
							System.err.println(x);
						}
					}



				}   // Ende der for-Schleife

			}   //Ende if


		} // Ende der for-Schleife


		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean isASCIIString( String pString ) {
		return CharMatcher.ASCII.matchesAllOf(pString);
	}

	private static StringBuffer getStringFromFile(String fileName, String charSetOfFile) {
		StringBuffer strBuffer = new StringBuffer();
		try(FileInputStream fis = new FileInputStream(fileName)) {
			byte[] buffer = new byte[10240]; //10K buffer;
			int readLen = -1;

			while( (readLen = fis.read(buffer)) != -1) {
				strBuffer.append( new String(buffer, 0, readLen, Charset.forName(charSetOfFile)));
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			strBuffer = new StringBuffer();
		}

		return strBuffer;
	}
}

//--after-context=1 


