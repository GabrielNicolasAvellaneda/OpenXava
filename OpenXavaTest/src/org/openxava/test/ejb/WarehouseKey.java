package org.openxava.test.ejb;
/**
 * Key class for Entity Bean: Storehouse
 */
public class WarehouseKey implements java.io.Serializable {
	static final long serialVersionUID = 3206093459760846163L;

	public int zoneNumber;
	public int number;
	
	public WarehouseKey() {
	}
	/**
	 * Creates a key for Entity Bean: Factura
	 */
	public WarehouseKey(int zoneNumber, int number) {
		this.zoneNumber = zoneNumber;
		this.number = number;
	}
	
	public String toString() {
		return "Warehouse: " + zoneNumber + "/" + number;
	}
	
	
	public boolean equals(java.lang.Object otherKey) {
		if (otherKey instanceof org.openxava.test.ejb.WarehouseKey) {
			org.openxava.test.ejb.WarehouseKey o = (org.openxava.test.ejb.WarehouseKey)otherKey;
			return ((this.zoneNumber == o.zoneNumber)
				&& (this.number == o.number));
		}
		return false;
	}

	public int hashCode() {
		return ((new java.lang.Integer(zoneNumber).hashCode())
			+ (new java.lang.Integer(number).hashCode()));
	}
}
