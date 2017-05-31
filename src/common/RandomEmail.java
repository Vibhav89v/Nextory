package common;

public class RandomEmail
{
	
	public static String email()
	{
		String a="automation" + System.currentTimeMillis() + "@frescano.se";
		System.out.println(a);
		return a;
		
	}  
	
	public static void main(String[] args)
	{
		for(int i=0; i<=5; i++)
		{
		email();
		}
	}

}