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

package ieee_11073.part_20601.asn1;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net
// Any modifications to this file will be lost upon recompilation of the source ASN.1.
//

import org.bn.*;
import org.bn.annotations.*;
import org.bn.annotations.constraints.*;
import org.bn.coders.*;
import org.bn.types.*;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "ManufSpecAssociationInformation", isSet = false )
    public class ManufSpecAssociationInformation implements IASN1PreparedElement {

        @ASN1Element ( name = "data-proto-id-ext", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 0  )

	private UuidIdent data_proto_id_ext = null;

  @ASN1Any( name = "" )

        @ASN1Element ( name = "data-proto-info-ext", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 1  )

	private byte[] data_proto_info_ext = null;



        public UuidIdent getData_proto_id_ext () {
            return this.data_proto_id_ext;
        }



        public void setData_proto_id_ext (UuidIdent value) {
            this.data_proto_id_ext = value;
        }



        public byte[] getData_proto_info_ext () {
            return this.data_proto_info_ext;
        }



        public void setData_proto_info_ext (byte[] value) {
            this.data_proto_info_ext = value;
        }




        public void initWithDefaults() {

        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(ManufSpecAssociationInformation.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
