package com.aotain.rtools.common;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * JComboPairModel
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午3:53:40
 */
public class JComboPairModel<K, V> extends AbstractListModel<Pair<K, V>> implements ComboBoxModel<Pair<K, V>> {
	private static final long serialVersionUID = -6547821138409993766L;

	private List<Pair<K, V>> list = null;

	private Object selected = null;

	public JComboPairModel() {
		list = new ArrayList<Pair<K, V>>();
	}

	public JComboPairModel(List<Pair<K, V>> list) {
		if (list == null) {
			this.list = new ArrayList<Pair<K, V>>();
		} else {
			this.list = list;
		}
	}

	public void add(Pair<K, V> pair) {
		list.add(pair);
	}

	public void clear() {
		list.clear();
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Pair<K, V> getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selected = anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

}
