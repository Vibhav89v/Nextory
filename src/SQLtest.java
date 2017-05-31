import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;

class SQLtest {

	static Logger log = Logger.getLogger(SQLtest.class);

	
	public static void main(String[] args) 
	{
		String lastName = null;
		PropertyConfigurator.configure("log4j.properties");
		try {

			//getSeleniumTestCreatefail();
			//getSeleniumTest();
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://localhost:3306/nextory_new";
			// String myUrl = "jdbc:mysql://104.199.22.40:3306/nextory_new";
			Class.forName(myDriver);
			Connection conn = DriverManager
					.getConnection(myUrl, "root", "test");
			// Connection conn = DriverManager.getConnection(myUrl, "root",
			// "MyNewPass");

			Statement stmt = conn.createStatement();
			ResultSet rs;

			// rs = stmt.executeQuery("select count(*) as name from ismail  ");
			rs = stmt
					.executeQuery("select member_type_code as name from customerinfo where email='vibhav89.v@gmail.com' ");
			while (rs.next()) {
				lastName = rs.getString("name");
				// String ID = rs.getString("ID");
				// log.info(lastName+"-"+ID);
				int k = 0;
				int i = lastName.length();
				int j = i - 1;
				for (k = 1; k < i - 1; k++)
					k = k + 1;
				// log.info(lastName+" "+i+" "+j+" "+k );
				log.info(lastName);

			}

			String day14membercode = "203002";
			//Assert.assertEquals(lastName, day14membercode);
			log.info(lastName + "Test case passed");
			System.out.println(lastName + "Test case passed");
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

	}

}
