import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Datte {

	public static String addDate()
	{
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		
		
		Calendar c=new GregorianCalendar();
		
		c.add(Calendar.DATE, -1);
		Date d=c.getTime();
		
		System.out.println(dateFormat.format(d));
		return dateFormat.format(d);
	}

	public static void main(String[] args) 
	{
		addDate();
	}
}
