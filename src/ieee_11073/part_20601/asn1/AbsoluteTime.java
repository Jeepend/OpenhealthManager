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
    @ASN1Sequence ( name = "AbsoluteTime", isSet = false )
    public class AbsoluteTime implements IASN1PreparedElement {

        @ASN1Element ( name = "century", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 0   )

	private INT_U8 century = null;


        @ASN1Element ( name = "year", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 1   )

	private INT_U8 year = null;


        @ASN1Element ( name = "month", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 2  )

	private INT_U8 month = null;


        @ASN1Element ( name = "day", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 3  )

	private INT_U8 day = null;


        @ASN1Element ( name = "hour", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 4  )

	private INT_U8 hour = null;


        @ASN1Element ( name = "minute", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 5  )

	private INT_U8 minute = null;


        @ASN1Element ( name = "second", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 6  )

	private INT_U8 second = null;


        @ASN1Element ( name = "sec-fractions", isOptional =  false , hasTag =  false  , hasDefaultValue =  false, hasExplicitOrder = true, declarationOrder = 7  )

	private INT_U8 sec_fractions = null;



        public INT_U8 getCentury () {
            return this.century;
        }



        public void setCentury (INT_U8 value) {
            this.century = value;
        }



        public INT_U8 getYear () {
            return this.year;
        }



        public void setYear (INT_U8 value) {
            this.year = value;
        }



        public INT_U8 getMonth () {
            return this.month;
        }



        public void setMonth (INT_U8 value) {
            this.month = value;
        }



        public INT_U8 getDay () {
            return this.day;
        }



        public void setDay (INT_U8 value) {
            this.day = value;
        }



        public INT_U8 getHour () {
            return this.hour;
        }



        public void setHour (INT_U8 value) {
            this.hour = value;
        }



        public INT_U8 getMinute () {
            return this.minute;
        }



        public void setMinute (INT_U8 value) {
            this.minute = value;
        }



        public INT_U8 getSecond () {
            return this.second;
        }



        public void setSecond (INT_U8 value) {
            this.second = value;
        }



        public INT_U8 getSec_fractions () {
            return this.sec_fractions;
        }



        public void setSec_fractions (INT_U8 value) {
            this.sec_fractions = value;
        }




        public void initWithDefaults() {

        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(AbsoluteTime.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
