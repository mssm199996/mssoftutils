package mssoftutils.scalehandlers;

import mssoftutils.datas.Weight;

@FunctionalInterface
public interface ISerialScaleInterceptor {

	public void onWeightIntercepted(Weight weight);
}
