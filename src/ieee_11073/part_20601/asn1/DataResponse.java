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
    @ASN1Sequence ( name = "DataResponse", isSet = false )
    public class DataResponse implements IASN1PreparedElement {

        @ASN1Element ( name = "rel-time-stamp", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 0  )

	private RelativeTime rel_time_stamp = null;


        @ASN1Element ( name = "data-req-result", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 1  )

	private DataReqResult data_req_result = null;


        @ASN1Element ( name = "event-type", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 2  )

	private OID_Type event_type = null;

  @ASN1Any( name = "" )

        @ASN1Element ( name = "event-info", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 3  )

	private byte[] event_info = null;



        public RelativeTime getRel_time_stamp () {
            return this.rel_time_stamp;
        }



        public void setRel_time_stamp (RelativeTime value) {
            this.rel_time_stamp = value;
        }



        public DataReqResult getData_req_result () {
            return this.data_req_result;
        }



        public void setData_req_result (DataReqResult value) {
            this.data_req_result = value;
        }



        public OID_Type getEvent_type () {
            return this.event_type;
        }



        public void setEvent_type (OID_Type value) {
            this.event_type = value;
        }



        public byte[] getEvent_info () {
            return this.event_info;
        }



        public void setEvent_info (byte[] value) {
            this.event_info = value;
        }




        public void initWithDefaults() {

        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(DataResponse.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
