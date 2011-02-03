/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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
    @ASN1Sequence ( name = "ActionArgumentSimple", isSet = false )
    public class ActionArgumentSimple implements IASN1PreparedElement {

        @ASN1Element ( name = "obj-handle", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 0  )

	private HANDLE obj_handle = null;


        @ASN1Element ( name = "action-type", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 1  )

	private OID_Type action_type = null;

  @ASN1Any( name = "" )

        @ASN1Element ( name = "action-info-args", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 2  )

	private byte[] action_info_args = null;



        public HANDLE getObj_handle () {
            return this.obj_handle;
        }



        public void setObj_handle (HANDLE value) {
            this.obj_handle = value;
        }



        public OID_Type getAction_type () {
            return this.action_type;
        }



        public void setAction_type (OID_Type value) {
            this.action_type = value;
        }



        public byte[] getAction_info_args () {
            return this.action_info_args;
        }



        public void setAction_info_args (byte[] value) {
            this.action_info_args = value;
        }




        public void initWithDefaults() {

        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(ActionArgumentSimple.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
