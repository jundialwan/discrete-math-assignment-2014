import java.io.*;
import java.util.*;

/*
 * Diffie-Hellman Key Exchange Algorithm
 * and
 * Discrete Logarithm
 * Application
 * 
 * Developed by Jundi Ahmad Alwan, 13xxxxxxxxx, B
 * In purpose for Assignment 1 
 * Discrete Matemathics 2
 *
 * v1.0
 *
 * Kelas B
 */

public class KeyExchange {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	static Map<String, Integer> alphabetMap = new HashMap<>();

	public static void main(String[] md2) throws IOException {
		// fil alphabet map
		for(int ii = 0; ii < alphabet.length; ii++) alphabetMap.put(alphabet[ii], ii);

		System.out.print("Your Name: (1)");
		String name1 = br.readLine();
		System.out.print("Your Name: (2)");
		String name2 = br.readLine();

			int c = 0;

		while (c < 1 || c >2) {
			System.out.println("Choose :");
			System.out.println();
			System.out.println("g Mod p");
			System.out.println();
			System.out.println("1. Generate g and p with Pseudorandom");
			System.out.println("2. Manual Input g and p");

			c = Integer.parseInt(br.readLine());	
		}
		

		/* essential - !IMPORTANT */
			int p = 0; // must be prime number
			int g = 0;

		if (c == 1) /* Generate */ {
			System.out.println("--------------------------");
			System.out.println("Generate random g and p");

			/* essential - !IMPORTANT */
				p = getPseudorandom(); // must be prime number
				g = getPrimitiveRoot(p);

			System.out.println("p: "+ p);
			System.out.println("g: "+ g);
			System.out.println();

			System.out.println("*Please note that the value of p is between 11 to 51, p is prime number");
			System.out.println("*g is a primitive root modulo number of p - see documentation for algorithm");
			System.out.println();

		} else if (c == 2) /* Manual */ {
			System.out.println("--------------------------");
			System.out.println("Manual input of g and p");

			if (p == 0 || !(isPrime(p))) {
				System.out.print("Value of p (must be prime number)");
				p = Integer.parseInt(br.readLine());
			}

			System.out.print("Value of g (primitive root modulo of p)");
				g = Integer.parseInt(br.readLine());

			System.out.println("*WARNING: Value of g, might be invalid");
			System.out.println();
				
		}

		
		System.out.println("------------- "+ name1.toUpperCase() +" SECRET NUMBER -------------");
		System.out.print("Choose Your Secret Number: ");

		/* Essenial - !IMPORTANT*/
			int name1_a_secret = Integer.parseInt(br.readLine());

		System.out.println();
		System.out.println("------------- "+ name2.toUpperCase() +" SECRET NUMBER -------------");
		System.out.print("Choose Your Secret Number: ");

		/* Essential - !IMPORTANT */
			int name2_a_secret = Integer.parseInt(br.readLine());

		System.out.println();
		System.out.println("------------- CALCULATE PUBLIC KEY -------------");

		/* Essential - !IMPORTANT */		
			long name1_public = getOwnPublicKey(g, p, name1_a_secret);
			long name2_public = getOwnPublicKey(g, p, name2_a_secret);

		System.out.println(name1.toUpperCase()+" PUBLIC KEY:  "+ name1_public);
		System.out.println(name2.toUpperCase()+" PUBLIC KEY:  "+ name2_public);
		System.out.println();

		//System.out.println("------------- CALCULATE SHARED KEY -------------");	

		/* Essential - !IMPORTANT */	
			long name1_sharedKey = getSharedKeyDiffieHellman(name1_a_secret, p, name2_public);
			long name2_sharedKey = getSharedKeyDiffieHellman(name2_a_secret, p, name1_public);

		System.out.println(name1.toUpperCase()+" SHARED KEY: "+ name1_sharedKey);
		System.out.println(name2.toUpperCase()+" SHARED KEY: "+ name2_sharedKey);

		//test cipher
		//System.out.println(cipher(1, "MXQGL", 3));

		System.out.println("------------- "+name1.toUpperCase()+" MESSAGES ------------");

			String msg = br.readLine();

		System.out.println();

			String e_msg = cipher(0, msg, (int) name1_sharedKey);

		System.out.println("Encrypted "+name1.toUpperCase()+"'s MESSAGES: "+e_msg);
		System.out.println();

		System.out.println("------------- "+name2.toUpperCase()+" DECRYPT "+name1.toUpperCase()+"'s MESSAGES -------------");
		System.out.println();

			String d_msg = cipher(1, e_msg, (int) name2_sharedKey);

		System.out.println("Decrypt "+name1.toUpperCase()+"'s MESSAGES: "+d_msg);
	}

	public static int getPseudorandom() {
		Random r = new Random();
		// Low = 11 High = 51
		int rn = (r.nextInt(51-11) + 11);

		while (!(isPrime(rn))) { 
			rn = (r.nextInt(51-11) + 11); 
		}

		return rn;
	}

	public static boolean isPrime(int n) {
		int ii = 2;
		while (ii <= n / 2)
		{
		    if (n % ii == 0) return false;
		    ii++;
		}
		return true;
	}

	public static int getPrimitiveRoot(int n) {		
		int o = 1;
		int k;
		List<Integer> p_root_of_n = new ArrayList<Integer>();
		int z = 0;

		for (int r = 2; r < n; r++) {
			k = (int) Math.pow(r*1.0, o*1.0);
			k %= n;

			while (k > 1) {
				o++;
				k *= r;
				k %= n;
			}

			if (o == (n-1)) {
				p_root_of_n.add(r);
				z++;
			}

			o = 1;
		}

		int smallest = p_root_of_n.get(0);

		for(int ii = 0; ii < p_root_of_n.size(); ii ++) {
			if(p_root_of_n.get(ii) < smallest) {
				smallest = p_root_of_n.get(ii);
			}
		}

		return smallest;
	}

	public static long getOwnPublicKey(int g_primitive, long p_prime, int a_secret) {
		// g^a mod p
		return (((long) Math.pow(g_primitive*1.0, a_secret*1.0)) % p_prime);
	}

	public static int getSharedKeyDiffieHellman(long a_secret, long p_prime, long exchangeKey) {
		// Exchange_key^Own_Secret_Key mod P
		//System.out.println(exchangeKey+"^"+a_secret+" mod "+p_prime);
		double y =Math.pow(exchangeKey*1.0, a_secret*1.0);
		//System.out.println(y);
		double x = 	(y % p_prime);
		//System.out.println(x);	
		return (int) x;
	}

	public static String cryptaAnalysisDiffieHellman() {
		return "";
	}

	public static String cipher(int mode, String msg, int n) {
		String str = msg.toUpperCase();
		StringBuilder result = new StringBuilder();
		String sub = "";

		for(int ii = 0; ii < msg.length(); ii++) {
			sub = str.substring(ii, ii+1);

			if (!alphabetMap.containsKey(sub)) result.append(" ");
			else {
				if (mode == 0) /* encrypt */ {
					result.append(alphabet[
						(alphabetMap.get(sub) + n) % 26
					]);	
				} else /* decrypt*/ {
					result.append(alphabet[
						((alphabetMap.get(sub) - n) < 0) ? (((alphabetMap.get(sub) - n) + 26) % 26) : (alphabetMap.get(sub) - n) % 26
					]);	
				} 
			}
		}

		return result.toString();
	}
}