package hu.integrity.jpa.lazyload;

import java.io.Serializable;
import java.util.List;

public class LoadResult<T> implements Serializable {
	public List<T> resultList;
	public int rowCount;
	private static final long serialVersionUID = 1L;
}