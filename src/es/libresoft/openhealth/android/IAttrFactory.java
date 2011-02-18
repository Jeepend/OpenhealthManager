/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

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

package es.libresoft.openhealth.android;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Parcelable;
import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.android.aidl.types.IAbsoluteTime;
import es.libresoft.openhealth.android.aidl.types.IAbsoluteTimeAdjust;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IAttrValMap;
import es.libresoft.openhealth.android.aidl.types.IAttrValMapEntry;
import es.libresoft.openhealth.android.aidl.types.IAuthBodyAndStrucType;
import es.libresoft.openhealth.android.aidl.types.IBITSTRING;
import es.libresoft.openhealth.android.aidl.types.IBasicNuObsValue;
import es.libresoft.openhealth.android.aidl.types.IBatMeasure;
import es.libresoft.openhealth.android.aidl.types.IConfigId;
import es.libresoft.openhealth.android.aidl.types.IFLOAT_Type;
import es.libresoft.openhealth.android.aidl.types.IHANDLE;
import es.libresoft.openhealth.android.aidl.types.IHighResRelativeTime;
import es.libresoft.openhealth.android.aidl.types.IINT_U16;
import es.libresoft.openhealth.android.aidl.types.IMdsTimeCapState;
import es.libresoft.openhealth.android.aidl.types.IMdsTimeInfo;
import es.libresoft.openhealth.android.aidl.types.IMetricIdList;
import es.libresoft.openhealth.android.aidl.types.IMetricSpecSmall;
import es.libresoft.openhealth.android.aidl.types.INomPartition;
import es.libresoft.openhealth.android.aidl.types.IOCTETSTRING;
import es.libresoft.openhealth.android.aidl.types.IOID_Type;
import es.libresoft.openhealth.android.aidl.types.IPowerStatus;
import es.libresoft.openhealth.android.aidl.types.IPrivateOid;
import es.libresoft.openhealth.android.aidl.types.IProductionSpec;
import es.libresoft.openhealth.android.aidl.types.IProductionSpecEntry;
import es.libresoft.openhealth.android.aidl.types.IRegCertData;
import es.libresoft.openhealth.android.aidl.types.IRegCertDataList;
import es.libresoft.openhealth.android.aidl.types.IRelativeTime;
import es.libresoft.openhealth.android.aidl.types.ISFloatType;
import es.libresoft.openhealth.android.aidl.types.ISystemModel;
import es.libresoft.openhealth.android.aidl.types.ITYPE;
import es.libresoft.openhealth.android.aidl.types.ITypeVer;
import es.libresoft.openhealth.android.aidl.types.ITypeVerList;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AbsoluteTimeAdjust;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.BatMeasure;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.HighResRelativeTime;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.MdsTimeInfo;
import ieee_11073.part_20601.asn1.MetricIdList;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.PowerStatus;
import ieee_11073.part_20601.asn1.ProdSpecEntry;
import ieee_11073.part_20601.asn1.ProductionSpec;
import ieee_11073.part_20601.asn1.RegCertData;
import ieee_11073.part_20601.asn1.RegCertDataList;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.asn1.TypeVer;
import ieee_11073.part_20601.asn1.TypeVerList;


public class IAttrFactory {

	private static IHANDLE HANDLE2parcelable(HANDLE handle) {
		IHANDLE ihandle = new IHANDLE(handle.getValue().getValue());
		return ihandle;
	}

	private static ITYPE TYPE2parcelable(TYPE type) {
		INomPartition partition = new INomPartition(type.getPartition().getValue());
		IOID_Type code = new IOID_Type(type.getCode().getValue().getValue());
		ITYPE itype = new ITYPE(partition, code, AttributeUtils.value2string(partition.getNomPart(), code.getType()));
		return itype;
	}

	private static ISystemModel SystemModel2parcelable(SystemModel model) {
		return new ISystemModel(new String(model.getManufacturer()), new String(model.getModel_number()));
	}

	private static IOCTETSTRING OCTETSTRING2parcelable(byte[] octetString) {
		return new IOCTETSTRING(octetString);
	}

	private static IConfigId ConfigId2parcelable(ConfigId confId) {
		return new IConfigId(confId.getValue());
	}

	private static IAttrValMap AttrValMap2parcelable(AttrValMap valMap) {
		ArrayList<IAttrValMapEntry> values = new ArrayList<IAttrValMapEntry>();
		Iterator<AttrValMapEntry> it = valMap.getValue().iterator();
		while (it.hasNext()) {
			AttrValMapEntry entry = it.next();
			values.add(new IAttrValMapEntry(new IOID_Type(entry.getAttribute_id().getValue().getValue()), entry.getAttribute_len()));
		}
		return new IAttrValMap(values);
	}

	private static IProductionSpec AttrProductionSpec2parcelable(ProductionSpec spec) {
		ArrayList<IProductionSpecEntry> values = new ArrayList<IProductionSpecEntry>();
		Iterator<ProdSpecEntry> it = spec.getValue().iterator();
		while (it.hasNext()) {
			ProdSpecEntry entry = it.next();
			values.add(new IProductionSpecEntry(entry.getSpec_type(),
									new IPrivateOid(entry.getComponent_id().getValue().getValue()),
									new IOCTETSTRING(entry.getProd_spec())));
		}
		return new IProductionSpec(values);
	}

	private static IMdsTimeInfo MdsTimeInfo2parcelable(MdsTimeInfo timeInfo) {
		return new IMdsTimeInfo(new IMdsTimeCapState(new IBITSTRING(timeInfo.getMds_time_cap_state().getValue().getValue(), timeInfo.getMds_time_cap_state().getValue().getTrailBitsCnt())),
				new IOID_Type(timeInfo.getTime_sync_protocol().getValue().getValue().getValue()),
				new IRelativeTime(timeInfo.getTime_sync_accuracy().getValue().getValue()),
				timeInfo.getTime_resolution_abs_time(), timeInfo.getTime_resolution_rel_time(),
				timeInfo.getTime_resolution_high_res_time().getValue());
	}

	private static IAbsoluteTime AbsoluteTime2parcelable(AbsoluteTime absTime) {
		return new IAbsoluteTime(absTime.getCentury().getValue(),
							absTime.getYear().getValue(),
							absTime.getMonth().getValue(),
							absTime.getDay().getValue(),
							absTime.getHour().getValue(),
							absTime.getMinute().getValue(),
							absTime.getSecond().getValue(),
							absTime.getSec_fractions().getValue());
	}

	private static IRelativeTime RelativeTime2parcelable(RelativeTime relTime) {
		return new IRelativeTime(relTime.getValue().getValue());
	}

	private static IHighResRelativeTime HighResRelativeTime2parcelable(HighResRelativeTime relTime) {
		return new IHighResRelativeTime(new IOCTETSTRING(relTime.getValue()));
	}

	private static IAbsoluteTimeAdjust AbsoluteTimeAdjust2parcelable(AbsoluteTimeAdjust absTimeAdj) {
		return new IAbsoluteTimeAdjust(new IOCTETSTRING(absTimeAdj.getValue()));
	}

	private static IPowerStatus PowerStatus2parcelable(PowerStatus powerStatus) {
		return new IPowerStatus(new IBITSTRING(powerStatus.getValue().getValue(), powerStatus.getValue().getTrailBitsCnt()));
	}

	private static IINT_U16 INT_U162parcelable(INT_U16 intu16) {
		return new IINT_U16(intu16.getValue());
	}

	private static IBatMeasure BatMeasure2parcelable(BatMeasure batMeasure) {
		try {
			return new IBatMeasure(new IFLOAT_Type(new FloatType(batMeasure.getValue().getValue().getValue()).doubleValueRepresentation()),
								new IOID_Type(batMeasure.getUnit_().getValue().getValue()));
		} catch (Exception e) {
			return null;
		}
	}

	private static IRegCertDataList RegCertDataList2parcelable(RegCertDataList asnAttr) {
		ArrayList<IRegCertData> values = new ArrayList<IRegCertData>();
		Iterator<RegCertData> it = asnAttr.getValue().iterator();
		while (it.hasNext()) {
			RegCertData entry = it.next();
			values.add(new IRegCertData(new IAuthBodyAndStrucType(entry.getAuth_body_and_struc_type().getAuth_body().getValue(),
											entry.getAuth_body_and_struc_type().getAuth_body_struc_type().getValue()),
											null));
		}
		return new IRegCertDataList(values);
	}

	private static ITypeVerList TypeVerList2parcelable(TypeVerList verList) {
		ArrayList<ITypeVer> values = new ArrayList<ITypeVer>();
		Iterator<TypeVer> it = verList.getValue().iterator();
		while (it.hasNext()) {
			TypeVer ver = it.next();
			values.add(new ITypeVer(new IOID_Type(ver.getType().getValue().getValue()), ver.getVersion()));
		}
		return new ITypeVerList(values);
	}

	private static IOID_Type OID_Type2parcelable(OID_Type type) {
		return new IOID_Type(type.getValue().getValue());
	}

	private static IMetricIdList MetricIdList2parcelable(MetricIdList idList) {
		ArrayList <IOID_Type> types = new ArrayList<IOID_Type>();
		for (OID_Type type: idList.getValue())
			types.add(OID_Type2parcelable(type));

		return new IMetricIdList(types);
	}

	private static IBasicNuObsValue BasicNuObsValue2parcelable(BasicNuObsValue obsValue) {
		SFloatType value;
		try {
			value = new SFloatType(obsValue.getValue().getValue());
			return new IBasicNuObsValue(new ISFloatType(value.getExponent(), value.getMagnitude(), value.doubleValueRepresentation(), value.toString()));
		} catch (Exception e) {
			return null;
		}
	}

	private static IMetricSpecSmall MetricSpecSmall2parcelable(MetricSpecSmall metricSpec) {
		return new IMetricSpecSmall(new IBITSTRING(metricSpec.getValue().getValue(), metricSpec.getValue().getTrailBitsCnt()));
	}

	public static final boolean getParcelableAttribute (Object asnAttr, IAttribute attr) {

		Parcelable parcel = null;

		if (attr == null)
			return false;

		if (asnAttr instanceof HANDLE)
			parcel = HANDLE2parcelable((HANDLE) asnAttr);
		else if (asnAttr instanceof TYPE)
			parcel = TYPE2parcelable((TYPE) asnAttr);
		else if (asnAttr instanceof SystemModel)
			parcel = SystemModel2parcelable((SystemModel) asnAttr);
		else if (asnAttr instanceof byte[])
			parcel = OCTETSTRING2parcelable((byte []) asnAttr);
		else if (asnAttr instanceof ConfigId)
			parcel = ConfigId2parcelable((ConfigId) asnAttr);
		else if (asnAttr instanceof AttrValMap)
			parcel = AttrValMap2parcelable((AttrValMap) asnAttr);
		else if (asnAttr instanceof ProductionSpec)
			parcel = AttrProductionSpec2parcelable((ProductionSpec) asnAttr);
		else if (asnAttr instanceof MdsTimeInfo)
			parcel = MdsTimeInfo2parcelable((MdsTimeInfo) asnAttr);
		else if (asnAttr instanceof AbsoluteTime)
			parcel = AbsoluteTime2parcelable((AbsoluteTime) asnAttr);
		else if (asnAttr instanceof RelativeTime)
			parcel = RelativeTime2parcelable((RelativeTime) asnAttr);
		else if (asnAttr instanceof HighResRelativeTime)
			parcel = HighResRelativeTime2parcelable((HighResRelativeTime) asnAttr);
		else if (asnAttr instanceof AbsoluteTimeAdjust)
			parcel = AbsoluteTimeAdjust2parcelable((AbsoluteTimeAdjust) asnAttr);
		else if (asnAttr instanceof PowerStatus)
			parcel = PowerStatus2parcelable((PowerStatus) asnAttr);
		else if (asnAttr instanceof INT_U16)
			parcel = INT_U162parcelable((INT_U16) asnAttr);
		else if (asnAttr instanceof BatMeasure)
			parcel = BatMeasure2parcelable((BatMeasure) asnAttr);
		else if (asnAttr instanceof RegCertDataList)
			parcel = RegCertDataList2parcelable((RegCertDataList) asnAttr);
		else if (asnAttr instanceof TypeVerList)
			parcel = TypeVerList2parcelable((TypeVerList) asnAttr);
		else if (asnAttr instanceof OID_Type)
			parcel = OID_Type2parcelable((OID_Type) asnAttr);
		else if (asnAttr instanceof BasicNuObsValue)
			parcel = BasicNuObsValue2parcelable((BasicNuObsValue) asnAttr);
		else if (asnAttr instanceof MetricSpecSmall)
			parcel = MetricSpecSmall2parcelable((MetricSpecSmall) asnAttr);
		else if (asnAttr instanceof MetricIdList)
			parcel = MetricIdList2parcelable((MetricIdList) asnAttr);

		if (parcel != null) {
			attr.setAttr(parcel);
			return true;
		}

		System.err.println("Unknown method provided. Can't create parcelable attribute for type " + asnAttr.getClass());
		return false;
	}

}
