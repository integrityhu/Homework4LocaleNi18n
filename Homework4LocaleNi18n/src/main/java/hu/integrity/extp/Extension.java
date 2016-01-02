package hu.integrity.extp;

public interface Extension {

	public static int RANK_NONE = 0;
	public static int RANK_TYPE = 1;
	public static int RANK_INSTANCE = 2;

	public boolean match(Object o, String ns);

	public Object get(Object o, String ns);

	public int getRank(Object o, String ns);
}
