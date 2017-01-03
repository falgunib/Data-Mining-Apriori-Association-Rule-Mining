package dmhw;
import java.math.BigInteger;

public class MakeCombination {

	
	private int n1;
	private int n2;
	private BigInteger rem;
	public BigInteger tcount;
	private int[] array;

	public MakeCombination(int n1, int n2) {
		if (n2 > n1) {
			throw new IllegalArgumentException();
		}
		if (n1 < 1) {
			throw new IllegalArgumentException();
		}
		this.n1 = n1;
		this.n2 = n2;
		array = new int[n2];
		BigInteger nFact = factfunc(n1);
		BigInteger rFact = factfunc(n2);
		BigInteger nminusrFact = factfunc(n1 - n2);
		tcount = nFact.divide(rFact.multiply(nminusrFact));
		resetvalues();
	}

	public void resetvalues() {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		rem = new BigInteger(tcount.toString());
	}
	public BigInteger getNumLeft() {
		return rem;
	}
	public boolean hasMore() {
		return rem.compareTo(BigInteger.ZERO) == 1;
	}

	public BigInteger getTotal() {
		return tcount;
	}

	private static BigInteger factfunc(int n1) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n1; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	// --------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	// --------------------------------------------------------

	public int[] getNext() {

		if (rem.equals(tcount)) {
			rem = rem.subtract(BigInteger.ONE);
			return array;
		}
		else{
			int i = n2 - 1;
			while (array[i] == n1 - n2 + i) {
				i--;
			}
			array[i] = array[i] + 1;
			for (int j = i + 1; j < n2; j++) {
				array[j] = array[i] + j - i;
			}

			rem = rem.subtract(BigInteger.ONE);
			return array;
		}

	}
}
