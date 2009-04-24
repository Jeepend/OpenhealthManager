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
package ieee_11073.part_10101;

	/*************************************************************************************************
	 * This interface represents nomenclature codes
	 * This annex contains the nomenclature codes used in this standard. They are either copied from
	 * ISO/IEEE 11073-10101 or created for this standard and incorporated into ISO/IEEE 11073-10101.
	 * The format used here follows the one defined in ISO/IEEE 11073-10101.
	 *************************************************************************************************/
public interface Nomenclature {
	
	/* Partition codes																		 		 */                                       
	public final int MDC_PART_OBJ						=	1;		/* Object Infrastr.              */                
	public final int MDC_PART_SCADA						=	2;		/* SCADA (Physio IDs             */
	public final int MDC_PART_DIM						=	4;		/* Dimension                     */
	public final int MDC_PART_INFRA						=	8;		/* Infrastructure                */
	public final int MDC_PART_PHD_DM					=	128;	/* Disease Mgmt                  */
	public final int MDC_PART_PHD_HF					=	129;	/* Health and Fitness            */
	public final int MDC_PART_PHD_AI					=	130;	/* Aging Independently           */
	public final int MDC_PART_RET_CODE					=	255;	/* Return Codes                  */
	public final int MDC_PART_EXT_NOM					=	256;	/* Ext. Nomenclature             */
	
	/*************************************************************************************************
	* From Object Infrastructure (MDC_PART_OBJ)
	**************************************************************************************************/
	public final int MDC_MOC_VMO_METRIC					=	4;		/*                               */
	public final int MDC_MOC_VMO_METRIC_ENUM			=	5;		/*                               */
	public final int MDC_MOC_VMO_METRIC_NU				=	6;		/*                               */
	public final int MDC_MOC_VMO_METRIC_SA_RT			=	9; 		/*                               */
	public final int MDC_MOC_SCAN						=	16;		/*                               */
	public final int MDC_MOC_SCAN_CFG					=	17;		/*                               */
	public final int MDC_MOC_SCAN_CFG_EPI				=	18;		/*                               */
	public final int MDC_MOC_SCAN_CFG_PERI				=	19;		/*                               */
	public final int MDC_MOC_VMS_MDS_SIMP				=	37;		/*                               */
	public final int MDC_MOC_VMO_PMSTORE				=	61;		/*                               */
	public final int MDC_MOC_PM_SEGMENT					=	62;		/*                               */
	public final int MDC_ATTR_CONFIRM_MODE				=	2323;	/*                               */
	public final int MDC_ATTR_CONFIRM_TIMEOUT			=	2324;	/*                               */
	public final int MDC_ATTR_ID_HANDLE					=	2337;	/*                               */
    public final int MDC_ATTR_ID_INSTNO 				=	2338;	/*                               */
    public final int MDC_ATTR_ID_LABEL_STRING			=	2343;	/*                               */
    public final int MDC_ATTR_ID_MODEL					=	2344;	/*                               */
    public final int MDC_ATTR_ID_PHYSIO					=	2347;	/*                               */
    public final int MDC_ATTR_ID_PROD_SPECN				=	2349;	/*                               */         
    public final int MDC_ATTR_ID_TYPE					=	2351;	/*                               */
    public final int MDC_ATTR_METRIC_STORE_CAPAC_CNT	=	2369;	/*                               */
    public final int MDC_ATTR_METRIC_STORE_SAMPLE_ALG 	=	2371;	/*                               */
    public final int MDC_ATTR_METRIC_STORE_USAGE_CNT 	=	2372;	/*                               */
    public final int MDC_ATTR_MSMT_STAT					=	2375;	/*                               */
    public final int MDC_ATTR_NU_ACCUR_MSMT				=	2378;	/*                               */
    public final int MDC_ATTR_NU_CMPD_VAL_OBS			=	2379;	/*                               */
    public final int MDC_ATTR_NU_VAL_OBS				=	2384;	/*                               */
    public final int MDC_ATTR_NUM_SEG					=	2385;	/*                               */
    public final int MDC_ATTR_OP_STAT					=	2387;	/*                               */
    public final int MDC_ATTR_POWER_STAT				=	2389;	/*                               */
	public final int MDC_ATTR_SA_SPECN					=	2413;	/*                               */
	public final int MDC_ATTR_SCALE_SPECN_I16			=	2415;	/*                               */
	public final int MDC_ATTR_SCALE_SPECN_I32			=	2416;	/*                               */
	public final int MDC_ATTR_SCALE_SPECN_I8			=   2417;	/*                               */
	public final int MDC_ATTR_SCAN_REP_PD				=   2421;	/*                               */
	public final int MDC_ATTR_SEG_USAGE_CNT				=   2427;	/*                               */
	public final int MDC_ATTR_SYS_ID					=   2436;	/*                               */
	public final int MDC_ATTR_SYS_TYPE					=   2438;	/*                               */
	public final int MDC_ATTR_TIME_ABS					=   2439;	/*                               */
	public final int MDC_ATTR_TIME_BATT_REMAIN			=	2440;	/*                               */
	public final int MDC_ATTR_TIME_END_SEG				=   2442;	/*                               */
	public final int MDC_ATTR_TIME_PD_SAMP				=   2445;	/*                               */
	public final int MDC_ATTR_TIME_REL					=   2447;	/*                               */
	public final int MDC_ATTR_TIME_STAMP_ABS			=   2448;	/*                               */
	public final int MDC_ATTR_TIME_STAMP_REL			=   2449;	/*                               */
	public final int MDC_ATTR_TIME_START_SEG			=   2450;	/*                               */
	public final int MDC_ATTR_TX_WIND					=   2453;	/*                               */
	public final int MDC_ATTR_UNIT_CODE					=   2454;	/*                               */
	public final int MDC_ATTR_UNIT_LABEL_STRING			=	2457;	/*                               */
	public final int MDC_ATTR_VAL_BATT_CHARGE			=	2460;	/*                               */
	public final int MDC_ATTR_VAL_ENUM_OBS				=   2462;	/*                               */
	public final int MDC_ATTR_TIME_REL_HI_RES			=   2536;	/*                               */
	public final int MDC_ATTR_TIME_STAMP_REL_HI_RES		=	2537;	/*                               */
	public final int MDC_ATTR_DEV_CONFIG_ID				=   2628;	/*                               */
	public final int MDC_ATTR_MDS_TIME_INFO				=   2629;	/*                               */
	public final int MDC_ATTR_METRIC_SPEC_SMALL			=	2630;	/*                               */
	public final int MDC_ATTR_SOURCE_HANDLE_REF			=	2631;	/*                               */
	public final int MDC_ATTR_SIMP_SA_OBS_VAL			=	2632;	/*                               */
	public final int MDC_ATTR_ENUM_OBS_VAL_SIMP_OID		=	2633;	/*                               */
	public final int MDC_ATTR_ENUM_OBS_VAL_SIMP_STR		=	2634;	/*                               */
	public final int MDC_ATTR_REG_CERT_DATA_LIST		=	2635;	/*                               */
	public final int MDC_ATTR_NU_VAL_OBS_BASIC			=	2636;	/*                               */
	public final int MDC_ATTR_PM_STORE_CAPAB			=   2637;	/*                               */
	public final int MDC_ATTR_PM_SEG_MAP				=   2638;	/*                               */
	public final int MDC_ATTR_PM_SEG_PERSON_ID 			=	2639;	/*                               */
	public final int MDC_ATTR_SEG_STATS					=   2640;	/*                               */
	public final int MDC_ATTR_SEG_FIXED_DATA			=   2641;	/*                               */
	public final int MDC_ATTR_PM_SEG_ELEM_STAT_ATTR		=	2642;	/*                               */
	public final int MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP	=	2643;	/*                               */
	public final int MDC_ATTR_SCAN_REP_PD_MIN         	=	2644;	/*                               */
	public final int MDC_ATTR_ATTRIBUTE_VAL_MAP       	=	2645;	/*                               */
	public final int MDC_ATTR_NU_VAL_OBS_SIMP         	=	2646;	/*                               */
	public final int MDC_ATTR_PM_STORE_LABEL_STRING 	=	2647;	/*                               */
	public final int MDC_ATTR_PM_SEG_LABEL_STRING     	=	2648;	/*                               */
	public final int MDC_ATTR_TIME_PD_MSMT_ACTIVE     	=	2649;	/*                               */
	public final int MDC_ATTR_SYS_TYPE_SPEC_LIST      	=	2650;	/*                               */
	public final int MDC_ATTR_METRIC_ID_PART			=   2655;	/*                               */
	public final int MDC_ATTR_ENUM_OBS_VAL_PART       	=	2656;	/*                               */
	public final int MDC_ATTR_SUPPLEMENTAL_TYPES      	=	2657;	/*                               */
	public final int MDC_ATTR_TIME_ABS_ADJUST        	=	2658;	/*                               */	
	public final int MDC_ATTR_CLEAR_TIMEOUT				=   2659;	/*                               */
	public final int MDC_ATTR_TRANSFER_TIMEOUT        	=	2660;	/*                               */
	public final int MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR =	2661;	/*                               */
	public final int MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR=	2662;	/*                               */
	public final int MDC_ATTR_METRIC_STRUCT_SMALL		=   2675;	/*                               */
	public final int MDC_ATTR_NU_CMPD_VAL_OBS_SIMP  	=	2676;	/*                               */
	public final int MDC_ATTR_NU_CMPD_VAL_OBS_BASIC 	=	2677;	/*                               */
	public final int MDC_ATTR_ID_PHYSIO_LIST			=	2678;	/*                               */
	public final int MDC_ATTR_SCAN_HANDLE_LIST 			=	2679;	/*                               */
	/* Partition: ACT */
	public final int MDC_ACT_SEG_CLR 					=	3084;	/*                               */
	public final int MDC_ACT_SEG_GET_INFO 				=	3085;	/*                               */
	public final int MDC_ACT_SET_TIME 					=	3095;	/*                               */
	public final int MDC_ACT_DATA_REQUEST 				=	3099;	/*                               */
	public final int MDC_ACT_SEG_TRIG_XFER 				=	3100;	/*                               */
	public final int MDC_NOTI_CONFIG 					=	3356;	/*                               */
	public final int MDC_NOTI_SCAN_REPORT_FIXED       	=	3357;	/*                               */
	public final int MDC_NOTI_SCAN_REPORT_VAR         		=	3358;	/*                           */
	public final int MDC_NOTI_SCAN_REPORT_MP_FIXED    		=	3359;	/*                           */
	public final int MDC_NOTI_SCAN_REPORT_MP_VAR      		=	3360;	/*                           */
	public final int MDC_NOTI_SEGMENT_DATA 					=	3361;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_VAR 		=	3362;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_FIXED 		=	3363;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED		=	3364;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR		=	3365;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED	=	3366;	/*                           */
	public final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED 	=	3367;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_VAR			=	3368;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_FIXED			=	3369;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_GROUPED   	=	3370;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_MP_VAR		=	3371;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED		=	3372;	/*                           */
	public final int MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED	=	3373;	/*                           */

	/*************************************************************************************************
	* From Medical supervisory control and data acquisition (MDC_PART_SCADA)
	**************************************************************************************************/
	public final int MDC_TEMP_BODY						=	19292;	/*                               */
	public final int MDC_MASS_BODY_ACTUAL 				=	57664;	/*                               */
	/* Partition: SCADA/Other                                                        				 */
	public final int MDC_BODY_FAT						=	57676;	/*                               */
	
	/*************************************************************************************************
	* From Dimensions (MDC_PART_DIM)
	**************************************************************************************************/                                             
	public final int MDC_DIM_PERCENT					=	544;	/* %                          	 */
	public final int MDC_DIM_KILO_G						=	1731;	/* kg 	                         */
	public final int MDC_DIM_MIN						=	2208;	/* min    	                     */
	public final int MDC_DIM_HR							=	2240;	/* h          	                 */
	public final int MDC_DIM_DAY						=	2272;	/* d              	             */
	public final int MDC_DIM_DEGC						=	6048;	/* ºC                 	         */

	/*************************************************************************************************
	* From Communication Infrastructure (MDC_PART_INFRA)
	**************************************************************************************************/
	public final int MDC_DEV_SPEC_PROFILE_PULS_OXIM		=	4100;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_BP			=	4103;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_TEMP			=	4104;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_SCALE			=	4111;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_GLUCOSE		=	4113;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_HF_CARDIO		=	4137;	/*                               */
	public final int MDC_DEV_SPEC_PROFILE_HF_STRENGTH 	=	4138;	/*                               */                                                      
	public final int MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB = 4167;	/*                               */                                                
	public final int MDC_DEV_SPEC_PROFILE_AI_MED_MINDER	=	4168;	/*                               */                                                 
	/* Placed 256 back from the start of the last Partition: OptionalPackageIdentifiers (i.e., 8192-256). 		  */
	public final int MDC_TIME_SYNC_NONE					=	7936;	/* no time synchronization protocol supported */
	public final int MDC_TIME_SYNC_NTPV3				=	7937;	/* RFC 1305 1992 Mar obs: 1119,1059,958 	  */
	public final int MDC_TIME_SYNC_NTPV4 				=	7938;	/* <under development at ntp.org>  			  */
	public final int MDC_TIME_SYNC_SNTPV4				=	7939;	/* RFC 2030 1996 Oct          obs: 1769       */
	public final int MDC_TIME_SYNC_SNTPV4330			=	7940;	/* RFC 4330 2006 Jan          obs: 2030,1769  */
	public final int MDC_TIME_SYNC_BTV1					=	7941;	/* Bluetooth Medical Device Profile           */

	/*************************************************************************************************
	* From Return Codes (MDC_PART_RET_CODE)
	**************************************************************************************************/
	public final int MDC_RET_CODE_UNKNOWN				=	1;		/* Generic error code			 */
	/* Partition MDC_PART_RET_CODE/OBJ Object errors                                          		               */
	public final int MDC_RET_CODE_OBJ_BUSY         		=	1000;	/* Object is busy so cannot handle the request */
	/* Partition MDC_PART_RETURN_CODES/STORE Storage errors                                    					   */
	public final int MDC_RET_CODE_STORE_EXH        		=	2000;	/* Storage such as disk is full                */
	public final int MDC_RET_CODE_STORE_OFFLN 			=	2001;	/* Storage such as disk is offline             */


}
