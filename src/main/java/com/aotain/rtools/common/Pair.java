package com.aotain.rtools.common;

/**
 * 键值对
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午3:24:52
 */
public class Pair<K, V> {
	private K key;
	private V value;

	private Object extData; // 附加数据

	public Object getExtData() {
		return extData;
	}

	public void setExtData(Object extData) {
		this.extData = extData;
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String value = this.value == null || this.value.toString().trim().equals("") ? "null" : this.value.toString();
		return key.toString() + " - " + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
