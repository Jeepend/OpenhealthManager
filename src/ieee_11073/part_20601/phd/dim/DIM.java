/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
package ieee_11073.part_20601.phd.dim;

import java.util.Hashtable;
import java.util.Iterator;

public abstract class DIM {

	private MDS mds;

	/*AttributeID,Attribute*/
	protected Hashtable<Integer,Attribute> attributeList;

	public DIM () {}

	public DIM (Hashtable<Integer,Attribute> attributes) throws InvalidAttributeException {
		if (attributes==null) {
			throw new InvalidAttributeException("Not attributes has been declared in MDS class.");
		} else if (attributes.isEmpty()) {
			throw new InvalidAttributeException("Not attributes has been declared in MDS class.");
		}
		this.attributeList = new Hashtable<Integer,Attribute>();
		checkAttributes(attributes);
		addCheckedAttributes(attributes);
	}

	public Attribute getAttribute(int attributeId){
		return attributeList.get(new Integer(attributeId));
	}

	public Attribute getAttribute(String friendlyName){
		Iterator<Attribute> i =  attributeList.values().iterator();
		Attribute attr;
		while (i.hasNext()) {
			attr = (Attribute)i.next();
			if (attr.getAttributeName().equals(friendlyName))
					return attr;
		}
		return null;
	}

	public void addAttribute(Attribute attr){
		attributeList.put(new Integer(attr.getAttributeID()), attr);
	}

	public abstract int getNomenclatureCode ();

	protected abstract void checkAttributes(Hashtable<Integer,Attribute> attributes) throws InvalidAttributeException;


	public void setMDS(MDS mds) {
		this.mds = mds;
	}

	public MDS getMDS() {
		return this.mds;
	}

	//-------------------------------------------PRIVATE------------------------------------------------------
	private void addCheckedAttributes(Hashtable<Integer,Attribute> attributes){
		/* Add attributes to attribute list */
		Iterator<Attribute> i = attributes.values().iterator();
		while (i.hasNext()){
			addAttribute(i.next());
		}
	}
}
