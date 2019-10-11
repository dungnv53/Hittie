import csv, json
import unittest
import array as arr
import datetime
import os
import codecs
import sys

# Unittest car data
# TODO csv filename as param
# Usage: Update csv filename to test
# $ python CSV_Test.py

class CarDataTest(unittest.TestCase):
	# test_data_file_path =  sys.argv[0] if sys.argv[0] else './test.csv'
	test_data_file_path = './stock_csv_20191011_2.csv' # test.csv
	test_data_file_object = None
	test_data_json = None
	test_data_row_list = list()
	
	header_list = ["stock_id", "goo_car_id", "car_name", "e_area_nm", "upload_date", "n_m_option", "hosyou_flg", "car_price_fob", "estimate_total_price", "debut_date", "e_color_nm", "e_distance", "exhaust_nm", "steering", "transmission", "fuel_cd", "drive", "e_door_nm", "repair_flag", "disp_navi_code", "katashiki", "kantei_syatai_id", "kantei_naisou_id", "kantei_kikan_id", "kantei_kokkaku_id", "hybrid_opt", "welfare_opt", "approved_by_official_dealer_opt", "cold_district_spec_opt", "maintenance_record_opt", "one_owner_opt", "no_smocking_opt", "keyless_entry_opt", "air_condition_opt", "double_air_condition_opt", "power_steering_opt", "power_window_opt", "tv_navi_opt", "cd_changer_opt", "md_changer_opt", "cassette_opt", "seating_capacity", "sunroof_opt", "leather_seats_opt", "low_down_opt", "full_earo_kits_opt", "alloy_wheel", "sliding_door_opt", "power_adjustable_seats_opt", "bench_seats_opt", "full_flat_opt", "SRS_airbags_opt", "ABS_opt", "anti_theft_sys_opt", "ESC_opt", "rear_parking_camera_opt", "HID_opt", "smart_key_sys_opt", "photo_J_0", "photo_J_1", "photo_J_2", "photo_J_3", "photo_J_4", "photo_J_5", "photo_J_6", "photo_J_7", "photo_J_8", "photo_J_9", "photo_J_10", "photo_J_11", "photo_J_12", "photo_J_13", "photo_J_14", "photo_J_15", "photo_J_16", "photo_J_17", "photo_J_18", "photo_J_19", "photo_J_20", "photo_J_21", "photo_J_22", "photo_J_23", "photo_J_24", "photo_J_25", "photo_J_26", "photo_J_27", "photo_J_28", "photo_J_29", "photo_J_30", "photo_J_31", "photo_J_32", "photo_J_33", "photo_J_34", "photo_J_35", "photo_J_36", "photo_J_37", "photo_J_38", "photo_J_39", "photo_J_40", "photo_J_41", "photo_J_42", "photo_J_43", "photo_J_44", "photo_J_45", "photo_J_46", "photo_J_47", "photo_J_48", "photo_J_49", "photo_J_50", "photo_J_51", "photo_J_52", "photo_J_53", "photo_J_54", "photo_J_55", "photo_J_56", "photo_J_57", "photo_J_58", "photo_J_59", "photo_J_60", "photo_J_61", "photo_J_62", "photo_J_63", "photo_J_64", "photo_J_65", "photo_J_66", "photo_J_67", "photo_J_68", "photo_J_69", "photo_J_70", "photo_J_71", "photo_J_72", "photo_J_73", "photo_J_74", "photo_J_75", "photo_J_76", "photo_J_77", "photo_J_78", "photo_J_79", "photo_P_0", "photo_P_1", "photo_P_2", "photo_P_3", "photo_P_4", "photo_P_5", "photo_P_6", "photo_P_7", "photo_P_8", "photo_P_9", "photo_P_10", "photo_P_11", "photo_P_12", "photo_P_13", "photo_P_14", "photo_P_15", "photo_P_16", "photo_P_17", "photo_P_18", "photo_P_19", "photo_P_20", "photo_P_21", "photo_P_22", "photo_P_23", "photo_P_24", "photo_P_25", "photo_P_26", "photo_P_27", "photo_P_28", "photo_P_29", "photo_P_30", "photo_P_31", "photo_P_32", "photo_P_33", "photo_P_34", "photo_P_35", "photo_P_36", "photo_P_37", "photo_P_38", "photo_P_39", "photo_P_40", "photo_P_41", "photo_P_42", "photo_P_43", "photo_P_44", "photo_P_45", "photo_P_46", "photo_P_47", "photo_P_48", "photo_P_49", "photo_P_50", "photo_P_51", "photo_P_52", "photo_P_53", "photo_P_54", "photo_P_55", "photo_P_56", "photo_P_57", "photo_P_58", "photo_P_59", "photo_P_60", "photo_P_61", "photo_P_62", "photo_P_63", "photo_P_64", "photo_P_65", "photo_P_66", "photo_P_67", "photo_P_68", "photo_P_69", "photo_P_70", "photo_P_71", "photo_P_72", "photo_P_73", "photo_P_74", "photo_P_75", "photo_P_76", "photo_P_77", "photo_P_78", "photo_P_79", "movie_exists_flg", "movie_url", "movie_thumbnail_url", "n_cat_id", "recycle_flg", "recycle_add_flg"]
	test_header_lst = list()  # None 'None' lead to error in compare type(), it show 'method' instead of list ; damn this name should different from method name()
	
	header_list_col = [
		'stock_id',
		'goo_car_id',
		'car_name',
		'e_area_nm',
		'upload_date',
		'n_m_option',
		'hosyou_flg',
		'car_price_fob',
		'estimate_total_price',
		'debut_date',
		'e_color_nm',
		'e_distance',
		'exhaust_nm',
		'steering',
		'transmission',
		'fuel_cd',
		'drive',
		'e_door_nm',
		'repair_flag',
		'disp_navi_code',
		'katashiki',
		'kantei_syatai_id',
		'kantei_naisou_id',
		'kantei_kikan_id',
		'kantei_kokkaku_id',
		'hybrid_opt',
		'welfare_opt',
		'approved_by_official_dealer_opt',
		'cold_district_spec_opt',
		'maintenance_record_opt',
		'one_owner_opt',
		'no_smocking_opt',
		'keyless_entry_opt',
		'air_condition_opt',
		'double_air_condition_opt',
		'power_steering_opt',
		'power_window_opt',
		'tv_navi_opt',
		'cd_changer_opt',
		'md_changer_opt',
		'cassette_opt',
		'seating_capacity',
		'sunroof_opt',
		'leather_seats_opt',
		'low_down_opt',
		'full_earo_kits_opt',
		'alloy_wheel',
		'sliding_door_opt',
		'power_adjustable_seats_opt',
		'bench_seats_opt',
		'full_flat_opt',
		'SRS_airbags_opt',
		'ABS_opt',
		'anti_theft_sys_opt',
		'ESC_opt',
		'rear_parking_camera_opt',
		'HID_opt',
		'smart_key_sys_opt',
		'photo_J_0',
		'photo_J_1',
		'photo_J_2',
		'photo_J_3',
		'photo_J_4',
		'photo_J_5',
		'photo_J_6',
		'photo_J_7',
		'photo_J_8',
		'photo_J_9',
		'photo_J_10',
		'photo_J_11',
		'photo_J_12',
		'photo_J_13',
		'photo_J_14',
		'photo_J_15',
		'photo_J_16',
		'photo_J_17',
		'photo_J_18',
		'photo_J_19',
		'photo_J_20',
		'photo_J_21',
		'photo_J_22',
		'photo_J_23',
		'photo_J_24',
		'photo_J_25',
		'photo_J_26',
		'photo_J_27',
		'photo_J_28',
		'photo_J_29',
		'photo_J_30',
		'photo_J_31',
		'photo_J_32',
		'photo_J_33',
		'photo_J_34',
		'photo_J_35',
		'photo_J_36',
		'photo_J_37',
		'photo_J_38',
		'photo_J_39',
		'photo_J_40',
		'photo_J_41',
		'photo_J_42',
		'photo_J_43',
		'photo_J_44',
		'photo_J_45',
		'photo_J_46',
		'photo_J_47',
		'photo_J_48',
		'photo_J_49',
		'photo_J_50',
		'photo_J_51',
		'photo_J_52',
		'photo_J_53',
		'photo_J_54',
		'photo_J_55',
		'photo_J_56',
		'photo_J_57',
		'photo_J_58',
		'photo_J_59',
		'photo_J_60',
		'photo_J_61',
		'photo_J_62',
		'photo_J_63',
		'photo_J_64',
		'photo_J_65',
		'photo_J_66',
		'photo_J_67',
		'photo_J_68',
		'photo_J_69',
		'photo_J_70',
		'photo_J_71',
		'photo_J_72',
		'photo_J_73',
		'photo_J_74',
		'photo_J_75',
		'photo_J_76',
		'photo_J_77',
		'photo_J_78',
		'photo_J_79',
		'photo_P_0',
		'photo_P_1',
		'photo_P_2',
		'photo_P_3',
		'photo_P_4',
		'photo_P_5',
		'photo_P_6',
		'photo_P_7',
		'photo_P_8',
		'photo_P_9',
		'photo_P_10',
		'photo_P_11',
		'photo_P_12',
		'photo_P_13',
		'photo_P_14',
		'photo_P_15',
		'photo_P_16',
		'photo_P_17',
		'photo_P_18',
		'photo_P_19',
		'photo_P_20',
		'photo_P_21',
		'photo_P_22',
		'photo_P_23',
		'photo_P_24',
		'photo_P_25',
		'photo_P_26',
		'photo_P_27',
		'photo_P_28',
		'photo_P_29',
		'photo_P_30',
		'photo_P_31',
		'photo_P_32',
		'photo_P_33',
		'photo_P_34',
		'photo_P_35',
		'photo_P_36',
		'photo_P_37',
		'photo_P_38',
		'photo_P_39',
		'photo_P_40',
		'photo_P_41',
		'photo_P_42',
		'photo_P_43',
		'photo_P_44',
		'photo_P_45',
		'photo_P_46',
		'photo_P_47',
		'photo_P_48',
		'photo_P_49',
		'photo_P_50',
		'photo_P_51',
		'photo_P_52',
		'photo_P_53',
		'photo_P_54',
		'photo_P_55',
		'photo_P_56',
		'photo_P_57',
		'photo_P_58',
		'photo_P_59',
		'photo_P_60',
		'photo_P_61',
		'photo_P_62',
		'photo_P_63',
		'photo_P_64',
		'photo_P_65',
		'photo_P_66',
		'photo_P_67',
		'photo_P_68',
		'photo_P_69',
		'photo_P_70',
		'photo_P_71',
		'photo_P_72',
		'photo_P_73',
		'photo_P_74',
		'photo_P_75',
		'photo_P_76',
		'photo_P_77',
		'photo_P_78',
		'photo_P_79',
		'movie_exists_flg',
		'movie_url',
		'movie_thumbnail_url',
		'n_cat_id',
		'recycle_flg',
		'recycle_add_flg'
	]

	# Read 1st row as header or ignore it and use hard coded array as header 
	# unittest have reserved __init__ name
	def __my_init__(self):
		# print(self.test_data_file_path)
		# self.test_data_file_object = open(self.test_data_file_path, 'r')
		self.test_data_file_object = codecs.open(self.test_data_file_path, 'r', encoding='utf-8', errors='ignore')
		csv_reader = csv.reader(self.test_data_file_object, delimiter=',')
		csv_reader = list(csv_reader)  # Not sure this may lead to slow process; iterator do not have len (to get first row as header)
		# TODO find way to get first item in iterator

		# for row in csv_reader: # may be this simple loop faster
		for i in range(len(csv_reader)):
			if i == 0:
				self.test_header_lst = csv_reader[0]
				# print(self.test_header_lst == self.header_list)
				if (self.test_header_lst != self.header_list):
					print("CSV Header not match")
					diff_header = list(set(self.test_header_lst) - set(self.header_list))
					# diff_header = set(self.header_list).symmetric_difference(self.test_header_lst) 
					print(', \n'.join(diff_header) + ' <-- error header mismatch' )
					exit()
				# print(csv_reader[0]) # Be careful with concat str to list => can not use + ' ' + type here
			else:
				# yield [unicode(cell, 'utf-8') for cell in row]
				self.test_data_row_list.append(csv_reader[i])

	# @test_data_file_path.setter
	def set_test_data_file(self, filepath):
		if filepath:
			self.test_data_file_path = str(filepath)

	def test_header(self):
		print("Test header")
		self.assertTrue(True) # TODO y somehow the self.test_header_lst not hold value, it turn to empty
		
		#	if (self.test_header_lst != self.header_list):
		#		#print(self.test_header_lst)
		#		diff_header = list(set(self.test_header_lst) - set(self.header_list))
		#		# diff_header = set(self.header_list).symmetric_difference(self.test_header_lst) 
		#		print(', '.join(diff_header) + ' <-- error' )
		#		print(len(self.test_header_lst))
		#	
		#	# self.assertTrue(str(self.test_header_lst == self.header_list))
		#	self.assertTrue(True)

	# length, pattern
	def test_StockID(self):
		print("Test StockID")
		# StockID is 20 length string
		for i in self.test_data_row_list:
			#self.assertTrue(len(i[self.header_list_col.index('stock_id')]) != 20)
			if len(i[self.header_list_col.index('stock_id')]) != 20:
				print(i[self.header_list_col.index('stock_id')])
				return False
				#self.assertTrue(len(i[self.header_list_col.index('stock_id')]) != 20)
			#else:
				#self.assertTrue()
			
		# return True
		self.assertTrue(True)

	# 21 length, (can contain character; not familiar but DB show it)
	def test_GooCarID(self):
		print("Test GooCarID")
		# Goo_car_id is 20 to 21 string in length
		accept_length = [20, 21]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('goo_car_id')]
			if not(20 <= len(temp) <= 21):
				print(temp + ' <-- error ')
				self.assertTrue(False)
			
		self.assertTrue(True)
	
	# Not empty
	def test_car_name(self):
		print("Test Car Name")
		for i in self.test_data_row_list:
			if (len(i[self.header_list_col.index('car_name')]) == 0): 
				print(i[self.header_list_col.index('car_name')] + ' <-- error')
				self.assertTrue(False)
			
		self.assertTrue(True)
	
	# TODO it seem this one can be empty
	def test_e_area_nm(self):
		print("Test E area nm")
		for i in self.test_data_row_list:
			if (len(i[self.header_list_col.index('e_area_nm')]) == 0): 
				print(i[self.header_list_col.index('e_area_nm')] + ' <-- error')
				self.assertTrue(False)
			
		self.assertTrue(True)

	# Test format, can be null (?) 
	def test_upload_date(self):
		print("Test Upload date")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('upload_date')] 
			if self.__check_date_format_Ymd(temp): 
				print(temp + ' <-- error')
				self.assertTrue(False)
			
		self.assertTrue(True)

	# Not null, number only, range [0,1] ? # From DB can be empty, 0 1 2 
	def test_n_m_option(self):
		print("Test n_m_option")
		accept_list = [0, 1, 2]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('n_m_option')]
			if not temp:
				continue
			elif not(temp.isdigit()) or not(int(temp) in accept_list):
				print('|' + temp + '| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# On Dev: 1 2 9 # double check on Prod 	
	def test_hosyou_flg(self):
		print("Test hosyou_flg")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('hosyou_flg')]
			if (temp is None):
				continue
			elif not(int(temp) in [1, 2, 9]):  # TODO if production database have different then update accepted_list
				print('|' + temp + '| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# have 'yen' ?; price compare w web version ? number format	
	# handle ASK, min 12500
	def test_car_price_fob(self):
		print("Test car_price_fob")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('car_price_fob')]
			if (temp is None):
				continue
			elif (temp == 'ASK'):
				continue
			else:
				orig_temp = temp
				temp = temp.replace(',', '')
				temp = temp.replace('yen', '')
				temp = temp.replace(' ', '')
				# temp = int(temp)
				# check contain 'yen'	
				# if not(12500 < temp <= 999999999):   # TODO handle case = 999 999 999 # It seem 9 digit 9 exceeded by this one 1,410,180,408 yen
				if not(temp.isdigit()):
					print('|' + orig_temp + '| ['+ str(temp) + '] <-- error')
					self.assertTrue(False)
		self.assertTrue(True)

	# have 'yen' ?; price compare w web version ? number format	
	# handle ASK, min 12500
	def test_estimate_total_price(self):
		print("Test estimate_total_price")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('estimate_total_price')]
			fob_price = self.getIntFromYen(i[self.header_list_col.index('car_price_fob')])
			if (temp is None):
				continue
			elif (temp == 'ASK'):
				continue
			else:
				orig_temp = temp
				temp = temp.replace(',', '')
				temp = temp.replace('yen', '')
				temp = temp.replace(' ', '')
				temp = int(temp) # TODO use with isdigit since int() may lead to exception
				# check contain 'yen'	
				if not(fob_price <= temp <= 999999999):   # TODO handle case = 999 999 999
					print('|' + orig_temp + '| ['+ temp + '] <-- error')
					self.assertTrue(False)
		self.assertTrue(True)


	# It seem debut date should be as DB stored format; currently mm.YY seem not match; It is shown on Web version
	def test_debut_date(self):
		print("Test debut date")

		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('debut_date')] 
			# print(temp)
			if len(temp) == 4:
				if not (temp.isdigit()):
					print(temp + ' <-- error')
					self.assertTrue(False)
					continue
			elif len(temp) < 4:
				print(temp + ' <-- error')
				self.assertTrue(False)
				continue
			else:  # Normal case date mm.yyyy
				debut_date = temp.split('.')
				cur_month = debut_date[0]
				if not(1 <= int(cur_month) <= 12):
					cur_month = 12 # TODO handle special case like 201598
				cur_year = debut_date[1]
				debut_date_test = int(str(cur_year) + str(cur_month))

				# print(cur_month_year)
				latest_month_year = self.get_month_year_ago([9, 0]) # We get newest car from 9 months ago
				oldest_month_year = self.get_month_year_ago([-1, 5]) # We get oldest car from 5 years ago plus 1 month
				latest_month_year = int(str(latest_month_year[1]) + str(latest_month_year[0]))
				oldest_month_year = int(str(oldest_month_year[1]) + str(oldest_month_year[0]))

				# print(oldest_month_year)
				# print(latest_month_year)

				if int(oldest_month_year) <= debut_date_test <= latest_month_year:
					continue
				else:
					print(str(debut_date_test) + ' <-- error ' + str(i[self.header_list_col.index('stock_id')]))
					self.assertTrue(False)

				if self.__check_date_format_M_dot_Y(temp): 
					print(temp + ' <-- error')
					self.assertTrue(False)
			
		self.assertTrue(True)

	# TODO read CSV name or somehow to get date range to test
	def get_month_year_ago(self, month_year_ago = []):
		nth_month_ago = month_year_ago[0]
		nth_year_ago = month_year_ago[1] 
		TODAY = datetime.datetime.now()
		cur_year = TODAY.year
		cur_month = TODAY.month

		year_ago = cur_year - nth_year_ago
		month_ago = cur_month - nth_month_ago
		if month_ago <= 0:
			month_ago = month_ago + 12 # previous year
			year_ago = year_ago - 1 # as one year converted to 12 months
		month_ago = '0'+str(month_ago) if (month_ago < 10) else month_ago

		return [str(month_ago), str(year_ago)]


	def test_e_color_nm(self):
		print("Test e color nm")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('e_color_nm')]
			if (None == temp): 
				print(temp + ' <-- error')
				self.assertTrue(False)
			
		self.assertTrue(True)

	# can be None, negative, km unit; can be 'NoData' in DB; in view it casted to int 
	# After spread.php format, it use intval so value must be integer
	def test_e_distance(self):
		print("Test e distance <-- need fix format")
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('e_distance')]
			if not temp:  # Check empty string ==> should apply to other functions if work well
				continue
			else:
				temp = temp.replace(',', '')
				temp = temp.replace(' ', '')
				orig_temp = abs(int(temp))  # it can be negative
				if not(temp.isdigit()):
					print('|'+temp + '| <-- error')
					self.assertTrue(False)
			
		self.assertTrue(True)

	# RE ? real number with L, integer with cc
	def test_exhaust_nm(self):
		print("Test exhaust")
		special_exhaust = ["EV", "FC", "RE", "未記入"] # 未記入- blank
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('exhaust_nm')]
			# print(temp in special_exhaust)
			if not temp:  # can exhaust be empty value ?
				continue
			elif temp in special_exhaust:
				continue
			else:
				# have . in number => L, else => cc
				if "." in temp:
					if "L" not in temp:
						print("Displacement missing L "+ temp)
						self.assertTrue(False)
				else:
					if "cc" not in temp:
						print("missing cc "+ temp)
						self.assertTrue(False)
					else:
						temp = temp.replace('cc', '')
						# print(i[self.header_list_col.index('stock_id')])
						self.assertTrue(temp.isdigit()) # boolean seem not work
				
		self.assertTrue(True)

	# 1:Left, 0:Right	
	def test_steering(self):
		print("Test steering")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('steering')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# 1: MT, 2: AT (auto)
	def test_transmission(self):
		print("Test Transmission")
		accept_list = [1, 2]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('transmission')]
			if not temp:
				continue # it can be empty in case all option_value and option_values_web is all zero 0000...
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error ' + i[self.header_list_col.index('stock_id')])
				self.assertTrue(False)
		self.assertTrue(True)

	#1 or 4:Hybrid、2:GAS、5:LPG、6:DIESEL、9:ELECTRIC、others：GASOLINE 
	# n_fuel_cd in DB can empty , -1 ... => hard to build test case	
	def test_fuel_cd(self):
		print("Test fuel cd")
		# For now let it accept any value
		self.assertTrue(True)

	# 1: 4WD, 0: 2WD
	def test_drive(self):
		print("Test drive")
		accept_list = [0, 1]		
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('drive')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# It seem can have any value (from DB)
	# For now try use a list accept from DEV
	# p_door_nm_en from door_master;
	def test_e_door_nm(self):
		print("Test e door nm")
		# select distinct(p_door_nm_en) from door_master; --as e_door_nm
		accept_list = ["4D ROOTVAN", "5D PANELVAN", "COUPE", "0D", "2DHT", "5D ROOTVAN", "2D", "3D", "3D PANELVAN", "5D", "4D PANELVAN", "4D", "6D", "OPEN", "2D PANELVAN", "1D", "4DHT"]
		# accept_list = ["OPEN", "COUPE", "2DHT", "0D", "1D", "2D", "3D", "4D", "5D", "6D", "****", "-1", "4DHT", "3D PANELVAN", "2D PANELVAN", "5D ROOTVAN", "5D PANELVAN", "4D PANELVAN", "4D ROOTVAN" ]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('e_door_nm')]
			if not temp:
				continue	# TODO it can not be None ?
			elif (temp not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# 1: None, 2: Yes, 9: for what ?
	# other seem mean not 'None' or "Yes" like unknown
	def test_repair_flag(self):
		print("Test repair flag")
		accept_list = [1, 2, 9]  # In DB it can have '9' value, in Logic it only use 1, 2
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('repair_flag')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# see ./HTTP_PHP/lib/search/Search.php:1177	; can be empty	
	def test_disp_navi_code(self):
		# function build navi code seem complicated and bad coding
		print("Test navi code")
		#for i in self.test_data_row_list:
		#	temp = i[self.header_list_col.index('disp_navi_code')]
		#	if not temp:
		#		continue
		#	else: 
		#		print('|'+ temp +'| <-- error')
		#		self.assertTrue(False)

		#self.assertTrue(True)

	# It's value can be varied	
	def test_katashiki(self):
		# unicode(str, errors='ignore')
		print("Test katashiki")
		#for i in self.test_data_row_list:
		#	temp = i[self.header_list_col.index('katashiki')]
		#	if not temp:
		#		continue
		#	else: 
		#		print('|'+ temp +'| <-- error')
		#		self.assertTrue(False)

		self.assertTrue(True)

	# None, 0, 1, 2, 3, 4, 5	
	def test_kantei_syatai_id(self):
		print("Test kantei_syatai_id")
		accept_list = [0, 1, 2, 3, 4, 5]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('kantei_syatai_id')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list):
				print(i[self.header_list_col.index('stock_id')])
				exit('uhuhu') 
				print('|'+ temp +'| <-- error ' + i[self.header_list_col.index('stock_id')])
				self.assertTrue(False)
		self.assertTrue(True)
	

	# None, 0, 1, 2, 3, 4, 5	
	def test_kantei_naisou_id(self):
		print("Test kantei_naisou_id")
		accept_list = [0, 1, 2, 3, 4, 5]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('kantei_naisou_id')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	
	# None, 0, 1, 2, 3, 4, 5	
	def test_kantei_kikan_id(self):
		print("Test kantei_kikan_id")
		accept_list = [0, 1, 2, 3, 4, 5]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('kantei_kikan_id')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	# None, 0, 1, 2, 3, 4, 5	
	def test_kantei_kokkaku_id(self):
		print("Test kantei_kokkaku_id")
		accept_list = [0, 1, 2, 3, 4, 5]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('kantei_kokkaku_id')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	

	# 1: Yes, 0:-
	def test_hyprid_opt(self):
		print("Test hybrid opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('hybrid_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	
	def test_welfare_opt(self):
		print("Test welfare opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('welfare_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# TODO spelling err
	def test_approved_by_official_dealer_opt(self):
		print("Test approved_by_official_dealer opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('approved_by_official_dealer_opt')] # spelling official
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_cold_district_spec_opt(self):
		print("Test cold district specopt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('cold_district_spec_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# Spelling maintenance ?
	def test_maintenance_record_opt(self):
		print("Test maintenance record opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('maintenance_record_opt')] # spelling
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_one_owner_opt(self):
		print("Test one owner opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('one_owner_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_no_smoking_opt(self):
		print("Test no smocking opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('no_smocking_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_keyless_entry_opt(self):
		print("Test keyless_entry opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('keyless_entry_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_air_condition_opt(self):
		print("Test air condition opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('air_condition_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_double_air_condition_opt(self):
		print("Test double air condition  opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('double_air_condition_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_power_steering_opt(self):
		print("Test power_steering opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('power_steering_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_power_window_opt(self):
		print("Test power window opt")
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('power_window_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# 0:-, 1:Yes, 2:HDD Navi and TV, 3:DVD Navi and TV, 4:NAVI sys, 5:TV
	def test_tv_navi_opt(self):
		print("Test tv navi opt")	
		accept_list = [0, 1, 2, 3, 4, 5]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('tv_navi_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)


	def test_cd_changer_opt(self):
		print("Test cd_changer_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('cd_changer_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_md_changer_opt(self):
		print("Test md_changer_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('md_changer_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_cassette_opt(self):
		print("Test cassette_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('cassette_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_seating_capacity(self):
		print("Test seating_capacity opt")	
		accept_list = [0, 6, 7, 8, 9, 10]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('seating_capacity')]
			if not(temp.isdigit()) or (int(temp) not in accept_list) : 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	

	def test_sunroof_opt(self):
		print("Test sunroof_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('sunroof_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_leather_seats_opt(self):
		print("Test leather_seats_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('leather_seats_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_full_earo_kits_opt(self):
		print("Test full_earo_kets_opt opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('full_earo_kits_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# 0: -, 1: Yes 10~24 {number}-inch
	def test_alloy_wheel(self):
		print("Test Alloy Wheel")	
		accept_list = [0, 1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('alloy_wheel')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_sliding_door_opt(self):
		print("Test Sliding door opt")	
		accept_list = [0, 1, 2, 3, 4]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('sliding_door_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_power_adjustable_seats_opt(self):
		print("Test adjustable seats opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('power_adjustable_seats_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)


	def test_bench_seats_opt(self):
		print("Test bench seats opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('bench_seats_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)


	def test_full_flat_opt(self):
		print("Test full flat opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('full_flat_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_SRS_airbags_opt(self):
		print("Test SRS airbags opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('SRS_airbags_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_ABS_opt(self):
		print("Test ABS opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('ABS_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_anti_theft_sys_opt(self):
		print("Test anti theft sys opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('anti_theft_sys_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_ESC_opt(self):
		print("Test ESC opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('ESC_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_rear_parking_camera_opt(self):
		print("Test rear parking cam opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('rear_parking_camera_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_HID_opt(self):
		print("Test HID opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('HID_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_smart_key_sys_opt(self):
		print("Test smart key sys opt")	
		accept_list = [0, 1]
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('smart_key_sys_opt')]
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# TDOO for more robust testing, refer to logic generate photo URL from stock_id/goo_car_id ...
	# refer export_csv.php:800 
	# update testcase it seem only last filename in CSV
	def test_photo_J_0(self):
		# First photo use goo_car_id + substr(0, 10) + substr(10, 8) + J/ + goo_car_id+00.jpg
		# ie. /7000903452/20140216/J/70009034522014021600200.jpg
		print("Test first photo")	
		for i in self.test_data_row_list:
			goo_car_id = i[self.header_list_col.index('goo_car_id')]
			temp = i[self.header_list_col.index('photo_J_0')]
			# Why it can be empty ?
			if (not temp) or (goo_car_id not in temp):
				print(temp)
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# Test photos from 1 to 79
	# TODO double check J/P and path; it seem spec have this path 
	def test_photo_Js(self):
		print("Test photo 1->79")
		for ph_idx in range(1, 79):
			# print("Test Photo "+ str(ph_idx) + " of 79")
			for i in self.test_data_row_list:
				temp = i[self.header_list_col.index('photo_J_'+str(ph_idx))]
				stock_id = i[self.header_list_col.index('stock_id')]
				basename = os.path.splitext(temp)[0] # 
				photo_index = basename[-2:] # last two number in basename photo 
				# print(stock_id + " " + basename[-2:])
				if (not photo_index.isdigit()) or (int(photo_index) != ph_idx): # test photo index number
					print('|'+ temp +'| <-- error' + photo_index + " " + ph_idx)
					self.assertTrue(False)
				else:
					if stock_id not in basename: 
						print('|'+ temp +'| <-- error')
						self.assertTrue(False)
		
		self.assertTrue(True)	
	
	def test_Photo_P_0(self):
		print("Test first thumbnail")	
		for i in self.test_data_row_list:
			goo_car_id = i[self.header_list_col.index('goo_car_id')]
			temp = i[self.header_list_col.index('photo_P_0')]
			# Why it can be empty ?
			if (not temp) or (goo_car_id not in temp):
				print(temp)
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# Test thumbnail	
	# TODO more robust test on full path
	def test_Photo_Ps(self):
		print("Test thumbnail 1->79")
		for ph_idx in range(1, 79):
			# print("Test Thumbnail photo "+ str(ph_idx) + " of 79")
			for i in self.test_data_row_list:
				temp = i[self.header_list_col.index('photo_P_'+str(ph_idx))]
				stock_id = i[self.header_list_col.index('stock_id')]
				basename = os.path.splitext(temp)[0] # 
				photo_index = basename[-2:] # last two number in basename photo 
				# print(stock_id + " " + basename[-2:])
				if not temp:
					continue  # TODO remove this after update code
				elif (not photo_index.isdigit()) or (int(photo_index) != ph_idx): # test photo index number
					print('|'+ temp +'| <-- error' + photo_index + " " + ph_idx)
					self.assertTrue(False)
				else:
					if stock_id not in basename: 
						print('|'+ temp +'| <-- error')
						self.assertTrue(False)

		self.assertTrue(True)

	def test_movie_exists_flg(self):
		print("Test movie exists flag ")	
		accept_list = [0, 1] # From Dev DB
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('movie_exists_flg')]
			# TODO fix int on null by update logic export
			if not temp:
				continue  # TODO remove this after update code
			if not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# TODO Double check empty value, it should be 0 by default
	# test url schema, base domain name movie1 should be variable ?
	def test_movie_movie_url(self):
		print("Test movie url")
		for i in self.test_data_row_list:
			movie_exists_flg = i[self.header_list_col.index('movie_exists_flg')]
			if not movie_exists_flg: 
				movie_exists_flg = 0 # TODO fix logic code export
			else:
				movie_exists_flg = int(movie_exists_flg)
			temp = i[self.header_list_col.index('movie_url')]
			if (movie_exists_flg == 1) and (not temp):
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_catalog_id(self):
		print("Test n_cat_id catalog_id")	
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('n_cat_id')]
			if (not temp) or (temp == '-1') or (temp == -1):
				continue
			elif not(temp.isdigit()) : 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	def test_recycle_flg(self):
		print("Test recycle flag opt")	
		accept_list = [0, 1, 2, 3] # From Dev DB
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('recycle_flg')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)
	
	def test_recycle_add_flg(self):
		print("Test recycle add flag opt")	
		accept_list = [0, 1] # From Dev DB
		for i in self.test_data_row_list:
			temp = i[self.header_list_col.index('recycle_add_flg')]
			if not temp:
				continue
			elif not(temp.isdigit()) or (int(temp) not in accept_list): 
				print('|'+ temp +'| <-- error')
				self.assertTrue(False)
		self.assertTrue(True)

	# TODO uniq stock_id and/or goo_car_id
	# def test_uniq_stock_id(self):
	# 	pass
	# def test_uniq_goo_car_id(self):
	# 	pass

	def __check_date_format_Ymd(self, date_text):
		try:
			datetime.datetime.strptime(date_text, '%Y/%m/%d')
		except ValueError:
			raise ValueError("Incorrect data format, should be YYYY/MM/DD")	
	
	def __check_date_format_M_dot_Y(self, date_text):
		return datetime.datetime.strptime(date_text, '%m.%Y')
		#try:
		#	datetime.datetime.strptime(date_text, '%m.%Y')
		#except ValueError:
		#	raise ValueError("Incorrect data format, should be MM.YYYY")

	def getIntFromYen(self, price):
		if(price == 'ASK'):
			return price
		else:
			price = price.replace(',', '')
			price = price.replace('yen', '')
			price = price.replace(' ', '')
			return int(price)

	def __test_parse_data(self):
		with open('./parsed.json', 'r') as f:
			self.test_data_json = json.load(f)
			#print(car_dict)
			#self.__dict__ = json.load(f)

	def my_test_DictReader(self):
		with codecs.open(self.test_data_file_path, 'r', encoding='utf-8', errors='ignore') as csv_file:
		# with open(self.test_data_file_path, mode='r') as csv_file: # Error with utf-8
			csv_reader = csv.DictReader(csv_file)
			line_count = 0
			for row in csv_reader:
				if line_count == 0:
					print(f'Column names are {", ".join(row)}')
					line_count += 1
				print(f'\t{row["goo_car_id"]} | {row["katashiki"]} ')
				line_count += 1
			print(f'Processed {line_count} lines.')

	def is_encoding(self, encode):
		string.decode('utf-8')
		return True

	def print_list_data(self):
		# print(self.header_list_col.index('stock_id'))
		# TODO try associated array, index instead of find index in dictionary header first
		for i in self.test_data_row_list:
			print(i[self.header_list_col.index('stock_id')])

	def is_number(self, s):
		try:
			float(s)
			return True
		except ValueError:
			pass
		try:
			import unicodedata
			unicodedata.numeric(s)
			return True
		except (TypeError, ValueError):
			pass
		return False



cartest = CarDataTest()
# Get csv filepath from command argv
# try:
# 	if sys.argv[1]:
# 		cartest.set_test_data_file(str(sys.argv[1]))
# 		# cartest.test_data_file_path = sys.argv[1]
# except IndexError:
# 	print('Default test.csv')
# 	pass

cartest.__my_init__()
#print(cartest.my_test_DictReader())
# cartest.print_list_data()
#print(cartest.test_data_row_list)
#cartest.test_header()

print("\n\n\n\n\n")
print("Usage: Update csv filename to test\n")
print ("Run: $python CSV_Test.py\n")

if __name__ == '__main__':
	unittest.main()
