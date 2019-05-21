package mssoftutils.datas;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

public class MSJRBeanCollectionDataSource<T> extends JRAbstractBeanDataSource{

	private int initialIndex;
	private List<T> datas;
	private int currentIndex;
	
	public MSJRBeanCollectionDataSource(List<T> datas, int initialIndex) {
		super(true);
		
		this.datas = datas;
		this.initialIndex = initialIndex;
		this.currentIndex = initialIndex;
	}
	
	public MSJRBeanCollectionDataSource(List<T> datas) {
		this(datas, -1);
	}
	
	@Override
	public void moveFirst() throws JRException {				
		this.currentIndex = this.initialIndex;
	}

	@Override
	public boolean next() throws JRException {				
		return ++this.currentIndex < this.datas.size();		
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {				
		Object row = this.datas.get(this.currentIndex);
		Object result = getFieldValue(row, jrField);
		
		return result;
	}
	
	public List<T> getDatas() {
		return datas;
	}


	public int getRecordCount() {
		return datas == null ? 0 : datas.size();
	}

	public MSJRBeanCollectionDataSource<T> cloneDataSource() {
		return new MSJRBeanCollectionDataSource<T>(this.datas, this.initialIndex);
	}
}
